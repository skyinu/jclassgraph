package com.skyinu.classanalyze.utils

import java.io.File

object ClassUtils {
    fun classPathToName(classPath: String): String {
        return classPath.replace(File.separator, ".").replace("/", ".")
    }
}