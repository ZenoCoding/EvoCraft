package me.zenox.superitems.data;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Util;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LanguageLoader {

    private final Character LIST_CONCAT_SEPERATOR = '␟';

    private final HashMap<String, String> translationMap = new HashMap<>();

    public LanguageLoader(SuperItems plugin) {
        File languageDirectory = new File(plugin.getDataFolder(), "languages/");
        File defaultLanguageFile = new File(plugin.getDataFolder(), "languages/en_US.yml");
        if (!languageDirectory.isDirectory() || SuperItems.getPlugin().getConfigLoader().getConfig().getBoolean("force_update_default")) {
            languageDirectory.mkdir();
            try {
                InputStream stream = plugin.getResource("languages/en_US.yml");
                FileUtils.copyInputStreamToFile(stream, defaultLanguageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (plugin.getConfig().getString("locale") != null && plugin.getConfig().getString("locale").equals("en_US")) {
            FileConfiguration translations = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "languages/" + plugin.getConfig().getString("locale") + ".yml"));
            for (String translation : translations.getKeys(false)) {
                // if the thing is a list (contains lore)
                if (translation.contains("lore")) {
                    StringBuilder concat = new StringBuilder();
                    for(String s : translations.getStringList(translation)) concat.append(s + LIST_CONCAT_SEPERATOR);
                    translationMap.put(translation, concat.toString());
                } else {
                    translationMap.put(translation, translations.getString(translation));
                }
            }
        } else {
            FileConfiguration translations = YamlConfiguration.loadConfiguration(defaultLanguageFile);
            for (String translation : translations.getKeys(false)) {
                // if the thing is a list (contains lore)
                if (translation.contains("lore")) {
                    StringBuilder concat = new StringBuilder();
                    for(String s : translations.getStringList(translation)) concat.append(s + LIST_CONCAT_SEPERATOR);
                    translationMap.put(translation, concat.toString());
                } else {
                    translationMap.put(translation, translations.getString(translation));
                }
            }
        }
    }

    public String get(String path) {
        return translationMap.get(path);
    }

    public List<String> getList(String path) {
        if (translationMap.get(path) == null) return List.of();
        return Arrays.asList(translationMap.get(path).split(LIST_CONCAT_SEPERATOR.toString()));
    }
}