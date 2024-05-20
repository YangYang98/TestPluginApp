package com.yangyang.mobile.test.myplugin3

import com.android.build.api.transform.Context
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
import groovy.io.FileType
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project


class MkAnalyticsTransform extends Transform{

    private static Project project
    private MkAnalyticsExtension sensorsAnalyticsExtension

    MkAnalyticsTransform(Project project, MkAnalyticsExtension sensorsAnalyticsExtension) {
        this.project = project
        this.sensorsAnalyticsExtension = sensorsAnalyticsExtension
    }

    @Override
    String getName() {
        return "MkAnalytics"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        _transform(transformInvocation.context, transformInvocation.inputs, transformInvocation.outputProvider, transformInvocation.incremental)
    }

    void _transform(Context context, Collection<TransformInput> inputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {

        if (!incremental) {
            outputProvider.deleteAll()
        }

        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                File dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                File dir = directoryInput.file

                if (dir) {
                    HashMap<String, File> modifyMap = new HashMap<>()
                    dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) {
                        File classFile ->
                            println(Const.TAG + "name:" + classFile.name)
                            if (MkAnalyticsClassModifier.isShouldModify(classFile.name)) {
                                File modified = null
                                if (!sensorsAnalyticsExtension.disableAppClick) {
                                    modified = MkAnalyticsClassModifier.modifyClassFile(dir, classFile, context.getTemporaryDir())
                                }
                                if (modified != null) {
                                    String key = classFile.absolutePath.replace(dir.absolutePath, "")
                                    modifyMap.put(key, modified)
                                }
                            }
                    }
                    FileUtils.copyDirectory(directoryInput.file, dest)
                    modifyMap.entrySet().each {
                        Map.Entry<String, File> en ->
                            File target = new File(dest.absolutePath + en.key)
                            if (target.exists()) {
                                target.delete()
                            }
                            FileUtils.copyFile(en.getValue(), target)
                            en.getValue().delete()
                    }
                }

            }

            input.jarInputs.each {JarInput jarInput ->
                handleJarInput(context, jarInput, outputProvider)
            }
        }
    }

    void handleJarInput(Context context, JarInput jarInput, TransformOutputProvider outputProvider) {
        String destName = jarInput.file.name

        /**截取文件路径的 md5 值重命名输出文件,因为可能同名,会覆盖*/
        def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath).substring(0, 8)
        /** 获取 jar 名字*/
        if (destName.endsWith(".jar")) {
            destName = destName.substring(0, destName.length() - 4)
        }

        File dest = outputProvider.getContentLocation(destName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
        def modifiedJar = null
        if (!sensorsAnalyticsExtension.disableAppClick) {
            modifiedJar = MkAnalyticsClassModifier.modifyJar(jarInput.file, context.getTemporaryDir(), true)
        }

        if (modifiedJar == null) {
            modifiedJar = jarInput.file
        }

        FileUtils.copyFile(modifiedJar, dest)
    }


}