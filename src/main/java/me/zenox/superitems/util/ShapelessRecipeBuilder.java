package me.zenox.superitems.util;

import me.zenox.superitems.SuperItems;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapelessRecipeBuilder {

    private String id;
    private ItemStack result;
    private List<RecipeChoice> choiceList;

    public ShapelessRecipeBuilder(){
        this.choiceList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public ShapelessRecipeBuilder id(String id) {
        this.id = id;
        return this;
    }

    public ItemStack getResult() {
        return result;
    }

    public ShapelessRecipeBuilder setResult(ItemStack result) {
        this.result = result;
        return this;
    }

    public List<RecipeChoice> getChoiceList() {
        return choiceList;
    }

    public ShapelessRecipeBuilder choiceList(List<RecipeChoice> choiceMap) {
        this.choiceList = choiceMap;
        return this;
    }

    public ShapelessRecipeBuilder addChoice(RecipeChoice choice){
        this.choiceList.add(choice);
        return this;
    }

    public ShapelessRecipe build(){
        ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(SuperItems.getPlugin(), id), result);
        for(RecipeChoice choice : choiceList){
            recipe.addIngredient(choice);
        }
        return recipe;
    }
}
