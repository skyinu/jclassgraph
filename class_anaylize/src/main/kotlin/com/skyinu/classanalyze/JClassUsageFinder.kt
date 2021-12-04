package com.skyinu.classanalyze

import com.skyinu.classanalyze.collect.JavaReferenceCollector
import com.skyinu.classanalyze.disassemble.ApkDisassemble
import com.skyinu.classanalyze.model.ArgsModel
import com.skyinu.classanalyze.render.JSONDataRender
import com.skyinu.classanalyze.utils.ClassUtils
import java.lang.RuntimeException

class JClassUsageFinder {
    fun findUsage(args: Array<String>) {
        val argsModel = ArgsParser().parseArgs(args)
        ClassUtils.shouldIgnoreOfficial = argsModel.shouldIgnoreOfficial
        println("options is $argsModel")
        val srcDir = disassembleArchive(argsModel)
        val collector = JavaReferenceCollector(argsModel.analyzeParams, argsModel.shouldIgnoreOfficial, srcDir)
        println("start collect reference")
        collector.collect()
        JSONDataRender(collector.getAllReferenceMap(), argsModel).render()
    }

    private fun disassembleArchive(argsModel: ArgsModel): String {
        val disassemble = if (argsModel.archiveFile.absolutePath.endsWith(".apk")) {
            ApkDisassemble(argsModel)
        } else {
            throw RuntimeException("archive is not supported")
        }
        disassemble.disassemble()
        return disassemble.getOutput()
    }
}