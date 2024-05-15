package com.yangyang.mobile.test.myplugin;



import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Create by Yang Yang on 2024/5/15
 */
public class TestMethodVisitor extends AdviceAdapter {
    protected TestMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(ASM7, methodVisitor, access, name, descriptor);
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();

        mv.visitLdcInsn("\u8fdb\u5165\u65b9\u6cd5");
        mv.visitLdcInsn("\u5411\u6240\u6709\u7684\u70e6\u607c\u8bf4\u62dc\u62dc");
        mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(POP);
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);

        mv.visitLdcInsn("\u9000\u51fa\u65b9\u6cd5");
        mv.visitLdcInsn("\u5411\u6240\u6709\u7684\u5feb\u4e50\u8bf4\u55e8\u55e8");
        mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(POP);
    }
}
