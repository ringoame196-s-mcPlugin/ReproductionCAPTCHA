package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.commands.Command
import com.github.ringoame196_s_mcPlugin.commands.TabCompleter
import com.github.ringoame196_s_mcPlugin.events.PlayerAct
import com.github.ringoame196_s_mcPlugin.managers.DataBaseManager
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Main : JavaPlugin() {
    override fun onEnable() {
        super.onEnable()
        val plugin = this
        makeDatabaseFile(plugin)
        saveResource("PlayerData.yml", false)
        server.pluginManager.registerEvents(PlayerAct(plugin), plugin)
        val command = getCommand("captcha")
        command!!.setExecutor(Command(plugin))
        command.tabCompleter = TabCompleter()
    }

    private fun makeDatabaseFile(plugin: Plugin) {
        val authDataFilePath = "${plugin.dataFolder}/${DataConst.FILEPATH}"
        val authDataFile = File(authDataFilePath)
        val dataBaseManager = DataBaseManager()
        if (!authDataFile.exists()) {
            val makeAuthKeyTableCommand =
                "CREATE TABLE ${DataConst.TABLE_NAME}(${DataConst.MC_UUID} VARCHAR(36) NOT NULL, ${DataConst.AUTH_KEY} VARCHAR(5) NOT NULL, ${DataConst.MAP_ID} INT NOT NULL);"
            dataBaseManager.runSQLCommand(authDataFilePath, makeAuthKeyTableCommand, mutableListOf())
        }
    }
}
