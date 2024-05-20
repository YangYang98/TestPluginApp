package com.yangyang.mobile.test.myplugin4

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class TestAopAsmPlugin implements Plugin<Project> {
    void apply(Project project) {

        AppExtension appExtension = project.extensions.findByType(AppExtension.class)
        appExtension.registerTransform(new TestAopAsmTransform())
    }
}