package com.github.tanokun.onestore.util.database

import com.github.tanokun.onestore.plugin
import com.github.tanokun.onestore.shop.Shop
import com.github.tanokun.onestore.util.itemStackFromBase64
import com.github.tanokun.onestore.util.itemStackToBase64
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
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
                "  `buyPrice` INTEGER NULL DEFAULT NULL," +
                "  `sellPrice` INTEGER NULL DEFAULT NULL," +
                "  `difference` INTEGER NULL DEFAULT NULL," +
                "  `plus` MEDIUMTEXT NULL DEFAULT NULL," +
                "  `subtract` MEDIUMTEXT NULL DEFAULT NULL," +
                "  PRIMARY KEY (`id`)" +
                ")")
    }

    fun close() = hikariDatabase.close()

    fun saveShop(shop: Shop) {
        val insert = hikariDatabase.connection
            .prepareStatement(
                "INSERT OR REPLACE INTO `shop` (`id`, `product`, `basePrice`, `stock`, `change`, `buyPrice`, `sellPrice`, `difference`, `plus`, `subtract`) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
        insert.setString(1, shop.id)
        insert.setString(2, itemStackToBase64(shop.product))
        insert.setInt(3, shop.basePrice)
        insert.setInt(4, shop.stock)
        insert.setInt(5, shop.change)
        insert.setInt(6, shop.buyPrice)
        insert.setInt(7, shop.sellPrice)
        insert.setInt(8, shop.difference)
        insert.setString(9, shop.sellExpressionPlus)
        insert.setString(10, shop.sellExpressionSubtract)
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
            val buyPrice = query.getInt("buyPrice")
            val sellPrice = query.getInt("sellPrice")
            val difference = query.getInt("difference")
            val plus = query.getString("plus")
            val subtract = query.getString("subtract")

            val shop = Shop(id, product, basePrice, stock, change)
            shop.buyPrice = buyPrice
            shop.sellPrice = sellPrice
            shop.difference = difference
            shop.sellExpressionPlus = plus
            shop.sellExpressionSubtract = subtract
            shops.add(shop)
        }

        return shops
    }
}
