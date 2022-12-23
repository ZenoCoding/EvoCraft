package me.zenox.superitems.item;

import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public record VariableType<T extends Serializable>(String name, LoreEntry loreEntry,
                                                   me.zenox.superitems.item.VariableType.Priority priority,
                                                   BiConsumer<LoreEntry, Variable> loreModifier) {
    private static final List<VariableType> variableList = new ArrayList<>();

    /**
     * @throws IllegalArgumentException
     */
    public VariableType(String name, LoreEntry loreEntry, Priority priority, BiConsumer<LoreEntry, Variable> loreModifier) {
        if (variableList.stream().anyMatch(variable -> variable.name == name))
            throw new IllegalArgumentException("ComplexItemMeta VariableType Key " + name + " is a duplicate");
        this.name = name;
        this.priority = priority;
        this.loreEntry = loreEntry;
        this.loreModifier = loreModifier;
        variableList.add(this);
    }

    @Nullable
    public static VariableType getVariableByPrefix(String name) {
        VariableType result = variableList.stream().filter(variableType ->
            variableType.name().equalsIgnoreCase(name)).findFirst().orElse(null);

        if(result == null) Util.logToConsole(ChatColor.RED + "[ERROR] Variable with name " + name + " doesn't seem to be registered!");
        return result;
    }

    public Variable<T> createVariable(T value, ComplexItemMeta meta) {
        return new Variable<T>(meta, this, value);
    }

    @Override
    public String toString() {
        return "VariableType{" +
                "name='" + name + '\'' +
                ", loreEntry=" + loreEntry +
                ", priority=" + priority +
                '}';
    }

    public enum Priority {
        ABOVE_STATS(0), ABOVE_LORE(1), ABOVE_ENCHANTS(2), ABOVE_ABILITIES(3), BELOW_ABILITIES(4), BELOW(Integer.MAX_VALUE);

        private final int weight;

        Priority(int weight) {
            this.weight = weight;
        }

        public int getWeight() {
            return this.weight;
        }

    }
}
