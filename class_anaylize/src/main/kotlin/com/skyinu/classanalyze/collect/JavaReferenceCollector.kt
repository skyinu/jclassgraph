package com.skyinu.classanalyze.collect

import com.skyinu.classanalyze.model.ClassNode
import java.io.File
import java.util.*

class JavaReferenceCollector(private val srcDir: String) {

    private val referenceHashMap = HashMap<String, ClassNode>()

    companion object {
        private const val JAVA_SUFFIX = ".java"
    }

    fun collect() {
        travelDir()
    }

    private fun travelDir() {
        val srcDirFile = File(srcDir)
        srcDirFile.walk()
            .filter { it.isFile }
            .filter { it.name.endsWith(JAVA_SUFFIX) }
            .forEach {
                val className = getClassName(it, srcDir)
                println("handle class = $className")
                parseReference(it, className)
            }
    }

    private fun parseReference(classFile: File, className: String) {
    }

    private fun getClassName(classFile: File, itemSrc: String): String {
        return classFile.absolutePath.substring(itemSrc.length + 1).replace(File.separator, ".")
    }
}