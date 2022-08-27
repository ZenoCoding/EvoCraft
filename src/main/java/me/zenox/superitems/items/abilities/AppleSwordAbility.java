package me.zenox.superitems.items.abilities;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.items.superitems.AppleSword;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AppleSwordAbility extends ItemAbility {
    public AppleSwordAbility() {
        super("Hunting the poor", "apple_sword_ability", AbilityAction.RIGHT_CLICK_ALL, 0, 300);

        this.addLineToLore(ChatColor.GRAY + "Bane of the stupid.");

    }

    @Override
    public void runExecutable(PlayerEvent event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Player p = e.getPlayer();
        double x = 3*Math.sqrt(p.getExpToLevel()) + 50;
        double x1 = x - 10;


        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 3));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 500, 2));
        this.getMeta.setAttributeModifiers(Stats.STRENGTH, x, Stats.HEALTH, x1 , Stats.REGENERATION, 30d, Stats.TOUGHNESS, 20d );



    }
}
