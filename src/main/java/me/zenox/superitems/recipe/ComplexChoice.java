package me.zenox.superitems.recipe;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.item.ItemRegistry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class ComplexChoice implements RecipeChoice {

    private Map<ComplexItem, Integer> choices;

    /**
     * Create a new instance of ComplexChoice, which represents a RecipeChoice that matches the complexitem and amount
     *
     * @param complexItem the item you want to match to match
     * @param amount      the amount you want to match, set to -1 to ignore amount
     */
    public ComplexChoice(@NotNull ComplexItem complexItem, Integer amount) {
        this(Map.of(complexItem, amount));
    }

    public ComplexChoice(@NotNull Map<ComplexItem, Integer> choices) {
        Preconditions.checkArgument(choices != null, "choices");
        Preconditions.checkArgument(!choices.isEmpty(), "Must have at least one choice");
        for (Map.Entry<ComplexItem, Integer> choice : choices.entrySet()) {
            Preconditions.checkArgument(choice != null, "Cannot have null choice");
        }

        this.choices = choices;
    }

    @NotNull
    @Override
    public ItemStack getItemStack() {
        return ItemRegistry.byId(((Map.Entry<String, Integer>) choices.entrySet().toArray()[0]).getKey()).getItemStack(((Map.Entry<String, Integer>) choices.entrySet().toArray()[0]).getValue());
    }

    public Map<ComplexItem, Integer> getChoices() {
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
        for (Map.Entry<ComplexItem, Integer> match : choices.entrySet()) {
            if (ComplexItemStack.of(t).getComplexItem().equals(match.getKey()) && (t.getAmount() == match.getValue() || match.getValue() == -1)) {
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
        return Objects.equals(this.choices, other.choices);
    }

    @Override
    public String toString() {
        return "ComplexChoice{" + "choices=" + choices + '}';
    }
}

