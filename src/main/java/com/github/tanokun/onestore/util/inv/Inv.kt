package com.github.tanokun.onestore.util.inv

import de.studiocode.invui.gui.GUI
import de.studiocode.invui.window.impl.single.SimpleWindow
import org.bukkit.Material
import org.bukkit.entity.Player

abstract class Inv(val title: String, val player: Player) {
    abstract fun open()
}