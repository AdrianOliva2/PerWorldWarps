package net.serveminecraft.minecrafteros.perworldwarps.tabcompleters

import net.serveminecraft.minecrafteros.perworldwarps.PerWorldWarps
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class SubCommandsCompleter(val plugin: PerWorldWarps): TabCompleter {

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String> {
        return listOf("help", "reload").toMutableList()
    }

}