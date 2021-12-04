package com.skyinu.classanalyze.model

import java.io.File

data class ArgsModel(val archiveFile: File, val rootClass: String, val outputDir: File, val analyzeParams: Boolean) {
    override fun toString(): String {
        return "ArgsModel(archiveFile=${archiveFile.absolutePath}, " +
                "rootClass='$rootClass', " +
                "outputDir=${outputDir.absolutePath}," +
                "analyzeParams = $analyzeParams)"
    }
}