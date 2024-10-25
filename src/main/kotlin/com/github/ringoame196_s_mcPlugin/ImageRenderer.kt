package com.github.ringoame196_s_mcPlugin

import org.bukkit.entity.Player
import org.bukkit.map.MapCanvas
import org.bukkit.map.MapRenderer
import org.bukkit.map.MapView
import java.awt.image.BufferedImage

class ImageRenderer(private val image: BufferedImage) : MapRenderer() {
    override fun render(map: MapView, canvas: MapCanvas, player: Player) {
        canvas.drawImage(0, 0, image)
    }
}
