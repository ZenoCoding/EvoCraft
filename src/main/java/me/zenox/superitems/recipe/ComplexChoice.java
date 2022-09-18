package me.zenox.superitems.recipe;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.item.ItemRegistry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ComplexChoice implements RecipeChoice{

    private Map<String, Integer> choices;

    public ComplexChoice(@NotNull String id, Integer amount) {
        this(Map.of(id, amount));
    }

    public ComplexChoice(@NotNull Map<String, Integer> choices) {
        Preconditions.checkArgument(choices != null, "choices");
        Preconditions.checkArgument(!choices.isEmpty(), "Must have at least one choice");
        for (Map.Entry<String, Integer> choice : choices.entrySet()) {
            Preconditions.checkArgument(choice != null, "Cannot have null choice");
        }

        this.choices = choices;
    }

    @NotNull
    @Override
    public ItemStack getItemStack() {
        return ItemRegistry.getBasicItemFromId(((Map.Entry<String, Integer>) choices.entrySet().toArray()[0]).getKey()).getItemStack(((Map.Entry<String, Integer>) choices.entrySet().toArray()[0]).getValue());
    }

    public Map<String, Integer> getChoices() {
        return ImmutableMap.copyOf(choices);
    }

    @Override
    public ComplexChoice clone() {
        try {
            ComplexChoice clone = (ComplexChoice) super.clone();
            clone.choices = choices;
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }

    @Override
    public boolean test(@NotNull ItemStack t) {
        for (Map.Entry<String, Integer> match : choices.entrySet()) {
            if (ComplexItemStack.of(t).getId().equalsIgnoreCase(match.getKey()) && t.getAmount() == match.getValue()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 11;
        hash = 41 * hash + Objects.hashCode(this.choices);
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
        final ComplexChoice other = (ComplexChoice) obj;
        if (!Objects.equals(this.choices, other.choices)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ComplexChoice{" + "choices=" + choices + '}';
    }
}

