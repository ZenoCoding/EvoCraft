package me.zenox.evocraft.data;

import me.zenox.evocraft.gameclass.GameClass;
import me.zenox.evocraft.gameclass.tree.Path;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;

    private GameClass playerClass;
    private final Map<Path, Integer> pathLevelMap;

    public PlayerData(UUID uuid, GameClass playerClass, Map<Path, Integer> pathLevelMap) {
        this.uuid = uuid;
        this.playerClass = playerClass;
        this.pathLevelMap = pathLevelMap;
    }

    public PlayerData(UUID uuid, GameClass playerClass) {
        this(uuid, playerClass, new HashMap<>());
        playerClass.tree().paths().forEach(path -> pathLevelMap.put(path, 0));
    }

    public UUID getUuid() {
        return uuid;
    }

    // Getter and Setter for playerClass
    public GameClass getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(GameClass playerClass) {
        this.playerClass = playerClass;
    }

    public Map<Path, Integer> getPathLevelMap() {
        return pathLevelMap;
    }

    // Method to get ability level
    public int getPathLevel(Path path) {
        return pathLevelMap.getOrDefault(path, -1);
    }

    // Method to set ability level
    public void setPathLevel(Path path, int level) {
        pathLevelMap.put(path, level);
    }

    // Progress the level of a specific ability
    public void progressAbility(Path path) {
        int currentLevel = getPathLevel(path);
        setPathLevel(path, currentLevel + 1);
    }

    // Display all abilities and their levels
    public void displayAbilities() {
        for (Path path : pathLevelMap.keySet()) {
            System.out.println(path.getId() + ": Level " + pathLevelMap.get(path));
        }
    }
}