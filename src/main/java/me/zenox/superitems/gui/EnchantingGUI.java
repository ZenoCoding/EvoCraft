package me.zenox.superitems.gui;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.skills.Skills;
import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.SlotElement;
import de.studiocode.invui.gui.impl.SimpleGUI;
import de.studiocode.invui.gui.structure.Structure;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.virtualinventory.VirtualInventoryManager;
import me.zenox.superitems.enchant.ComplexEnchantment;
import me.zenox.superitems.gui.item.BookshelfItem;
import me.zenox.superitems.gui.item.CloseItem;
import me.zenox.superitems.gui.item.EnchantItem;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.item.LoreEntry;
import me.zenox.superitems.item.VariableType;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Enchantment GUI that is shown to players
 */
public class EnchantingGUI extends SimpleGUI {

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
    private final Block etable;
    private int bookshelfPower = 0;

    public EnchantingGUI(int width, int height, Player p, Block etable) {
        super(width, height);
        this.p = p;
        this.etable = etable;
    }

    public EnchantingGUI(@NotNull Structure structure, Player p, Block etable) {
        super(structure.getWidth(), structure.getHeight());
        applyStructure(structure);
        this.p = p;
        this.etable = etable;
    }

    /**
     * Applies enchants to an item in the enchant GUI, given the enchant level, bookshelf power, player enchanting level, and fuel.
     * <ol>
     *     <li>- Enchantment Level - Increases variety and maximum strength of enchants</li>
     *     <li>- Bookshelf Power - Increases variety of enchants</li>
     *     <li>- Player Enchanting Level - Increases maximum strength of enchants and increases chance of rarer enchants</li>
     *     <li>- Fuel Level - Increases Maximum strength of enchants & increases prioritizes rarer enchants</li>
     * </ol>
     *
     * @param level enchantment level
     * @param xp    the amount of experience to take (levels)
     * @return whether or not the enchantment process worked
     */
    public boolean enchantItem(int level, int xp) {
        Util.sendMessage(p, "Attempting to enchant... ");
        ComplexItemStack item = ComplexItemStack.of(getEItem());
        ComplexItemStack cItem = ComplexItemStack.of(getFuelItem());
        Random r = new Random();

        if (item == null || cItem == null) {
            Util.sendMessage(p, "Failed due to complexitems being null");
            return false;
        }

        if (p.getLevel() < xp) {
            Util.sendMessage(p, "Failed due to player xp being too little. Player XP was: " + p.getTotalExperience() + " | Required XP was: " + xp);
            return false;
        }
        //if (!cItem.getComplexMeta().hasVariable(ENCHANT_FUEL_VAR)) return false; // Fuel invalid

        int fuelStrength = 10;/*(int) ((int) (cItem.getComplexMeta().getVariable(ENCHANT_FUEL_VAR).getValue()) * Math.sqrt(cItem.getItem().getAmount()));*/

        double variety = r.nextDouble(1d, (Math.sqrt(this.bookshelfPower) * level / 3d) + 1.01d);
        // between 0 and 1
        double strength = Math.min(1d, ((level / 2 + Math.sqrt(fuelStrength)) * (2 * Math.sqrt(AureliumAPI.getSkillLevel(p, Skills.ENCHANTING)))) / 200);

        Util.sendMessage(p, "Enchant | Strength: " + strength + " | Variety: " + variety);

        HashMap<ComplexEnchantment, Integer> result = new HashMap<>();

        while (variety >= 1) {
            for (ComplexEnchantment enchantment : ComplexEnchantment.getRegisteredEnchants()
                    .stream()
                    /*.filter(complexEnchantment ->
                            complexEnchantment.getTypes().contains(item.getComplexItem().getType()))*/.toList()) {
                if (r.nextInt(0, Math.max(1, ComplexEnchantment.getRegisteredEnchants().size() * (enchantment.getRarity() / 100))) == 0) {
                    result.put(enchantment, Math.max(1, r.nextInt((int) (enchantment.getMaxLevel() / (strength + 1)))));
                    variety--;
                }
            }
        }

        Map<ComplexEnchantment, Integer> current = item.getComplexMeta().getComplexEnchants();
        HashMap<ComplexEnchantment, Integer> resultCombined = new HashMap<>();

        for (Map.Entry<ComplexEnchantment, Integer> entry : result.entrySet()) {
            if (current.containsKey(entry.getKey())) {
                Util.sendMessage(p, current.get(entry.getKey()) + " | " + entry.getValue());
                if (current.get(entry.getKey()).equals(entry.getValue()))
                    resultCombined.put(entry.getKey(), entry.getValue() + 1);
                else resultCombined.put(entry.getKey(), Math.max(entry.getValue(), current.get(entry.getKey())));
            } else resultCombined.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<ComplexEnchantment, Integer> entry : current.entrySet()) {
            resultCombined.putIfAbsent(entry.getKey(), entry.getValue());
        }

        item.getComplexMeta().setComplexEnchantments(resultCombined);

        // Set fuel to be empty
        VirtualInventoryManager.getInstance().getOrCreate(Util.constantUUID(ENCHANT_GUI_FUEL_KEY + p.getName()), 1).setItemStack(null, 0, new ItemStack(Material.AIR));
        // update virtualcontainer with ench version
        VirtualInventoryManager.getInstance().getOrCreate(Util.constantUUID(ENCHANT_GUI_ITEM_KEY + p.getName()), 1).setItemStack(null, 0, item.getItem());

        this.etable.getWorld().playSound(this.etable.getLocation(), ENCHANT_SOUND, 1f+level, 1f-level*0.15f);

        p.sendExperienceChange(p.getExp(), p.getLevel() - xp);
        p.setLevel(p.getLevel() - xp);

        Util.sendMessage(p, "Enchanted item " + item.getItem().getItemMeta().getDisplayName() + " w/ enchants: " + resultCombined);
        Util.sendMessage(p, "Not Combined: " + result);

        return true;
    }

