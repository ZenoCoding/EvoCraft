package me.zenox.evocraft.gui;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.skills.Skills;
import me.zenox.evocraft.enchant.ComplexEnchantment;
import me.zenox.evocraft.gui.item.BookshelfItem;
import me.zenox.evocraft.gui.item.BooleanItem;
import me.zenox.evocraft.gui.item.CloseItem;
import me.zenox.evocraft.gui.item.EnchantItem;
import me.zenox.evocraft.item.ComplexItem;
import me.zenox.evocraft.item.ComplexItemStack;
import me.zenox.evocraft.item.LoreEntry;
import me.zenox.evocraft.item.VariableType;
import me.zenox.evocraft.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.AbstractGui;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.SlotElement;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.inventory.VirtualInventoryManager;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.util.*;

import static java.lang.Math.min;

/**
 * Enchantment GUI that is shown to players
 */
public class EnchantingGUI extends AbstractGui {

    public static final VariableType<Integer> ENCHANT_FUEL_VAR = new VariableType<>("enchant_fuel",
            new LoreEntry("enchant_fuel",
                    List.of(ChatColor.LIGHT_PURPLE + "Enchantment Fuel Strength: ")),
            VariableType.Priority.BELOW_ABILITIES,
            (loreEntry, variable) -> loreEntry.setLore(List.of(ChatColor.LIGHT_PURPLE + "Enchantment Fuel Strength: " + variable.getValue())));

    private static final String ENCHANT_GUI_ITEM_KEY = "enchant_gui_eitem_";
    private static final String ENCHANT_GUI_FUEL_KEY = "enchant_gui_fuel_";

    private static final Sound ENCHANT_SOUND = Sound.BLOCK_ENCHANTMENT_TABLE_USE;
    private static final Sound PRE_ENCHANT_SOUND = Sound.ENTITY_EVOKER_PREPARE_ATTACK;

    private final Player p;
    private final Block eTable;
    private int bookshelfPower = 0;

    public EnchantingGUI(int width, int height, Player p, Block eTable) {
        super(width, height);
        this.p = p;
        this.eTable = eTable;
    }

    public EnchantingGUI(@NotNull Structure structure, Player p, Block eTable) {
        super(structure.getWidth(), structure.getHeight());
        applyStructure(structure);
        this.p = p;
        this.eTable = eTable;
    }

