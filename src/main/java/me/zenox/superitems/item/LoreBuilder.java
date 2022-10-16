package me.zenox.superitems.item;

import java.util.ArrayList;
import java.util.List;

public class LoreBuilder {
    private final List<LoreEntry> loreEntries = new ArrayList<>();

    public LoreBuilder() {

    }

    public void entry(LoreEntry entry) {
        this.loreEntries.add(entry);
    }

    public List<LoreEntry> getLoreEntries() {
        return loreEntries;
    }

    public List<LoreEntry> getLoreEntryById(String id) {
        return loreEntries.stream().filter(loreEntry -> loreEntry.getId().equalsIgnoreCase(id)).toList();
    }

    public List<String> build() {
        List<String> result = new ArrayList<>();
        loreEntries.forEach(loreEntry -> loreEntry.modify(this));
        loreEntries.forEach(loreEntry -> result.addAll(loreEntry.getLore()));
        return result;
    }

}
