package net.serveminecraft.minecrafteros.perworldwarps

import io.papermc.paper.plugin.configuration.PluginMeta
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.serveminecraft.minecrafteros.perworldwarps.commands.*
import net.serveminecraft.minecrafteros.perworldwarps.configs.CustomConfig
import net.serveminecraft.minecrafteros.perworldwarps.tabcompleters.SubCommandsCompleter
import net.serveminecraft.minecrafteros.perworldwarps.tabcompleters.WarpsCompleter
import org.bukkit.command.PluginCommand
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

class PerWorldWarps : JavaPlugin() {

    @Suppress("UnstableApiUsage")
    private val pluginMeta: PluginMeta = this.getPluginMeta()
    private val pluginName: String = pluginMeta.name
    private val pluginVersion: String = pluginMeta.version
    private lateinit var warpsConfig: CustomConfig
    lateinit var warpsConfigFile: FileConfiguration
    private lateinit var messagesConfig: CustomConfig
    lateinit var messagesConfigFile: FileConfiguration
    var prefix: String = ""

    override fun onEnable() {
        warpsConfig = CustomConfig("warps.yml", this)
        warpsConfigFile = warpsConfig.getConfig()
        messagesConfig = CustomConfig("messages.yml", this)
        messagesConfigFile = messagesConfig.getConfig()
        prefix = messagesConfigFile.getString("prefix", "")!!
        registerCommands()

        this.server.consoleSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&8[&b$pluginName&8] &aHas been enabled on version &8[&b$pluginVersion&8]"))
    }

    private fun registerCommands() {
        val perWorldWarpsCommand: PluginCommand? = getCommand("perworldwarps")
        perWorldWarpsCommand?.aliases = listOf("pww")
        perWorldWarpsCommand?.tabCompleter = SubCommandsCompleter(this)
        perWorldWarpsCommand?.setExecutor(PerWorldWarpsCommand(this))
        getCommand("warp")?.setExecutor(WarpCommand(this))
        getCommand("warp")?.tabCompleter = WarpsCompleter(this)
        getCommand("warps")?.setExecutor(WarpsCommand(this))
        getCommand("setwarp")?.setExecutor(SetWarpCommand(this))
        getCommand("delwarp")?.setExecutor(DelWarpCommand(this))
        getCommand("delwarp")?.tabCompleter = WarpsCompleter(this)
    }

    fun saveWarpsConfig() {
        warpsConfig.saveConfig()
    }

    fun reloadWarpsConfig() {
        warpsConfig.reloadConfig()
        warpsConfigFile = warpsConfig.getConfig()
    }

    fun reloadMessagesConfig() {
        messagesConfig.reloadConfig()
        messagesConfigFile = messagesConfig.getConfig()
        prefix = messagesConfigFile.getString("prefix", "")!!
    }

    override fun onDisable() {
        this.server.consoleSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&8[&b$pluginName&8] &cHas been disabled on version &8[&b$pluginVersion&8]"))
    }

}
