package com.github.tanokun.onestore

import com.github.tanokun.onestore.commands.StoreCommand
import com.github.tanokun.onestore.commands.logger.OstoreAppender
import com.github.tanokun.onestore.listeners.OperationClickListener
import com.github.tanokun.onestore.shop.ShopManager
import com.github.tanokun.onestore.util.database.Database
import dev.jorel.commandapi.CommandAPI
import jp.jyn.jecon.Jecon
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.Logger
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
        shopManager.loadHologram()

        CommandAPI.onEnable(plugin)

        registerCommands()
        registerListeners()

        logger()
    }

    override fun onDisable() {
        shopManager.shops.forEach {
            db.saveShop(it.value)
        }

        shopManager.holograms.forEach {
            db.saveHologramLocation(it.key, it.value)
        }

        val log: Logger = LogManager.getRootLogger() as Logger
        log.removeAppender(OstoreAppender)

        db.close()
}

    private fun registerCommands() {
        StoreCommand()
    }


    private fun registerListeners() {
        Bukkit.getPluginManager().registerEvents(OperationClickListener(), this)
    }

    private fun logger() {
        val log: Logger = LogManager.getRootLogger() as Logger
        log.addAppender(OstoreAppender)
    }
}

infix fun <T> Boolean.then(other: T) = if (this) other else null