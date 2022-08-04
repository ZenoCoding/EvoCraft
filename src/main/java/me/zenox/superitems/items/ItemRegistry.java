package me.zenox.superitems.items;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.basicitems.*;
import me.zenox.superitems.items.superitems.*;
import me.zenox.superitems.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRegistry {

    private final SuperItems plugin;

    private final List<BasicItem> registeredItems;

    public ItemRegistry(SuperItems plugin) {
        this.plugin = plugin;
        registeredItems = List.of(new GardenerSapling(),
                new EnchantedMagmaBlock(), new PurifiedMagmaDistillate(), new EnchantedBlazeRod(), new BurningAshes(),
                new MoltenPowder(), new EnchantedEnderPearl(), new CompactedEnderPearl(), new AbsoluteEnderPearl(),
                new DarkSkull(), new TormentedSoul(), new EnchantedIronBlock(), new ToyStick(), new SoulCrystal(), new FieryEmberStaff(), new DarkEmberStaff(), new TormentedBlade(),
                new SpeedyGonzalas(), new JackassGonzalas(), new SwordOfJustice());
    }

    public void addRecipes() {
        for (BasicItem item : registeredItems) {
            List<Recipe> recipeList = item.getRecipes(registeredItems);
            for(Recipe recipe : recipeList){
                try {
                    Bukkit.addRecipe(recipe);
                } catch (IllegalStateException e) {
                    Util.logToConsole("Recipe: " + recipe.toString());
                    Util.logToConsole(e.toString());
                    if(recipe instanceof Keyed){
                        Util.logToConsole("Found duplicate recipe, re-adding.");
                        Bukkit.removeRecipe(((Keyed) recipe).getKey());
                        Bukkit.addRecipe(recipe);
                    } else Util.logToConsole("Found duplicate recipe that wasn't keyed, skipping.");
                }
            }
        }
    }

    @Nullable
    public BasicItem getBasicItemFromItemStack(ItemStack item) {
        try {
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            String id = container.get(SuperItem.GLOBAL_ID, PersistentDataType.STRING);
            return this.getBasicItemFromId(id);
        } catch (NullPointerException e){
            return null;
        }
    }

    public BasicItem getBasicItemFromId(String id) {
        for (BasicItem item : this.registeredItems) {
            if (id == null) return null;
            if (id.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }

    public List<BasicItem> getRegisteredItems() {
        return registeredItems;
    }

}
