package me.zenox.superitems.item;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.persistence.SerializedPersistentType;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;

import java.io.Serializable;

/**
 * A class that represents different variables or fields on an item
 *
 * @param <T> The type of the variable
 */
class Variable<T extends Serializable> {

    private final ComplexItemMeta complexItemMeta;
    private final VariableType type;
    private T value;

    public Variable(ComplexItemMeta complexItemMeta, VariableType variableType, T value) throws IllegalArgumentException {
        this.complexItemMeta = complexItemMeta;
        this.type = variableType;
        this.value = value;
    }

    public VariableType getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public Variable<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public void write(PersistentDataContainer container, LoreBuilder loreBuilder) throws CloneNotSupportedException{
        LoreEntry entry = (LoreEntry) type.getLoreEntry().clone();
        type.getLoreModifier().accept(entry, this);
        container.set(new NamespacedKey(SuperItems.getPlugin(), ComplexItemMeta.VAR_PREFIX + type.getName()), new SerializedPersistentType<T>(), value);
        loreBuilder.entry(entry);
    }

    @Override
    public String toString(){
        return "[Variable: {Type: " + value.getClass().getName() + ", Value: " + value + "}]";
    }

}

//-353 63 630
