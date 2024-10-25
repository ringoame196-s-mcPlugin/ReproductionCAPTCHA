package com.github.ringoame196_s_mcPlugin.managers

import com.github.ringoame196_s_mcPlugin.DataConst
import org.bukkit.plugin.Plugin

class AuthPlayerManager(plugin: Plugin) {
    private val authDataFilePath = "${plugin.dataFolder}/${DataConst.FILEPATH}"
    private val dataBaseManager = DataBaseManager()

    fun isActProhibitingPlayer(uuid: String): Boolean {
        val command = "SELECT EXISTS(SELECT 1 FROM ${DataConst.TABLE_NAME} WHERE ${DataConst.MC_UUID} = ?);"
        return dataBaseManager.acquisitionBooleanValue(authDataFilePath, command, mutableListOf(uuid))
    }

    fun saveToDB(uuid: String, authKey: String, mapID: Int) {
        deleteToDB(uuid)
        val setCommand = "INSERT INTO ${DataConst.TABLE_NAME} (${DataConst.MC_UUID}, ${DataConst.AUTH_KEY}, ${DataConst.MAP_ID}) VALUES (?,?,?);"
        dataBaseManager.runSQLCommand(authDataFilePath, setCommand, mutableListOf(uuid, authKey, mapID))
    }

    fun deleteToDB(uuid: String) {
        val deleteCommand = "DELETE FROM ${DataConst.TABLE_NAME} WHERE ${DataConst.MC_UUID} = ?;"
        dataBaseManager.runSQLCommand(authDataFilePath, deleteCommand, mutableListOf(uuid))
    }

    fun isAuthenticated(uuid: String, authKey: String): Boolean {
        val command = "SELECT EXISTS(SELECT 1 FROM ${DataConst.TABLE_NAME} WHERE ${DataConst.MC_UUID} = ? AND ${DataConst.AUTH_KEY} = ?);"
        return dataBaseManager.acquisitionBooleanValue(authDataFilePath, command, mutableListOf(uuid, authKey))
    }

    fun acquisitionMapID(uuid: String): String? {
        val command = "SELECT ${DataConst.MAP_ID} FROM ${DataConst.TABLE_NAME} WHERE ${DataConst.MC_UUID} = ?;"
        return dataBaseManager.acquisitionStringValue(authDataFilePath, command, mutableListOf(uuid), DataConst.MAP_ID)
    }
}
