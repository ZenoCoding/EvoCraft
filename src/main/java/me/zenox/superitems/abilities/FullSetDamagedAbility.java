package me.zenox.superitems.abilities;

import me.zenox.superitems.util.TriConsumer;
import me.zenox.superitems.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FullSetDamagedAbility extends FullSetArmorAbility<EntityDamageEvent> {

    private List<EntityDamageEvent.DamageCause> validCauses;

    public FullSetDamagedAbility(AbilitySettings settings) {
        super(settings);
    }

    public FullSetDamagedAbility(AbilitySettings settings, List<EntityDamageEvent.DamageCause> validCauses) {
        super(settings);
        this.validCauses = validCauses;
    }

    public FullSetDamagedAbility(AbilitySettings settings, TriConsumer<EntityDamageEvent, Player, ItemStack> executable) {
        super(settings, executable);
    }

    public FullSetDamagedAbility(AbilitySettings settings, TriConsumer<EntityDamageEvent, Player, ItemStack> executable, List<EntityDamageEvent.DamageCause> validCauses) {
        super(settings, executable);
        this.validCauses = validCauses;
    }

    @Override
    protected boolean checkEventExec(EntityDamageEvent e) {
        return e.getEntity() instanceof Player && !Util.isInvulnerable(e.getEntity()) && this.validCauses.contains(e.getCause());
    }

    @Override
    Player getPlayerOfEvent(EntityDamageEvent e) {
        return ((Player) e.getEntity());
    }

    @Override
    List<ItemStack> getItem(Player p, EntityDamageEvent e) {
        return this.getSlot().item(p);
    }

    public static void golemsHeartAbility(EntityDamageEvent event, Player player, ItemStack itemStack) {
        // Negate 25% of the damage taken (explosion damage)
        event.setDamage(event.getDamage() * 0.75);
    }

    public static void titansHeartAbility(EntityDamageEvent event, Player player, ItemStack itemStack) {
        // Negate 50% of the damage taken (explosion damage)
        event.setDamage(event.getDamage() * 0.5);
    }

    // Static ability executables


}