    /**
     * Helper method that gets the item that is being enchanted
     *
     * @return the item being enchanted
     */
    private ItemStack getEItem() {
        return VirtualInventoryManager.getInstance().getOrCreate(Util.constantUUID(ENCHANT_GUI_ITEM_KEY + p.getName()), 1).getItemStack(0);
    }

    /**
     * Helper method that gets the fuel used for enchanting
     *
     * @return the fuel item
     */
    private ItemStack getFuelItem() {
        return VirtualInventoryManager.getInstance().getOrCreate(Util.constantUUID(ENCHANT_GUI_FUEL_KEY + p.getName()), 1).getItemStack(0);
    }

    public Block getEtable() {
        return etable;
    }

    public void setBookshelfPower(int bookshelfPower) {
        this.bookshelfPower = bookshelfPower;
    }

    public static GUI getGui(Player p, Block block) {
        return new EnchantGUIBuilder(GUITypes.ENCHANT, p, block)
                .setStructure(
                        "# # # # # R R 1 #",
                        "# # # # # R # # #",
                        "# L R R E R R 2 #",
                        "# # # # # R # # #",
                        "# # # # # R R 3 #",
                        "# # # # C B # # #"
                )
                // Lapis Slot
                .addIngredient('E', new SlotElement.VISlotElement(VirtualInventoryManager.getInstance().getOrCreate(Util.constantUUID(ENCHANT_GUI_ITEM_KEY + p.getName()), 1), 0, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)))
                .addIngredient('L', new SlotElement.VISlotElement(VirtualInventoryManager.getInstance().getOrCreate(Util.constantUUID(ENCHANT_GUI_FUEL_KEY + p.getName()), 1), 0, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE)))
                .addIngredient('R', new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(""))
                .addIngredient('1', new EnchantItem(1))
                .addIngredient('2', new EnchantItem(2))
                .addIngredient('3', new EnchantItem(3))
                .addIngredient('B', new BookshelfItem())
                .addIngredient('C', new CloseItem())
                .build();
    }
}
