package me.zenox.superitems.items;

import me.zenox.superitems.util.Executable;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public interface SuperItemInterface {
    //"Magic Toy Stick", "magic_toy_stick", SuperItem.Rarity.LEGENDARY, SuperItem.Type.SUPERITEM, new ItemAbility("Magic Missile", ToyStick.generateToyStickAbilityLore(), ToyStick.getToyStickExecutable()), Material.STICK, ToyStick.getToyStickItemMeta(), ToyStick.getToyStickRecipe()
    String getName();
    String getId();
    SuperItem.Rarity getRarity();
    SuperItem.Type getType();
    ItemAbility getItemAbility();
    ItemMeta getItemMeta();
    Material getMaterial();
    List<String> getItemAbilityLore();
    Executable getItemAbilityExecutable();
    //ShapedRecipe getRecipe();
}
