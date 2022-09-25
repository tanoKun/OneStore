package com.github.tanokun.onestore.shop.inv

import com.github.tanokun.onestore.shop.Shop
import com.github.tanokun.onestore.shop.inv.items.BuyItem
import com.github.tanokun.onestore.shop.inv.items.SellItem
import com.github.tanokun.onestore.util.inv.Inv
import de.studiocode.invui.gui.GUI
import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.guitype.GUIType
import de.studiocode.invui.item.builder.ItemBuilder
import de.studiocode.invui.item.impl.SimpleItem
import de.studiocode.invui.window.impl.single.SimpleWindow
import org.bukkit.Material
import org.bukkit.entity.Player

class OperationShopInv(val shop: Shop, player: Player): Inv("§e§l売買", player) {

    private val border = SimpleItem(ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§r"))
    private val border2 = SimpleItem(ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setDisplayName("§r"))

    private val window: GUI
    init {

        window = GUIBuilder(GUIType.NORMAL)
            .setStructure(
                "# # # # # # # # #",
                "# a a a $ a a a #",
                "# a b $ x $ s a #",
                "# a a a $ a a a #",
                "# # # # # # # # #")
            .addIngredient('x', SimpleItem(ItemBuilder(shop.product)))
            .addIngredient('a', SimpleItem(ItemBuilder(Material.AIR)))
            .addIngredient('b', BuyItem(shop))
            .addIngredient('s', SellItem(shop))
            .addIngredient('#', border)
            .addIngredient('$', border2)
            .build()
    }

    override fun open() = SimpleWindow(player, title, window).show()
}