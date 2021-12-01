package com.skyinu.classanalyze

import com.skyinu.classanalyze.model.ArgsModel
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import java.lang.RuntimeException


class ArgsParser {
    private val commandOptions: Options = Options()

    companion object {
        private const val ARGS_KEY_ROOT_CLASS = "c"
    }

    init {
        commandOptions.addOption(ARGS_KEY_ROOT_CLASS, true, "java class which will be analyzed")
    }

    fun parseArgs(args: Array<String>): ArgsModel {
        val parser: CommandLineParser = DefaultParser()
        val cmd: CommandLine = parser.parse(commandOptions, args)
        val argsModel = ArgsModel()
        if (cmd.hasOption(ARGS_KEY_ROOT_CLASS)) {
            argsModel.rootClass = cmd.getOptionValue(ARGS_KEY_ROOT_CLASS)
            return argsModel
        } else {
            throw RuntimeException("key options not set")
        }
    }
}