    /**
     * Applies enchants to an item in the enchant GUI, given the enchantment level, bookshelf power, player enchanting level, and fuel.
     * <ol>
     *     <li>- Enchantment Level - Increases variety and maximum strength of enchants</li>
     *     <li>- Bookshelf Power - Increases variety of enchants</li>
     *     <li>- Player Enchanting Level - Increases maximum strength of enchants and increases chance of rarer enchants</li>
     *     <li>- Fuel Level - Increases Maximum strength of enchants & increases prioritizes rarer enchants</li>
     * </ol>
     *
     * @param level enchantment level
     * @param xpRequired xp required to enchant
     * @return whether the enchantment process worked
     */
    public boolean enchantItem(int level, int xpRequired) {
        ComplexItemStack item = ComplexItemStack.of(getEItem());
        ComplexItemStack fuelItem = ComplexItemStack.of(getFuelItem());
        Random r = new Random();

        int fuelStrength = (int) (fuelItem.getComplexMeta().getVariable(ENCHANT_FUEL_VAR).getValue());

        double variety = calculateVariety(bookshelfPower);
        double strength = calculateStrength(level, fuelStrength, AureliumAPI.getSkillLevel(p, Skills.ENCHANTING));

        // Util.sendMessage(p, "Enchant | Strength: " + strength + " | Variety: " + variety);

        HashMap<ComplexEnchantment, Integer> result = new HashMap<>();
        Map<ComplexEnchantment, Integer> currentEnchantments = item.getComplexMeta().getComplexEnchants();

        // Obtain list of enchantments that cannot be applied
        List<ComplexEnchantment> possibleEnchantments = new ArrayList<>(ComplexEnchantment.getRegisteredEnchants()
                .stream().filter(complexEnchantment -> complexEnchantment.getTypes().contains(item.getComplexItem().getType())).toList());

        for(ComplexEnchantment enchantment : currentEnchantments.keySet()) possibleEnchantments.removeAll(enchantment.getExclusive());


        while (true) {
            // Assign all enchantments that can be applied an index based on their weight
            HashMap<ComplexEnchantment, Integer> enchantmentWeights = new HashMap<>();
            int currentWeight = 0;
            for (ComplexEnchantment enchantment : possibleEnchantments) {
                if (enchantment.getWeight() > 0) {
                    enchantmentWeights.put(enchantment, enchantment.getWeight() + currentWeight);
                    currentWeight += enchantment.getWeight();
                }  //Util.logToConsole(ChatColor.RED + "[ERROR]" + ChatColor.WHITE + " Enchantment " + enchantment.getName() + " has a weight of 0 or less!");

            }

            // If there are no enchantments that can be applied, break
            if (enchantmentWeights.isEmpty()) break;

            // Get a random enchantment based on the weight
            int randomWeight = r.nextInt(currentWeight);

            ComplexEnchantment enchantment = null;
            for (Map.Entry<ComplexEnchantment, Integer> entry : enchantmentWeights.entrySet()) {
                if (randomWeight < entry.getValue()) {
                    enchantment = entry.getKey();
                    break;
                }
            }

            if (enchantment == null) throw new IllegalStateException("Could not find a valid enchant!");

            // Get the enchantment level
            int enchantmentLevel = calculateLevel(strength, enchantment.getMaxLevel(), enchantment.getWeight());

            // Append the enchantment to the result
            result.put(enchantment, enchantmentLevel);

            // Remove the enchantment from the list of possible enchantments
            possibleEnchantments.remove(enchantment);
            possibleEnchantments.removeAll(enchantment.getExclusive());

            if(variety < 1 && r.nextDouble() > variety) break;
            if(variety <= 0) break;
            variety--;
        }

        HashMap<ComplexEnchantment, Integer> resultCombined = combineEnchantMaps(result, currentEnchantments);

        // Apply enchantments to item
        item.getComplexMeta().setComplexEnchantments(resultCombined);

        fuelItem.getItem().setAmount(fuelItem.getItem().getAmount() - 1);

        // Set fuel to be empty
        VirtualInventoryManager.getInstance().getOrCreate(Util.constantUUID(ENCHANT_GUI_FUEL_KEY + p.getName()), 1).setItem(null, 0, fuelItem.getItem());
        // update virtual container with enchanted version
        VirtualInventoryManager.getInstance().getOrCreate(Util.constantUUID(ENCHANT_GUI_ITEM_KEY + p.getName()), 1).setItem(null, 0, item.getItem());

        this.eTable.getWorld().playSound(this.eTable.getLocation(), ENCHANT_SOUND, 1f + level, 1f - level * 0.15f);

        // Send Enchanting Particles
        for (int i = 0; i < 5; i++) {
            this.eTable.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, this.eTable.getLocation().add(0.5, 1.5, 0.5), 5, 0.5, 0, 0.5);
        }

        // Update player's XP levels
        // the "-level + 1" is to account for some underlying issue regarding enchanting :thinking:
        p.setLevel(p.getLevel() - xpRequired - level + 1);

