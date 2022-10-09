package me.zenox.superitems.item;

import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class VariableType<T extends Serializable> {
    private static final List<VariableType> variableList = new ArrayList<>();

    private final String name;
    private final LoreEntry loreEntry;
    private final Priority priority;
    private final BiConsumer<LoreEntry, Variable> loreModifier;

    public VariableType(String name, LoreEntry loreEntry, Priority priority, BiConsumer<LoreEntry, Variable> loreModifier) throws IllegalArgumentException {
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
        try {
            return variableList.stream().filter(variableType -> {
                return variableType.getName().equalsIgnoreCase(name);
            }).toList().get(0);
        } catch (IndexOutOfBoundsException e) {
            Util.logToConsole(ChatColor.RED + "[ERROR] Variable with name " + name + " doesn't seem to be registered!");
            return null;
        }
    }

    public Variable<T> createVariable(T value, ComplexItemMeta meta) {
        return new Variable<T>(meta, this, value);
    }

    public String getName() {
        return name;
    }

    public Priority getPriority() {
        return priority;
    }

    public LoreEntry getLoreEntry() {
        return this.loreEntry;
    }

    public BiConsumer<LoreEntry, Variable> getLoreModifier() {
        return loreModifier;
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
