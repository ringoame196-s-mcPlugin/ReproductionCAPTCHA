package com.github.ringoame196_s_mcPlugin.managers

import com.github.ringoame196_s_mcPlugin.DataConst
import com.github.ringoame196_s_mcPlugin.ImageRenderer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.plugin.Plugin
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.util.Random

class AuthMapManager(plugin: Plugin) {
    private val authPlayerManager = AuthPlayerManager(plugin)

    private fun creationAuthKey(): String {
        val minValue = 0x10000 // 16進数の "10000"
        val maxValue = 0xFFFFF // 16進数の "ZZZZZ"
        return String.format("%05X", (minValue..maxValue).random())
    }

    private fun isHaveAuthMap(player: Player, mapID: Int): Boolean {
        for (item in player.inventory) {
            item ?: continue
            if (item.type != Material.FILLED_MAP) continue
            val meta = item.itemMeta as MapMeta
            if (meta.displayName != DataConst.AUTH_MAP_NAME) continue
            if (meta.mapId == mapID) return true
        }
        return false
    }

    fun giveAuthMap(player: Player) {
        val authKey = creationAuthKey()
        val uuid = player.uniqueId.toString()
        val authImage = makeAuthImage(authKey)
        val mapID = authPlayerManager.acquisitionMapID(uuid)
        if (mapID == null || !isHaveAuthMap(player, mapID.toInt())) {
            makeAuthMap(authKey, player, authImage)
        } else {
            updateMakeAuthMap(authKey, mapID.toInt(), uuid, authImage)
        }
    }

    private fun makeAuthMap(authKey: String, player: Player, authImage: BufferedImage) {
        val uuid = player.uniqueId.toString()
        val mapItem = ItemStack(Material.FILLED_MAP) // 使用するマップアイテム
        val mapMeta = mapItem.itemMeta as MapMeta // MapMetaとしてキャスト

        mapMeta.setDisplayName(DataConst.AUTH_MAP_NAME)

        // 新しいMapViewを作成
        val mapView = Bukkit.createMap(player.world)

        val mapID = mapView.id
        // Map IDを設定
        mapMeta.mapView = mapView
        mapItem.itemMeta = mapMeta

        changeMapImage(mapID, authImage)

        player.inventory.addItem(mapItem)

        authPlayerManager.saveToDB(uuid, authKey, mapID)
    }

    private fun updateMakeAuthMap(authKey: String, mapID: Int, uuid: String, authImage: BufferedImage) {
        changeMapImage(mapID, authImage)
        authPlayerManager.saveToDB(uuid, authKey, mapID)
    }

    private fun changeMapImage(mapID: Int, authImage: BufferedImage) {
        val mapView = Bukkit.getMap(mapID)
        mapView?.addRenderer(ImageRenderer(authImage)) // レンダラーを追加
    }

    private fun makeAuthImage(authKey: String): BufferedImage {
        // 画像の作成
        val width = 128
        val height = 128
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g: Graphics2D = image.createGraphics()

        // 背景を白に設定
        g.color = Color.WHITE
        g.fillRect(0, 0, width, height)

        // アンチエイリアスを有効化
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // フォントリスト（読みづらいフォントを選択）
        val fonts = listOf(
            Font("Comic Sans MS", Font.PLAIN, 16),
            Font("Brush Script MT", Font.ITALIC, 16),
            Font("Serif", Font.BOLD, 16),
            Font("Courier New", Font.BOLD or Font.ITALIC, 16)
        )

        // ランダムに文字を描画する
        var x = 10
        val y = (height / 2) + 24 // 中央付近に配置

        for (i in authKey.indices) {
            val char = authKey[i].toString()

            // ランダムにフォントを選択
            val randomFont = fonts[Random().nextInt(fonts.size)]
            g.font = randomFont

            // 色もランダムに設定
            g.color = Color(Random().nextInt(255), Random().nextInt(255), Random().nextInt(255))

            // 軽い回転効果を加える（読みづらさ向上）
            val rotation = (Random().nextDouble() - 0.5) * 0.2
            g.rotate(rotation, x.toDouble(), y.toDouble())

            // 文字を描画
            g.drawString(char, x, y)

            // 回転を元に戻す
            g.rotate(-rotation, x.toDouble(), y.toDouble())

            // 次の文字の位置へ
            x += g.fontMetrics.stringWidth(char) + Random().nextInt(5)
        }

        // リソース解放
        g.dispose()

        return image
    }
}
