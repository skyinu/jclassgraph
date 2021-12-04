package com.skyinu.classanalyze.collect

import com.skyinu.classanalyze.model.ClassNode
import com.skyinu.classanalyze.utils.ClassUtils
import org.objectweb.asm.*
import org.objectweb.asm.signature.SignatureReader
import org.objectweb.asm.signature.SignatureVisitor
import java.util.HashMap

class JavaClassVisitor(
    api: Int,
    private val referenceHashMap: HashMap<String, ClassNode>,
    private val analyzeParams: Boolean
) : ClassVisitor(api) {
    private var className = ""
    private lateinit var currentNode: ClassNode
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.className = ClassUtils.classPathToName(name!!)
        val rootNode = createNode(name)
        currentNode = rootNode
        val superNode = createNode(superName!!)
        rootNode.addOutClass(superNode)
        superNode.addInClass(rootNode)
        interfaces?.forEach {
            val interfaceNode = createNode(it)
            rootNode.addOutClass(interfaceNode)
            interfaceNode.addInClass(rootNode)
        }
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor? {
        if (!className.contains("MemoryFileParser")) {
            return null
        }
        signature?.let {
            val signatureReader = SignatureReader(it)
            signatureReader.accept(JavaSignatureVisitor())
        }
        val visitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        return JavaMethodVisitor(visitor, referenceHashMap, className, name!!, analyzeParams)
    }

    inner class JavaSignatureVisitor() : SignatureVisitor(Opcodes.ASM5) {

        override fun visitClassType(name: String?) {
            super.visitClassType(name)
            val sigClass = createNode(name!!)
            currentNode.addOutClass(sigClass)
            sigClass.addInClass(currentNode)
        }
    }

    private fun createNode(classPath: String): ClassNode {
        val name = ClassUtils.classPathToName(classPath)
        val classNode = referenceHashMap.getOrDefault(name, ClassNode(name))
        referenceHashMap.putIfAbsent(name, classNode)
        return classNode
    }
}