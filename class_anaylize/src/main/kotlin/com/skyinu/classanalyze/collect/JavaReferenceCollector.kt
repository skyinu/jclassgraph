package com.skyinu.classanalyze.collect

import com.skyinu.classanalyze.model.ClassNode
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.FileHeader
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import java.io.InputStream
import java.util.*


class JavaReferenceCollector(
    private val analyzeParams: Boolean,
    private val output: String
) {

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
            classStream.close()
        }
    }

    private fun parseReference(classStream: InputStream) {
        val reader = ClassReader(classStream)
        reader.accept(JavaClassVisitor(Opcodes.ASM5, referenceHashMap, analyzeParams), 0)
    }
}