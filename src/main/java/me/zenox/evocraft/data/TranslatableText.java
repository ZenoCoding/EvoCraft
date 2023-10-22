package me.zenox.evocraft.data;

import me.zenox.evocraft.EvoCraft;
import org.bukkit.ChatColor;

import java.io.Serializable;

public class TranslatableText implements Serializable {
    private final String key;

    public TranslatableText(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        String translation = EvoCraft.getPlugin().getLang().get(key);
        if (translation == null) {
            translation = key;
            // Util.logToConsole(ChatColor.RED + "[ERROR] " + ChatColor.WHITE + "Unregistered translatable string: " + key);
        }
        return ChatColor.translateAlternateColorCodes('&', translation);
    }

    public enum Type {
        ITEM_NAME("item-name"), ITEM_LORE("item-lore"),
        ABILITY_NAME("ability-name"), ABILITY_LORE("ability-lore"),
        COMMAND("cmd"), MISC_MSG("msg"), ENCHANT_NAME("enchant-name"),
        ATTRIBUTE("attribute"), GUI("gui"),
        CLASS_NAME("class-name"), CLASS_DESC("class-desc");

        private final String key;

        Type(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return key;
        }
    }
}
