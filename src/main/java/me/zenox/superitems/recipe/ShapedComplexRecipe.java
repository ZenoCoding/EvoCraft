package me.zenox.superitems.recipe;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapedComplexRecipe implements ComplexRecipe{

    private NamespacedKey id;
    private List<String> shape;
    private ComplexItemStack result;
    private Map<Character, ComplexChoice> choiceMap;

    public ShapedComplexRecipe() {
        this.choiceMap = new HashMap<>();
    }

    public NamespacedKey getKey() {
        return id;
    }

    public ShapedComplexRecipe id(String id) {
        this.id = new NamespacedKey(SuperItems.getPlugin(), id);
        return this;
    }

    public List<String> getShape() {
        return shape;
    }

    public ShapedComplexRecipe shape(String... shape) {
        if (shape.length != 3 || shape[0].length() != 3 || shape[1].length() != 3 || shape[2].length() != 3) {
            throw new IllegalArgumentException("Matrix shape in RecipeBuilder#shape(shape) must be size 3x3");
        }
        this.shape = List.of(shape);
        return this;
    }

    public ComplexItemStack getResult() {
        return result;
    }

    public ShapedComplexRecipe setResult(ComplexItemStack result) {
        this.result = result;
        return this;
    }

    public Map<Character, ComplexChoice> getChoiceMap() {
        return choiceMap;
    }

    public ShapedComplexRecipe choiceMap(Map<Character, ComplexChoice> choiceMap) {
        this.choiceMap = choiceMap;
        return this;
    }

    public ShapedComplexRecipe addChoice(Character character, ComplexChoice choice) {
        this.choiceMap.put(character, choice);
        return this;
    }

    public ComplexRecipe register() {
        // Register vanilla recipe
        ShapedRecipe recipe = new ShapedRecipe(id, result.clone().getItem());
        recipe.shape(shape.get(0), shape.get(1), shape.get(2));
        for (Map.Entry<Character, ComplexChoice> entry : choiceMap.entrySet()) {
            recipe.setIngredient(entry.getKey(), entry.getValue().getItemStack().getType());
        }
        try {
            Bukkit.addRecipe(recipe);
        } catch (IllegalStateException e) {
            if (recipe instanceof Keyed) {
                Bukkit.removeRecipe(((Keyed) recipe).getKey());
                Bukkit.addRecipe(recipe);
            }
        }

        // Check for "similar" recipes
        // "similar" recipes are recipes that have the same minecraft representation, but have different complex representation - this helps build a cache of all of those relationships
        for (ComplexRecipe recipe1 : registeredRecipes) {
            if(recipe1 instanceof ShapedComplexRecipe && similar(recipe1)) {
                similarRecipeMap.put(this, recipe1);
                similarRecipeMap.put(recipe1, this);
            }
        }

        // add to registry
        registeredRecipes.add(this);
        return this;
    }

    public ComplexChoice getChoiceatIndex(int index){
        return choiceMap.get(shape.get((index - index % 3) / 3).charAt(index % 3));
    }

    @Override
    public boolean similar(ComplexRecipe otherRecipe) {
        if(!(otherRecipe instanceof ShapedComplexRecipe)) return false;
        ShapedComplexRecipe other = ((ShapedComplexRecipe) otherRecipe);
        for (int i = 0;i < 3;i++){
            for (int j = 0;j < 3;j++){
                ComplexChoice thisChoice = choiceMap.get(shape.get(i).charAt(j));
                ComplexChoice otherChoice = other.choiceMap.get(other.shape.get(i).charAt(j));
                if(thisChoice == null && otherChoice == null) continue;
                if(thisChoice != null && otherChoice == null) return false;
                if(thisChoice == null && otherChoice != null) return false;
                if (thisChoice.getItemStack().getType() !=
                        otherChoice.getItemStack().getType()) return false;
            }
        }
        return true;
    }

}
