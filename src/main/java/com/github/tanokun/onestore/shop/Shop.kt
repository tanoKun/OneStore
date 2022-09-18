package com.github.tanokun.onestore.shop

import net.objecthunter.exp4j.ExpressionBuilder
import org.bukkit.inventory.ItemStack
import kotlin.math.roundToInt

class Shop(val id: String, val product: ItemStack, var basePrice: Int, stock: Int, var change: Int) {
    var sellExpressionPlus: String = "n + n * 0.01"

    var sellExpressionSubtract: String = "n - n * 0.01"

    var stock: Int = stock
        set(value) {
            difference =+ stock - value
            if (difference < -change) {
                sellPrice = ExpressionBuilder(sellExpressionPlus).variables("n")
                    .build().setVariable("n", sellPrice.toDouble()).evaluate().roundToInt()

                difference = 0
            }

            if (difference > change) {
                sellPrice = ExpressionBuilder(sellExpressionSubtract).variables("n")
                    .build().setVariable("n", sellPrice.toDouble()).evaluate().roundToInt()

                difference = 0
            }


            field = value
        }
    var buyPrice = 0
    var sellPrice = 0

    var difference = 0

    init {
        buyPrice = (basePrice * 1.05).roundToInt()
        sellPrice = (basePrice / 1.05).roundToInt()
    }
}