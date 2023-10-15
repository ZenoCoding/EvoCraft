package me.zenox.evocraft.abilities;

import me.zenox.evocraft.util.TriConsumer;
import me.zenox.evocraft.util.Util;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FullSetEntityDamagedAbility extends FullSetArmorAbility<EntityDamageByEntityEvent> {

    public FullSetEntityDamagedAbility(AbilitySettings settings) {
        super(settings);
    }

    public FullSetEntityDamagedAbility(AbilitySettings settings, TriConsumer<EntityDamageByEntityEvent, Player, ItemStack> executable) {
        super(settings, executable);
    }

    public FullSetEntityDamagedAbility(String id, int manaCost, double cooldown, TriConsumer<EntityDamageByEntityEvent, Player, ItemStack> exectuable) {
        super(id, manaCost, cooldown, exectuable);
    }

    @Override
    protected boolean checkEventExec(EntityDamageByEntityEvent e) {
        return e.getEntity() instanceof Player && !Util.isInvulnerable(e.getEntity());
    }

    @Override
    Player getPlayerOfEvent(EntityDamageByEntityEvent e) {
        return ((Player) e.getEntity());
    }

    @Override
    List<ItemStack> getItem(Player p, EntityDamageByEntityEvent e) {
        return this.getSlot().item(p);
    }

    // Static ability executables

    public static void diamantineShieldAbility(EntityDamageByEntityEvent entityDamageByEntityEvent, Player player, ItemStack itemStack) {
        // If the player is damaged, 10% to negate the damage and gain 2 absorption hearts
        if (Math.random() < 0.1) {
            entityDamageByEntityEvent.setDamage(0);
            player.setAbsorptionAmount(Math.min(4, player.getAbsorptionAmount() + 4));
            // Play a diamond hitting sound effect, as well as spawn blue redstone particles
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_AXE_SCRAPE, 0.4f, 2f);
            player.getWorld().spawnParticle(org.bukkit.Particle.REDSTONE, player.getLocation(), 10, 0.3, 0.3, 0.3, 0.1, new org.bukkit.Particle.DustOptions(Color.AQUA, 1));
        }
    }
}