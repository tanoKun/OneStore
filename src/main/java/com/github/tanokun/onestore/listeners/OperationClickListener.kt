package com.github.tanokun.onestore.listeners

import com.github.tanokun.onestore.shop.inv.OperationShopInv
import com.github.tanokun.onestore.shopManager
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class OperationClickListener: Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onClick(e: PlayerInteractEvent) = shopManager.lHolograms[e.clickedBlock?.location]?.let {
        e.isCancelled = true
        OperationShopInv(it.first, e.player).open()
    }
}