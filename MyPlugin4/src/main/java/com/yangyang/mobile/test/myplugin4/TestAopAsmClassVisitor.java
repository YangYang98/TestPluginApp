package com.yangyang.mobile.test.myplugin4;

import static org.objectweb.asm.Opcodes.ASM9;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * Create by Yang Yang on 2024/5/20
 */
public class TestAopAsmClassVisitor extends ClassVisitor {

    private String clazzName;

    public TestAopAsmClassVisitor(ClassVisitor classVisitor) {
        super(ASM9, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);

        this.clazzName = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);

        return new TestAopAsmMethodVisitor(api, methodVisitor, access, name, descriptor, this.clazzName);
    }
}
