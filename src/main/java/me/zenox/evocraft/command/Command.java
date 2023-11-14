package me.zenox.evocraft.command;

import com.google.common.primitives.Ints;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.data.PlayerData;
import me.zenox.evocraft.data.PlayerDataManager;
import me.zenox.evocraft.enchant.ComplexEnchantment;
import me.zenox.evocraft.gameclass.GameClass;
import me.zenox.evocraft.gameclass.tree.Path;
import me.zenox.evocraft.item.ComplexItem;
import me.zenox.evocraft.item.ComplexItemMeta;
import me.zenox.evocraft.item.ComplexItemStack;
import me.zenox.evocraft.loot.LootTable;
import me.zenox.evocraft.loot.LootTableRegistry;
import me.zenox.evocraft.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class Command implements CommandExecutor, TabCompleter {

    private final EvoCraft plugin;

    public Command(EvoCraft plugin) {
        this.plugin = plugin;
        plugin.getCommand("evocraft").setExecutor(this);
        plugin.getCommand("evocraft").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (args.length == 0) {
            Util.sendMessage(sender, "EvoCraft Help Page.");
            return true;
        }

        switch (args[0]) {
            case "give" -> {
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
                ComplexItem itemtype = ComplexItem.itemRegistry.get(args[2]);
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
            }
            case "loottable" -> {
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
            }
            case "dropitematplayer" -> {
                if (args.length < 2 || sender.getServer().getPlayer(args[1]) == null) {
                    Util.sendMessage(sender, "Please specify a valid user to drop an item at.");
                    return true;
                }
                Player dropgivento = sender.getServer().getPlayer(args[1]);
                if (args.length < 3) {
                    Util.sendMessage(sender, "Please specify a item to drop.");
                    return true;
                }
                ComplexItem itemtypetodrop = ComplexItem.itemRegistry.get(args[2]);
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
            }
            case "droploottable" -> {
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
            }
            case "enchant" -> {
                if (args.length < 2 || sender.getServer().getPlayer(args[1]) == null) {
                    Util.sendMessage(sender, "Please specify a valid user to give an item.");
                    return true;
                }
                if (args.length < 3) {
                    Util.sendMessage(sender, "Please specify a item to give.");
                    return true;
                }
                Player enchanted = sender.getServer().getPlayer(args[1]);
                ComplexEnchantment enchant = ComplexEnchantment.byId(args[2]);
                if (enchant == null) {
                    Util.sendMessage(sender, "Enchantment " + ChatColor.WHITE + args[2] + " doesn't exist.");
                } else {
                    Object level;
                    if (args.length >= 4) {
                        level = Ints.tryParse(args[3]);
                        if (level == null) {
                            Util.sendMessage(sender, args[3] + " is not a valid integer! Please specify an integer for argument <level>.");
                            return true;
                        }
                    } else {
                        level = 1;
                    }

                    ComplexItemMeta meta = ComplexItemStack.of(enchanted.getInventory().getItemInMainHand()).getComplexMeta();
                    if(((int) level) > 0) meta.addEnchantment(enchant, (Integer) level);
                    else meta.removeEnchantment(enchant);
                    Util.sendMessage(sender, "You enchanted " + enchanted.getDisplayName() + " with " + ChatColor.WHITE + enchant.getName() + " " + level);
                }
                return true;
            }
            case "reload" -> {
                plugin.reload();
                Util.sendMessage(sender, ChatColor.WHITE + "EvoCraft " + ChatColor.GREEN + "v" + plugin.getDescription().getVersion() + ChatColor.WHITE + " has been reloaded.");
                return true;
            }
            case "model" -> {
                ItemStack item = ((Player) sender).getEquipment().getItemInMainHand();
                if(item.getType() == Material.AIR) {
                    Util.sendMessage(sender, ChatColor.WHITE + "This item has no CustomModelData (that is created by EvoCraft)");
                    return true;
                }
                Util.sendMessage(sender, ChatColor.WHITE + "The CustomModelData of " + item.getItemMeta().getDisplayName() + ChatColor.WHITE + "  is " + ComplexItem.of(item).getCustomModelData());
                return true;
            }
            case "class" -> // Open the class selection GUI
                    new SimpleWindow(((Player) sender), "Class Selection", GameClass.getGui(), true, true).show();
            case "progresspath" -> {
                if (args.length < 2) {
                    Util.sendMessage(sender, "Please specify a valid path.");
                    return true;
                }
                Player player = ((Player) sender);
                PlayerData data = PlayerDataManager.getInstance().getPlayerData(player.getUniqueId());
                Path path = data.getPlayerClass().tree().path(args[1]);
                if (path == null) {
                    Util.sendMessage(sender, "This path does not exist!");
                    return true;
                }
                try {
                    data.progressAbility(path);
                } catch (IllegalArgumentException e) {
                    Util.sendMessage(sender, e.getMessage());
                    return true;
                }
                return true;
            }
            default -> Util.sendMessage(sender, "EvoCraft Help Page.");
        }
        return true;
    }

    final List<String> arguments = new ArrayList<>();
    final List<String> items = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return getCommandSuggestions(args[0]);
        } else if (args.length == 2 && "progresspath".equalsIgnoreCase(args[0]) && sender instanceof Player) {
            return getPathSuggestions((Player) sender, args[1]);
        } else if (args.length == 3) {
            return getThirdArgumentSuggestions(args[0], args[2]);
        } else if (args.length == 4 && "enchant".equalsIgnoreCase(args[0])) {
            return Collections.singletonList("<level>");
        } else if (args.length == 4 && "give".equalsIgnoreCase(args[0])) {
            return Collections.singletonList("<amount>");
        }
        return Collections.emptyList();
    }

    private List<String> getCommandSuggestions(String arg) {
        List<String> commands = Arrays.asList("give", "loottable", "droploottable", "dropitematplayer",
                "enchant", "reload", "removechapterdata", "removemetadata",
                "setchapter", "class", "progresspath");
        return commands.stream()
                .filter(a -> a.toLowerCase().startsWith(arg.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<String> getPathSuggestions(Player player, String arg) {
        GameClass gameClass = PlayerDataManager.getInstance().getPlayerData(player.getUniqueId()).getPlayerClass();
        return gameClass.tree().paths().stream()
                .map(Path::getId)
                .filter(path -> path.toLowerCase().startsWith(arg.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<String> getThirdArgumentSuggestions(String arg0, String arg2) {
        if ("give".equalsIgnoreCase(arg0)) {
            return getItemSuggestions(arg2);
        } else if ("enchant".equalsIgnoreCase(arg0)) {
            return getEnchantmentSuggestions(arg2);
        }
        return Collections.emptyList();
    }

    private List<String> getItemSuggestions(String arg) {
        if (items.isEmpty()) {
            items.addAll(ComplexItem.itemRegistry.keySet());
        }
        return items.stream()
                .filter(b -> b.toLowerCase().startsWith(arg.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<String> getEnchantmentSuggestions(String arg) {
        return ComplexEnchantment.getRegisteredEnchants().stream()
                .map(ComplexEnchantment::getId)
                .filter(id -> id.toLowerCase().startsWith(arg.toLowerCase()))
                .collect(Collectors.toList());
    }
}
