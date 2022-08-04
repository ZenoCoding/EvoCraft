package me.zenox.superitems.items.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Speed100 extends ItemAbility {
    private int speed;

    public Speed100(Integer speed, Integer manaCost) {
        super("Speed " + (speed*100), "speed100", AbilityAction.RIGHT_CLICK_ALL, manaCost, 30);

        this.speed = speed;

        this.addLineToLore(ChatColor.GRAY + "Speedy AF.");
        this.addLineToLore(ChatColor.GRAY + "Gives you " + ChatColor.WHITE + "speed" + ChatColor.GRAY + " at an incredible rate.");
        this.addLineToLore(ChatColor.GRAY + "Beginners Magic!");
    }

    @Override
    public void runExecutable(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1, 0);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, speed-1));
    }
}
