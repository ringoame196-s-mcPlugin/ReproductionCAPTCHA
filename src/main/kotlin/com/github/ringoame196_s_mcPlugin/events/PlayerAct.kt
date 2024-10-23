package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.managers.DataBaseManager
import org.bukkit.ChatColor
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.Plugin

class PlayerAct(plugin: Plugin) : Listener {
    private val dataBaseManager = DataBaseManager()

    private fun actProhibiting(e: Cancellable) {
        if (e !is PlayerEvent) return
        val player = e.player
        val uuid = player.uniqueId.toString()
        val message = "${ChatColor.RED}認証を完了させてください"

        if (isActProhibitingPlayer(uuid)) {
            e.isCancelled = true
            player.sendMessage(message)
        }
    }

    private fun isActProhibitingPlayer(uuid: String): Boolean {
        return false
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        actProhibiting(e)
    }

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        actProhibiting(e)
    }

    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {
        actProhibiting(e)
    }
}
