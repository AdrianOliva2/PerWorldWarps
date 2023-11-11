package net.serveminecraft.minecrafteros.perworldwarps.commands

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.serveminecraft.minecrafteros.perworldwarps.PerWorldWarps
import net.serveminecraft.minecrafteros.perworldwarps.utils.ListUtils
import net.serveminecraft.minecrafteros.perworldwarps.utils.MessagesUtil
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class SetWarpCommand(private val plugin: PerWorldWarps): CommandExecutor {

    private val replaces: HashMap<String, String> = HashMap()

    init {
        replaces["%prefix%"] = plugin.prefix
    }

    private fun setWarp(player: Player, warpName: String, permission: Boolean = false) {
        replaces["%warp%"] = warpName
        val warpsConfig: FileConfiguration = plugin.warpsConfigFile
        val location: Location = player.location
        val world: World = location.world
        if (warpsConfig.get("Worlds.${world.name}.$warpName") == null) {
            val x: Double = location.x
            val y: Double = location.y
            val z: Double = location.z
            val yaw: Float = location.yaw
            val pitch: Float = location.pitch
            warpsConfig.set("Worlds.${world.name}.$warpName.x", x)
            warpsConfig.set("Worlds.${world.name}.$warpName.y", y)
            warpsConfig.set("Worlds.${world.name}.$warpName.z", z)
            warpsConfig.set("Worlds.${world.name}.$warpName.yaw", yaw)
            warpsConfig.set("Worlds.${world.name}.$warpName.pitch", pitch)

            if (permission)
                warpsConfig.set("Worlds.${world.name}.$warpName.permission", "perworldwarps.warp.$warpName")

            plugin.saveWarpsConfig()
            val message = LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "warp-created-successfully", replaces))
            player.sendMessage(message)
        } else {
            val message = LegacyComponentSerializer.legacyAmpersand()
                .deserialize(
                    MessagesUtil.getFullMessageFromConfig(
                        plugin,
                        "warp-already-exists",
                        replaces
                    )
                )
            player.sendMessage(message)
        }
        replaces.remove("%warp%")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "console-command-error", replaces)))
        } else {
            val player: Player = sender
            if (player.isOp || player.hasPermission("perworldwarps.setwarp")) {
                if (args != null) {
                    when (args.size) {
                        1 -> {
                            setWarp(player, args[0])
                        }
                        2 -> {
                            if (args[1] == "true" || args[1] == "false") {
                                val permission: Boolean = args[1].toBoolean()
                                setWarp(player, args[0], permission)
                            } else {
                                val message = LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "setwarp-command-help", replaces))
                                player.sendMessage(message)
                            }
                        }
                        else -> {
                            val message = LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "setwarp-command-help", replaces))
                            player.sendMessage(message)
                        }
                    }
                } else {
                    val message = LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "setwarp-command-help", replaces))
                    player.sendMessage(message)
                }
            } else {
                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(MessagesUtil.getFullMessageFromConfig(plugin, "no-permission", replaces)))
                return false
            }
        }
        return true
    }

}