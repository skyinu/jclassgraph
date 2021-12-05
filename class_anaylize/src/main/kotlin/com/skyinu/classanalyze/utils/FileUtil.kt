package com.skyinu.classanalyze.utils

import sun.net.www.protocol.file.FileURLConnection
import java.io.*
import java.net.JarURLConnection
import java.net.URL

object FileUtil {
    @Throws(IOException::class)
    fun loadRecourseFromJarByFolder(folderPath: String, targetFolderPath: String, clazz: Class<*>) {
        val url = clazz.getResource(folderPath)
        val urlConnection = url.openConnection()
        if (urlConnection is FileURLConnection) {
            copyFileResources(url, folderPath, targetFolderPath, clazz)
        } else if (urlConnection is JarURLConnection) {
            copyJarResources(urlConnection, folderPath, targetFolderPath, clazz)
        }
    }

    @Throws(IOException::class)
    private fun copyFileResources(url: URL, folderPath: String, targetFolderPath: String, clazz: Class<*>) {
        val root = File(url.path)
        if (root.isDirectory) {
            val files = root.listFiles()
            for (file in files) {
                if (file.isDirectory) {
                    loadRecourseFromJarByFolder(folderPath + "/" + file.name, targetFolderPath, clazz)
                } else {
                    loadRecourseFromJar(folderPath + "/" + file.name, targetFolderPath, clazz)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun copyJarResources(
        jarURLConnection: JarURLConnection,
        folderPath: String,
        targetFolderPath: String,
        clazz: Class<*>
    ) {
        val jarFile = jarURLConnection.jarFile
        val entrys = jarFile.entries()
        while (entrys.hasMoreElements()) {
            val entry = entrys.nextElement()
            if (entry.name.startsWith(jarURLConnection.entryName) && !entry.name.endsWith("/")) {
                loadRecourseFromJar("/" + entry.name, targetFolderPath, clazz)
            }
        }
        jarFile.close()
    }

    @Throws(IOException::class)
    fun loadRecourseFromJar(path: String, recourseFolder: String, clazz: Class<*>) {
        require(path.startsWith("/")) { "The path has to be absolute (start with '/')." }
        require(!path.endsWith("/")) { "The path has to be absolute (cat not end with '/')." }
        val index = path.lastIndexOf('/')
        var filename = path.substring(index + 1)
        val folderPath = recourseFolder + path.substring(0, index + 1)
        val dir = File(folderPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        filename = folderPath + filename
        val file = File(filename)
        if (!file.exists() && !file.createNewFile()) {
            return
        }
        val buffer = ByteArray(4096)
        var readBytes: Int
        val url = clazz.getResource(path)
        val urlConnection = url.openConnection()
        val `is` = urlConnection.getInputStream() ?: throw FileNotFoundException("File $path was not found inside JAR.")
        val os: OutputStream = FileOutputStream(file)
        try {
            while (`is`.read(buffer).also { readBytes = it } != -1) {
                os.write(buffer, 0, readBytes)
            }
        } finally {
            os.close()
            `is`.close()
        }
    }
}