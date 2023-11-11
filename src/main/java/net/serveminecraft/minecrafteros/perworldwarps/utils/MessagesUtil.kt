package net.serveminecraft.minecrafteros.perworldwarps.utils

import net.serveminecraft.minecrafteros.perworldwarps.PerWorldWarps
import org.bukkit.configuration.file.FileConfiguration

class MessagesUtil {

    companion object{
        fun getFullMessageFromConfig(plugin: PerWorldWarps, messagePath: String, replaces: Map<String, String>): String {
            val messagesConfig: FileConfiguration = plugin.messagesConfigFile
            val configMessage: String? = messagesConfig.getString(messagePath, "")
            if (configMessage != null){
                var replacedMessage: String = configMessage.toString()
                for (replace: String in replaces.keys) {
                    replacedMessage = replacedMessage.replace(replace, replaces.getValue(replace))
                }
                return replacedMessage
            }
            return ""
        }
        fun getFullMessageListFromConfig(plugin: PerWorldWarps, messagePath: String, replaces: Map<String, String>): MutableList<String> {
            val messagesConfig: FileConfiguration = plugin.messagesConfigFile
            val configMessages: MutableList<String> = messagesConfig.getStringList(messagePath)
            for (i in 1..<configMessages.size) {
                for (replace: String in replaces.keys) {
                    if (configMessages.elementAtOrNull(i) != null) {
                        configMessages[i] = configMessages.elementAt(i).replace(replace, replaces.getValue(replace))
                    }
                }
            }
            return configMessages
        }
    }

}