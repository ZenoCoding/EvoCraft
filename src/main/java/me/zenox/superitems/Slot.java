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
import java.util.function.Function;

public enum Slot {
    MAIN_HAND((PlayerInventory inv) -> List.of(inv.getItemInMainHand())),
    OFF_HAND((PlayerInventory inv) -> List.of(inv.getItemInOffHand())),
    EITHER_HAND((PlayerInventory inv) -> List.of(inv.getItemInMainHand(), inv.getItemInOffHand())),
    HEAD((PlayerInventory inv) -> List.of(inv.getHelmet())),
    CHEST((PlayerInventory inv) -> List.of(inv.getChestplate())),
    LEGS((PlayerInventory inv) -> List.of(inv.getLeggings())),
    BOOTS((PlayerInventory inv) -> List.of(inv.getBoots())),
    ARMOR((PlayerInventory inv) -> Arrays.asList(inv.getArmorContents())),
    INVENTORY((PlayerInventory inv) -> Arrays.asList(inv.getContents())),
    ;

    //groups that apply to multiple items will return the first item applicable
    private final Function<PlayerInventory, List<ItemStack>> getItem;

    Slot(Function<PlayerInventory, List<ItemStack>> getItem) {
        this.getItem = getItem;
    }

    /**
     * Gets all the unique abilities of a player's equipped gear.
     *
     * @param p The player
     * @return The list of unique abilities that the player possesses
     */
    @Deprecated
    public static List<Ability> uniqueEquipped(Player p) {
        List<ItemStack> items = Arrays.stream(new ItemStack[]{MAIN_HAND.item(p).get(0), OFF_HAND.item(p).get(0), HEAD.item(p).get(0), CHEST.item(p).get(0), LEGS.item(p).get(0), BOOTS.item(p).get(0)})
                .filter(Objects::nonNull).toList();
        List<Ability> abilities = new ArrayList<>();
        items.stream()
                .map(ComplexItemStack::of)
                .filter(Objects::nonNull)
                .forEach((complexItemStack) -> abilities.addAll(complexItemStack.getAbilities()));
        return abilities.stream().distinct().toList();
    }

    public List<ItemStack> item(PlayerInventory inv) {
        return this.getItem.apply(inv);
    }

    public List<ItemStack> item(Player p) {
        return this.getItem.apply(p.getInventory());
    }
}
