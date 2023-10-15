package me.zenox.evocraft.recipe;

import me.zenox.evocraft.EvoCraft;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapedRecipeBuilder {

    private String id;
    private List<String> shape;
    private ItemStack result;
    private Map<Character, RecipeChoice> choiceMap;

    public ShapedRecipeBuilder() {
        this.choiceMap = new HashMap<>();

    }

    public String getId() {
        return id;
    }

    public ShapedRecipeBuilder id(String id) {
        this.id = id;
        return this;
    }

    public List<String> getShape() {
        return shape;
    }

    public ShapedRecipeBuilder shape(String... shape) {
        if (shape.length != 3 || shape[0].length() != 3 || shape[1].length() != 3 || shape[2].length() != 3) {
            throw new IllegalArgumentException("Matrix RecipeBuilder#shape(shape) must be 3x3");
        }
        this.shape = List.of(shape);
        return this;
    }

    public ItemStack getResult() {
        return result;
    }

    public ShapedRecipeBuilder setResult(ItemStack result) {
        this.result = result;
        return this;
    }

    public Map<Character, RecipeChoice> getChoiceMap() {
        return choiceMap;
    }

    public ShapedRecipeBuilder choiceMap(Map<Character, RecipeChoice> choiceMap) {
        this.choiceMap = choiceMap;
        return this;
    }

    public ShapedRecipeBuilder addChoice(Character character, RecipeChoice choice) {
        this.choiceMap.put(character, choice);
        return this;
    }

    public ShapedRecipe build() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(EvoCraft.getPlugin(), id), result);
        recipe.shape(shape.get(0), shape.get(1), shape.get(2));
        for (Map.Entry<Character, RecipeChoice> entry : choiceMap.entrySet()) {
            recipe.setIngredient(entry.getKey(), entry.getValue());
        }
        return recipe;
    }
}
