package me.zenox.superitems.data;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;

public class TranslatableText {
    private String key;

    public TranslatableText(String key){
        this.key = key;
    }

    @Override
    public String toString() {
        String translation = SuperItems.getPlugin().getLang().get(key);
        if(translation == null) {
            translation = key;
            Util.logToConsole(ChatColor.RED + "[ERROR] " + ChatColor.WHITE + "Unregistered translatable string: " + key);
        }
        return ChatColor.translateAlternateColorCodes('&', translation);
    }

    public enum TranslatableType {
        ITEM_NAME("item-name"), ITEM_LORE("item-lore"), ABILITY_NAME("ability-name"), ABILITY_LORE("ability_lore"), COMMAND("cmd"), MISC_MSG("msg");

        private String key;

        TranslatableType(String key){
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
