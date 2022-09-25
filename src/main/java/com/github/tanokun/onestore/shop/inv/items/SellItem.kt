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

class SellItem(val shop: Shop): BaseItem() {

    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(Material.BUCKET)
            .setDisplayName("§a§lクリックで§d売却§aします")
            .addLoreLines(
                "§b§l売値: §6${shop.sellPrice.roundToInt()}RNT",
                "",
                "§7シフトで64個一括売却できます。"
            )
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        if (player.hasMetadata(COOL_DOWN)) return

        if (clickType.isShiftClick && !shop.increase) {
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 10.0F, 1F)
            player.closeInventory()
            player.sendMessage("§cこのショップは同時複数売却が無効化されています")
            return
        }

        val amount = (clickType.isShiftClick then shop.product.amount * 64) ?: shop.product.amount
        val price = shop.sellPrice.roundToInt() * amount

        if (!player.inventory.containsAtLeast(shop.product, amount)) {
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 10.0F, 1F)
            player.closeInventory()
            player.sendMessage("§c売るアイテムがありません")
            return
        }

        val item = shop.product.clone()
        item.amount = amount
        player.inventory.removeItem(item)

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0F, 10.0F)
        shop.addStock(amount)
        jecon.repository.set(player.uniqueId, jecon.repository?.get(player.uniqueId)?.get()?.add(BigDecimal(price)))
        player.sendMessage("§7商品を${amount}つ売却しました §a+${price}RNT")
        OperationShopInv(shop, player).open()

        player.setMetadata(COOL_DOWN, FixedMetadataValue(plugin, true))
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            player.removeMetadata(COOL_DOWN, plugin)
        }, 3)
    }
}
