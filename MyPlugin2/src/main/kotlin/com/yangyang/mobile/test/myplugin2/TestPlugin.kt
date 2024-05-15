package com.yangyang.mobile.test.myplugin2

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * Create by Yang Yang on 2024/5/15
 */
class TestPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("yangyang >>>>>>>>  " + this.javaClass.name)
        val trackTimeExtension = project.extensions.create("trackTime", GrockTrackTimeExtension::class.java)
        val transform = GrockTransformSimple(trackTimeExtension)
        val baseExtension = project.extensions.getByType(BaseExtension::class.java)
        baseExtension.registerTransform(transform)
        /*val appExtension = project.extensions.findByType(
            AppExtension::class.java
        ) ?: return
        appExtension.registerTransform(TestTransform())*/
    }
}