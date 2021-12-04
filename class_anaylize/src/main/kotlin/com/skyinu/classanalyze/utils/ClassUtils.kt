package com.skyinu.classanalyze.utils

import com.skyinu.classanalyze.model.ClassNode
import java.io.File

object ClassUtils {
    private val OFFICIAL_PACKAGE_PREFIXS = arrayOf("java.", "kotlin.", "android.", "androidx.")
     var shouldIgnoreOfficial = false
    fun classPathToName(classPath: String): String {
        return classPath.replace(File.separator, ".").replace("/", ".")
    }

    fun bindClassNode(source: ClassNode, target: ClassNode) {
        if (shouldIgnoreOfficial && !shouldFilter(target.nodeName())) {
            source.addOutClass(target)
            target.addInClass(source)
        }
    }

    fun shouldFilter(className: String): Boolean {
        OFFICIAL_PACKAGE_PREFIXS.forEach {
            if (className.startsWith(it, true)) {
                return true
            }
        }
        return false
    }
}