package com.yangyang.mobile.test.myplugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Create by Yang Yang on 2024/5/15
 */
public class TestClassVisitor extends ClassVisitor implements Opcodes {
    public TestClassVisitor(ClassVisitor classVisitor) {
        super(ASM7, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        System.out.println("yangyang >>>>>>>>  " + "4" + ">>>>>>> 1");
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        System.out.println("yangyang >>>>>>>>  " + "4" + ">>>>>>> 2");
        return methodVisitor == null ? null : new TestMethodVisitor(methodVisitor, access, name, descriptor);
    }
}
