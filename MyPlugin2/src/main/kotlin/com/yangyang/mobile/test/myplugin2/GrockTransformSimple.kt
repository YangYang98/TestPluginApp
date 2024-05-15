package com.yangyang.mobile.test.myplugin2

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.io.FileUtils
import org.gradle.internal.impldep.com.google.common.collect.ImmutableSet


/**
 * Create by Yang Yang on 2024/5/15
 */
class GrockTransformSimple(val trackConfig: GrockTrackTimeExtension) : Transform() {
    override fun getName(): String {
        return "GrockTransformSimple"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return ImmutableSet.of(QualifiedContent.Scope.PROJECT)
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun transform(transformInvocation: TransformInvocation) {
        val inputs = transformInvocation.inputs
        val output = transformInvocation.outputProvider
        inputs.forEach { input ->
            //依赖的jar包内容保持不动
            input.jarInputs.forEach { jar ->
                //传递给下一个任务
                val dest = output.getContentLocation(
                    jar.name, jar.contentTypes, jar.scopes, Format.JAR
                )
                FileUtils.copyFile(jar.file, dest)
            }
            //当前项目源码
            input.directoryInputs.forEach { dirInput ->
                //处理字节码
                handlerDirInput(dirInput)
                //传递给下一个任务
                val dest = output.getContentLocation(
                    dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY
                )
                FileUtils.copyDirectory(dirInput.file, dest)
            }
        }
    }

    private fun handlerDirInput(dirInput: DirectoryInput) {
        /*val files = fileRecurse(dirInput.file.absolutePath, mutableListOf())
        val suffix = formatPackageName(trackConfig.packageName) + "\\" + trackConfig.className + ".class"
        files?.forEach { classFile ->
            if (classFile.absolutePath.endsWith(suffix)) {
                //确认class文件
            }
        }*/
    }


}