package com.skyinu.classanalyze.disassemble

import com.skyinu.classanalyze.model.ArgsModel
import jadx.cli.JadxCLI
import java.io.File

class ApkDisassemble(private val argsModel: ArgsModel) : IDisassemble {
    companion object {
        private const val SOURCE_DIR = "sources"
    }

    override fun disassemble() {
        if (File(getSrcDir()).exists()) {
            return
        }
        val args = arrayOf(argsModel.archiveFile.absolutePath, "-d", argsModel.outputDir.absolutePath, "-r")
        val result = JadxCLI.execute(args)
        println("disassemble result = $result")
    }

    override fun getSrcDir(): String {
        return argsModel.outputDir.absolutePath + File.separator + SOURCE_DIR
    }
}