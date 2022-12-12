package me.zenox.superitems.recipe;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.item.VanillaItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class ComplexChoice {

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

    public ComplexChoice(@NotNull Material material, Integer amount) {
        this(Map.of(VanillaItem.of(material), amount));
    }

    public ComplexChoice(@NotNull ComplexItem complexItem) {
        this(Map.of(complexItem, 1));
    }

    public ComplexChoice(@NotNull ComplexItemStack itemStack){
        this(Map.of(itemStack.getComplexItem(), itemStack.getItem().getAmount()));
    }
    public ComplexChoice(@NotNull Material material) {
        this(Map.of(VanillaItem.of(material), 1));
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
    public ItemStack getItemStack() {
        return new ComplexItemStack(((Map.Entry<ComplexItem, Integer>) choices.entrySet().toArray()[0]).getKey(), ((Map.Entry<ComplexItem, Integer>) choices.entrySet().toArray()[0]).getValue()).getItem();
    }

    @NotNull
    public int getAmount(){
        return ((Map.Entry<ComplexItem, Integer>) choices.entrySet().toArray()[0]).getValue();
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

    public boolean test(@NotNull ItemStack t) {
        ComplexItem cItem = ComplexItemStack.of(t).getComplexItem();
        for (Map.Entry<ComplexItem, Integer> match : choices.entrySet()) {
            if (cItem.equals(match.getKey()) && (t.getAmount() >= match.getValue() || match.getValue() == -1)) {
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

