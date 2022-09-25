package com.github.tanokun.onestore.util.database

import com.github.tanokun.onestore.plugin
import com.github.tanokun.onestore.shop.Shop
import com.github.tanokun.onestore.shop.createShopHologram
import com.github.tanokun.onestore.shopManager
import com.github.tanokun.onestore.util.itemStackFromBase64
import com.github.tanokun.onestore.util.itemStackToBase64
import com.github.tanokun.onestore.util.locationFromBase64
import com.github.tanokun.onestore.util.locationToBase64
import com.gmail.filoghost.holographicdisplays.api.Hologram
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.bukkit.Location
import java.io.File


class Database {
    private val hikariDatabase: HikariDataSource

    init {
        val db = File(plugin.dataFolder, "data.db")
        val dbConfig = HikariConfig()

        if (!db.exists()) {
            db.parentFile.mkdirs()
            db.createNewFile()
        }

        dbConfig.driverClassName = "org.sqlite.JDBC"
        dbConfig.jdbcUrl = "jdbc:sqlite:${db.path}"

        hikariDatabase = HikariDataSource(dbConfig)

        val statement = hikariDatabase.connection.createStatement()

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS `shop` (" +
                "  `id` MEDIUMTEXT NULL DEFAULT NULL," +
                "  `product` MEDIUMTEXT NULL DEFAULT NULL," +
                "  `basePrice` INTEGER NULL DEFAULT NULL," +
                "  `stock` INTEGER NULL DEFAULT NULL," +
                "  `change` INTEGER NULL DEFAULT NULL," +
                "  `buyPrice` DOUBLE NULL DEFAULT NULL," +
                "  `sellPrice` DOUBLE NULL DEFAULT NULL," +
                "  `difference` INTEGER NULL DEFAULT NULL," +
                "  `plus` MEDIUMTEXT NULL DEFAULT NULL," +
                "  `subtract` MEDIUMTEXT NULL DEFAULT NULL," +
                "  `increase` BOOLEAN NULL DEFAULT NULL," +
                "  PRIMARY KEY (`id`)" +
                ")")

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS `hologram` (" +
                "  `id` MEDIUMTEXT NULL DEFAULT NULL," +
                "  `location` MEDIUMTEXT NULL DEFAULT NULL," +
                "  PRIMARY KEY (`id`)" +
                ")")
    }

    fun close() = hikariDatabase.close()

    fun saveShop(shop: Shop) {
        val insert = hikariDatabase.connection
            .prepareStatement(
                "INSERT OR REPLACE INTO `shop` (`id`, `product`, `basePrice`, `stock`, `change`, `buyPrice`, `sellPrice`, `difference`, `plus`, `subtract`, `increase`) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
        insert.setString(1, shop.id)
        insert.setString(2, itemStackToBase64(shop.product))
        insert.setInt(3, shop.basePrice)
        insert.setInt(4, shop.stock)
        insert.setInt(5, shop.change)
        insert.setDouble(6, shop.buyPrice)
        insert.setDouble(7, shop.sellPrice)
        insert.setInt(8, shop.difference)
        insert.setString(9, shop.expressionPlus)
        insert.setString(10, shop.expressionSubtract)
        insert.setBoolean(11, shop.increase)
        insert.executeUpdate()
    }

    fun deleteShop(id: String) {
        val delete = hikariDatabase.connection
            .prepareStatement(
                "DELETE FROM shop WHERE id = ?")
        delete.setString(1, id)
        delete.executeUpdate()
    }

    fun loadShops(): ArrayList<Shop> {
        val query = hikariDatabase.connection.prepareStatement("select * from shop").executeQuery()

        val shops = ArrayList<Shop>()

        while (query.next()) {
            val id = query.getString("id")
            val product = itemStackFromBase64(query.getString("product"))
            val basePrice = query.getInt("basePrice")
            val stock = query.getInt("stock")
            val change = query.getInt("change")
            val buyPrice = query.getDouble("buyPrice")
            val sellPrice = query.getDouble("sellPrice")
            val difference = query.getInt("difference")
            val plus = query.getString("plus")
            val subtract = query.getString("subtract")
            val increase = query.getBoolean("increase")

            val shop = Shop(id, product, basePrice, stock, change, increase)
            shop.buyPrice = buyPrice
            shop.sellPrice = sellPrice
            shop.difference = difference
            shop.expressionPlus = plus
            shop.expressionSubtract = subtract
            shops.add(shop)
        }

        return shops
    }

    fun saveHologramLocation(id: String, data: Pair<Location, Hologram>) {
        val insert = hikariDatabase.connection
            .prepareStatement(
                "INSERT OR REPLACE INTO `hologram` (`id`, `location`) " +
                        "VALUES (?, ?)")
        insert.setString(1, id)
        insert.setString(2, locationToBase64(data.first))
        insert.executeUpdate()
    }

    fun deleteHologramLocation(id: String) {
        val delete = hikariDatabase.connection
            .prepareStatement(
                "DELETE FROM hologram WHERE id = ?")
        delete.setString(1, id)
        delete.executeUpdate()
    }

    fun loadHologramLocations(): HashMap<String, Pair<Location, Hologram>> {
        val query = hikariDatabase.connection.prepareStatement("select * from hologram").executeQuery()

        val holograms = HashMap<String, Pair<Location, Hologram>>()

        while (query.next()) {
            val id = query.getString("id")
            val location = locationFromBase64(query.getString("location"))
            val shop = shopManager.shops[id] ?: continue

            holograms[id] = Pair(location, createShopHologram(shop, location))
        }

        return holograms
    }
}
