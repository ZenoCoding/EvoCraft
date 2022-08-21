package me.zenox.superitems.items.superitems;

import com.archyx.aureliumskills.stats.Stats;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.VoidWarp;
import me.zenox.superitems.items.abilities.VoidularRecall;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class VoidScepter extends ComplexItem {
    public VoidScepter() {
        super("Void Scepter", "void_scepter", Rarity.EPIC, Type.STAFF, Material.NETHERITE_SHOVEL, Map.of(Stats.WISDOM, 50d), List.of(new VoidWarp(), new VoidularRecall()));

        List<String> lore = List.of(ChatColor.GRAY + "Forged from the souls of corrupted endermen.");

        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "superitems:attack_damage", 12, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setLore(lore);
        this.getMeta().setUnbreakable(true);

    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        return List.of();
    }

}
