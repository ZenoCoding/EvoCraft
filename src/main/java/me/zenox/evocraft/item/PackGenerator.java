package me.zenox.evocraft.item;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ticxo.modelengine.api.ModelEngineAPI;
import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.util.ItemUtils;
import me.zenox.evocraft.util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 * Resource pack generator that is used to generate resource packs for the plugin
 * <p>
 * Users should place item files in `assets/textures/items` in the plugin's data folder
 * <p>
 * Embedded in the JAR is a default set of PNG files that will be copied to the data folder if it doesn't exist, as well as a template
 */
public class PackGenerator implements Listener {

    private static final Pattern PNG_PATTERN = Pattern.compile("(.+?)(?:_\\d+)?\\.png");
    public static final String TEXTURE_LOCATION = "assets/evocraft/textures/item/";
    public static final String MODEL_LOCATION = "assets/evocraft/models/item/";
    public static final String VANILLA_LOCATION = "assets/minecraft/models/item/";
    private final String targetPath;
    private String url;
    private byte[] hash;
    private File resourcePack;

    public PackGenerator(String targetPath) {
        this.targetPath = targetPath;
        this.resourcePack = createPack();
        this.url = EvoCraft.getPlugin().getConfig().getString("resource-pack-url");
        this.hash = generateHash(resourcePack);

        Bukkit.getPluginManager().registerEvents(this, EvoCraft.getPlugin());

        if ("FILL IN".equals(this.url)) {
            new BukkitRunnable(){

                @Override
                public void run() {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.isOp() && "FILL IN".equals(url)) {
                            Util.sendMessage(player, "&cResource pack URL not set in config! Set with /evocraft pack <url>");
                        }
                    }
                }
            }.runTaskTimer(EvoCraft.getPlugin(), 0, 4000);
            EvoCraft.getPlugin().getLogger().warning("Resource pack URL not set in config! Set with /evocraft pack <url>");
        }
    }

    public File createPack() {
        // Location of the png files for items, this is where devs/users will add them
        File pngDirectory = new File(EvoCraft.getPlugin().getDataFolder(), "assets/textures/items");
        HashMap<ComplexItem, List<File>> map = readPngs(pngDirectory);

        // Next, we want to compile a new resource pack folder
        // We can use a bare-bones template with all the required files and folders and simply copy it
        // For a first prototype, we'll only modify item files and textures, and deal with sounds + MEG later on
        resourcePack = new File(EvoCraft.getPlugin().getDataFolder(), targetPath);
        // delete the old resource pack folder
        if (resourcePack.exists()) {
            try {
                Util.logToConsole("Regenerating resource pack.");
                // delete the directory and all files inside it
                Files.walk(Paths.get(resourcePack.getAbsolutePath()))
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        copyTemplatePack(resourcePack);

        // Now we need to copy the PNG files into the resource pack
        copyPNGToPack(map);

        // Clone modelEngine files
        File modelEngine = new File(ModelEngineAPI.api.getDataFolder(), "resource pack");
        copyModelEngineFiles(modelEngine, resourcePack);

        // Afterward, we need to compile the ComplexItem JSON files and add them to the resource pack
        compileComplexItemJSON(map);

        // Then, we need to compile the vanilla item JSON files (add overrides) and add them to the resource pack
        compileVanillaItemJSON(map);

        // Finally, we need to zip the resource pack folder and save it as a .zip file
        resourcePack = zipResourcePack(resourcePack);

        return resourcePack;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static void deepCopyFolder(File from, File to, StandardCopyOption option){
        to.mkdirs();
        for (File sub: Objects.requireNonNull(from.listFiles())) {
            if (sub.isDirectory()) {
                deepCopyFolder(sub, new File(to, sub.getName()), option);
            } else {
                try {
                    Files.copy(sub.toPath(), new File(to, sub.getName()).toPath(), option);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Method to read PNG files from a directory and associate them with ComplexItems by ID
     * @param directory the directory to read PNG files from
     * @return a HashMap of ComplexItems and their associated PNG files
     */
    private HashMap<ComplexItem, List<File>> readPngs(File directory) {
        HashMap<ComplexItem, List<File>> map = new HashMap<>();

        if (!directory.exists()) {
            directory.mkdirs();
            copyDefaultPNGs(directory);
        }

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                map.putAll(readPngs(file));
            } else if (isPng(file)) {
                String itemName = extractItemName(file.getName());
                ComplexItem item = ItemRegistry.byId(itemName);
                if (item != null) {
                    map.computeIfAbsent(item, k -> new ArrayList<>()).add(file);
                } else {
                    Util.logToConsole("ComplexItem not found for " + itemName);
                }
            } else {
                Util.logToConsole("File " + file.getName() + " is not a PNG file");
            }
        }
        return map;
    }

    private boolean isPng(File file) {
        return file.getName().toLowerCase().endsWith(".png");
    }

    private String extractItemName(String fileName) {
        Matcher matcher = PNG_PATTERN.matcher(fileName);
        if (matcher.matches()) {
            return matcher.group(1).toLowerCase();
        }
        return null;
    }

    /**
     * Copy the default PNG files from the JAR to the specified directory
     *
     * Notice that this copies from the embedded JAR, not the plugin's data folder,
     * and that this is purely for the purpose of providing a default set of PNG files (think of it for "other" users)
     * @param directory the directory to copy PNG files to
     */
    private void copyDefaultPNGs(File directory) {
        // Scan through JAR entries to find PNGs in the specified directory
        try {
            CodeSource src = EvoCraft.class.getProtectionDomain().getCodeSource();
            if (src != null) {
                int count = 0;
                URL jar = src.getLocation();
                try (ZipInputStream zip = new ZipInputStream(jar.openStream())) {
                    ZipEntry ze = null;
                    while ((ze = zip.getNextEntry()) != null) {
                        String entryName = ze.getName();
                        if (entryName.startsWith("assets/textures/items") && entryName.endsWith(".png")) {
                            // Found a PNG, now copy it
                            count++;
                            File outFile = new File(directory, new File(entryName).getName());
                            if (!outFile.exists()) {
                                try (InputStream in = EvoCraft.getPlugin().getResource(entryName)) {
                                    Files.copy(in, outFile.toPath());
                                }
                            }
                        }
                    }
                }
                Util.logToConsole("Copied " + count + " PNG files from JAR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Copies an embedded template resource pack folder to the specified directory
     * Look at the copyDefaultPNGs method for reference
     * @param directory the directory to copy to
     * @return the resulting resource pack folder
     */
    private File copyTemplatePack(File directory) {
        String resourcePath = "assets/pack_template";

        try {
            URL dirURL = EvoCraft.getPlugin().getClass().getClassLoader().getResource(resourcePath);
            if (dirURL != null && dirURL.getProtocol().equals("jar")) {
                JarURLConnection jarConnection = (JarURLConnection) dirURL.openConnection();
                JarFile jar = jarConnection.getJarFile();
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith(resourcePath)) {
                        String entryPath = name.substring(resourcePath.length() + 1); // +1 to remove the leading slash
                        File outputFile = new File(directory, entryPath);
                        if (!entry.isDirectory()) {
                            try (InputStream in = EvoCraft.getPlugin().getClass().getClassLoader().getResourceAsStream(name)) {
                                Files.copy(in, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            }
                        } else {
                            outputFile.mkdirs();
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy template pack", e);
        }

        return directory; // Return the directory where the files have been copied
    }


    /**
     * Copies PNG files to the resource pack folder
     *
     * The path of the files in the resource pack folder should be `assets/evocraft/textures/items`
     * @param map a HashMap of ComplexItems and their associated PNG files
     */
    private void copyPNGToPack(HashMap<ComplexItem, List<File>> map) {
        for (Map.Entry<ComplexItem, List<File>> entry : map.entrySet()) {
            for (File file : entry.getValue()) {
                try {
                    Files.copy(file.toPath(), new File(resourcePack, TEXTURE_LOCATION + file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Copies the ModelEngine files to the resource pack folder
     *
     * Copies ALL contents of:
     * <ul>
     * <li>assets/modelengine</li>
     *  <li>assets/modelengine/models/... (there are many folders for all the different entities)</li>
     *  <li>assets/modelengine/textures/entity (there are many files for all the different entities)</li>
     * <li>assets/minecraft</li>
     *  <li>assets/minecraft/models/item/leather_horse_armor.json</li>
     * </ul>
     * <p>
     * Recursively does a deep copy on the needed ModelEngine folders, copying files into preexisting folders if necessary.
     * <p>
     * Note: It may be beneficial to write a recursive algorithm that copies all files from one directory to another,
     * maintaining directory structure and previous files, as it will be more versatile and you will not need to fix your code if ModelEngine updates
     * @param modelEngine the ModelEngine resource pack folder
     * @param resourcePack the resource pack folder
     */
    private void copyModelEngineFiles(File modelEngine, File resourcePack) {
        File assetsFolder = new File(modelEngine, "assets");
        if(!assetsFolder.exists()){
            throw new RuntimeException("ModelEngine resource pack folder does not exist");
        }

        // copy item model to resource pack
        File itemModel = new File(assetsFolder, "minecraft/models/item/leather_horse_armor.json");
        File itemModelDest = new File(resourcePack, "assets/minecraft/models/item/leather_horse_armor.json");
        itemModelDest.mkdirs();
        try {
            Files.copy(itemModel.toPath(), itemModelDest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // copy modelengine files
        // textures first
        File texturesFolder = new File(assetsFolder, "modelengine/textures/entity");

        deepCopyFolder(texturesFolder, new File(resourcePack, "assets/modelengine/textures/entity"), StandardCopyOption.REPLACE_EXISTING);

        // then, copy models which have subfolders
        File modelsFolder = new File(assetsFolder, "modelengine/models");
        deepCopyFolder(modelsFolder, new File(resourcePack, "assets/modelengine/models"), StandardCopyOption.REPLACE_EXISTING);

    }

    /**
     * <p>
     *     Creates and compiles the ComplexItem model JSON files,
     *     these are not the files that override the vanilla files, but rather the files that define the model of the JSON itself.</p>
     * <p>
     * They exist at {@code assets/minecraft/models/item/}
     * <p>
     * A example file might look like this:
     * {@code enchanted_magma_block.json}
     * <pre>
     * {
     *   "parent": "minecraft:item/generated",
     *   "textures": {
     *     "layer0": "evocraft:item/enchanted_magma_block"
     *   }
     * }</pre>
     *<p>
     *     Make sure to research what the different parent items are, as it's pertinent to how the item is displayed in-game. You'll reference these when generating your vanilla JSON files.
     *</p>
     *
     * Make sure to research writing to JSON and output streams.
     *
     * @param itemFiles a HashMap of ComplexItems and their associated PNG files
     */
    private void compileComplexItemJSON(Map<ComplexItem, List<File>> itemFiles) {
        File dir = new File(EvoCraft.getPlugin().getDataFolder(), targetPath + File.separator + MODEL_LOCATION);
        dir.mkdirs();

        for (Map.Entry<ComplexItem, List<File>> entry : itemFiles.entrySet()) {
            ComplexItem item = entry.getKey();
            List<File> files = entry.getValue();

            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                String variantId = item.getId() + (i > 0 ? "_" + i : ""); // Adjust ID for variants

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("parent", "minecraft:item/" + (ItemUtils.isHandheld(item.getMaterial()) ? "handheld" : "generated"));
                JsonObject textures = new JsonObject();
                textures.addProperty("layer0", "evocraft:item/" + variantId);
                jsonObject.add("textures", textures);

                // Save the JSON file using variantId
                saveJSONFile(MODEL_LOCATION, jsonObject, variantId);
            }
        }
    }

    private void saveJSONFile(String location, JsonObject jsonObject, String itemId) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            // Construct the file path
            String filePath = EvoCraft.getPlugin().getDataFolder() + File.separator + targetPath + File.separator + location + itemId + ".json";
            File file = new File(filePath);

            // Create the file and directories if they do not exist
            file.getParentFile().mkdirs();
            file.createNewFile();

            // Write the JSON content to the file
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(gson.toJson(jsonObject));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     *     Creates and compiles the vanilla model files that override the vanilla files, with overrides for CustomModelData.
     * </p>
     * <p>These files also exist at {@code assets/minecraft/models/item/}</p>
     * <p>This is marginally more difficult than implementing the ComplexItem JSON generator, as there exist multiple ComplexItems for each VanillaItem.
     * <p>
     * I have gone ahead and compiled a map of Material -> {@code List<ComplexItem>},
     * so that for every material, you simply have to get Minecraft's default template and then add the overrides for each ComplexItem. </p>
     * </p>
     *
     * A example file might look like this:
     * {@code blaze_rod.json}
     * <pre>
     * {
     *   "parent": "minecraft:item/handheld",
     *   "textures": {
     *     "layer0": "minecraft:item/blaze_rod"
     *   },
     *     "overrides": [
     *     {
     *         "predicate": {
     *             "custom_model_data": 2673713
     *         },
     *         "model": "minecraft:item/fiery_ember_staff"
     *     },
     *     {
     *       "predicate": {
     *         "custom_model_data": 3240166
     *       },
     *       "model": "minecraft:item/enchanted_blaze_rod"
     *     },
     *     {
     *       "predicate": {
     *         "custom_model_data": 4732991
     *       },
     *       "model": "minecraft:item/dark_ember_staff"
     *     },
     *     {
     *       "predicate": {
     *         "custom_model_data": 4732992
     *       },
     *       "model": "minecraft:item/dark_ember_staff_2"
     *     }
     *   ]
     * }
     * }</pre>
     *
     * <p>You should be able to fetch the CustomModelData of the item via the ComplexItem#getCustomModelData() method of each ComplexItem.</p>
     *
     */
    private void compileVanillaItemJSON(Map<ComplexItem, List<File>> itemFiles) {
        File directory = new File(EvoCraft.getPlugin().getDataFolder(), targetPath + File.separator + VANILLA_LOCATION);
        directory.mkdirs();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, List<ComplexItem>> materialItemsMap = organizeItemsByMaterial(new ArrayList<>(itemFiles.keySet()));

        for (Map.Entry<String, List<ComplexItem>> entry : materialItemsMap.entrySet()) {
            String material = entry.getKey();
            JsonObject rootObject = createRootJsonObject(material);
            JsonArray overrides = createOverridesArray(entry.getValue(), itemFiles);
            rootObject.add("overrides", overrides);
            saveJSONFile(VANILLA_LOCATION, rootObject, material);
        }
    }

    private Map<String, List<ComplexItem>> organizeItemsByMaterial(List<ComplexItem> items) {
        Map<String, List<ComplexItem>> materialItemsMap = new HashMap<>();
        for (ComplexItem item : items) {
            String materialKey = item.getMaterial().toString().toLowerCase();
            materialItemsMap.computeIfAbsent(materialKey, k -> new ArrayList<>()).add(item);
        }
        return materialItemsMap;
    }

    private JsonObject createRootJsonObject(String material) {
        JsonObject rootObject = new JsonObject();
        rootObject.addProperty("parent", "minecraft:item/" + getItemType(material));
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", "minecraft:item/" + material);
        rootObject.add("textures", textures);
        return rootObject;
    }

    private String getItemType(String material) {
        return ItemUtils.isHandheld(Material.valueOf(material.toUpperCase())) ? "handheld" : "generated";
    }

    private JsonArray createOverridesArray(List<ComplexItem> items, Map<ComplexItem, List<File>> itemFiles) {
        JsonArray overrides = new JsonArray();
        for (ComplexItem item : items) {
            List<File> variants = itemFiles.get(item);
            for (int i = 0; i < variants.size(); i++) {
                JsonObject predicate = new JsonObject();
                predicate.addProperty("custom_model_data", item.getCustomModelData() + i); // Adjusting custom model data for variants

                JsonObject override = new JsonObject();
                override.add("predicate", predicate);
                String modelId = "evocraft:item/" + item.getId() + (i > 0 ? "_" + i : ""); // Adjusting model ID for variants
                override.addProperty("model", modelId);
                overrides.add(override);
            }
        }
        return overrides;
    }

    /**
     * Zip and save the resource pack folder into the resource pack directory. Make sure you zip the contents of the folder, not the folder itself, as that will cause issues.
     * @param directory the directory to zip
     */
    private File zipResourcePack(@NotNull File directory) {
        // Zip the directory
        // Save the zip file to the resource pack directory
        try {
            //input file zip path
            pack(directory.getAbsolutePath(), directory.getAbsolutePath() + ".zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(directory.getAbsolutePath() + ".zip");
    }

    private static void pack(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Paths.get(zipFilePath);
        if (Files.exists(p)) {
            Files.delete(p);
        }

        Files.createFile(p);
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
              .filter(path -> !Files.isDirectory(path))
              .forEach(path -> {
                  ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                  try {
                      zs.putNextEntry(zipEntry);
                      Files.copy(path, zs);
                      zs.closeEntry();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              });
        }
    }


    /**
     * Generate a sha1 hash given the resource pack file
     * @return a byte array containing the sha1 hash
     */
    private byte[] generateHash(File resourcePack) {
        try {
            // Create an instance of MessageDigest for SHA-1
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            // Create a FileInputStream to read the resource pack file
            try (FileInputStream fis = new FileInputStream(resourcePack)) {
                byte[] byteArray = new byte[1024];
                int bytesCount;

                // Read the file data and update the MessageDigest
                while ((bytesCount = fis.read(byteArray)) != -1) {
                    digest.update(byteArray, 0, bytesCount);
                }
            }

            // Complete the hash computation
            return digest.digest();

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null; 
        }
    }

    public void applyPack(Player player){
        String message = "Applying resource pack...";
        Util.sendMessage(player, message);
        // Set the resource pack for the player
        player.setResourcePack(this.url, this.hash, Component.text("Please install this resource pack in order to properly" +
                " render server models. You will be disconnected upon rejection of the pack."), true);
    }

    /**
     * Sets the resource pack for a player when they join the server
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if ("FILL IN".equals(this.url)) return; // If the URL is not set, don't do anything

        applyPack(event.getPlayer());
    }

    public String getSha1() {
        return Base64.getEncoder().encodeToString(this.hash);
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }
}
