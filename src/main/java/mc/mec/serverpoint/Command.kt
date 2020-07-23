package mc.mec.serverpoint

import mc.mec.serverpoint.ServerPoint.Companion.plugin
import mc.mec.serverpoint.ServerPoint.Companion.prefix
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object Command : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is ConsoleCommandSender){
            sender.sendMessage("This command can only be executed by the player.")
            return false
        }

        val p = sender as Player

        if(args.isEmpty()){
            //  permission
            if(!p.hasPermission(Permission.GENERAL)){
                p.sendMessage("${prefix}§c権限がありません。You do not have permission.")
                return false
            }
            //  getPoint
            object : BukkitRunnable(){
                override fun run() {
                    val point = PointAPI.getPoint(p.uniqueId.toString())
                    p.sendMessage("${prefix}§aあなたのポイント: $point")
                }
            }.runTask(plugin)
            return true
        }
        if (p.hasPermission(Permission.ADMIN)) {
            when(args[0]){
                "add" -> {
                    // command: /spoint add <player> <amount>
                    if(args.size < 2){
                        p.sendMessage("${prefix}§cプレイヤーを指定してください。")
                        return false
                    }
                    val mcid = args[1]
                    val player = Bukkit.getPlayer(mcid) ?: Bukkit.getOfflinePlayer(mcid)
                    val uuid = player.uniqueId.toString()
                    if(args.size < 3){
                        p.sendMessage("${prefix}§cポイントを入力してください。")
                        return false
                    }
                    val arg2 = args[2]
                    val point = arg2.toIntOrNull()
                    if(point == null){
                        p.sendMessage("${prefix}§cポイントを半角数字で入力してください。")
                        return false
                    }
                    //  Execute
                    object : BukkitRunnable(){
                        override fun run() {
                            if(PointAPI.addPoint(uuid,point)) {
                                p.sendMessage("${prefix}§aポイントを追加しました。")
                            }else{
                                p.sendMessage("${prefix}§cエラーが発生したため、ポイントを追加できませんでした。")
                            }
                        }
                    }.runTask(plugin)
                }
                "remove" -> {
                    // command: /spoint remove <player> <amount>
                    if(args.size < 2){
                        p.sendMessage("${prefix}§cプレイヤーを指定してください。")
                        return false
                    }
                    val mcid = args[1]
                    val player = Bukkit.getPlayer(mcid) ?: Bukkit.getOfflinePlayer(mcid)
                    val uuid = player.uniqueId.toString()
                    if(args.size < 3){
                        p.sendMessage("${prefix}§cポイントを入力してください。")
                        return false
                    }
                    val arg2 = args[2]
                    val point = arg2.toIntOrNull()
                    if(point == null){
                        p.sendMessage("${prefix}§cポイントを半角数字で入力してください。")
                        return false
                    }
                    //  Execute
                    object : BukkitRunnable(){
                        override fun run() {
                            if(PointAPI.removePoint(uuid,point)) {
                                p.sendMessage("${prefix}§aポイントを剥奪しました。")
                            }else{
                                p.sendMessage("${prefix}§cエラーが発生したため、ポイントを剥奪できませんでした。")
                            }
                        }
                    }.runTask(plugin)
                }
                "delete" -> {
                    //  command: /spoint delete <player>
                    if(args.size < 2){
                        p.sendMessage("${prefix}§cプレイヤーを指定してください。")
                        return false
                    }
                    val mcid = args[1]
                    val player = Bukkit.getPlayer(mcid) ?: Bukkit.getOfflinePlayer(mcid)
                    val uuid = player.uniqueId.toString()
                    //  Execute
                    object : BukkitRunnable(){
                        override fun run() {
                            if(PointAPI.deletePlayer(uuid)) {
                                p.sendMessage("${prefix}§aプレイヤーデータを削除しました。")
                            }else{
                                p.sendMessage("${prefix}§cエラーが発生したため、プレイヤーデータを削除できませんでした。")
                            }
                        }
                    }.runTask(plugin)
                }
                "help" -> {
                    // command: /spoint help
                    sendHelp(p)
                }
                "show" -> {
                    //  command:/spoint show <player>
                    if(args.size < 2){
                        p.sendMessage("${prefix}§cプレイヤーを指定してください。")
                        return false
                    }
                    val mcid = args[1]
                    val player = Bukkit.getPlayer(mcid) ?: Bukkit.getOfflinePlayer(mcid)
                    val uuid = player.uniqueId.toString()
                    //  Execute
                    object : BukkitRunnable(){
                        override fun run() {
                            p.sendMessage("§a${player.name}のポイント: ${PointAPI.getPoint(uuid)}")
                        }
                    }.runTask(plugin)
                }
                else -> sendHelp(p)
            }

        }else {
            p.sendMessage("§cThis command can be executed by Admin or higher.")
        }
        return true
    }

    private fun sendHelp(player:Player){
        val msg = """
            §e§l===================================
            §6/spoint <- ポイントを確認します。
            §6/spoint help <- ヘルプの表示
            §cAdmin Commands
            §c/spoint add <player> <amount> <- ポイントを付与します。
            §c/spoint remove <player> <amount> <- ポイントを剥奪します。
            §c/spoint delete <player> <- プレイヤーデータを削除します。
            §c/spoint show <player> <- 指定したプレイヤーのポイントを確認します。
            §dversion: ${plugin.version}
            §e§l===================================
        """.trimIndent()
        player.sendMessage(msg)
    }
}