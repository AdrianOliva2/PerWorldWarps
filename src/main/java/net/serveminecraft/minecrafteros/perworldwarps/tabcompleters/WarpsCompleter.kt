package net.serveminecraft.minecrafteros.perworldwarps.tabcompleters

import net.serveminecraft.minecrafteros.perworldwarps.PerWorldWarps
import net.serveminecraft.minecrafteros.perworldwarps.commands.WarpsCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class WarpsCompleter(val plugin: PerWorldWarps): TabCompleter {

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String>? {
        return if (sender !is Player){
            null
        } else {
            val player: Player = sender
            val warpsCommand = WarpsCommand(plugin)
            warpsCommand.getAvailableWarps(player)
        }
    }

}