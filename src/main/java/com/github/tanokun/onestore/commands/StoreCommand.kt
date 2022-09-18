package com.github.tanokun.onestore.commands

import com.github.tanokun.onestore.db
import com.github.tanokun.onestore.shop.Shop
import com.github.tanokun.onestore.shop.inv.list.ShopListInv
import com.github.tanokun.onestore.shopManager
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import org.bukkit.Material
import org.bukkit.entity.Player

class StoreCommand {
    val idArg: Argument<String> = TextArgument("id").replaceSuggestions(ArgumentSuggestions.strings { shopManager.shops.keys.toList().toTypedArray() })

    init {
        store()
    }

    private fun store() {
        CommandAPICommand("ostore")
            .withPermission(CommandPermission.OP)
            .withSubcommand(run())
            .withSubcommand(create())
            .withSubcommand(delete())
            .withSubcommand(list())
            .withSubcommand(set())
            .register()
    }

    private fun run(): CommandAPICommand {
        return CommandAPICommand("run")
            .withPermission(CommandPermission.OP)
            .executesPlayer(PlayerCommandExecutor { player: Player, args: Array<Any> ->
            })
    }

    private fun create(): CommandAPICommand {
        return CommandAPICommand("create")
            .withPermission(CommandPermission.OP)
            .withArguments(TextArgument("id"))
            .withArguments(IntegerArgument("元価格"))
            .withArguments(IntegerArgument("ストック"))
            .withArguments(IntegerArgument("変化幅"))
            .executesPlayer(PlayerCommandExecutor { player: Player, args: Array<Any> ->
                val id = args[0] as String
                val basePrice = args[1] as Int
                val stock = args[2] as Int
                val change = args[3] as Int
                val item = player.inventory.itemInMainHand

                if (item.type == Material.AIR) {
                    player.sendMessage("§c手にアイテムを持ってコマンドを実行してください")
                    return@PlayerCommandExecutor
                }

                if (shopManager.shops.containsKey(id)) {
                    player.sendMessage("§c既にそのショップは存在します")
                    return@PlayerCommandExecutor
                }

                shopManager.shops[id] = Shop(id, item, basePrice, stock, change)
                player.sendMessage("§b新しく「${id}」を作成しました")
            })
    }

    private fun delete(): CommandAPICommand {
        return CommandAPICommand("delete")
            .withPermission(CommandPermission.OP)
            .withArguments(idArg)
            .executesPlayer(PlayerCommandExecutor { player: Player, args: Array<Any> ->
                val id = args[0] as String
                val shop = shopManager.shops[id]

                if (shop == null) {
                    player.sendMessage("§cそのショップは存在しません")
                    return@PlayerCommandExecutor
                }

                shopManager.shops.remove(id)
                db.deleteShop(id)
                player.sendMessage("§bショップ「${id}」を削除しました")
            })
    }

    private fun set(): CommandAPICommand {
        return CommandAPICommand("set")
            .withPermission(CommandPermission.OP)
            .withSubcommand(CommandAPICommand("baseprice").withFullDescription("元価格を変更します")
                .withArguments(idArg)
                .withArguments(IntegerArgument("元価格"))
                .executesPlayer(PlayerCommandExecutor { player: Player, args: Array<Any> ->
                    val id = args[0] as String
                    val basePrice = args[1] as Int
                    val shop = shopManager.shops[id]

                    if (shop == null) {
                        player.sendMessage("§cそのショップは存在しません")
                        return@PlayerCommandExecutor
                    }

                    shop.basePrice = basePrice

                    player.sendMessage("§b「${id}」の元価格を「${basePrice}」に変更しました")

                }))
            .withSubcommand(CommandAPICommand("buyprice").withFullDescription("買値を強制的に変更します")
                .withArguments(idArg)
                .withArguments(IntegerArgument("買値"))
                .executesPlayer(PlayerCommandExecutor { player: Player, args: Array<Any> ->
                    val id = args[0] as String
                    val price = args[1] as Int
                    val shop = shopManager.shops[id]

                    if (shop == null) {
                        player.sendMessage("§cそのショップは存在しません")
                        return@PlayerCommandExecutor
                    }

                    shop.buyPrice = price

                    player.sendMessage("§b「${id}」の買値を「${price}」に変更しました")

                }))
            .withSubcommand(CommandAPICommand("sellprice").withFullDescription("売値を強制的に変更します")
                .withArguments(idArg)
                .withArguments(IntegerArgument("売値"))
                .executesPlayer(PlayerCommandExecutor { player: Player, args: Array<Any> ->
                    val id = args[0] as String
                    val price = args[1] as Int
                    val shop = shopManager.shops[id]

                    if (shop == null) {
                        player.sendMessage("§cそのショップは存在しません")
                        return@PlayerCommandExecutor
                    }

                    shop.sellPrice = price

                    player.sendMessage("§b「${id}」の買値を「${price}」に変更しました")

                }))
            .withSubcommand(CommandAPICommand("stock").withFullDescription("ストックを変更します")
                .withArguments(idArg)
                .withArguments(IntegerArgument("ストック"))
                .executesPlayer(PlayerCommandExecutor { player: Player, args: Array<Any> ->
                    val id = args[0] as String
                    val stock = args[1] as Int
                    val shop = shopManager.shops[id]

                    if (shop == null) {
                        player.sendMessage("§cそのショップは存在しません")
                        return@PlayerCommandExecutor
                    }

                    shop.stock = stock

                    player.sendMessage("§b「${id}」のストックを「${stock}」に変更しました")

                }))
            .withSubcommand(CommandAPICommand("change").withFullDescription("変化幅を変更します")
                .withArguments(idArg)
                .withArguments(IntegerArgument("変化幅"))
                .executesPlayer(PlayerCommandExecutor { player: Player, args: Array<Any> ->
                    val id = args[0] as String
                    val change = args[1] as Int
                    val shop = shopManager.shops[id]

                    if (shop == null) {
                        player.sendMessage("§cそのショップは存在しません")
                        return@PlayerCommandExecutor
                    }

                    shop.change = change

                    player.sendMessage("§b「${id}」の変化幅を「${change}」に変更しました")

                }))
            .withSubcommand(CommandAPICommand("expression").withFullDescription("それぞれの計算式を変更します")
                .withArguments(idArg)
                .withArguments(MultiLiteralArgument("SELL_PLUS", "SELL_SUBTRACT"))
                .withArguments(TextArgument("expression"))
                .executesPlayer(PlayerCommandExecutor { player: Player, args: Array<Any> ->
                    val id = args[0] as String
                    val type = args[1] as String
                    val expression = args[2] as String
                    val shop = shopManager.shops[id]

                    if (shop == null) {
                        player.sendMessage("§cそのショップは存在しません")
                        return@PlayerCommandExecutor
                    }

                    when (type) {
                        "SELL_PLUS" -> shop.sellExpressionPlus = "n - n * 0.01"
                        "SELL_SUBTRACT" -> shop.sellExpressionSubtract = "n - n * 0.01"
                    }

                    player.sendMessage("§b「${id}」の計算式(${type})を「${expression}」に変更しました")

                }))

    }

    private fun list(): CommandAPICommand {
        return CommandAPICommand("list")
            .withPermission(CommandPermission.OP)
            .executesPlayer(PlayerCommandExecutor { player: Player, args: Array<Any> ->
                ShopListInv(player).open()
            })
    }
}