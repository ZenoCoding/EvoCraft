package me.zenox.superitems.loot;

import me.zenox.superitems.loot.LootTable;
import me.zenox.superitems.loot.LootTableEntry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static me.zenox.superitems.items.ItemRegistry.*;

public class LootTableRegistry {

    public static final List<LootTable> lootTableList = new ArrayList();

    public static LootTable BLAZIEL_DROPS = registerLootTable(new LootTable("blaziel", "Blaziel Loot", List.of(
            new LootTableEntry(PYTHEMION_GEM.getItemStack(1), 1, 1, 0.005),
            new LootTableEntry(DARK_SKULL.getItemStack(1), 1, 1, 0.01),
            new LootTableEntry(BURNING_ASHES.getItemStack(1), 2, 4, 0.07),
            new LootTableEntry(PURIFIED_MAGMA_DISTILLATE.getItemStack(1), 1, 2, 0.1),
            new LootTableEntry(ENCHANTED_BLAZE_ROD.getItemStack(1), 1, 3, 0.4),
            new LootTableEntry(ENCHANTED_MAGMA_BLOCK.getItemStack(1), 1, 15, 0.5),
            new LootTableEntry(new ItemStack(Material.BLAZE_ROD), 5, 32, 1))));

    public static LootTable DESECRATOR_DROPS = registerLootTable(new LootTable("desecrator", "Desecrator Loot", List.of(
            new LootTableEntry(DESECRATOR_CLAW.getItemStack(1), 1, 2, 0.01),
            new LootTableEntry(DESECRATOR_TOE.getItemStack(1), 1, 2, 0.2),
            new LootTableEntry(DESECRATOR_SCALE.getItemStack(1), 1, 2, 0.5),
            new LootTableEntry(RAVAGER_SKIN.getItemStack(1), 6, 10, 1),
            new LootTableEntry(new ItemStack(Material.LEATHER), 5, 32, 1))));

    public static LootTable DEMISER_DROPS = registerLootTable(new LootTable("demiser", "Demiser Loot", List.of(
            new LootTableEntry(VOID_STONE.getItemStack(1), 1, 1, 0.02),
            new LootTableEntry(HYPER_CRUX.getItemStack(1), 1, 1, 0.25),
            new LootTableEntry(TORMENTED_SOUL.getItemStack(1), 1, 2, 0.5),
            new LootTableEntry(new ItemStack(Material.TOTEM_OF_UNDYING), 1, 2, 1))));


    private static LootTable registerLootTable(LootTable table) {
        lootTableList.add(table);
        return table;
    }
}
