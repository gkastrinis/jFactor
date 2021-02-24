package org.codesimius.jfactor

@Singleton
class Database {

	File baseDir = new File("build/out")

	void init() {
		baseDir.mkdirs()
		this.properties.each { prop, val ->
			if(prop in ["metaClass", "class", "instance", "baseDir"]) return
			this[prop].delete()
			this[prop].createNewFile()
		}
	}

	File formals = new File(baseDir, "FormalParam.facts")
	File methodExceptions = new File(baseDir, "MethodDeclaresException.facts")
	File lineNumbers = new File(baseDir, "LineNumber.facts")

	File stmts = new File(baseDir, "Stmt.facts")
	File opcodes = new File(baseDir, "Opcode.facts")
	File vars = new File(baseDir, "Var.facts")
	File invocations = new File(baseDir, "Invocation.facts")
	File allocTypes = new File(baseDir, "AllocType.facts")
	File labels = new File(baseDir, "Label.facts")
	File handlers = new File(baseDir, "Handler.facts")
}
