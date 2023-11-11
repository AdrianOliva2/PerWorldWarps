package net.serveminecraft.minecrafteros.perworldwarps.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

class ListUtils {
    companion object{
        fun componentListToStringList(list: MutableList<Component>?): MutableList<String>? {
            if (!list.isNullOrEmpty()) {
                val stringList: MutableList<String> = emptyList<String>().toMutableList()
                for (component: Component in list) {
                    stringList += LegacyComponentSerializer.legacyAmpersand().serialize(component)
                }
                return stringList
            }
            return null
        }

        fun stringListToComponentList(list: MutableList<String>?): MutableList<Component>?{
            if (!list.isNullOrEmpty()){
                val componentList: MutableList<Component> = emptyList<Component>().toMutableList()
                for (string: String in list) {
                    componentList += LegacyComponentSerializer.legacyAmpersand().deserialize(string)
                }
                return componentList
            }
            return null
        }
    }
}