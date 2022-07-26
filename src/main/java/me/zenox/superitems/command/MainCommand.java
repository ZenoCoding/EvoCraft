package me.zenox.superitems.command;

import com.google.common.primitives.Ints;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.BasicItem;
import me.zenox.superitems.items.SuperItem;
import me.zenox.superitems.items.SuperItemRegistry;
import me.zenox.superitems.tabcompleter.MainTabCompleter;
import me.zenox.superitems.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;

public class MainCommand implements CommandExecutor {

    private SuperItems plugin;

    public MainCommand(SuperItems plugin){
        this.plugin = plugin;
        plugin.getCommand("superitems").setExecutor(this);
        plugin.getCommand("superitems").setTabCompleter(new MainTabCompleter());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            Util.logToConsole("This command can only be run in-game!");
            return true;
        }
        Player p = (Player) sender;
        if(args.length == 0){
            Util.sendMessage( p, "SuperItems Help Page.");
            return true;
        }
        switch(args[0]){
            case "give":
                if(args.length == 1){
                    Util.sendMessage(p, "Please specify a valid item to give.");
                    return true;
                }
                BasicItem itemtype = plugin.registry.getBasicItemFromId(args[1]);
                if(itemtype == null){
                    Util.sendMessage(p, "This item could not be found!");
                    Util.sendMessage(p, plugin.registry.getRegisteredItems().toString());
                    return true;
                } else {
                    Object amount;
                    if(args.length >= 3) {
                        amount = Ints.tryParse(args[2]);
                        if (amount == null){
                            Util.sendMessage(p, args[2] + " is not a valid integer! Please specify an integer for argument <amount>.");
                        }
                    } else{
                        amount = 1;
                    }
                    p.getInventory().addItem(itemtype.getItemStack((int) amount));
                    Util.sendMessage(p, "You have been given x" + amount + " [" + itemtype.getDisplayName() + ChatColor.GOLD + "]");
                    return true;
                }
            case "removedatacooldown":
                PersistentDataContainer container = p.getPersistentDataContainer();
                NamespacedKey cooldownKey = new NamespacedKey(SuperItems.getPlugin(), "soul_rift_cooldown");
                container.remove(cooldownKey);
                Util.sendMessage(p, "success");
            default:
                Util.sendMessage( (Player) sender, "SuperItems Help Page.");
                break;
        }
        return false;
    }
}
