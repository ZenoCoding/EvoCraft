package me.zenox.superitems.tabcompleter;

import me.zenox.superitems.enchant.ComplexEnchantment;
import me.zenox.superitems.item.ComplexItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MainTabCompleter implements TabCompleter {
    final List<String> arguments = new ArrayList<>();
    final List<String> items = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (arguments.isEmpty()) {
            arguments.add("give");
            arguments.add("loottable");
            arguments.add("droploottable");
            arguments.add("dropitematplayer");
            arguments.add("enchant");
            arguments.add("reload");
        }

        if (items.isEmpty()) {
            for (ComplexItem item : ComplexItem.itemRegistry) {
                items.add(item.getId());
            }
        }

        List<String> results = new ArrayList<>();
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
        } else if (args.length == 3 && args[0].equalsIgnoreCase("enchant")) {
            for (ComplexEnchantment b : ComplexEnchantment.getRegisteredEnchants()) {
                if (b.getId().toLowerCase().startsWith(args[2].toLowerCase())) {
                    results.add(b.getId());
                }
            }
            return results;
        } else if (args.length == 4 && args[0].equalsIgnoreCase("enchant")) {
            results.add("<level>");
            return results;
        } else if (args.length == 4 && args[0].equalsIgnoreCase("give")) {
            results.add("<amount>");
            return results;
        }
        return null;
    }
}