        // Update player's Skill XP
        AureliumAPI.addXp(p, Skills.ENCHANTING, calculateSkillXP(level, strength, calculateVariety(bookshelfPower)));
        return true;
    }

    /**
     * Helper method that gets the item that is being enchanted
     *
     * @return the item being enchanted
     */
    private ItemStack getEItem() {
        return VirtualInventoryManager.getInstance().getOrCreate(Util.constantUUID(ENCHANT_GUI_ITEM_KEY + p.getName()), 1).getItem(0);
    }

    /**
     * Helper method that gets the fuel used for enchanting
     *
     * @return the fuel item
     */
    private ItemStack getFuelItem() {
        return VirtualInventoryManager.getInstance().getOrCreate(Util.constantUUID(ENCHANT_GUI_FUEL_KEY + p.getName()), 1).getItem(0);
    }

    public Block getETable() {
        return eTable;
    }

    public void setBookshelfPower(int bookshelfPower) {
        this.bookshelfPower = bookshelfPower;
    }

    public static boolean enchantValid(EnchantingGUI gui, int power, int XPRequired) {
        int skillRequirement = 0;
        switch (power) {
            case 2 -> skillRequirement = 10;
            case 3 -> skillRequirement = 25;
        }
        // Check XP and skill level
        return fuelValid(gui.getFuelItem()) && itemValid(gui.getEItem()) && AureliumAPI.getSkillLevel(gui.p, Skills.ENCHANTING) >= skillRequirement && gui.p.getLevel() >= XPRequired;
    }

    private static boolean itemValid(ItemStack item) {
        try {
            ComplexItem.Type type = ComplexItem.of(item).getType();
            return item.getType() != Material.AIR && !ComplexEnchantment.getRegisteredEnchants()
                    .stream()
                    .filter(complexEnchantment ->
                            complexEnchantment.getTypes().contains(type)).toList().isEmpty();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean fuelValid(ItemStack item) {
        try {
            return ComplexItemStack.of(item).getComplexMeta().hasVariable(ENCHANT_FUEL_VAR);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static double calculateStrength(int level, int fuelStrength, int enchantingSkill) {
        double levelFactor = 0.1d + level * 0.3d;
        double fuelFactor = Math.cbrt(fuelStrength)/6;
        double skillFactor = Math.sqrt(enchantingSkill + 4)/3;
        return min(1d, (skillFactor * fuelFactor)) * levelFactor;
    }

    public static double calculateVariety(int bookshelfPower) {
        return min(2, Math.pow(bookshelfPower, 1/4d)/2.569d + 1d);
    }

    public static int calculateLevel(double strength, int maxLevel, int weight){
        double strengthFactor = strength * maxLevel;
        Random random = new Random();
        return Math.max(1, Math.min(maxLevel, (int) strengthFactor - (random.nextDouble() < (double) weight / 100 ? 1 : 0)));
    }

    public static int calculateSkillXP(int power, double strength, double variety){
        return (int) (Math.pow(power, 2) * strength * Math.sqrt(8 * variety) * 4000);
    }

    /**
     * Combines two sets of enchantments to create a third resulting set of enchantments, similar to the behavior of an anvil
     * @param enchantSet1 the first set of enchantments- in case of disputes, this one will take priority
     * @param enchantSet2 the second set of enchantments
     * @return the resulting set of enchantments
     */
    public static HashMap<ComplexEnchantment, Integer> combineEnchantMaps(HashMap<ComplexEnchantment, Integer> enchantSet1, Map<ComplexEnchantment, Integer> enchantSet2){
        HashMap<ComplexEnchantment, Integer> result = (HashMap<ComplexEnchantment, Integer>) enchantSet1.clone();
        // Create a list of enchants to ignore
        List<ComplexEnchantment> invalidEnchantments = new ArrayList<>();
        enchantSet1.forEach(((complexEnchantment, integer) -> invalidEnchantments.addAll(complexEnchantment.getExclusive())));

        for (Map.Entry<ComplexEnchantment, Integer> entry : enchantSet2.entrySet()) {
            if(invalidEnchantments.contains(entry.getKey())) continue;
            result.computeIfPresent(entry.getKey(), ((complexEnchantment, integer) -> combineEnchantLevels(entry.getKey().getMaxLevel(), entry.getValue(), integer)));
            result.putIfAbsent(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private static int combineEnchantLevels(int maxLevel, int enchantLevel1, int enchantLevel2){
        int result = Math.max(enchantLevel1, enchantLevel1);
        if(enchantLevel1 == enchantLevel2) return enchantLevel1 + 1;
        return Math.min(maxLevel, result);
    }

    public static Gui getGui(Player p, Block block) {
        return new EnchantGuiBuilder(p, block)
                .setStructure(
                        "# # # # # $ $ 1 #",
                        "# # # # # $ # # #",
                        "# F @ @ E $ % 2 #",
                        "# # # # # ^ # # #",
                        "# # # # # ^ ^ 3 #",
                        "# # # # C B # # #"
                )
                .addIngredient('E', new SlotElement.InventorySlotElement(VirtualInventoryManager.getInstance().getOrCreate(Util.constantUUID(ENCHANT_GUI_ITEM_KEY + p.getName()), 1), 0, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)))
                .addIngredient('F', new SlotElement.InventorySlotElement(VirtualInventoryManager.getInstance().getOrCreate(Util.constantUUID(ENCHANT_GUI_FUEL_KEY + p.getName()), 1), 0, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE)))
                .addIngredient('R', new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(""))
                .addIngredient('1', new EnchantItem(1, 0))
                .addIngredient('2', new EnchantItem(2, 10))
                .addIngredient('3', new EnchantItem(3, 25))
                .addIngredient('@', new BooleanItem(enchantingGUI -> fuelValid(enchantingGUI.getFuelItem())))
                .addIngredient('$', new BooleanItem(enchantingGUI -> enchantValid(enchantingGUI, 1, 20)))
                .addIngredient('%', new BooleanItem(enchantingGUI -> enchantValid(enchantingGUI, 2, 30)))
                .addIngredient('^', new BooleanItem(enchantingGUI -> enchantValid(enchantingGUI, 3, 50)))
                .addIngredient('B', new BookshelfItem())
                .addIngredient('C', new CloseItem())
                .build();
    }
}
