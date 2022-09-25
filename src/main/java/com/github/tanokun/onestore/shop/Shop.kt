package com.github.tanokun.onestore.shop

import net.objecthunter.exp4j.ExpressionBuilder
import org.bukkit.inventory.ItemStack
import kotlin.math.abs as abs

class Shop(val id: String, val product: ItemStack, var basePrice: Int, var stock: Int, var change: Int, var increase: Boolean) {
    var expressionPlus: String = "n + n * 0.01"

    var expressionSubtract: String = "n - n * 0.01"

    var buyPrice = 0.0
    var sellPrice = 0.0

    var difference = 0

    init {
        buyPrice = basePrice * 1.05
        sellPrice = basePrice / 1.05
    }

    fun addStock(stock: Int) {
        this.stock += stock
        this.difference += stock

        while (difference >= change) {
            sellPrice = ExpressionBuilder(expressionSubtract).variables("n")
                .build().setVariable("n", sellPrice).evaluate()

            buyPrice = ExpressionBuilder(expressionSubtract).variables("n")
                .build().setVariable("n", buyPrice).evaluate()
            difference -= change
        }
    }

    fun removeStock(stock: Int) {
        this.stock -= stock
        this.difference -= stock

        while (abs(difference) >= change) {
            buyPrice = ExpressionBuilder(expressionPlus).variables("n")
                .build().setVariable("n", buyPrice).evaluate()

            sellPrice = ExpressionBuilder(expressionPlus).variables("n")
                .build().setVariable("n", sellPrice).evaluate()
            difference += change
        }
    }
}