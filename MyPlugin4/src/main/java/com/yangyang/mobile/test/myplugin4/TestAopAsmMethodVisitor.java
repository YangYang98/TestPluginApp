package com.yangyang.mobile.test.myplugin4;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Create by Yang Yang on 2024/5/20
 */
public class TestAopAsmMethodVisitor extends AdviceAdapter {

    private String qualifiedName;
    private String clazzName;
    private String methodName;
    private int access;
    private String desc;
    protected TestAopAsmMethodVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor, String clazzName) {
        super(api, methodVisitor, access, name, descriptor);
        this.qualifiedName = clazzName.replaceAll("/", ".");
        this.clazzName = clazzName;
        this.methodName = name;
        this.access = access;
        this.desc = descriptor;
    }

    @Override
    protected void onMethodEnter() {
        enter();
    }

    @Override
    protected void onMethodExit(int opcode) {
        exit(opcode);
    }

    private void enter() {
        System.out.println("【MethodAdviceAdapter.enter】 => " + clazzName + ", " + methodName + ", " + access + ", " + desc);
        if (methodName.equals("<init>")) {
            return;
        }

        mv.visitLdcInsn("YangYang");
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitLdcInsn(qualifiedName + ".onCreate.onEnter timestamp = ");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(POP);
    }

    private void exit(int opcode) {
        System.out.println("【MethodAdviceAdapter.exit】 => " + opcode);
        if (methodName.equals("<init>")) {
            return;
        }

        mv.visitLdcInsn("YangYang");
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitLdcInsn(qualifiedName+ ".onCreate.onExit timestamp = ");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(POP);
    }
}
