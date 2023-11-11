package net.serveminecraft.minecrafteros.perworldwarps.commands

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.serveminecraft.minecrafteros.perworldwarps.PerWorldWarps
import net.serveminecraft.minecrafteros.perworldwarps.utils.MessagesUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

class WarpsCommand(private val plugin: PerWorldWarps): CommandExecutor {

    private val replaces: HashMap<String, String> = HashMap()

    init {
        replaces["%prefix%"] = plugin.prefix
    }

    fun getAvailableWarps(player: Player): MutableList<String>? {
        val warpsConfig: FileConfiguration = plugin.warpsConfigFile
        val warps: MutableSet<String>? = warpsConfig.getConfigurationSection("Worlds.${player.world.name}")?.getKeys(false)

        if (warps != null) {
            val warpsList: MutableList<String> = mutableListOf()
            for (warp: String in warps) {
                if (warpsConfig.contains("Worlds.${player.world.name}.$warp.permission")) {
                    val permission: String = warpsConfig.getString("Worlds.${player.world.name}.$warp.permission")!!
                    if (player.isOp || player.hasPermission(permission))
                        warpsList += warp
                } else {
                    warpsList += warp
                }
            }
            return warpsList.sorted().toMutableList()
        }

        return null
    }

    fun showAvailableWarps(player: Player): Boolean {
        val warps: MutableSet<String>? = getAvailableWarps(player)?.toMutableSet()
        if (warps != null) {
            val warpsText = StringBuilder()
            for (warp: String in warps) {
                warpsText.append("$warp,")
            }
            if (warpsText.isNotEmpty()) {
                val warpsTextString: String = warpsText.removeRange(warpsText.length-1, warpsText.length).split(',').sorted().joinToString()
                replaces["%warps%"] = warpsTextString
                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "available-warps", replaces)))
                replaces.remove("%warps%")
            } else {
                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "no-available-warps", replaces)))
                return false
            }
        } else {
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "no-available-warps", replaces)))
            return false
        }
        return true
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "console-command-error", replaces)))
            return false
        } else {
            val player: Player = sender
            showAvailableWarps(player)
        }
        return true
    }

}