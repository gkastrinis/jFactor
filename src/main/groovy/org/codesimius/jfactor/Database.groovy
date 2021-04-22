package org.codesimius.jfactor

@Singleton
class Database {

	File baseDir = new File("build/out_tmp")

	void init() {
		baseDir.mkdirs()
		this.properties.each { prop, val ->
			if(prop in ["metaClass", "class", "instance", "baseDir"]) return
			this[prop].delete()
			this[prop].createNewFile()
		}
	}

	File stmts = new File(baseDir, "BytecodeStmt.facts")
	File labels = new File(baseDir, "Label.facts")
	File handlers = new File(baseDir, "Handler.facts")
	File invocations = new File(baseDir, "Invocation.facts")
	File allocTypes = new File(baseDir, "AllocType.facts")
	File incValues = new File(baseDir, "IncValue.facts")
	File fields = new File(baseDir, "Field.facts")
	File vars = new File(baseDir, "Var.facts")
	File opcodes = new File(baseDir, "Opcode.facts")

	File formals = new File(baseDir, "FORMAL_PARAM.facts")
	File methodExceptions = new File(baseDir, "MethodDeclaresException.facts")
	File lineNumbers = new File(baseDir, "LineNumber.facts")
}
