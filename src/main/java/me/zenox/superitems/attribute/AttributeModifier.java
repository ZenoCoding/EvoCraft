package me.zenox.superitems.attribute;

import com.archyx.aureliumskills.modifier.StatModifier;
import me.zenox.superitems.Slot;
import me.zenox.superitems.attribute.types.AureliumAttribute;
import me.zenox.superitems.attribute.types.MinecraftAttribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

public class AttributeModifier implements Serializable {
    private final String name;
    private final UUID uuid;
    private final Attribute attribute;
    private double value;
    private final Operation operation;
    private final Slot slot;

    public AttributeModifier(String name, UUID uuid, Attribute attribute, double value, Operation operation, Slot slot) {
        this.name = name;
        this.uuid = uuid;
        this.attribute = attribute;
        this.value = value;
        this.operation = operation;
        this.slot = slot;

        if(attribute instanceof MinecraftAttribute && slot.getMcSlot() == null) throw new IllegalArgumentException("Slot " + slot.name() + " is not compatible with Vanilla Attribute Modifiers");
    }

    public AttributeModifier(String name, Attribute attribute, double value, Operation operation, Slot slot) {
        this(name, UUID.randomUUID(), attribute, value, operation, slot);
    }

    public static AttributeModifier of(org.bukkit.attribute.Attribute attribute, org.bukkit.attribute.AttributeModifier modifier){
        try {
            return new AttributeModifier(modifier.getName(), modifier.getUniqueId(),
                    ((Attribute) Attribute.attributeRegistry.stream()
                            .filter(attribute1 -> attribute1 instanceof MinecraftAttribute && ((MinecraftAttribute) attribute1).getMcAttribute() == attribute)
                            .toArray()[0]), modifier.getAmount(), modifier.getOperation(), ((Slot) Arrays.stream(Slot.values())
                    .filter(slot1 -> slot1.getMcSlot() == modifier.getSlot())
                    .toArray()[0]));
        } catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("AttributeModifier " + modifier + " does not exist as a minecraft attribute");
        }
    }

    public static AttributeModifier of(StatModifier modifier){
        try {
            return new AttributeModifier(modifier.getName(), UUID.randomUUID(),
                    ((Attribute) Attribute.attributeRegistry.stream()
                            .filter(attribute1 -> attribute1 instanceof AureliumAttribute && ((AureliumAttribute) attribute1).getStat() == modifier.getStat())
                            .toArray()[0]), modifier.getValue(), Operation.ADD_NUMBER, modifier.getName().contains("item") && !modifier.getName().contains("armor") ? Slot.MAIN_HAND : Slot.ARMOR);
        } catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("AttributeModifier " + modifier + " was");
        }
    }

    /**
     * ::equals to check if a modifier is roughly equal to another, regardless of value
     * @param o Another object
     * @return whether the two objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttributeModifier that)) return false;

        if (!name.equals(that.name)) return false;
        return attribute.equals(that.attribute);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + attribute.hashCode();
        result = 31 * result + operation.hashCode();
        result = 31 * result + slot.hashCode();
        return result;
    }

    public void apply(ItemStack item){
        attribute.apply(item, this);
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Operation getOperation() {
        return operation;
    }

    public Slot getSlot() {
        return slot;
    }

    public double getValue() {
        return value;
    }

    public AttributeModifier setValue(double value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "AttributeModifier{"
                + "uuid=" + this.uuid.toString()
                + ", name=" + this.name
                + ", operation=" + this.operation.name()
                + ", value=" + this.value
                + ", slot=" + (this.slot != null ? this.slot.name() : "")
                + "}";
    }
}
