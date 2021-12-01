package com.skyinu.classanalyze.model

import java.io.File

data class ArgsModel(val archiveFile: File, val rootClass: String, val outputDir: File) {
    override fun toString(): String {
        return "ArgsModel(archiveFile=${archiveFile.absolutePath}, " +
                "rootClass='$rootClass', " +
                "outputDir=${outputDir.absolutePath})"
    }
}