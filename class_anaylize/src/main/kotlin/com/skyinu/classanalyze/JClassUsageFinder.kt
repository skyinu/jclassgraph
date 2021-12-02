package com.skyinu.classanalyze

import com.skyinu.classanalyze.collect.JavaReferenceCollector
import com.skyinu.classanalyze.disassemble.ApkDisassemble
import com.skyinu.classanalyze.model.ArgsModel
import java.lang.RuntimeException

class JClassUsageFinder {
    fun findUsage(args: Array<String>) {
        val argsModel = ArgsParser().parseArgs(args)
        println("options is $argsModel")
        val srcDir = disassembleArchive(argsModel)
        val collector = JavaReferenceCollector(srcDir)
        println("start collect reference")
        collector.collect()
    }

    private fun disassembleArchive(argsModel: ArgsModel): String {
        val disassemble = if (argsModel.archiveFile.absolutePath.endsWith(".apk")) {
            ApkDisassemble(argsModel)
        } else {
            throw RuntimeException("archive is not supported")
        }
        disassemble.disassemble()
        return disassemble.getSrcDir()
    }
}