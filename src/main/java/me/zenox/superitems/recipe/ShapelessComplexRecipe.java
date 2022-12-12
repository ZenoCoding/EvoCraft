package me.zenox.superitems.recipe;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.item.VanillaItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;

public class ShapelessComplexRecipe implements ComplexRecipe{

    private NamespacedKey id;
    private ComplexItemStack result;
    private List<ComplexChoice> choiceList = new ArrayList<>();

    public ShapelessComplexRecipe() {
    }

    public NamespacedKey getKey() {
        return id;
    }

    public ShapelessComplexRecipe id(String id) {
        this.id = new NamespacedKey(SuperItems.getPlugin(), id);
        return this;
    }

    public ComplexItemStack getResult() {
        return result;
    }

    public ShapelessComplexRecipe setResult(ComplexItemStack result) {
        this.result = result;
        return this;
    }

    public ShapelessComplexRecipe setResult(Material material, int amount) {
        this.result = new ComplexItemStack(VanillaItem.of(material), amount);
        return this;
    }

    public List<ComplexChoice> getChoiceList() {
        return choiceList;
    }

    public ShapelessComplexRecipe choiceList(List<ComplexChoice> choiceMap) {
        this.choiceList = choiceMap;
        return this;
    }

    public ShapelessComplexRecipe addChoice(ComplexChoice choice) {
        this.choiceList.add(choice);
        return this;
    }

    @Override
    public ComplexRecipe register() {
        // add to registry
        registeredRecipes.add(this);
        return this;
    }


    public boolean similar(ComplexRecipe other) {
        if(!(other instanceof ShapelessComplexRecipe)) return false;
        ShapelessComplexRecipe otherRecipe = ((ShapelessComplexRecipe) other);
        if(otherRecipe.choiceList.size() != choiceList.size()) return false;
        for (int i = 0; i < otherRecipe.choiceList.size(); i++) {
            if(choiceList.get(i).getItemStack().getType() != otherRecipe.choiceList.get(i).getItemStack().getType()) return false;
        }
        return true;
    }
}
