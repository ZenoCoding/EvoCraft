package me.zenox.evocraft.attribute.types;

import dev.aurelium.auraskills.api.AuraSkillsBukkit;
import dev.aurelium.auraskills.api.item.ItemManager;
import dev.aurelium.auraskills.api.item.ModifierType;
import dev.aurelium.auraskills.api.stat.Stat;
import dev.aurelium.auraskills.api.stat.StatModifier;
import me.zenox.evocraft.Slot;
import me.zenox.evocraft.attribute.Attribute;
import me.zenox.evocraft.attribute.AttributeModifier;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class AureliumAttribute extends Attribute {

    private static final ItemManager itemManager = AuraSkillsBukkit.get().getItemManager();

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
        double curValue = itemManager.getStatModifiers(item,
                        List.of(Slot.HEAD, Slot.CHEST, Slot.LEGS, Slot.FEET, Slot.ARMOR)
                                .contains(modifier.getSlot()) ? ModifierType.ARMOR : ModifierType.ITEM)
                .stream()
                .filter(statModifier -> stat.equals(statModifier.stat()))
                .mapToDouble(StatModifier::value)
                .sum();

        if (modifier.getOperation().equals(org.bukkit.attribute.AttributeModifier.Operation.ADD_SCALAR)){
            curValue *= value;
        } else if (modifier.getOperation().equals(org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER)) {
            curValue += value;
        }

        // Returns an armor modifier if the item's type is a valid armor type, otherwise adds an item modifier
        if(List.of(Slot.HEAD, Slot.CHEST, Slot.LEGS, Slot.FEET, Slot.ARMOR).contains(modifier.getSlot())){
            return itemManager.addStatModifier(item, ModifierType.ARMOR, stat, curValue, false);
        } else {
            return itemManager.addStatModifier(item, ModifierType.ITEM, stat, curValue, false);
        }
    }

    @Override
    public ItemStack remove(ItemStack item, @NotNull AttributeModifier modifier) {
        return itemManager.removeStatModifier(item, List.of(Slot.HEAD, Slot.CHEST, Slot.LEGS, Slot.FEET, Slot.ARMOR)
                .contains(modifier.getSlot()) ? ModifierType.ARMOR : ModifierType.ITEM, stat);
    }

    public Stat getStat() {
        return stat;
    }
}
