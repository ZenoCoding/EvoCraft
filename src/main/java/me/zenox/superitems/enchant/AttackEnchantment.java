package me.zenox.superitems.enchant;

import com.archyx.aureliumskills.modifier.StatModifier;
import me.zenox.superitems.Slot;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.util.QuadConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AttackEnchantment extends ComplexEnchantment{
    public AttackEnchantment(String id, int maxLevel, int rarity, List<ComplexItem.Type> types, Slot slot, List<StatModifier> stats, QuadConsumer<Event, Integer, ItemStack, Player> executable) {
        super(id, maxLevel, rarity, types, slot, stats, executable, EntityDamageByEntityEvent.class);
    }

    @Override
    boolean checkEvent(Event e) {
        return ((EntityDamageByEntityEvent) e).getDamager() instanceof Player;
    }

    @Override
    Player getPlayerOfEvent(Event e) {
        return ((Player) ((EntityDamageByEntityEvent) e).getDamager());
    }
}
