package net.serveminecraft.minecrafteros.perworldwarps.commands

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.serveminecraft.minecrafteros.perworldwarps.PerWorldWarps
import net.serveminecraft.minecrafteros.perworldwarps.utils.MessagesUtil
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

class WarpCommand(private val plugin: PerWorldWarps): CommandExecutor {

    private val replaces: HashMap<String, String> = HashMap()

    init {
        replaces["%prefix%"] = plugin.prefix
    }

    private fun teleport(player: Player, warpsConfig: FileConfiguration, warpName: String) {
        val x: Double = warpsConfig.getDouble("Worlds.${player.world.name}.$warpName.x")
        val y: Double = warpsConfig.getDouble("Worlds.${player.world.name}.$warpName.y")
        val z: Double = warpsConfig.getDouble("Worlds.${player.world.name}.$warpName.z")
        val yaw: Float = warpsConfig.getDouble("Worlds.${player.world.name}.$warpName.yaw").toFloat()
        val pitch: Float = warpsConfig.getDouble("Worlds.${player.world.name}.$warpName.pitch").toFloat()
        val location = Location(player.world, x, y, z, yaw, pitch)
        replaces["%warp%"] = warpName
        player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "teleporting-to-warp", replaces)))
        replaces.remove("%warp%")
        player.teleport(location)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "console-command-error", replaces)))
            return false
        } else {
            val player: Player = sender
            val warpsConfig: FileConfiguration = plugin.warpsConfigFile
            if (!args.isNullOrEmpty()) {
                if (args.size == 1) {
                    val warpName: String = args[0]
                    if (warpsConfig.contains("Worlds.${player.world.name}.$warpName")) {
                        if (warpsConfig.contains("Worlds.${player.world.name}.$warpName.permission")){
                            if (player.isOp || player.hasPermission(warpsConfig.getString("Worlds.${player.world.name}.$warpName.permission")!!)){
                                teleport(player, warpsConfig, args[0])
                            } else {
                                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "no-permission", replaces)))
                            }
                        } else {
                            teleport(player, warpsConfig, args[0])
                        }
                    } else {
                        replaces["%warp%"] = warpName
                        player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "warp-not-exists", replaces)))
                        replaces.remove("%warp%")
                        return false
                    }
                } else {
                    player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "warp-command-help", replaces)))
                    return false
                }
            } else {
                val warpsCommand = WarpsCommand(plugin)
                warpsCommand.showAvailableWarps(player)
            }
        }
        return true
    }

}