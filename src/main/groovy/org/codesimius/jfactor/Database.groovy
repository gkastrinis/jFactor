package org.codesimius.jfactor

@Singleton
class Database {

	void init() {
		opcodes.delete()
		vars.delete()
	}

	File opcodes = new File("Opcode.facts")
	File vars = new File("Var.facts")
}
