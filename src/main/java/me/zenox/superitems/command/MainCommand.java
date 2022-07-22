package me.zenox.superitems.command;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.SuperItem;
import me.zenox.superitems.items.SuperItemRegistry;
import me.zenox.superitems.tabcompleter.MainTabCompleter;
import me.zenox.superitems.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    private SuperItems plugin;

    public MainCommand(SuperItems plugin){
        this.plugin = plugin;
        plugin.getCommand("superitems").setExecutor(this);
        plugin.getCommand("superitems").setTabCompleter(new MainTabCompleter());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0){
            Util.sendMessage( (Player) sender, "SuperItems Help Page.");
            return true;
        }
        switch(args[0]){
            case "give":
                Player p = (Player) sender;
                SuperItem itemtype = SuperItemRegistry.getSuperItemFromId(args[1]);
                if(itemtype == null){
                    Util.sendMessage(p, "This item could not be found!");
                    return false;
                } else {
                    p.getInventory().addItem(itemtype.getItemStack(1));
                    Util.sendMessage(p, "You have been given " + itemtype.getDisplayName());
                    return true;
                }
            default:
                Util.sendMessage( (Player) sender, "SuperItems Help Page.");
                break;
        }
        return false;
    }
}
