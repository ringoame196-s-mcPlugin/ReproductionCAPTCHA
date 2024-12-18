package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.managers.AuthPlayerManager
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.Plugin

class PlayerAct(plugin: Plugin) : Listener {
    private val authPlayerManager = AuthPlayerManager(plugin)

    private fun actProhibiting(e: Cancellable) {
        if (e !is PlayerEvent) return
        val player = e.player
        val uuid = player.uniqueId.toString()
        val title = "${ChatColor.RED}BOT認証をしてください"
        val subTitle = "${ChatColor.YELLOW}/captcha auth <入力>"
        val sound = Sound.BLOCK_NOTE_BLOCK_BELL

        if (authPlayerManager.isActProhibitingPlayer(uuid)) {
            e.isCancelled = true
            player.sendTitle(title, subTitle)
            player.playSound(player, sound, 1f, 1f)
        }
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val to = e.to ?: return
        val from = e.from

        if (to.x != from.x || to.y != from.y || to.z != from.z) {
            actProhibiting(e)
        }
    }

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        actProhibiting(e)
    }

    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {
        actProhibiting(e)
    }

    @EventHandler
    fun onBlockPlayerInteract(e: PlayerInteractEvent) {
        actProhibiting(e)
    }
}
