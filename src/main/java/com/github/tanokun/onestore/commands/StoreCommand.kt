package com.github.tanokun.onestore.commands

import com.github.tanokun.onestore.shop.Shop
import com.github.tanokun.onestore.shop.inv.list.ShopListInv
import com.github.tanokun.onestore.shopManager
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.TextArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import org.bukkit.Material
import org.bukkit.entity.Player

class StoreCommand {
    init {
        store()
    }

    private fun store() {
        CommandAPICommand("ostore")
            .withPermission(CommandPermission.OP)
            .withSubcommand(run())
            .withSubcommand(create())
            .withSubcommand(list())
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
            .withArguments(IntegerArgument("basePrice"))
            .withArguments(IntegerArgument("stock"))
            .executesPlayer(PlayerCommandExecutor { player: Player, args: Array<Any> ->
                val id = args[0] as String
                val basePrice = args[1] as Int
                val stock = args[2] as Int
                val item = player.inventory.itemInMainHand

                if (item.type == Material.AIR) {
                    player.sendMessage("§c手にアイテムを持ってコマンドを実行してください")
                    return@PlayerCommandExecutor
                }

                if (shopManager.shops.containsKey(id)) {
                    player.sendMessage("§c既にそのショップは存在します")
                    return@PlayerCommandExecutor
                }

                shopManager.shops[id] = Shop(id, item, basePrice, stock)
                player.sendMessage("§b新しく「${id}」を作成しました")
            })
    }

    private fun list(): CommandAPICommand {
        return CommandAPICommand("list")
            .withPermission(CommandPermission.OP)
            .executesPlayer(PlayerCommandExecutor { player: Player, args: Array<Any> ->
                ShopListInv(player).open()
            })
    }

}