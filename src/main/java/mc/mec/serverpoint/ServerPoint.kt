package mc.mec.serverpoint

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class ServerPoint : JavaPlugin(), Listener {

    companion object {
        lateinit var plugin: ServerPoint
        var prefix = "§e[§6Server§aPoint§e]§f"
        lateinit var dataBase: DataBase
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
        //  Create Table
        PointAPI.createTable()

        //  Command
        getCommand("spoint")?.executor = Command
        //  Event
        server.pluginManager.registerEvents(this, this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    //  Join
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        //  Create Player Data
        PointAPI.createPlayer(player.uniqueId.toString())
        //  Show
        object : BukkitRunnable() {
            override fun run() {
                val point = PointAPI.getPoint(player.uniqueId.toString())
                player.sendMessage("${prefix}§aあなたのポイント: $point")
            }
        }.runTask(plugin)
    }

}