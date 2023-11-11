package net.serveminecraft.minecrafteros.perworldwarps.commands

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.serveminecraft.minecrafteros.perworldwarps.PerWorldWarps
import net.serveminecraft.minecrafteros.perworldwarps.utils.MessagesUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PerWorldWarpsCommand(private val plugin: PerWorldWarps): CommandExecutor {

    private val replaces: HashMap<String, String> = HashMap()

    init {
        replaces["%prefix%"] = plugin.prefix
    }

    private fun help(sender: CommandSender): Boolean {
        return if ((sender is Player && sender.isOp || sender.hasPermission("perworldwarps.help")) || sender !is Player) {
            val messageList: MutableList<String> = MessagesUtil.getFullMessageListFromConfig(plugin, "help", replaces)
            for (message in messageList) {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message))
            }
            true
        } else {
            sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "no-permission", replaces)))
            false
        }
    }

    private fun reload() {
        plugin.reloadConfig()
        plugin.saveDefaultConfig()
        plugin.reloadWarpsConfig()
        plugin.reloadMessagesConfig()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args.isNullOrEmpty()) {
            if (!help(sender))
                return false
        } else {
            if (args.size == 1){
                when(args[0]) {
                    "help" -> {
                        if (!help(sender))
                            return false
                    }
                    "reload" -> {
                        if ((sender is Player && sender.isOp || sender.hasPermission("perworldwarps.help")) || sender !is Player) {
                            reload()
                            sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "plugin-reloaded-successfully", replaces)))
                        } else {
                            sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "no-permission", replaces)))
                            return false
                        }
                    }
                    else -> {
                        if (!help(sender))
                            return false
                    }
                }
            }
        }
        return true
    }

}