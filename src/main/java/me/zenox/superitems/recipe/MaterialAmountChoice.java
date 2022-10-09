package me.zenox.superitems.recipe;


import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import me.zenox.superitems.util.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a choice of multiple matching Materials, that also matches an amount.
 */
public class MaterialAmountChoice implements RecipeChoice {

    private Map<Material, Integer> choices;

    public MaterialAmountChoice(@NotNull Material choice, @NotNull Integer amount) {
        this(Map.of(choice, amount));
    }

    public MaterialAmountChoice(@NotNull Map<Material, Integer> choices) {
        Preconditions.checkArgument(choices != null, "choices");
        Preconditions.checkArgument(!choices.isEmpty(), "Must have at least one choice");
        for (Map.Entry<Material, Integer> choice : choices.entrySet()) {
            Preconditions.checkArgument(choice != null, "Cannot have null choice");
        }

        this.choices = new HashMap<>(choices);
    }

    @Override
    public boolean test(@NotNull ItemStack t) {
        for (Map.Entry<Material, Integer> match : choices.entrySet()) {
            Util.logToConsole("Material Value: " + t.getType() + " | " + "Recipe Material Value: " + match.getKey());
            Util.logToConsole("Amount Value: " + t.getAmount() + " | " + "Recipe Amount Value: " + match.getValue());
            if (t.getType() == match.getKey() && t.getAmount() == match.getValue()) {
                return true;
            }
        }

        return false;
    }

    @NotNull
    @Override
    public ItemStack getItemStack() {
        ItemStack stack = new ItemStack(((Map.Entry<Material, Integer>) choices.entrySet().toArray()[0]).getKey());
        stack.setAmount(((Map.Entry<Material, Integer>) choices.entrySet().toArray()[0]).getValue());

        // For compat
        if (choices.size() > 1) {
            stack.setDurability(Short.MAX_VALUE);
        }

        return stack;
    }

    @NotNull
    public Map<Material, Integer> getChoices() {
        return ImmutableMap.copyOf(choices);
    }

    @NotNull
    @Override
    public MaterialAmountChoice clone() {
        try {
            MaterialAmountChoice clone = (MaterialAmountChoice) super.clone();
            clone.choices = new HashMap(choices);
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }

    @Override
    public int hashCode() {
        int hash = 4;
        hash = 37 * hash + Objects.hashCode(this.choices);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MaterialAmountChoice other = (MaterialAmountChoice) obj;
        return Objects.equals(this.choices, other.choices);
    }

    @Override
    public String toString() {
        return "MaterialChoice{" + "choices=" + choices + '}';
    }
}
