package mc.mec.serverpoint

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

object Command : CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        val p = sender as Player
        val cmd = args?.get(0)

        if (sender is ConsoleCommandSender){
            sender.sendMessage("This command can only be executed by the player.")
            return false
        }

        if (p.hasPermission("spoint.admin")) {

            if (cmd == "add") {
                // command: /spoint add <player> <amount>

            }

            if (cmd == "remove") {
                // command: /spoint remove <player> <amount>

            }

            if (cmd == "help") {
                // command: /spoint help

            }

        }else {
            p.sendMessage("§cThis command can be executed by Admin or higher.")
        }
        return true
    }
}