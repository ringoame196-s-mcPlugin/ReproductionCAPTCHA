package com.github.ringoame196_s_mcPlugin.managers

import com.github.ringoame196_s_mcPlugin.DataConst
import org.bukkit.plugin.Plugin
import java.io.File

class AuthPlayerManager(plugin: Plugin) {
    private val playerDataFile = File("${plugin.dataFolder}/PlayerData.yml")
    private val authDataFilePath = "${plugin.dataFolder}/${DataConst.FILEPATH}"
    private val ymlFileManager = YmlFileManager()
    private val dataBaseManager = DataBaseManager()

    fun isActProhibitingPlayer(uuid: String): Boolean {
        val authWithingPlayer = ymlFileManager.acquisitionListValue(playerDataFile, DataConst.AUTH_WITHIN_PLAYER)
        return authWithingPlayer.contains(uuid)
    }

    fun additionAuthWithingPlayer(uuid: String) {
        val authWithingPlayer = ymlFileManager.acquisitionListValue(playerDataFile, DataConst.AUTH_WITHIN_PLAYER)
        authWithingPlayer.add(uuid)
        ymlFileManager.setValue(playerDataFile, DataConst.AUTH_WITHIN_PLAYER, authWithingPlayer)
    }

    fun reduceAuthWithingPlayer(uuid: String) {
        val authWithingPlayer = ymlFileManager.acquisitionListValue(playerDataFile, DataConst.AUTH_WITHIN_PLAYER)
        authWithingPlayer.remove(uuid)
        ymlFileManager.setValue(playerDataFile, DataConst.AUTH_WITHIN_PLAYER, authWithingPlayer)
    }

    fun creationAuthKey(): String {
        val minValue = 0x10000 // 16進数の "10000"
        val maxValue = 0xFFFFF // 16進数の "ZZZZZ"
        return String.format("%05X", (minValue..maxValue).random())
    }

    fun saveToDB(uuid: String, authKey: String) {
        deleteToDB(uuid)
        val setCommand = "INSERT INTO ${DataConst.TABLE_NAME} (${DataConst.MC_UUID}, ${DataConst.AUTH_KEY}) VALUES (?,?);"
        dataBaseManager.runSQLCommand(authDataFilePath, setCommand, mutableListOf(uuid, authKey))
    }

    fun deleteToDB(uuid: String) {
        val deleteCommand = "DELETE FROM ${DataConst.TABLE_NAME} WHERE ${DataConst.MC_UUID} = ?;"
        dataBaseManager.runSQLCommand(authDataFilePath, deleteCommand, mutableListOf(uuid))
    }

    fun isAuthenticated(uuid: String, authKey: String): Boolean {
        val command = "SELECT EXISTS(SELECT 1 FROM ${DataConst.TABLE_NAME} WHERE ${DataConst.MC_UUID} = ? AND ${DataConst.AUTH_KEY} = ?);"
        return dataBaseManager.acquisitionBooleanValue(authDataFilePath, command, mutableListOf(uuid, authKey))
    }
}
