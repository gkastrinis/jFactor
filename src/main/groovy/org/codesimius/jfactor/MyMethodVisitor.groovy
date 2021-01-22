package org.codesimius.jfactor

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class MyMethodVisitor extends MethodVisitor implements Opcodes {

	int access
	String name
	String desc
	String signature
	String[] exceptions

	int counter

	MyMethodVisitor(int access, String name, String desc, String signature, String[] exceptions) {
		super(ASM9)
		this.access = access
		this.name = name
		this.desc = desc
		this.signature = signature
		this.exceptions = exceptions
		println "$access $name $desc $signature"
		counter = -1
	}

	void visitInsn(int opcode) {
		counter++
		switch (opcode) {
			case ICONST_0: rec("iconst_0")
				break
			case POP2: rec("pop2")
				break
			case DMUL: rec("dmul")
				break
			case IRETURN: rec("ireturn")
				break
			case DRETURN: rec("dreturn")
				break
			case RETURN: rec("return")
				break
			default: rec(opcode, "??", "??")
				break
		}
	}

	void visitVarInsn(int opcode, int var) {
		counter++
		switch (opcode) {
			case ALOAD: rec("aload", var)
				break
			case ILOAD: rec("iload", var)
				break
			case DLOAD: rec("dload", var)
				break
			case DSTORE: rec("dstore", var)
				break
			default: rec(opcode, var, "??")
				break
		}
	}

	void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
		counter++
		switch (opcode) {
			case INVOKESPECIAL: rec("invokespecial", "${owner.replace("/", ".")}.${name}$descriptor")
				break
			case INVOKEVIRTUAL: rec("invokevirtual", "${owner.replace("/", ".")}.${name}$descriptor")
				break
			default: rec(opcode, "??", "??")
				break
		}
	}

	void visitLdcInsn(Object value) {
		counter++
		rec("ldc", value)
	}

	void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
		counter++
		switch (opcode) {
			case PUTFIELD: rec("putfield", "<$owner: $descriptor $name>")
				break
			case GETSTATIC: rec("getstatic", "<$owner: $descriptor $name>")
				break
			default: rec(opcode, "??", "??")
				break
		}
	}

	void visitIntInsn(int opcode, int operand) {
		counter++
		switch (opcode) {
			case BIPUSH: rec("bipush", operand)
				break
			default: rec(opcode, "??", "??")
				break
		}
	}

	void visitLabel(Label label) {
		println "Label $label:"
	}

	void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
		println "$index Local $descriptor $name ($signature), start: $start, end: $end"
		Database.instance.vars << "${this.name}\t$index\t$name\n"
	}

	void visitLineNumber(int line, Label start) {
		println "----- $line ($start)"
	}

	void rec(def opcode, def oper1 = "_", def oper2 = "_") {
		printf("$counter: %h $oper1 $oper2\n", opcode)
		Database.instance.opcodes << "$name\t$counter\t$opcode\t$oper1\t$oper2\n"
	}
}
