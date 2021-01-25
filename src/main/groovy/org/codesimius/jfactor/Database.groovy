package org.codesimius.jfactor

@Singleton
class Database {

	File baseDir = new File("build")

	void init() {
		this.properties.each { prop, val ->
			if(prop in ["metaClass", "class", "instance", "baseDir"]) return
			this[prop].delete()
		}
	}

	File opcodes = new File(baseDir, "Opcode.facts")
	File vars = new File(baseDir, "Var.facts")
	File formals = new File(baseDir, "FormalParam.facts")
	File calls = new File(baseDir, "Call.facts")
}
