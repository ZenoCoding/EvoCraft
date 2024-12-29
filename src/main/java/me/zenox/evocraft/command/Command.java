package me.zenox.evocraft.command;

import com.google.common.primitives.Ints;
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
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.window.Window;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Command implements CommandExecutor, TabCompleter {

    private final EvoCraft plugin;
    private final Map<String, Consumer<CommandContext>> subcommands = new HashMap<>();

    public Command(EvoCraft plugin) {
        this.plugin = plugin;

        // Register subcommands
        subcommands.put("give", this::handleGive);
        subcommands.put("loottable", this::handleLootTable);
        subcommands.put("dropitematplayer", this::handleDropItemAtPlayer);
        subcommands.put("droploottable", this::handleDropLootTable);
        subcommands.put("enchant", this::handleEnchant);
        subcommands.put("reload", this::handleReload);
        subcommands.put("model", this::handleModel);
        subcommands.put("class", this::handleClass);
        subcommands.put("progresspath", this::handleProgressPath);

        plugin.getCommand("evocraft").setExecutor(this);
        plugin.getCommand("evocraft").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 0) {
            Util.sendMessage(sender, "EvoCraft Help Page. Available commands: " + String.join(", ", subcommands.keySet()));
            return true;
        }

        Consumer<CommandContext> subcommand = subcommands.get(args[0].toLowerCase());
        if (subcommand == null) {
            Util.sendMessage(sender, "Unknown subcommand. Type /evocraft for help.");
            return true;
        }

        CommandContext context = new CommandContext(sender, args);
        try {
            subcommand.accept(context);
        } catch (IllegalArgumentException e) {
            Util.sendMessage(sender, ChatColor.RED + "Error: " + e.getMessage());
        }
        return true;
    }

    private void handleGive(CommandContext context) {
        Player giver = context.requirePlayer();
        Player recipient = context.requirePlayerArgument(1, "Please specify a valid player to give an item.");
        String itemId = context.requireStringArgument(2, "Please specify an item to give.");
        ComplexItem item = ComplexItem.itemRegistry.get(itemId);
        if (item == null) throw new IllegalArgumentException("Item not found: " + itemId);

        int amount = context.optionalIntArgument(3, 1);
        recipient.getInventory().addItem(new ComplexItemStack(item, amount).getItem());
        Util.sendMessage(giver, "Gave " + recipient.getDisplayName() + " x" + amount + " [" + item.getDisplayName() + "]");
    }

    private void handleLootTable(CommandContext context) {
        Player player = context.requirePlayer();
        Player recipient = context.requirePlayerArgument(1, "Please specify a valid player.");
        String lootTableId = context.requireStringArgument(2, "Please specify a valid loot table.");
        int threatLevel = context.optionalIntArgument(3, 1);

        LootTable lootTable = LootTableRegistry.lootTableList.stream()
                .filter(lt -> lootTableId.equalsIgnoreCase(lt.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Loot table not found: " + lootTableId));

        lootTable.openLootGUI(recipient, threatLevel);
        Util.sendMessage(player, "Opened loot table " + lootTableId + " for " + recipient.getDisplayName());
    }

    private void handleDropItemAtPlayer(CommandContext context) {
        Player player = context.requirePlayer();
        Player target = context.requirePlayerArgument(1, "Please specify a valid player.");
        String itemId = context.requireStringArgument(2, "Please specify an item to drop.");
        ComplexItem item = ComplexItem.itemRegistry.get(itemId);
        if (item == null) throw new IllegalArgumentException("Item not found: " + itemId);

        int amount = context.optionalIntArgument(3, 1);
        target.getWorld().dropItemNaturally(target.getLocation(), new ComplexItemStack(item, amount).getItem());
        Util.sendMessage(player, "Dropped x" + amount + " [" + item.getDisplayName() + "] at " + target.getDisplayName());
    }

    private void handleDropLootTable(CommandContext context) {
        String entityUUID = context.requireStringArgument(1, "Please specify a valid entity UUID.");
        String lootTableId = context.requireStringArgument(2, "Please specify a valid loot table.");
        int amount = context.optionalIntArgument(3, 1);

        LootTable lootTable = LootTableRegistry.lootTableList.stream()
                .filter(lt -> lootTableId.equalsIgnoreCase(lt.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Loot table not found: " + lootTableId));

        lootTable.dropLoot(context.getSender().getServer().getEntity(UUID.fromString(entityUUID)).getLocation(), 1);
    }

    private void handleReload(CommandContext context) {
        plugin.reload();
        Util.sendMessage(context.getSender(), ChatColor.GREEN + "EvoCraft has been reloaded.");
    }

    private void handleProgressPath(CommandContext context) {
        Player player = context.requirePlayer();
        String pathId = context.requireStringArgument(1, "Please specify a valid path.");
        int level = context.optionalIntArgument(2, -1);

        PlayerData data = PlayerDataManager.getInstance().getPlayerData(player.getUniqueId());
        Path path = data.getPlayerClass().tree().path(pathId);
        if (path == null) throw new IllegalArgumentException("Path not found: " + pathId);

        if (level == -1) {
            data.progressPath(path);
            Util.sendMessage(player, "Progressed on path " + path.getId() + " to level " + data.getPathLevel(path) + ".");
        } else {
            data.setPathLevel(path, level);
            Util.sendMessage(player, "Set path " + path.getId() + " to level " + level + ".");
        }
    }

    private void handleEnchant(CommandContext context) {
        Player player = context.requirePlayer();
        Player target = context.requirePlayerArgument(1, "Please specify a valid player.");
        String enchantmentId = context.requireStringArgument(2, "Please specify an enchantment.");
        ComplexEnchantment enchantment = ComplexEnchantment.byId(enchantmentId);
        if (enchantment == null) throw new IllegalArgumentException("Enchantment not found: " + enchantmentId);

        int level = context.optionalIntArgument(3, 1);
        ItemStack item = target.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) throw new IllegalArgumentException("The target's main hand is empty.");

        ComplexItemMeta meta = ComplexItemStack.of(item).getComplexMeta();
        if (level > 0) {
            meta.addEnchantment(enchantment, level);
        } else {
            meta.removeEnchantment(enchantment);
        }
        Util.sendMessage(player, "Enchanted " + target.getDisplayName() + "'s item with " + enchantment.getName() + " (level " + level + ").");
    }

    private void handleModel(CommandContext context) {
        Player player = context.requirePlayer();
        ItemStack item = player.getEquipment().getItemInMainHand();
        if (item.getType().isAir()) {
            Util.sendMessage(player, "This item has no CustomModelData (that is created by EvoCraft).");
            return;
        }

        ComplexItem complexItem = ComplexItem.of(item);
        if (complexItem == null) {
            Util.sendMessage(player, "This item is not a valid EvoCraft item.");
            return;
        }

        Util.sendMessage(player, "The CustomModelData of " + item.getItemMeta().getDisplayName() + " is " + complexItem.getCustomModelData());
    }

    private void handleClass(CommandContext context) {
        Player player = context.requirePlayer();
        Window.single()
                .setViewer(player)
                .setTitle("Class Selection")
                .setGui(GameClass.getGui())
                .setCloseable(true)
                .build()
                .open();
    }



    // Additional handlers...

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 1) {
            return subcommands.keySet().stream()
                    .filter(cmd -> cmd.startsWith(args[0].toLowerCase()))
                    .sorted()
                    .collect(Collectors.toList());
        }

        if (args.length >= 2) {
            switch (args[0].toLowerCase()) {
                case "give", "dropitematplayer" -> {
                    if (args.length == 2) {
                        // Suggest player names
                        return getOnlinePlayers(sender).stream()
                                .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                                .sorted()
                                .collect(Collectors.toList());
                    } else if (args.length == 3) {
                        // Suggest item IDs
                        return ComplexItem.itemRegistry.keySet().stream()
                                .filter(item -> item.toLowerCase().startsWith(args[2].toLowerCase()))
                                .sorted()
                                .collect(Collectors.toList());
                    }
                }
                case "loottable", "droploottable" -> {
                    if (args.length == 2) {
                        // Suggest player or entity UUIDs
                        return getOnlinePlayers(sender).stream()
                                .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                                .sorted()
                                .collect(Collectors.toList());
                    } else if (args.length == 3) {
                        // Suggest loot table IDs
                        return LootTableRegistry.lootTableList.stream()
                                .map(LootTable::getId)
                                .filter(id -> id.toLowerCase().startsWith(args[2].toLowerCase()))
                                .sorted()
                                .collect(Collectors.toList());
                    }
                }
                case "progresspath" -> {
                    if (args.length == 2 && sender instanceof Player) {
                        // Suggest paths
                        Player player = (Player) sender;
                        GameClass gameClass = PlayerDataManager.getInstance()
                                .getPlayerData(player.getUniqueId()).getPlayerClass();
                        return gameClass.tree().paths().stream()
                                .map(Path::getId)
                                .filter(path -> path.toLowerCase().startsWith(args[1].toLowerCase()))
                                .sorted()
                                .collect(Collectors.toList());
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    private List<String> getOnlinePlayers(CommandSender sender) {
        return sender.getServer().getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    private static class CommandContext {
        private final CommandSender sender;
        private final String[] args;

        public CommandContext(CommandSender sender, String[] args) {
            this.sender = sender;
            this.args = args;
        }

        public CommandSender getSender() {
            return sender;
        }

        public Player requirePlayer() {
            if (!(sender instanceof Player)) throw new IllegalArgumentException("This command can only be used by a player.");
            return (Player) sender;
        }

        public Player requirePlayerArgument(int index, String errorMessage) {
            if (args.length <= index) throw new IllegalArgumentException(errorMessage);
            Player player = sender.getServer().getPlayer(args[index]);
            if (player == null) throw new IllegalArgumentException("Player not found: " + args[index]);
            return player;
        }

        public String requireStringArgument(int index, String errorMessage) {
            if (args.length <= index) throw new IllegalArgumentException(errorMessage);
            return args[index];
        }

        public int optionalIntArgument(int index, int defaultValue) {
            if (args.length <= index) return defaultValue;
            Integer value = Ints.tryParse(args[index]);
            if (value == null) throw new IllegalArgumentException("Invalid number: " + args[index]);
            return value;
        }

        public org.bukkit.Server requireServer() {
            return sender.getServer();
        }
    }
}