package com.yangyang.mobile.test.myplugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Create by Yang Yang on 2024/5/14
 */
public class TestPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        System.out.println("yangyang >>>>>>>>  " + this.getClass().getName());

        AppExtension appExtension = project.getExtensions().findByType(AppExtension.class);
        if (appExtension == null) {
            return;
        }
        appExtension.registerTransform(new TestTransform());
    }
}
