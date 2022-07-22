package me.zenox.superitems.items;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Executable;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ItemAbility {
    private String name;
    private String id;
    private List<String> lore;
    private List<Action> actions;
    private Executable executable;
    private int cooldown;

    public ItemAbility(String name, String id, List<String> lore, List<Action> actions, Executable executable, int cooldown){
        this.name = name;
        this.lore = lore;
        this.actions = actions;
        this.executable = executable;
        this.cooldown = cooldown;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public Executable getExecutable() {
        return executable;
    }

    public void setExecutable(Executable executable) {
        this.executable = executable;
    }

    public void runExecutable(PlayerInteractEvent e){
        if(actions.contains(e.getAction())) {

            PersistentDataContainer container = e.getPlayer().getPersistentDataContainer();
            NamespacedKey cooldownKey = new NamespacedKey(SuperItems.getPlugin(), getId() + "_cooldown");
            Long cooldown = container.get(cooldownKey, PersistentDataType.LONG);

            if (cooldown != null && Math.ceil((cooldown - System.currentTimeMillis()) / 1000) > 0) {
                Util.sendMessage(e.getPlayer(), "This ability is on cooldown for " + ChatColor.RED + Math.ceil((cooldown - System.currentTimeMillis()) / 1000));
                return;
            }

            this.getExecutable().run(e);


            container.set(cooldownKey, PersistentDataType.LONG, System.currentTimeMillis() + (getCooldown() * 1000));
        }
    }


    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
