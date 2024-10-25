package com.github.ringoame196_s_mcPlugin.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class TabCompleter : TabCompleter {
    override fun onTabComplete(commandSender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        return when (args.size) {
            1 -> mutableListOf(CommandConst.AUTH_COMMAND, CommandConst.CHECK_COMMAND)
            2 -> when (args[0]) {
                CommandConst.AUTH_COMMAND -> mutableListOf("[認証キー]")
                CommandConst.CHECK_COMMAND -> (Bukkit.getOnlinePlayers().map { it.name } + "@a" + "@p" + "@s" + "@r").toMutableList()
                else -> mutableListOf()
            }
            else -> mutableListOf()
        }
    }
}
