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
import me.zenox.evocraft.story.Chapter;
import me.zenox.evocraft.story.ChapterManager;
import me.zenox.evocraft.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            case "removechapterdata" -> {
                if (sender instanceof Player){
                    ((Player) sender).getPersistentDataContainer().remove(ChapterManager.CHAPTER_KEY);
                    Util.sendMessage(sender, "All chapter data has been removed.");
                }
                else {
                    Util.sendMessage(sender, "You must be a player to use this command!");
                }
            }
            case "removemetadata" -> {
                if (sender instanceof Player){
                    sender.getServer().getPlayer(args[1]).removeMetadata("hasStarted", EvoCraft.getPlugin());
                    Util.sendMessage(sender, "All chapter data has been removed.");
                }
                else {
                    Util.sendMessage(sender, "You must be a player to use this command!");
                }
            }
            case "setchapter" -> {
                // Set the chapter given the player and the chapter's id
                if (args.length < 2 || sender.getServer().getPlayer(args[1]) == null) {
                    Util.sendMessage(sender, "Please specify a valid user to set the chapter of.");
                    return true;
                }
                if (args.length < 3) {
                    Util.sendMessage(sender, "Please specify a valid chapter.");
                    return true;
                }
                Player player = sender.getServer().getPlayer(args[1]);
                Chapter chapter = EvoCraft.getChapterManager().getChapter(Ints.tryParse(args[2]));
                if (chapter == null) {
                    Util.sendMessage(sender, "This chapter does not exist!");
                    return true;
                }
                EvoCraft.getChapterManager().setChapter(player, chapter);
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
                data.progressAbility(path);
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
        if (arguments.isEmpty()) {
            arguments.add("give");
            arguments.add("loottable");
            arguments.add("droploottable");
            arguments.add("dropitematplayer");
            arguments.add("enchant");
            arguments.add("reload");
            arguments.add("removechapterdata");
            arguments.add("removemetadata");
            arguments.add("setchapter");
            arguments.add("class");
            arguments.add("progresspath");
        }

        if (items.isEmpty()) {
            items.addAll(ComplexItem.itemRegistry.keySet());
        }

        List<String> results = new ArrayList<>();
        if (args.length == 1) {
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                    results.add(a);
                }
            }
            return results;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("progresspath")){
            GameClass gameClass = PlayerDataManager.getInstance().getPlayerData(((Player) sender).getUniqueId()).getPlayerClass();
            for (Path path: gameClass.tree().paths()){
                if (path.getId().toLowerCase().startsWith(args[1].toLowerCase())){
                    results.add(path.getId());
                }
            }
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
