package me.zenox.superitems.items.abilities;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class FlyingSwordAbility extends ItemAbility {



    public FlyingSwordAbility() {
        super("Flying Sword", "flying_sword_ability", AbilityAction.SHIFT_LEFT_CLICK, 150, 60);

        this.addLineToLore(ChatColor.GRAY + "can you do this part the lore is in the next qoutes" + ChatColor.BLACK + "Swings your scythe around to deal massive AOE near you");
        this.addLineToLore(ChatColor.GRAY + "Deals massive" + ChatColor.RED + " damage" + ChatColor.GRAY + " on impact.");


    }
    double count = getCooldown();
    @Override
    protected void runExecutable(PlayerEvent event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Random r = new Random();
        Player p = e.getPlayer();
        World w = p.getWorld();

        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2000, 2));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 2, true, false, false));

            if (count < 30) {
                p.setAllowFlight(true);
            } else {
                p.setAllowFlight(false);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 10, 1) );
            }


    }
}
