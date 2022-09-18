package com.github.tanokun.onestore.util

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

fun itemStackToBase64(item: ItemStack): String {
    val outputStream = ByteArrayOutputStream()
    val dataOutput = BukkitObjectOutputStream(outputStream)

    dataOutput.writeInt(1)
    dataOutput.writeObject(item)

    dataOutput.close()
    return Base64Coder.encodeLines(outputStream.toByteArray())

}

fun itemStackFromBase64(data: String): ItemStack {
    val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(data))
    val dataInput = BukkitObjectInputStream(inputStream)
    dataInput.readInt()

    val item = dataInput.readObject() as ItemStack

    dataInput.close()
    return item
}