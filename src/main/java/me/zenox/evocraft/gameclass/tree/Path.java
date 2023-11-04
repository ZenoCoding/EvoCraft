package me.zenox.evocraft.gameclass.tree;

import me.zenox.evocraft.data.PlayerData;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's path on the AbilityTree
 */
public abstract class Path {

    private String id;

    public static final List<Path> registeredPaths = new ArrayList<>();

    // Constructor
    public Path(String id) {
        this.id = id;
        registeredPaths.add(this);
    }

    public static Path getFromID(String key) {
        return registeredPaths.stream()
                .filter(path -> path.getId().equals(key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No path with ID " + key + " found"));
    }

    // Getter for ID
    public String getId() {
        return id;
    }

    // Method to progress the ability
    public void progress(PlayerData playerData) {
        int currentLevel = playerData.getPathLevel(this);
        playerData.setPathLevel(this, currentLevel + 1);

        // Apply the effects to the PlayerData
        apply(playerData);
    }

    // Abstract method to apply stats, metadata, etc. to player
    public abstract void apply(PlayerData playerData);
}