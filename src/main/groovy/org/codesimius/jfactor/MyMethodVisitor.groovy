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

	String declaringType
	int counter

	Label firstLabel
	int formalCounter

	MyMethodVisitor(int access, String name, String desc, String signature, String[] exceptions, String declaringType) {
		super(ASM9)
		this.access = access
		this.name = name
		this.desc = desc
		this.signature = signature
		this.exceptions = exceptions
		this.declaringType = declaringType
		println "$access $name $desc $signature"
		counter = -1
		formalCounter = 0
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
			case INVOKEVIRTUAL: rec("invokevirtual", "${owner.replace("/", ".")}.${name}$descriptor", countParams(descriptor))
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
		if (!firstLabel) firstLabel = label
		println "Label $label:"
	}

	void visitLineNumber(int line, Label start) {
		println "----- $line ($start)"
	}

	void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
		println "$index Local $descriptor $name ($signature), start: $start, end: $end"
		Database.instance.vars << "${methID()}\t$index\t${varID(name)}\t$descriptor\n"
		if (name != "this" && start == firstLabel) Database.instance.formals << "${methID()}\t${formalCounter++}\t${varID(name)}\n"
	}


	void rec(def opcode, def oper1 = "_", def oper2 = "_") {
		println "${methID()}\t$counter\t$opcode\t$oper1\t$oper2"
		Database.instance.opcodes << "${methID()}\t$counter\t$opcode\t$oper1\t$oper2\n"
	}

	def methID() { "<${declaringType} ${name}$desc>" }

	def varID(def name) { "${methID()}/$name" }

	int countParams(def desc) {
		if (!desc) return 0
		switch (desc[0]) {
			case 'B':
			case 'C':
			case 'D':
			case 'F':
			case 'I':
			case 'J':
			case 'S':
			case 'Z':
				return 1 + countParams(desc[1..-1])
			case 'L':
				def end = desc.indexOf(";")
				return 1 + countParams(desc[end+1..-1])
			case '[':
				return countParams(desc[1..-1])
			case '(': return countParams(desc[1..-1])
			case ')': return 0
		}
	}
}
