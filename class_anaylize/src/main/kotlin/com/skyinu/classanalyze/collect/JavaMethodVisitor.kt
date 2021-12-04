package com.skyinu.classanalyze.collect

import com.skyinu.classanalyze.model.ClassNode
import com.skyinu.classanalyze.utils.ClassUtils
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import java.util.HashMap

class JavaMethodVisitor(
    mv: MethodVisitor?,
    private val referenceHashMap: HashMap<String, ClassNode>,
    private val className: String,
    private val methodName: String,
    private val analyzeParams: Boolean,
    private val shouldIgnoreOfficial: Boolean
) :
    MethodVisitor(Opcodes.ASM5, mv) {

    private val currentClassNode = referenceHashMap[className]!!

    override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, desc: String?, itf: Boolean) {
        super.visitMethodInsn(opcode, owner, name, desc, itf)
        val ownerName = ClassUtils.classPathToName(owner!!)
        if (shouldIgnoreOfficial && ClassUtils.shouldFilter(ownerName)) {
            return
        }
        val callClass = createNode(owner)
        if (ownerName == className) {
            return
        }
        ClassUtils.bindClassNode(currentClassNode, callClass)
        if (analyzeParams) {
            val argumentType = Type.getArgumentTypes(desc)
            argumentType.forEach {
                val argumentNode = createNodeFromType(it)
                argumentNode?.let {
                    ClassUtils.bindClassNode(currentClassNode, argumentNode)
                }

            }
            val returnType = Type.getReturnType(desc)
            val returnNode = createNodeFromType(returnType)
            returnNode?.let {
                ClassUtils.bindClassNode(currentClassNode, returnNode)
            }
        }
    }

    override fun visitFieldInsn(opcode: Int, owner: String?, name: String?, desc: String?) {
        super.visitFieldInsn(opcode, owner, name, desc)
//        val ownerName = ClassUtils.classPathToName(owner!!)
//        if (ownerName != className) {
//            val callClass = createNode(ownerName)
//            referenceHashMap[className]!!.addOutClass(callClass)
//        }
    }

    private fun createNodeFromType(type: Type): ClassNode? {
        var className = ""
        className = when (type.sort) {
            Type.OBJECT -> {
                type.className
            }
            Type.ARRAY -> {
                val elementType = type.elementType
                return createNodeFromType(elementType)
            }
            else -> {
                return null
            }
        }
        return createNode(className)
    }

    private fun createNode(classPath: String): ClassNode {
        val name = ClassUtils.classPathToName(classPath)
        val classNode = referenceHashMap.getOrDefault(name, ClassNode(name))
        referenceHashMap.putIfAbsent(name, classNode)
        return classNode
    }
}