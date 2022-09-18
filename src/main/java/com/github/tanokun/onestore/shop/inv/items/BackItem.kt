package com.github.tanokun.onestore.shop.inv.items

import de.studiocode.invui.gui.impl.PagedGUI
import de.studiocode.invui.item.ItemProvider
import de.studiocode.invui.item.builder.ItemBuilder
import de.studiocode.invui.item.impl.controlitem.PageItem
import org.bukkit.Material

class BackItem : PageItem(false) {

    override fun getItemProvider(gui: PagedGUI): ItemProvider {
        val builder = ItemBuilder(Material.ARROW)
            .setDisplayName("§7前のページに戻る")
            .addLoreLines("§7§e${gui.currentPageIndex} ページ")

        if (!gui.hasPageBefore()) return ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)

        return builder
    }
}
