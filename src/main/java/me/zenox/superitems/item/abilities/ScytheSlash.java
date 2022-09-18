package me.zenox.superitems.item.abilities;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class ScytheSlash extends ItemAbility {



    public ScytheSlash() {
        super("Scythe Slash", "scythe_slash", AbilityAction.SHIFT_LEFT_CLICK, 0, 1.5);

        this.addLineToLore(ChatColor.GRAY + "can you do this part the lore is in the next qoutes" + ChatColor.BLACK + "Swings your scythe around to deal massive AOE near you");
        this.addLineToLore(ChatColor.GRAY + "Deals massive" + ChatColor.RED + " damage" + ChatColor.GRAY + " on impact.");


    }

    @Override
    protected void runExecutable(PlayerEvent event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Random r = new Random();
        Player p = e.getPlayer();
        World w = p.getWorld();
        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2000, 2));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 2, true, false, false));



        //ttyl
    }
}
