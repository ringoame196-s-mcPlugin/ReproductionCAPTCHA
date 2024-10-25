package com.github.ringoame196_s_mcPlugin.commands

import com.github.ringoame196_s_mcPlugin.managers.AuthMapManager
import com.github.ringoame196_s_mcPlugin.managers.AuthPlayerManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class Command(plugin: Plugin) : CommandExecutor {
    private val authPlayerManager = AuthPlayerManager(plugin)
    private val authMapManager = AuthMapManager(plugin)

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size < 2) return false
        val subCommand = args[0]
        val input = args[1]

        when (subCommand) {
            CommandConst.AUTH_COMMAND -> authCommand(sender, input)
            CommandConst.CHECK_COMMAND -> checkCommand(sender, input)
            else -> {
                val message = "${ChatColor.RED}コマンド構文が間違っています"
                sender.sendMessage(message)
            }
        }
        return true
    }

    private fun authCommand(sender: CommandSender, input: String) {
        if (sender !is Player) {
            val message = "${ChatColor.RED}このコマンドはプレイヤーのみ実行可能です"
            sender.sendMessage(message)
            return
        }
        val uuid = sender.uniqueId.toString()
        val sound = Sound.BLOCK_ANVIL_USE

        if (!authPlayerManager.isActProhibitingPlayer(uuid)) return

        val auth = authPlayerManager.isAuthenticated(uuid, input)

        if (auth) {
            val message = "${ChatColor.YELLOW}認証完了しました"
            authPlayerManager.deleteToDB(uuid)
            sender.sendMessage(message)
            sender.playSound(sender, sound, 1f, 1f)
        } else authSettingProcess(sender)
    }

    private fun checkCommand(sender: CommandSender, input: String) {
        if (!sender.isOp) {
            val message = "${ChatColor.RED}権限がありません"
            sender.sendMessage(message)
            return
        }
        if (input.contains("@e")) {
            val message = "エンティティを含めることはできません"
            sender.sendMessage(message)
            return
        }

        val selectPlayers = Bukkit.selectEntities(sender, input)

        for (selectPlayer in selectPlayers) {
            selectPlayer as? Player ?: continue
            if (authPlayerManager.isActProhibitingPlayer(selectPlayer.uniqueId.toString())) continue
            authSettingProcess(selectPlayer)
        }
    }

    private fun authSettingProcess(selectPlayer: Player) {
        val message = "${ChatColor.YELLOW}BOT認証をしてください \n/captcha auth <入力>"
        authMapManager.giveAuthMap(selectPlayer)
        selectPlayer.sendMessage(message)
    }
}
