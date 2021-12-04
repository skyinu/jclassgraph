package com.skyinu.classanalyze.model

import java.util.*

class ClassNode(private val className: String) {
    private val inClassList: ArrayList<ClassNode> = ArrayList<ClassNode>()
    private val outClassList: ArrayList<ClassNode> = ArrayList<ClassNode>()

    fun addInClass(node: ClassNode) {
        if (inClassList.contains(node)) {
            return
        }
        inClassList.add(node)
    }

    fun addOutClass(node: ClassNode) {
        if (outClassList.contains(node)) {
            return
        }
        outClassList.add(node)
    }

    fun nodeName() = className

    fun getInClassList(): List<ClassNode> {
        return inClassList
    }

    fun getOutClassList(): List<ClassNode> {
        return outClassList
    }
}