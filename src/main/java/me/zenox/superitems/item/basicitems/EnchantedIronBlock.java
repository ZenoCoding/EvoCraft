package me.zenox.superitems.item.basicitems;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnchantedIronBlock extends ComplexItem implements Listener {

    public EnchantedIronBlock() {
        super("Enchanted Iron Block", "enchanted_iron_block", Rarity.UNCOMMON, Type.MISC, Material.IRON_BLOCK, Map.of());
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Pure, solid metal. Radioactive?");
        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

        Bukkit.getPluginManager().registerEvents(this, SuperItems.getPlugin());
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("III", "III", "III");
        recipe.setIngredient('I', Material.IRON_BLOCK);
        ShapelessRecipe recipe2 = new ShapelessRecipe(new NamespacedKey(SuperItems.getPlugin(), "enchanted_iron_block_to_normal"), new ItemStack(Material.IRON_BLOCK, 9));
        recipe2.addIngredient(new RecipeChoice.ExactChoice(this.getItemStack(1)));
        return List.of(recipe);
    }
}
