package me.zenox.superitems.tabcompleter;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.SuperItem;
import me.zenox.superitems.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainTabCompleter implements TabCompleter {
    List<String> arguments = new ArrayList<String>();
    List<String> items = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (arguments.isEmpty()) {
            arguments.add("give");
        }

        if(items.isEmpty()){
            for (Map.Entry<String, SuperItem> entry: SuperItems.getPlugin().registry.getRegisteredItems().entrySet()) {
                items.add(entry.getKey());
            }
        }

        List<String> results = new ArrayList<String>();
        if (args.length == 1) {
            for (String a : arguments) {
                if(a.toLowerCase().startsWith(args[0].toLowerCase())) {
                    results.add(a);
                }
            }
            return results;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            for (String b : items) {
                if(b.toLowerCase().startsWith(args[1].toLowerCase())) {
                    results.add(b);
                }
            }
            return results;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            results.add("<amount>");
            return results;
        }
        return null;
    }
}
