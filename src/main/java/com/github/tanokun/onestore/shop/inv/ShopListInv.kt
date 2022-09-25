package com.github.tanokun.onestore.shop.inv

import com.github.tanokun.onestore.shop.inv.items.BackItem
import com.github.tanokun.onestore.shop.inv.items.ForwardItem
import com.github.tanokun.onestore.shopManager
import com.github.tanokun.onestore.util.inv.Inv
import de.studiocode.invui.gui.GUI
import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.guitype.GUIType
import de.studiocode.invui.gui.structure.Markers
import de.studiocode.invui.item.Item
import de.studiocode.invui.item.builder.ItemBuilder
import de.studiocode.invui.item.impl.SimpleItem
import de.studiocode.invui.window.impl.single.SimpleWindow
import org.bukkit.Material
import org.bukkit.entity.Player
import kotlin.math.roundToInt

class ShopListInv(player: Player): Inv("§e§lストア一覧", player) {

    private val border = SimpleItem(ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§r"))

    private val window: GUI

    private val items = ArrayList<Item>()

    init {
        shopManager.shops.forEach {
            items.add(SimpleItem(
                ItemBuilder(it.value.product)
                    .addLoreLines(
                        " ",
                        "§7ID: §b${it.value.id}",
                        "§7基本価格: §b${it.value.basePrice}",
                        "§7買値: §b${it.value.buyPrice.roundToInt()}",
                        "§7売値: §b${it.value.sellPrice.roundToInt()}",
                        "§7在庫: §b${it.value.stock}",
                        "§7変化幅: §b${it.value.change}",
                        "§7変動までの差: §b${it.value.difference}",
                        "§7一括できるか: §b${it.value.increase}",
                        "  ",
                        "§7式(減法): §b${it.value.expressionSubtract}",
                        "§7式(加法): §b${it.value.expressionPlus}",
                    )
            ))
        }

        window = GUIBuilder(GUIType.PAGED_ITEMS)
            .setStructure(
                "# # # # # # # # #",
                "# x x x x x x x #",
                "# x x x x x x x #",
                "# x x x x x x x #",
                "# x x x x x x x #",
                "# # # < # > # # #")
            .addIngredient('x', Markers.ITEM_LIST_SLOT_HORIZONTAL)
            .addIngredient('#', border)
            .addIngredient('<', BackItem())
            .addIngredient('>', ForwardItem())
            .setItems(items)
            .build()
    }

    override fun open() = SimpleWindow(player, title, window).show()
}