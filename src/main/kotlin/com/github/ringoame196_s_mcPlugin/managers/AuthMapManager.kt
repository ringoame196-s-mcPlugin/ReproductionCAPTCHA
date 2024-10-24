package com.github.ringoame196_s_mcPlugin.managers

import com.github.ringoame196_s_mcPlugin.ImageRenderer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.util.Random

class AuthMapManager {
    fun makeAuthMap(authKey: String): ItemStack {
        val authImage = makeAuthImage(authKey)
        val mapItem = ItemStack(Material.FILLED_MAP) // 使用するマップアイテム
        val mapMeta = mapItem.itemMeta as MapMeta // MapMetaとしてキャスト

        // 新しいMapViewを作成
        val mapView = Bukkit.getWorld("world")?.let { Bukkit.createMap(it) } // ワールド名を指定
        mapView?.addRenderer(ImageRenderer(authImage)) // レンダラーを追加

        // Map IDを設定
        mapMeta.mapView = mapView
        mapItem.itemMeta = mapMeta

        return mapItem
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
