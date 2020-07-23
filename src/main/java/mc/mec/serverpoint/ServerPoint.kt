package mc.mec.serverpoint

import org.bukkit.plugin.java.JavaPlugin

class ServerPoint : JavaPlugin() {

    companion object{
        lateinit var plugin:ServerPoint
        var prefix = "§e[§6Server§aPoint§e]§f"
        var enabled = false
        lateinit var dataBase:DataBase

    }

    //  Plugin Information
    val version = "0.0"

    override fun onEnable() {
        // Plugin startup logic
        //  Logger
        logger.info("Enabled ServerPoint ver-$version")
        //  instance
        plugin = this
        //  Config
        this.saveDefaultConfig()
        //  DB
        dataBase = DataBase("ServerPoint")
        //  Create Table
        PointAPI.createTable()

        //  Command
        getCommand("spoint")?.executor = Command
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}