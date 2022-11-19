package me.zenox.superitems.item;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class LoreEntry implements Cloneable {
    private final String id;
    private List<String> lore;
    private final BiConsumer<LoreBuilder, LoreEntry> modifier;

    public LoreEntry(String id, @NotNull List<String> lore, BiConsumer<LoreBuilder, LoreEntry> modifier) {
        this.id = id;
        this.lore = new ArrayList<>(lore);
        this.modifier = modifier;
    }

    public LoreEntry(String id, List<String> lore) {
        this(id, lore, ((loreBuilder, loreEntry) -> {}));
    }

    public String getId() {
        return id;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(@NotNull List<String> lore) {
        this.lore = lore;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void modify(LoreBuilder builder) {
        this.modifier.accept(builder, this);
    }

    @Override
    public String toString() {
        return "LoreEntry{" +
                "id='" + id + '\'' +
                ", lore=" + lore +
                '}';
    }
}
