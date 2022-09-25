package com.github.tanokun.onestore.shop

import com.github.tanokun.onestore.db
import com.github.tanokun.onestore.plugin
import com.gmail.filoghost.holographicdisplays.api.Hologram
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import org.bukkit.Location
import kotlin.math.floor

const val COOL_DOWN = "SHOP_COOL_DOWN"

class ShopManager {
    val shops = HashMap<String, Shop>()

    val holograms = HashMap<String, Pair<Location, Hologram>>()

    val lHolograms = HashMap<Location, Pair<Shop, Hologram>>()

    init {
        db.loadShops().forEach {
            shops[it.id] = it
        }
    }

    fun loadHologram() {
        db.loadHologramLocations().forEach {
            holograms[it.key] = it.value
            shops[it.key]?.let { shop ->
                val loc = it.value.first.clone()
                loc.x = floor(loc.x)
                loc.y = floor(loc.y)
                loc.z = floor(loc.z)
                println(it.value.first)
                lHolograms[loc] = Pair(shop, it.value.second)
            }
        }
    }
}

fun createShopHologram(shop: Shop, location: Location): Hologram {
    val hologram = HologramsAPI.createHologram(plugin, location.add(0.0, 2.0, 0.0))
    location.add(0.0, -2.0, 0.0)
    hologram.appendItemLine(shop.product)
    hologram.appendTextLine("§b下のブロックをクリックで操作します")
    return hologram
}