package com.github.tanokun.onestore

import dev.jorel.commandapi.CommandAPI
import jp.jyn.jecon.Jecon
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

lateinit var plugin: OneStore

lateinit var jecon: Jecon

class OneStore : JavaPlugin() {
    override fun onEnable() {
        plugin = this
        jecon = Bukkit.getPluginManager().getPlugin("Jecon") as Jecon

        CommandAPI.onEnable(plugin)
    }

    override fun onDisable() {
    }
}