package me.zenox.superitems.events;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.recipe.ComplexChoice;
import me.zenox.superitems.recipe.ComplexRecipe;
import me.zenox.superitems.recipe.ShapedComplexRecipe;
import me.zenox.superitems.recipe.ShapelessComplexRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CraftEvent implements Listener {
    private SuperItems plugin;

    public CraftEvent(SuperItems plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void prepareCraftEvent(PrepareItemCraftEvent e){
        if(e.getInventory().getType() != InventoryType.WORKBENCH) return;

        // Get the related complex recipe
        for (ComplexRecipe recipe : ComplexRecipe.registeredRecipes) {
            if (e.getRecipe() != null && recipe.getKey().equals(((Keyed) e.getRecipe()).getKey())){
                if(recipe instanceof ShapedComplexRecipe){
                    // Shaped Logic
                    if (!testRecipe(recipe, e.getInventory().getMatrix())){
                        e.getInventory().setResult(new ItemStack(Material.AIR));
                        for(ComplexRecipe complexRecipe : ComplexRecipe.similarRecipeMap.get(recipe)){
                            if(testRecipe(complexRecipe, e.getInventory().getMatrix())) {
                                e.getInventory().setResult(complexRecipe.getResult().getItem());
                                break;
                            }
                        }
                    }
                }
                break;
            } else if(recipe instanceof ShapelessComplexRecipe) {
                if(testRecipe(recipe, e.getInventory().getMatrix())){
                    e.getInventory().setResult(recipe.getResult().getItem());
                }
            }
        }

    }

    private boolean testRecipe(ComplexRecipe recipe, ItemStack[] matrix){
        if(recipe instanceof ShapedComplexRecipe) {
            ShapedComplexRecipe shapedRecipe = (ShapedComplexRecipe) recipe;
            int index = 0;
            for (String row : shapedRecipe.getShape()) {
                for (char c : row.toCharArray()) {
                    if (!shapedRecipe.getChoiceMap().get(c).test(matrix[index])) {
                        return false;
                    }
                    index++;
                }
            }
            return true;
        } else {
            ShapelessComplexRecipe shapelessRecipe = (ShapelessComplexRecipe) recipe;
            HashMap<ComplexItem, Integer> requiredItems = new HashMap<>();
            for (ComplexChoice choice : shapelessRecipe.getChoiceList()) {
                requiredItems.computeIfPresent(((Map.Entry<ComplexItem, Integer>) choice.getChoices().entrySet().toArray()[0]).getKey(), ((complexItem, integer) -> integer + choice.getAmount()));
                requiredItems.putIfAbsent(((Map.Entry<ComplexItem, Integer>) choice.getChoices().entrySet().toArray()[0]).getKey(), ((Map.Entry<ComplexItem, Integer>) choice.getChoices().entrySet().toArray()[0]).getValue());
            }

            for (ItemStack item : matrix){
                if(item == null) continue;
                ComplexItem cItem = ComplexItemStack.of(item).getComplexItem();
                if(!requiredItems.containsKey(cItem)) return false;
                requiredItems.replace(cItem, requiredItems.get(cItem) - item.getAmount());
            }

            for (Map.Entry<ComplexItem, Integer> entry :
                    requiredItems.entrySet()) {
                if(entry.getValue() > 0) return false;
            }

            return true;
        }
    }

    @EventHandler
    public void craftEvent(InventoryClickEvent e){
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().getType() != InventoryType.WORKBENCH) return;
        if(e.getSlot() != 0) return;
        CraftingInventory inventory = ((CraftingInventory) e.getClickedInventory());
        // Check that there is something being crafted - and identify what it is
        ComplexRecipe recipe = null;

        for (ComplexRecipe recipe1 : ComplexRecipe.registeredRecipes) {
            if (testRecipe(recipe1, inventory.getMatrix())){
                recipe = recipe1;
                break;
            }
        }

        if(recipe == null) return;

        if(e.getClick().isShiftClick()){
            if(recipe instanceof ShapedComplexRecipe) {
                for (int i = 0; i < 9; i++) {
                    inventory.getMatrix()[i].setAmount(inventory.getMatrix()[i].getAmount() % ((ShapedComplexRecipe) recipe).getChoiceatIndex(i).getItemStack().getAmount());
                }
            } else {

            }
        } else {
            if(recipe instanceof ShapedComplexRecipe) {
                for (int i = 0; i < 9; i++) {
                    inventory.getMatrix()[i].setAmount(inventory.getMatrix()[i].getAmount() - ((ShapedComplexRecipe) recipe).getChoiceatIndex(i).getItemStack().getAmount());
                }
            } else {
                ShapelessComplexRecipe shapelessRecipe = ((ShapelessComplexRecipe) recipe);
                HashMap<ComplexItem, Integer> requiredItems = new HashMap<>();
                for (ComplexChoice choice : shapelessRecipe.getChoiceList()) {
                    requiredItems.computeIfPresent(((Map.Entry<ComplexItem, Integer>) choice.getChoices().entrySet().toArray()[0]).getKey(), ((complexItem, integer) -> integer + choice.getAmount()));
                    requiredItems.putIfAbsent(((Map.Entry<ComplexItem, Integer>) choice.getChoices().entrySet().toArray()[0]).getKey(), ((Map.Entry<ComplexItem, Integer>) choice.getChoices().entrySet().toArray()[0]).getValue());
                }

                for (int i = 0; i < 9; i++) {
                    ItemStack item = inventory.getMatrix()[i];
                    if(item == null) continue;
                    ComplexItem cItem = ComplexItemStack.of(item).getComplexItem();
                    if(requiredItems.get(cItem) <= 0) continue;

                    int amountToSubtract = Math.min(item.getAmount(), requiredItems.get(cItem));
                    item.setAmount(item.getAmount() - amountToSubtract);

                    requiredItems.replace(cItem, requiredItems.get(cItem) - amountToSubtract);
                }
            }
        }

    }
}
