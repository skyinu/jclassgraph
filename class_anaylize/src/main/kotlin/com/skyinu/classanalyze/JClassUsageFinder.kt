package com.skyinu.classanalyze

class JClassUsageFinder {
    fun findUsage(args: Array<String>) {
        val argsModel = ArgsParser().parseArgs(args)
        println("options is $argsModel" )
    }
}