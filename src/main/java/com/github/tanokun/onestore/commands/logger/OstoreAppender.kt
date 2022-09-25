package com.github.tanokun.onestore.commands.logger

import org.apache.logging.log4j.core.*
import org.apache.logging.log4j.core.appender.AbstractAppender
import org.bukkit.Bukkit
import java.text.SimpleDateFormat
import java.util.*

object OstoreAppender: AbstractAppender("Test", null, null) {
    init {
        start()
    }
    override fun append(e: LogEvent) {
        Bukkit.getOnlinePlayers().forEach {
            var message = "[${SimpleDateFormat("HH:mm:ss").format(Date(e.timeMillis))} ${e.level.name()}]: ${e.message.formattedMessage}"

            if (e.loggerName != "net.minecraft.world.item.crafting.CraftingManager") {
                message = "[${SimpleDateFormat("HH:mm:ss").format(Date(e.timeMillis))} ${e.level.name()}]: [${e.loggerName}] ${e.message.formattedMessage}"
            }
        }
    }
}
