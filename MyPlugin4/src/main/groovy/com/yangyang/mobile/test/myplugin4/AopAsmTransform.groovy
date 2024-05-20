package com.yangyang.mobile.test.myplugin4

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager

import com.android.utils.*
import org.apache.commons.io.FileUtils
import org.apache.commons.codec.digest.DigestUtils

import java.io.IOException
import java.util.Set

public class AopAsmTransform extends Transform {
    @Override
    public String getName() {
        return "AopAsmPlugin"
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    public boolean isIncremental() {
        return true // 是否支持增量编译
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        // 重载该方法，用来 Hook 住 class -> dex 的过程
        // 这里就是我们来读取 class 文件，进行 asm 操纵的真正入口
        // 这里一定要实现，否则在 ./build/intermediates/transforms/dexBuilder目录下，是空目录
        // 可以前后对比 dexBuilder 使用插件与不使用插件的输出内容

        System.out.println("【AOP ASM】---------------- begin")

        TransformOutputProvider provider = transformInvocation.getOutputProvider()
        for (TransformInput input : transformInvocation.getInputs()) {

            for (DirectoryInput di : input.getDirectoryInputs()) {
                System.out.println("DirectoryInput = " + di.getFile().getAbsolutePath())
                copyQualifiedContent(provider, di, null, Format.DIRECTORY)
            }

            for (JarInput ji : input.getJarInputs()) {
                System.out.println("JarInput = " + ji.getFile().getAbsolutePath())
                copyQualifiedContent(provider, ji, getUniqueName(ji.getFile()), Format.JAR)
            }

        }

        System.out.println("【AOP ASM】----------------  end  ----------------")
    }

    /***********************************************************************************************
     * 重名名输出文件,因为可能同名(N个classes.jar),会覆盖
     ***********************************************************************************************/
    private String getUniqueName(File jar) {
        String name = jar.getName();
        String suffix = "";
        if (name.lastIndexOf(".") > 0) {
            suffix = name.substring(name.lastIndexOf("."));
            name = name.substring(0, name.lastIndexOf("."));
        }
        String hexName = DigestUtils.md5Hex(jar.getAbsolutePath());
        return String.format("%s_%s%s", name, hexName, suffix);
    }

    private void copyQualifiedContent(TransformOutputProvider provider, QualifiedContent file, String fileName, Format format) throws IOException {
        boolean useDefaultName = fileName == null;
        File dest = provider.getContentLocation(useDefaultName ? file.getName() : fileName, file.getContentTypes(), file.getScopes(), format);
        //没有创建出一个文件，只是一个文件夹
        /*if (!dest.exists()) {
            dest.mkdirs();
            dest.createNewFile();
        }*/

        if (useDefaultName) {
            FileUtils.copyDirectory(file.getFile(), dest);
        } else {
            FileUtils.copyFile(file.getFile(), dest);
        }
    }
}
