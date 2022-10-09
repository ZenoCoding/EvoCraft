package me.zenox.superitems.command;

import com.google.common.primitives.Ints;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.item.ItemRegistry;
import me.zenox.superitems.loot.LootTable;
import me.zenox.superitems.loot.LootTableRegistry;
import me.zenox.superitems.tabcompleter.MainTabCompleter;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MainCommand implements CommandExecutor {

    private final SuperItems plugin;

    public MainCommand(SuperItems plugin) {
        this.plugin = plugin;
        plugin.getCommand("superitems").setExecutor(this);
        plugin.getCommand("superitems").setTabCompleter(new MainTabCompleter());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            Util.sendMessage(sender, "SuperItems Help Page.");
            return true;
        }

        switch (args[0]) {

            case "give":

                if (args.length < 2 || sender.getServer().getPlayer(args[1]) == null) {
                    Util.sendMessage(sender, "Please specify a valid user to give an item.");
                    return true;
                }

                Player p = (Player) sender;
                Player givento = sender.getServer().getPlayer(args[1]);

                if (args.length < 3) {
                    Util.sendMessage(p, "Please specify a item to give.");
                    return true;
                }

                ComplexItem itemtype = ItemRegistry.getBasicItemFromId(args[2]);

                if (itemtype == null) {
                    Util.sendMessage(p, "This item could not be found!");
                } else {
                    Object amount;
                    if (args.length >= 4) {
                        amount = Ints.tryParse(args[3]);
                        if (amount == null) {
                            Util.sendMessage(p, args[3] + " is not a valid integer! Please specify an integer for argument <amount>.");
                            return true;
                        }
                    } else {
                        amount = 1;
                    }

                    givento.getInventory().addItem(new ComplexItemStack(itemtype, (int) amount).getItem());
                    Util.sendMessage(p, "You gave " + givento.getDisplayName() + " x" + amount + " [" + itemtype.getDisplayName() + ChatColor.GOLD + "]");
                }
                return true;
            case "loottable":
                if (args.length < 2 || sender.getServer().getPlayer(args[1]) == null) {
                    Util.sendMessage(sender, "Please specify a valid user to give a loottable for.");
                    return true;
                }

                if (args.length < 3) {
                    Util.sendMessage(sender, "Please specify a valid loottable.");
                    return true;
                }

                int threatlevel;

                if (args.length < 4 || Ints.tryParse(args[3]) == null) {
                    threatlevel = 1;
                } else {
                    threatlevel = Ints.tryParse(args[3]);
                }

                for (LootTable lootTable : LootTableRegistry.lootTableList) {
                    if (args[2].equalsIgnoreCase(lootTable.getId())) {
                        lootTable.openLootGUI(sender.getServer().getPlayer(args[1]), threatlevel);
                        return true;
                    }
                }
                break;
            case "dropitematplayer":

                if (args.length < 2 || sender.getServer().getPlayer(args[1]) == null) {
                    Util.sendMessage(sender, "Please specify a valid user to drop an item at.");
                    return true;
                }

                Player dropgivento = sender.getServer().getPlayer(args[1]);

                if (args.length < 3) {
                    Util.sendMessage(sender, "Please specify a item to drop.");
                    return true;
                }

                ComplexItem itemtypetodrop = ItemRegistry.getBasicItemFromId(args[2]);

                if (itemtypetodrop == null) {
                    Util.sendMessage(sender, "This item could not be found!");
                } else {
                    Object amount;
                    if (args.length >= 4) {
                        amount = Ints.tryParse(args[3]);
                        if (amount == null) {
                            Util.sendMessage(sender, args[3] + " is not a valid integer! Please specify an integer for argument <amount>.");
                            return true;
                        }
                    } else {
                        amount = 1;
                    }

                    dropgivento.getWorld().dropItemNaturally(dropgivento.getLocation(), new ComplexItemStack(itemtypetodrop, (int) amount).getItem());
                    Util.sendMessage(sender, "You gave " + dropgivento.getDisplayName() + " x" + amount + " [" + itemtypetodrop.getDisplayName() + ChatColor.GOLD + "]");
                }
                break;
            case "droploottable":
                if (args.length < 2 || sender.getServer().getEntity(UUID.fromString(args[1])) == null) {
                    Util.sendMessage(sender, "Please specify a valid entity to give a loot table for.");
                    return true;
                }

                if (args.length < 3) {
                    Util.sendMessage(sender, "Please specify a valid loot table.");
                    return true;
                }

                for (LootTable lootTable : LootTableRegistry.lootTableList) {
                    if (args[2].equalsIgnoreCase(lootTable.getId())) {
                        lootTable.dropLoot(sender.getServer().getEntity(UUID.fromString(args[1])).getLocation(), 1);
                        return true;
                    }
                }
                return false;
            case "reload":
                plugin.reload();
                Util.sendMessage(sender, ChatColor.WHITE + "SuperItems " + ChatColor.GOLD + "v" + plugin.getDescription().getVersion() + ChatColor.WHITE + " has been reloaded.");

                return true;
            default:
                Util.sendMessage(sender, "SuperItems Help Page.");
                break;
        }
        return false;
    }
}
