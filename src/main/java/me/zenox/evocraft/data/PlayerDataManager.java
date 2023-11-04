package me.zenox.evocraft.data;

import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.gameclass.GameClass;
import me.zenox.evocraft.gameclass.tree.Path;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
    private final File playerDataFile;
    private final FileConfiguration playerDataConfig;

    private final Map<UUID, PlayerData> loadedPlayerData = new ConcurrentHashMap<>();

    public PlayerDataManager() {
        playerDataFile = new File(EvoCraft.getPlugin().getDataFolder(), "playerdata.yml");
        if (!playerDataFile.exists()) {
            try {
                playerDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);

        startAutoSave();
    }

    private void startAutoSave() {
        long delay = 20L*60L*EvoCraft.getPlugin().getConfig().getLong("autosave-minutes");

        Bukkit.getScheduler().runTaskTimer(EvoCraft.getPlugin(), this::saveAllPlayerData, delay, delay);
    }

    public PlayerData getPlayerData(UUID uuid) {
        return loadedPlayerData.computeIfAbsent(uuid, this::loadPlayerData);
    }

    public void saveAllPlayerData() {
        loadedPlayerData.values().forEach(this::savePlayerData);
    }

    // Call this method when a critical change has occurred
    public void savePlayerDataImmediately(PlayerData playerData) {
        savePlayerData(playerData);
        loadedPlayerData.put(playerData.getUuid(), playerData); // Update the in-memory cache
    }

    // Call this method during server shutdown
    public void shutdown() {
        saveAllPlayerData();
    }

    public void savePlayerData(PlayerData playerData) {
        // Serialize PlayerData object to YAML
        // You will need to implement serialization for your complex types
        playerDataConfig.createSection(playerData.getUuid().toString(), serializePlayerData(playerData));
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerData loadPlayerData(@NotNull UUID uuid) {
        // Deserialize from YAML to PlayerData
        // You will need to implement deserialization for your complex types
        return deserializePlayerData(Objects.requireNonNull(playerDataConfig.getConfigurationSection(uuid.toString())));
    }

    private @NotNull @Unmodifiable Map<?, ?> serializePlayerData(@NotNull PlayerData playerData) {
        Map<String, Integer> pathMap = new HashMap<>();
        for (Map.Entry<Path, Integer> entry: playerData.getPathLevelMap().entrySet())
            pathMap.put(entry.getKey().getId(), entry.getValue());
        return Map.of(
                "data_version", 0,
                "class", playerData.getPlayerClass().id(),
                "paths", pathMap
        );
    }

    private PlayerData deserializePlayerData(@NotNull ConfigurationSection section) {
        return switch (((int) section.get("data_version", 0))) {
            case 0 -> new PlayerData(
                        UUID.fromString(section.getName()),
                        GameClass.getFromID(section.getString("class")),
                        deserializePathMap(section.getConfigurationSection("paths"))
                );
            default -> throw new IllegalArgumentException("Invalid data version");
        };
    }

    private @NotNull Map<Path, Integer> deserializePathMap(@NotNull ConfigurationSection paths) {
        Map<Path, Integer> pathMap = new HashMap<>();
        for (String key: paths.getKeys(false)){
            pathMap.put(Path.getFromID(key), paths.getInt(key));
        }
        return pathMap;

    }
}
