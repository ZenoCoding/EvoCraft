package me.zenox.superitems.data;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>TranslatableList</b>
 *
 * <p>TranslatableList object used for translating lists of strings/text that are translated as one group. (i.e. lore)</p>
 */
public class TranslatableList {
    private String key;

    /**
     * TranslatableList object used for translating lists of strings/text that are translated as one group. <br>(i.e. lore)
     * Text is defined in lang files (i.e. en_US.yml)
     * @param key the key provided for this translatable text
     */
    public TranslatableList(String key){
        this.key = key;
    }

    public List<String> getList() {
        List<String> translation = new ArrayList<>(SuperItems.getPlugin().getLang().getList(key));
        if(translation == null) {
            translation = new ArrayList<>();
        }
        for (String s : translation) translation.set(translation.indexOf(s), ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', s));

        return translation;
    }
}
