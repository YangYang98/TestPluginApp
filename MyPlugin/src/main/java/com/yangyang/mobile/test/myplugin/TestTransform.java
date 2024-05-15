package com.yangyang.mobile.test.myplugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * Create by Yang Yang on 2024/5/15
 */
public class TestTransform extends Transform {
    @Override
    public String getName() {
        return "TestTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return true;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        System.out.println("yangyang >>>>>>>>  " + this.getClass().getName());
        Collection<TransformInput> inputs = transformInvocation.getInputs();

        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();

        for (TransformInput input : inputs) {
            for (DirectoryInput dirInput : input.getDirectoryInputs()) {
                File dest = outputProvider.getContentLocation(dirInput.getName(), dirInput.getContentTypes(), dirInput.getScopes(), Format.DIRECTORY);
                transformDir(dirInput.getFile(), dest);
            }
        }


    }

    private void transformDir(File input, File dest) throws IOException {
        if (dest.exists()) {
            FileUtils.forceDelete(dest);
        }

        FileUtils.forceMkdir(dest);
        String srcDirPath = input.getAbsolutePath();
        String destDirPath = dest.getAbsolutePath();
        File[] fileList = input.listFiles();
        if (fileList == null) {
            return;
        }
        for (File file : fileList) {
            System.out.println("yangyang >>>>>>>>  " + "1");
            String destFilePath = file.getAbsolutePath().replace(srcDirPath, destDirPath);
            System.out.println("yangyang >>>>>>>>  " + "2");
            File destFile = new File(destFilePath);
            if (file.isDirectory()) {
                transformDir(file, destFile);
            } else {
                FileUtils.touch(destFile);
                asmHandleFile(file.getAbsolutePath(), destFile.getAbsolutePath());
            }
        }

    }

    private void asmHandleFile(String inputPath, String destPath) {
        try {
            System.out.println("yangyang >>>>>>>>  " + "3");
            FileInputStream is = new FileInputStream(inputPath);
            ClassReader cr = new ClassReader(is);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            TestClassVisitor visitor = new TestClassVisitor(cw);
            System.out.println("yangyang >>>>>>>>  " + "4");

            cr.accept(visitor, 0);

            System.out.println("yangyang >>>>>>>>  " + "5");
            FileOutputStream fos = new FileOutputStream(destPath);
            System.out.println("yangyang >>>>>>>>  " + "6");
            fos.write(cw.toByteArray());
            System.out.println("yangyang >>>>>>>>  " + "7");
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
