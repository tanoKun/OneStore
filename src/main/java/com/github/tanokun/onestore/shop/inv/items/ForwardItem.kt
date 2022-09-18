package com.github.tanokun.onestore.shop.inv.items

import de.studiocode.invui.gui.impl.PagedGUI
import de.studiocode.invui.item.ItemProvider
import de.studiocode.invui.item.builder.ItemBuilder
import de.studiocode.invui.item.impl.controlitem.PageItem
import org.bukkit.Material

class ForwardItem: PageItem(true) {
    override fun getItemProvider(gui: PagedGUI): ItemProvider {
        val builder = ItemBuilder(Material.ARROW)
            .setDisplayName("§7次のページに行く")
            .addLoreLines("§7§e${gui.currentPageIndex + 2} ページ")

        if (!gui.hasNextPage()) return ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)

        return builder
    }
}