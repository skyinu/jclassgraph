package com.skyinu.classanalyze.collect

import com.skyinu.classanalyze.model.ClassNode
import jdk.internal.org.objectweb.asm.ClassReader
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.FileHeader
import java.io.InputStream
import java.util.*


class JavaReferenceCollector(private val output: String) {

    private val referenceHashMap = HashMap<String, ClassNode>()

    companion object {
        private const val CLASS_SUFFIX = ".class"
    }

    fun collect() {
        travelJar()
    }

    private fun travelJar() {
        val jarFile = ZipFile(output)
        val fileHeaders = jarFile.fileHeaders
        fileHeaders.stream().filter { it.fileName.endsWith(CLASS_SUFFIX) }.forEach { fileHeader: FileHeader ->
            val classStream = jarFile.getInputStream(fileHeader)
            println("handle class = " + fileHeader.fileName)
            parseReference(classStream)
        }
    }

    private fun parseReference(classStream: InputStream) {
        val reader = ClassReader(classStream)
    }
}