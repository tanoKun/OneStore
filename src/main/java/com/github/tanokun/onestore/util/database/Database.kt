package com.github.tanokun.onestore.util.database

import com.github.tanokun.onestore.plugin
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

        dbConfig.connectionInitSql = "SELECT 1"

        hikariDatabase = HikariDataSource(dbConfig)
    }

    fun close() = hikariDatabase.close()
}
