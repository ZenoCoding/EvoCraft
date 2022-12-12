package me.zenox.superitems.recipe;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.zenox.superitems.item.ComplexItemStack;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;

public interface ComplexRecipe {
    List<ComplexRecipe> registeredRecipes = new ArrayList<>();
    Multimap<ComplexRecipe, ComplexRecipe> similarRecipeMap = ArrayListMultimap.create();

    NamespacedKey getKey();
    ComplexItemStack getResult();
    ComplexRecipe register();

    boolean similar(ComplexRecipe other);

}
