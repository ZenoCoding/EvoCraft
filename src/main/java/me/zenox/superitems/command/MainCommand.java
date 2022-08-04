package me.zenox.superitems.command;

import com.google.common.primitives.Ints;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.BasicItem;
import me.zenox.superitems.loot.LootTable;
import me.zenox.superitems.loot.LootTableEntry;
import me.zenox.superitems.tabcompleter.MainTabCompleter;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

                BasicItem itemtype = plugin.registry.getBasicItemFromId(args[2]);

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
                    List<String> argsection = args.length > 4 ? List.of(Arrays.copyOfRange(args, 5, args.length)) : new ArrayList<>();

                    givento.getInventory().addItem(itemtype.getItemStackWithData((int) amount, argsection));
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

                List<LootTable> lootTableList = new ArrayList();
                lootTableList.add(new LootTable("blaziel", List.of(
                        new LootTableEntry(plugin.registry.getBasicItemFromId("dark_skull").getItemStack(1), 1, 1, 0.01),
                        new LootTableEntry(plugin.registry.getBasicItemFromId("burning_ashes").getItemStack(1), 2, 4, 0.07),
                        new LootTableEntry(plugin.registry.getBasicItemFromId("purified_magma_distillate").getItemStack(1), 1, 2, 0.1),
                        new LootTableEntry(plugin.registry.getBasicItemFromId("enchanted_blaze_rod").getItemStack(1), 1, 3, 0.4),
                        new LootTableEntry(plugin.registry.getBasicItemFromId("enchanted_magma_block").getItemStack(1), 1, 15, 0.5),
                        new LootTableEntry(new ItemStack(Material.BLAZE_ROD), 5, 32, 1)
                )));

                int threatlevel;

                if (args.length < 4 || Ints.tryParse(args[3]) == null) {
                    threatlevel = 1;
                } else {
                    threatlevel = Ints.tryParse(args[3]);
                }

                for (LootTable lootTable : lootTableList) {
                    if (args[2].equalsIgnoreCase(lootTable.getId())) {
                        lootTable.openLootGUI(sender.getServer().getPlayer(args[1]), threatlevel);
                        return true;
                    }
                }

            default:
                Util.sendMessage(sender, "SuperItems Help Page.");
                break;
        }
        return false;
    }
}
