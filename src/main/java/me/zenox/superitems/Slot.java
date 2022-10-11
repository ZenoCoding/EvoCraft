package me.zenox.superitems;

import me.zenox.superitems.abilities.Ability;
import me.zenox.superitems.item.ComplexItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;

public enum Slot {
    MAIN_HAND((PlayerInventory inv, Ability ability) -> ComplexItemStack.of(inv.getItemInMainHand()).getAbilities().contains(ability), (PlayerInventory inv) -> inv.getItemInMainHand()),
    OFF_HAND((PlayerInventory inv, Ability ability) -> ComplexItemStack.of(inv.getItemInOffHand()).getAbilities().contains(ability), (PlayerInventory inv) -> inv.getItemInOffHand()),
    EITHER_HAND((PlayerInventory inv, Ability ability) -> MAIN_HAND.predicate.test(inv, ability) || OFF_HAND.predicate.test(inv, ability), (PlayerInventory inv) -> inv.getItemInMainHand()),
    HEAD((PlayerInventory inv, Ability ability) -> ComplexItemStack.of(inv.getHelmet()).getAbilities().contains(ability), (PlayerInventory inv) -> inv.getHelmet()),
    CHEST((PlayerInventory inv, Ability ability) -> ComplexItemStack.of(inv.getChestplate()).getAbilities().contains(ability), (PlayerInventory inv) -> inv.getChestplate()),
    LEGS((PlayerInventory inv, Ability ability) -> ComplexItemStack.of(inv.getLeggings()).getAbilities().contains(ability), (PlayerInventory inv) -> inv.getLeggings()),
    BOOTS((PlayerInventory inv, Ability ability) -> ComplexItemStack.of(inv.getBoots()).getAbilities().contains(ability), (PlayerInventory inv) -> inv.getBoots()),
    ARMOR((PlayerInventory inv, Ability ability) -> {
        for (ItemStack item : inv.getArmorContents())
            if (ComplexItemStack.of(item).getAbilities().contains(ability)) return true;
        return false;
    }, (PlayerInventory inv) -> inv.getHelmet()),
    INVENTORY((PlayerInventory inv, Ability ability) -> {
        for (ItemStack item : inv.getContents())
            if (ComplexItemStack.of(item).getAbilities().contains(ability)) return true;
        return false;
    }, (PlayerInventory inv) -> inv.getItemInMainHand()),
    ;

    private final BiPredicate<PlayerInventory, Ability> predicate;

    //groups that apply to multiple items will return the first item applicable
    private final Function<PlayerInventory, ItemStack> getItem;

    Slot(BiPredicate<PlayerInventory, Ability> predicate, Function<PlayerInventory, ItemStack> getItem) {
        this.predicate = predicate;
        this.getItem = getItem;
    }

    /**
     * Gets all the unique abilities of a player's equipped gear.
     *
     * @param p The player
     * @return The list of unique abilities that the player possesses
     */
    public static List<Ability> uniqueEquipped(Player p) {
        List<ItemStack> items = Arrays.stream(new ItemStack[]{MAIN_HAND.item(p), OFF_HAND.item(p), HEAD.item(p), CHEST.item(p), LEGS.item(p), BOOTS.item(p)})
                .filter(Objects::nonNull).toList();
        List<Ability> abilities = new ArrayList<>();
        items.stream()
                .map(ComplexItemStack::of)
                .filter(Objects::nonNull)
                .forEach((complexItemStack) -> abilities.addAll(complexItemStack.getAbilities()));
        return abilities.stream().distinct().toList();
    }

    public Boolean evaluate(PlayerInventory inv, Ability ability) {
        try {
            return this.predicate.test(inv, ability);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public ItemStack item(PlayerInventory inv) {
        return this.getItem.apply(inv);
    }

    public ItemStack item(Player p) {
        return this.getItem.apply(p.getInventory());
    }
}
