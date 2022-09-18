package me.zenox.superitems.item;

import me.zenox.superitems.util.Util;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

class LoreEntry implements Cloneable{
    private final String id;
    private List<String> lore;
    private BiConsumer<LoreBuilder, LoreEntry> modifier;

    LoreEntry(String id, @NotNull List<String> lore, BiConsumer<LoreBuilder, LoreEntry> modifier) {
        this.id = id;
        this.lore = new ArrayList<>(lore);
        this.modifier = modifier;
        Util.logToConsole("LoreEntry: " + this);
    }
    LoreEntry(String id, List<String> lore) {
        this(id, lore, ((loreBuilder, loreEntry) -> {}));
    }

    public String getId(){
        return id;
    }

    public List<String> getLore() {
        return lore;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public LoreEntry setLore(@NotNull List<String> lore) {
        this.lore = lore;
        return this;
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
