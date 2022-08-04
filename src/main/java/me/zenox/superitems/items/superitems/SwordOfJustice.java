package me.zenox.superitems.items.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.BasicItem;
import me.zenox.superitems.items.SuperItem;
import me.zenox.superitems.items.abilities.Tarhelm;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class SwordOfJustice extends SuperItem implements Listener {

    public SwordOfJustice() {
        super("Sword of Justice", "sword_of_justice", Rarity.SPECIAL, Type.SWORD, Material.IRON_SWORD, false, 1, Map.of(Stats.STRENGTH, 30d), List.of());

        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Minecraft 1.8.9!");
        lore.add("");
        lore.add(ChatColor.GOLD + "Item Ability: Justice");
        lore.add(ChatColor.GRAY + "Gives " + ChatColor.RED + "Regneration II" + ChatColor.GRAY + " on hit.");
        this.getMeta().setLore(lore);
        this.getMeta().setUnbreakable(true);
        this.getMeta().addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "superitems:attack_speed", 100, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        this.getMeta().addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "superitems:attack_damage", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        Bukkit.getPluginManager().registerEvents(this, SuperItems.getPlugin());
    }

    @EventHandler
    public void onDamageEntityEvent(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            ItemStack item = p.getInventory().getItemInMainHand();
            BasicItem basicItem = SuperItems.getPlugin().registry.getBasicItemFromItemStack(item);
            if(!(basicItem == null) && basicItem.getId() == "sword_of_justice"){
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 1));
                p.playSound(p.getLocation(), Sound.ITEM_AXE_SCRAPE, 1, 1.4f);
                if(new Random().nextInt(3) == 0) e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation());
            }
        }
    }

    @Override
    public List<Recipe> getRecipes(List<BasicItem> registeredItems) {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        return List.of();
    }
}
