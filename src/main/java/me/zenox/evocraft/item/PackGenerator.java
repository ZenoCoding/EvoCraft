package me.zenox.evocraft.item;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ticxo.modelengine.api.ModelEngineAPI;
import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.util.Util;
import net.kyori.adventure.text.Component;

import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
    private final String targetPath;
    private String url;
    private byte[] hash;
    private final File resourcePack;

    public PackGenerator(String targetPath) {
        this.targetPath = targetPath;
        this.resourcePack = createPack();
        this.url = EvoCraft.getPlugin().getConfig().getString("resource-pack-url");
        this.hash = generateHash(resourcePack);

        Bukkit.getPluginManager().registerEvents(this, EvoCraft.getPlugin());
    }

    public File createPack() {
        // Location of the png files for items, this is where devs/users will add them
        File pngDirectory = new File(EvoCraft.getPlugin().getDataFolder(), "assets/textures/items");
        HashMap<ComplexItem, File> map = readPngs(pngDirectory);

        // Next, we want to compile a new resource pack folder
        // We can use a bare-bones template with all the required files and folders and simply copy it
        // For a first prototype, we'll only modify item files and textures, and deal with sounds + MEG later on
        File resourcePack = copyTemplatePack(new File(EvoCraft.getPlugin().getDataFolder(), targetPath));

        // Now we need to copy the PNG files into the resource pack
        copyPNGToPack(map);

        // Clone modelEngine files
        File modelEngine = new File(ModelEngineAPI.api.getDataFolder(), "resource pack");
        copyModelEngineFiles(modelEngine, resourcePack);

        // Afterward, we need to compile the ComplexItem JSON files and add them to the resource pack
        compileComplexItemJSON(new ArrayList<>(map.keySet()));

        // Then, we need to compile the vanilla item JSON files (add overrides) and add them to the resource pack
        compileVanillaItemJSON(new ArrayList<>(map.keySet()));

        // Finally, we need to zip the resource pack folder and save it as a .zip file
        zipResourcePack(resourcePack);

        return resourcePack;
    }


    /**
     * Method to read PNG files from a directory and associate them with ComplexItems by ID
     * @param directory the directory to read PNG files from
     * @return a HashMap of ComplexItems and their associated PNG files
     */
    private HashMap<ComplexItem, File> readPngs(File directory) {
        HashMap<ComplexItem, File> map = new HashMap<>();

        // Check if directory exists
        if (!directory.exists()) {
            directory.mkdirs();  // Create directory if it doesn't exist

            // Copy PNG files from JAR to directory
            copyDefaultPNGs(directory);
        }

        // Your existing logic here:
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                map.putAll(readPngs(file));
            } else {
                String extension = file.getName().substring(file.getName().length() - 4);
                if (!extension.equals(".png")) {
                    // Log message, continue
                    Util.logToConsole("File " + file.getName() + " is not a PNG file");
                } else {
                    String name = file.getName().substring(0, file.getName().length() - 4).toLowerCase();
                    ComplexItem item = ItemRegistry.byId(name);
                    if (item == null) {
                        // Log message
                        Util.logToConsole("ComplexItem not found for " + name);
                    } else {
                        map.put(item, file);
                    }
                }
            }
        }
        return map;
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
            } else {
                // Handle the case where it's not in a JAR - possibly during development
                File source = new File(dirURL.getPath());
                Files.walk(source.toPath())
                        .forEach(sourcePath -> {
                            Path destPath = Paths.get(directory.getPath(), sourcePath.subpath(source.toPath().getNameCount(), sourcePath.getNameCount()).toString());
                            try {
                                Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException e) {
                                throw new RuntimeException("Error copying files", e);
                            }
                        });
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy template pack", e);
        }

        return directory; // Return the directory where the files have been copied
    }


    /**
     * Copies PNG files to the resource pack folder
     *
     * The path of the files in the resource pack folder should be `assets/textures/items`
     * @param map a HashMap of ComplexItems and their associated PNG files
     */
    private void copyPNGToPack(HashMap<ComplexItem, File> map) {
        for (ComplexItem item : map.keySet()) {
            File file = map.get(item);
            try {
                Files.copy(file.toPath(), Paths.get(EvoCraft.getPlugin().getDataFolder().getAbsolutePath(), "assets/textures/items/" + file.getName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
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
     *  <li>assets/minecraft/atlases/blocks.json</li>
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
        throw new NotImplementedException("copyModelEngineFiles not implemented");
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
     * @param items The list of items to compile JSON files for
     */
    private void compileComplexItemJSON(List<ComplexItem> items) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // Create a new JSON file for every single item with the format above, referencing the PNG file (Hint: it's just the id of the item + .png)
        for (ComplexItem item : items) {
            // Create JSON file
            // Write JSON file to resource pack folder (hint: saveResource())
            // Use gson to write JSON file
            // Use the item's ID to reference the PNG file
            // Use the item's ID to reference the JSON file
            // Use the item's ID to reference the vanilla JSON file
            // Use the item's ID to reference the vanilla model file
            // Use the item's ID to reference the vanilla texture file
            //we need to add the vanilla json file
            //we need to add the vanilla model file
            //we need to add the vanilla texture file
            JsonObject vanillaObject = new JsonObject();
            vanillaObject.addProperty("parent", "minecraft:item/" + item.getMaterial().toString());
            JsonObject vanillaTextures = new JsonObject();
            vanillaTextures.addProperty("layer0", "minecraft:item/" + item.getMaterial().toString());
            vanillaObject.add("textures", vanillaTextures);
            try {
                File file = new File(EvoCraft.getPlugin().getDataFolder(), targetPath + File.separator + "assets/minecraft/models/item/" + item.getMaterial().toString() + ".json");
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(gson.toJson(vanillaObject));
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
     * @param items
     */
    private void compileVanillaItemJSON(List<ComplexItem> items) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Organize the ComplexItems by their material.
        Map<String, List<ComplexItem>> materialItemsMap = new HashMap<>();
        for (ComplexItem item : items) {
            Material material = item.getMaterial(); // Assuming ComplexItem has a method 'getMaterial'
            if (!materialItemsMap.containsKey(material.toString())) {
                materialItemsMap.put(material.toString(), new ArrayList<>());
            }
            materialItemsMap.get(material.toString()).add(item);
        }

        //loop over each material type
        for (String material : materialItemsMap.keySet()) {
            //get the vanilla item json file
            //add the overrides for each complex item
            //save the json file

            //making object handheld
            JsonObject rootObject = new JsonObject();
            String value = "minecraft:item/" + material;
            rootObject.addProperty("parent", value);

            //making object textures
            JsonObject textures = new JsonObject();
            textures.addProperty("layerO", "minecraft:item/" + material);


            //making object overrides
            JsonObject overrides = new JsonObject();
            for (ComplexItem item : materialItemsMap.get(material)) {
                //explain what this is doing
                //this is adding the overrides for each complex item
                JsonObject predicate = new JsonObject();
                predicate.addProperty("custom_model_data", item.getCustomModelData());
                JsonObject model = new JsonObject();
                model.addProperty("model", "evocraft:item/" + item.getId());
                JsonObject override = new JsonObject();
                override.add("predicate", predicate);
                override.add("model", model);
                overrides.add(item.getId(), override);
            }

            rootObject.add("overrides", overrides);

            //write json file to resource pack folder
            try {

                File file = new File(EvoCraft.getPlugin().getDataFolder(), targetPath + File.separator + "assets/minecraft/models/item/" + material + ".json");
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(gson.toJson(rootObject));
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //the above function :
    //it creates a json file for each material type
    //it adds the overrides for each complex item
    //it saves the json file
    //it writes the json file to the resource pack folder
    //it uses the gson library to write the json file

    /**
     * Zip and save the resource pack folder into the resource pack directory. Make sure you zip the contents of the folder, not the folder itself, as that will cause issues.
     * @param directory the directory to zip
     */
    private void zipResourcePack(@NotNull File directory) {
        // Zip the directory
        // Save the zip file to the resource pack directory
        try {
            //input file zip path
            pack(directory.getAbsolutePath(), directory.getAbsolutePath() + ".zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * @return
     */
    private byte[] generateHash(File resourcePack) {
        //throw new NotImplementedException("generateHash not implemented");
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
            return null; // or handle the exception as per your requirement
        }
    }

    /**
     * Sets the resource pack for a player when they join the server
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        // Send a message to the player briefly telling them about the resource pack
        Player player = event.getPlayer();
        String message = "Welcome to the server!";
        Util.sendMessage(player, message);
        // Set the resource pack for the player
        Player p = event.getPlayer();
        // public void setResourcePack(@NotNull String url, byte @Nullable [] hash, net.kyori.adventure.text.@Nullable Component prompt, boolean force);
        p.setResourcePack(this.url, this.hash, Component.text("Please install this resource pack in order to properly render server models"), true);
        throw new NotImplementedException("Setting resource pack on join not yet implemented.");
    }
}
