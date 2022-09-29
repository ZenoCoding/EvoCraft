package me.zenox.superitems.item.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Tarhelm extends ItemAbility {
    public Tarhelm() {
        super("tarhelm", AbilityAction.SHIFT_RIGHT_CLICK, 50, 120);

        this.addLineToLore(ChatColor.GRAY + "Tarhelm.");
        this.addLineToLore(ChatColor.GRAY + "Gives you " + ChatColor.RED + "strength" + ChatColor.GRAY + " but drastically lowers your walk speed.");
    }

    @Override
    public void runExecutable(PlayerEvent event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Player p = e.getPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_RAVAGER_ATTACK, 1, 0);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 3));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 400, 0));
    }
}
