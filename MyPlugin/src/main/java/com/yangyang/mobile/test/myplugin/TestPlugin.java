package com.yangyang.mobile.test.myplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Create by Yang Yang on 2024/5/14
 */
public class TestPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        System.out.println("yangyang >>>>>>>>  " + this.getClass().getName());
    }
}
