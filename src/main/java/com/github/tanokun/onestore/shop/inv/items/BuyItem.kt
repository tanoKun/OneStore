package com.github.tanokun.onestore.shop.inv.items

import com.github.tanokun.onestore.jecon
import com.github.tanokun.onestore.plugin
import com.github.tanokun.onestore.shop.COOL_DOWN
import com.github.tanokun.onestore.shop.Shop
import com.github.tanokun.onestore.shop.inv.OperationShopInv
import com.github.tanokun.onestore.then
import de.studiocode.invui.item.ItemProvider
import de.studiocode.invui.item.builder.ItemBuilder
import de.studiocode.invui.item.impl.BaseItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.metadata.FixedMetadataValue
import java.math.BigDecimal
import kotlin.math.roundToInt

class BuyItem(val shop: Shop): BaseItem() {
    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(Material.EMERALD)
            .setDisplayName("§a§lクリックで購入します")
            .addLoreLines(
                "§b§l買値: §6${shop.buyPrice.roundToInt()}RNT",
                "",
                "§7シフトで64個一括購入できます。")
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        if (player.hasMetadata(COOL_DOWN)) return

        if (clickType.isShiftClick && !shop.increase) {
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 10.0F, 1F)
            player.closeInventory()
            player.sendMessage("§cこのショップは同時複数購入が無効化されています")
            return
        }

        val amount = (clickType.isShiftClick then shop.product.amount * 64) ?: shop.product.amount
        val price = shop.buyPrice.roundToInt() * amount

        if ((jecon.repository?.get(player.uniqueId)?.get()?.toLong() ?: 0) < price) {
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 10.0F, 1F)
            player.closeInventory()
            player.sendMessage("§cお金が足りません")
            return
        }

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0F, 10.0F)
        shop.removeStock(amount)
        jecon.repository.set(player.uniqueId, jecon.repository?.get(player.uniqueId)?.get()?.subtract(BigDecimal(price)))
        player.sendMessage("§7商品を${amount}つ購入しました §c-${price}RNT")
        OperationShopInv(shop, player).open()
        val item = shop.product.clone()
        item.amount = amount
        player.inventory.addItem(item)

        player.setMetadata(COOL_DOWN, FixedMetadataValue(plugin, true))
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            player.removeMetadata(COOL_DOWN, plugin)
        }, 3)
    }
}