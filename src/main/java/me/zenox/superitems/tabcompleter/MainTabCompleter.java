package me.zenox.superitems.tabcompleter;

import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.ItemRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MainTabCompleter implements TabCompleter {
    List<String> arguments = new ArrayList();
    List<String> items = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (arguments.isEmpty()) {
            arguments.add("give");
            arguments.add("loottable");
            arguments.add("droploottable");
            arguments.add("dropitematplayer");
        }

        if (items.isEmpty()) {
            for (ComplexItem item : ItemRegistry.getRegisteredItems()) {
                items.add(item.getId());
            }
        }

        List<String> results = new ArrayList<String>();
        if (args.length == 1) {
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                    results.add(a);
                }
            }
            return results;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            for (String b : items) {
                if (b.toLowerCase().startsWith(args[2].toLowerCase())) {
                    results.add(b);
                }
            }
            return results;
        } else if (args.length == 4 && args[0].equalsIgnoreCase("give")) {
            results.add("<amount>");
            return results;
        }
        return null;
    }
}
