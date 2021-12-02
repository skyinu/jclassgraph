package com.skyinu.classanalyze.disassemble

import com.googlecode.dex2jar.tools.Dex2jarCmd
import com.skyinu.classanalyze.model.ArgsModel
import java.io.File

class ApkDisassemble(private val argsModel: ArgsModel) : IDisassemble {
    companion object {
        private const val SOURCE_JAR = ".jar"
    }

    override fun disassemble() {
        if (File(buildName()).exists()) {
            return
        }
        val args = arrayOf(
            "-o",
            argsModel.outputDir.absolutePath + File.separator + buildName(),
            argsModel.archiveFile.absolutePath
        )
        Dex2jarCmd.main(*args)
    }

    override fun getOutput(): String {
        return argsModel.outputDir.absolutePath + File.separator + buildName()
    }

    private fun buildName(): String {
        return argsModel.archiveFile.name.replace(".", "_") + SOURCE_JAR
    }
}