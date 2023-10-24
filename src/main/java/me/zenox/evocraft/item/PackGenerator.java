package me.zenox.evocraft.item;

import me.zenox.evocraft.EvoCraft;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Resource pack generator that is used to generate resource packs for the plugin
 * <p>
 * Users should place item files in `assets/textures/items` in the plugin's data folder
 * <p>
 * Embedded in the JAR is a default set of PNG files that will be copied to the data folder if it doesn't exist, as well as a template
 */
public class PackGenerator {
    private final String targetPath;
    //read pngs from file
    //copy it a new zip file
    //add pngs to zip file
    //save zip file
    //return zip file
    //associate each png with a complex item
    //structure is so that it is in the format of EvoCraft-Pack-V3.0.0
    //navigate to minecraft items and check if there is a png file for it and if there is edit json for the vanilla material that it uses
    //else just keep it as is
    //use overide to make it so that it is a custom item
    //use complex item ID
    //construct the EvoCraft-Pack-V3.0.0 from scratch 
    //first read the textures
    //go to the resource pack folder and read the textures

    public PackGenerator(String targetPath) {
        this.targetPath = targetPath;
        createPack();
    }

    public void createPack() {
        // Location of the png files for items, this is where devs/users will add them
        File pngDirectory = new File(EvoCraft.getPlugin().getDataFolder(), "assets/textures/items");
        HashMap<ComplexItem, File> map = readPngs(pngDirectory);

        // Next, we want to compile a new resource pack folder
        // We can use a bare-bones template with all the required files and folders and simply copy it
        // For a first prototype, we'll only modify item files and textures, and deal with sounds + MEG later on
        File resourcePack = copyTemplatePack(new File(EvoCraft.getPlugin().getDataFolder(), targetPath));

        // Now we need to copy the PNG files into the resource pack
        copyPNGToPack(map);

        // Afterward, we need to compile the ComplexItem JSON files and add them to the resource pack
        compileComplexItemJSON(new ArrayList<>(map.keySet()));

        // Then, we need to compile the vanilla item JSON files (add overrides) and add them to the resource pack
        compileVanillaItemJSON(new ArrayList<>(map.keySet()));

        // Finally, we need to zip the resource pack folder and save it as a .zip file
        zipResourcePack(resourcePack);

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
                } else {
                    String name = file.getName().substring(0, file.getName().length() - 4).toLowerCase();
                    ComplexItem item = ItemRegistry.byId(name);
                    if (item == null) {
                        // Log message
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
                URL jar = src.getLocation();
                try (ZipInputStream zip = new ZipInputStream(jar.openStream())) {
                    ZipEntry ze = null;
                    while ((ze = zip.getNextEntry()) != null) {
                        String entryName = ze.getName();
                        if (entryName.startsWith("assets/textures/items") && entryName.endsWith(".png")) {
                            // Found a PNG, now copy it
                            File outFile = new File(directory, new File(entryName).getName());
                            if (!outFile.exists()) {
                                try (InputStream in = EvoCraft.getPlugin().getResource(entryName)) {
                                    Files.copy(in, outFile.toPath());
                                }
                            }
                        }
                    }
                }
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
        return null;
    }

    /**
     * Copies PNG files to the resource pack folder
     *
     * The path of the files in the resource pack folder should be `assets/evocraft/textures/items`
     * @param map a HashMap of ComplexItems and their associated PNG files
     */
    private void copyPNGToPack(HashMap<ComplexItem, File> map) {
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
        // Create a new JSON file for every single item with the format above, referencing the PNG file (Hint: it's just the id of the item + .png)
        for (ComplexItem item : items) {
            // Create JSON file
            // Write JSON file to resource pack folder (hint: saveResource())
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

    }

    /**
     * Zip and save the resource pack folder into the resource pack directory. Make sure you zip the contents of the folder, not the folder itself, as that will cause issues.
     * @param directory the directory to zip
     */
    private void zipResourcePack(File directory) {

    }


}
