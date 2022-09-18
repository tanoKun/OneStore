package com.github.tanokun.onestore

import com.github.tanokun.onestore.commands.StoreCommand
import com.github.tanokun.onestore.listeners.Test
import com.github.tanokun.onestore.shop.ShopManager
import com.github.tanokun.onestore.util.database.Database
import dev.jorel.commandapi.CommandAPI
import jp.jyn.jecon.Jecon
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

lateinit var plugin: OneStore

lateinit var jecon: Jecon

lateinit var db: Database

lateinit var shopManager: ShopManager

class OneStore : JavaPlugin() {
    override fun onEnable() {
        plugin = this
        jecon = Bukkit.getPluginManager().getPlugin("Jecon") as Jecon
        db = Database()
        shopManager = ShopManager()

        CommandAPI.onEnable(plugin)

        registerCommands()
        registerListeners()
    }

    override fun onDisable() {
        shopManager.shops.forEach {
            db.saveShop(it.value)
        }

        db.close()
}

    private fun registerCommands() {
        StoreCommand()
    }


    private fun registerListeners() {
        Bukkit.getPluginManager().registerEvents(Test(), this)
    }
}