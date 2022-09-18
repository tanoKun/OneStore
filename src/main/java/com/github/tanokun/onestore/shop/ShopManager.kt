package com.github.tanokun.onestore.shop

import com.github.tanokun.onestore.db
import org.bukkit.Location

class ShopManager {
    val signs = ArrayList<Location>()

    val shops = HashMap<String, Shop>()

    init {
        db.loadShops().forEach {
            shops[it.id] = it
        }
    }
}