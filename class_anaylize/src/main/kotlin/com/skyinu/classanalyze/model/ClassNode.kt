package com.skyinu.classanalyze.model

import java.util.*

class ClassNode(private val className: String) {
    private val inClassList: ArrayList<ClassNode> = ArrayList<ClassNode>()
    private val outClassList: ArrayList<ClassNode> = ArrayList<ClassNode>()

    fun addInClass(node: ClassNode) {
        inClassList.add(node)
    }

    fun addOutClass(node: ClassNode) {
        outClassList.add(node)
    }

    fun getInClassList(): List<ClassNode> {
        return inClassList
    }

    fun getOutClassList(): List<ClassNode> {
        return outClassList
    }
}