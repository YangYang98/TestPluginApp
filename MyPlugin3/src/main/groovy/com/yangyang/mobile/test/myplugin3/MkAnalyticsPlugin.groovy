package com.yangyang.mobile.test.myplugin3

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator
import org.gradle.invocation.DefaultGradle


class MkAnalyticsPlugin implements Plugin<Project> {
    void apply(Project project) {

        MkAnalyticsExtension extension = project.extensions.create("MkAnalytics", MkAnalyticsExtension)

        boolean disableSensorsAnalyticsPlugin = false
        Properties properties = new Properties()
        if (project.rootProject.file('gradle.properties').exists()) {
            properties.load(project.rootProject.file('gradle.properties').newDataInputStream())
            disableSensorsAnalyticsPlugin = Boolean.parseBoolean(properties.getProperty("sensorsAnalytics.disablePlugin", "false"))
        }

        if (!disableSensorsAnalyticsPlugin) {
            println("------------开启yy插件--------------")
            AppExtension appExtension = project.extensions.findByType(AppExtension.class)
            appExtension.registerTransform(new MkAnalyticsTransform(project, extension))
        } else {
            println("------------您已关闭了yy插件--------------")
        }
    }
}