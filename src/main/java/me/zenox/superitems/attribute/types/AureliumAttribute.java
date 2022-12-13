package me.zenox.superitems.attribute.types;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.modifier.ModifierType;
import com.archyx.aureliumskills.modifier.StatModifier;
import com.archyx.aureliumskills.stats.Stat;
import me.zenox.superitems.Slot;
import me.zenox.superitems.attribute.Attribute;
import me.zenox.superitems.attribute.AttributeModifier;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class AureliumAttribute extends Attribute {

    private final Stat stat;

    public AureliumAttribute(String id, ChatColor color, Stat stat) {
        this(id, color, stat, (value) -> (value > 0 ? "+" : "") + value);
    }

    public AureliumAttribute(String id, ChatColor color, Stat stat, Function<Double, String> valueFormatter) {
        super(id, color, AttributeSource.AURELIUM, valueFormatter);
        this.stat = stat;
    }

    @Override
    public ItemStack apply(ItemStack item, @NotNull AttributeModifier modifier) {
        double value = modifier.getValue();
        double curValue = Util.getAureliumModifiers(item,
                        List.of(Slot.HEAD, Slot.CHEST, Slot.LEGS, Slot.FEET, Slot.ARMOR)
                                .contains(modifier.getSlot()) ? ModifierType.ARMOR : ModifierType.ITEM)
                .stream()
                .filter(statModifier -> stat.equals(statModifier.getStat()))
                .mapToDouble(StatModifier::getValue)
                .sum();

        if (modifier.getOperation().equals(org.bukkit.attribute.AttributeModifier.Operation.ADD_SCALAR)){
            curValue *= value;
        } else if (modifier.getOperation().equals(org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER)) {
            curValue += value;
        }

        // Returns an armor modifier if the item's type is a valid armor type, otherwise adds an item modifier
        if(List.of(Slot.HEAD, Slot.CHEST, Slot.LEGS, Slot.FEET, Slot.ARMOR).contains(modifier.getSlot())){
            return AureliumAPI.addArmorModifier(item, stat, curValue, false);
        } else {
            return AureliumAPI.addItemModifier(item, stat, curValue, false);
        }
    }

    @Override
    public ItemStack remove(ItemStack item, @NotNull AttributeModifier modifier) {
        return Util.removeAureliumModifier(item, List.of(Slot.HEAD, Slot.CHEST, Slot.LEGS, Slot.FEET, Slot.ARMOR)
                .contains(modifier.getSlot()) ? ModifierType.ARMOR : ModifierType.ITEM, stat);
    }

    public Stat getStat() {
        return stat;
    }
}
