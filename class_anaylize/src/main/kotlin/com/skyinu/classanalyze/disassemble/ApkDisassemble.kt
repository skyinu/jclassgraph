package com.skyinu.classanalyze.disassemble

import com.skyinu.classanalyze.model.ArgsModel
import jadx.cli.JadxCLI

class ApkDisassemble(val argsModel: ArgsModel) : IDisassemble {
    override fun disassemble() {
        val args = arrayOf(argsModel.archiveFile.absolutePath, "-d", argsModel.outputDir.absolutePath)
        JadxCLI.main(args)
    }

    override fun getSrcDir(): List<String> {
        return arrayListOf()
    }
}