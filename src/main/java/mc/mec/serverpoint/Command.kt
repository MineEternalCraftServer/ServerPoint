package mc.mec.serverpoint

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

object Command : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is ConsoleCommandSender){
            sender.sendMessage("This command can only be executed by the player.")
            return false
        }

        val p = sender as Player

        if (p.hasPermission(Permission.ADMIN)) {

            if(args.isEmpty()){
                //  help

                return true
            }

            when(args[0]){
                "add" -> {
                    // command: /spoint add <player> <amount>

                }
                "remove" -> {
                    // command: /spoint remove <player> <amount>

                }
                "help" -> {
                    // command: /spoint help
                }
            }

        }else {
            p.sendMessage("§cThis command can be executed by Admin or higher.")
        }
        return true
    }
}