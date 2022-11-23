package me.zenox.superitems;

import me.zenox.superitems.abilities.Ability;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.ComplexItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public enum Slot {
    MAIN_HAND((PlayerInventory inv) -> Arrays.stream(new ItemStack[]{inv.getItemInMainHand()}).toList(), EquipmentSlot.HAND),
    OFF_HAND((PlayerInventory inv) -> Arrays.stream(new ItemStack[]{inv.getItemInOffHand()}).toList(), EquipmentSlot.OFF_HAND),
    EITHER_HAND((PlayerInventory inv) -> Arrays.stream(new ItemStack[]{inv.getItemInMainHand(), inv.getItemInOffHand()}).toList(), EquipmentSlot.HAND),
    HEAD((PlayerInventory inv) -> Arrays.stream(new ItemStack[]{inv.getHelmet()}).toList(), EquipmentSlot.HEAD),
    CHEST((PlayerInventory inv) -> Arrays.stream(new ItemStack[]{inv.getChestplate()}).toList(), EquipmentSlot.CHEST),
    LEGS((PlayerInventory inv) -> Arrays.stream(new ItemStack[]{inv.getLeggings()}).toList(), EquipmentSlot.LEGS),
    FEET((PlayerInventory inv) -> Arrays.stream(new ItemStack[]{inv.getBoots()}).toList(), EquipmentSlot.FEET),
    ARMOR((PlayerInventory inv) -> Arrays.stream(inv.getArmorContents()).toList(), null),
    INVENTORY((PlayerInventory inv) -> Arrays.stream(inv.getContents()).toList(), null),
    ;

    //groups that apply to multiple items will return the first item applicable
    private final Function<PlayerInventory, List<ItemStack>> getItem;
    private final EquipmentSlot mcSlot;

    Slot(Function<PlayerInventory, List<ItemStack>> getItem, EquipmentSlot mcSlot) {
        this.getItem = getItem;
        this.mcSlot = mcSlot;
    }

    /**
     * Gets all the unique abilities of a player's equipped gear.
     *
     * @param p The player
     * @return The list of unique abilities that the player possesses
     */
    @Deprecated
    public static List<Ability> uniqueEquipped(Player p) {
        List<ItemStack> items = Arrays.stream(new ItemStack[]{MAIN_HAND.item(p).get(0), OFF_HAND.item(p).get(0), HEAD.item(p).get(0), CHEST.item(p).get(0), LEGS.item(p).get(0), FEET.item(p).get(0)})
                .filter(Objects::nonNull).toList();
        List<Ability> abilities = new ArrayList<>();
        items.stream()
                .map(ComplexItemStack::of)
                .forEach((complexItemStack) -> abilities.addAll(complexItemStack.getAbilities()));
        return abilities.stream().distinct().toList();
    }

    public List<ItemStack> item(PlayerInventory inv) {
        return this.getItem.apply(inv);
    }

    public List<ItemStack> item(Player p) {
        return this.getItem.apply(p.getInventory());
    }

    public EquipmentSlot getMcSlot() {
        return mcSlot;
    }

    public static Slot fromType(ComplexItem.Type type){
        if(!type.isWearable()) return Slot.MAIN_HAND;
        switch (type){
            case HELMET -> {
                return Slot.HEAD;
            }
            case CHESTPLATE -> {
                return Slot.CHEST;
            }
            case LEGGINGS -> {
                return Slot.LEGS;
            }
            case BOOTS -> {
                return Slot.FEET;
            }
            default -> {
                return Slot.MAIN_HAND;
            }
        }
    }
}
