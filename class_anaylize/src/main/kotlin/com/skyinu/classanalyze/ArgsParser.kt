package com.skyinu.classanalyze

import com.skyinu.classanalyze.model.ArgsModel
import org.apache.commons.cli.*
import java.io.File


class ArgsParser {
    private val commandOptions: Options = Options()

    companion object {
        private const val ARGS_KEY_ROOT_CLASS = "c"
        private const val ARGS_KEY_ARCHIVE = "a"
        private const val ARGS_KEY_OUTPUT = "o"
    }

    init {
        commandOptions.addOption(ARGS_KEY_ARCHIVE, true, "the archive which you want to analyze")
        commandOptions.addOption(ARGS_KEY_ROOT_CLASS, true, "java class which will be analyzed")
        commandOptions.addOption(ARGS_KEY_OUTPUT, true, "build output dir")
    }

    fun parseArgs(args: Array<String>): ArgsModel {
        val parser: CommandLineParser = DefaultParser()
        val cmd: CommandLine = parser.parse(commandOptions, args)
        var rootClass = ""
        var archiveFile: File? = null
        var outputDir: File = File(".")
        if (cmd.hasOption(ARGS_KEY_ROOT_CLASS)) {
            rootClass = cmd.getOptionValue(ARGS_KEY_ROOT_CLASS)
        }
        if (cmd.hasOption(ARGS_KEY_ARCHIVE)) {
            archiveFile = File(cmd.getOptionValue(ARGS_KEY_ARCHIVE))
        }
        if (cmd.hasOption(ARGS_KEY_OUTPUT)) {
            outputDir = File(cmd.getOptionValue(ARGS_KEY_OUTPUT))
        }
        if (rootClass.isNullOrEmpty() || archiveFile == null) {
            val header = "please input the required options"
            val footer = ""
            val formatter = HelpFormatter()
            formatter.printHelp("classanalyze", header, commandOptions, footer, true)
            throw RuntimeException("key option not set")
        }
        return ArgsModel(archiveFile, rootClass, outputDir)
    }
}
