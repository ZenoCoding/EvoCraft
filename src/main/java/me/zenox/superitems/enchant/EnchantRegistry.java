package me.zenox.superitems.enchant;

import me.zenox.superitems.Slot;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.util.QuadConsumer;
import me.zenox.superitems.util.Util;
import net.minecraft.world.item.Items;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class EnchantRegistry {

    public static ComplexEnchantment SYPHON = new AttackEnchantment("syphon", 5, List.of(ComplexItem.Type.SWORD, ComplexItem.Type.AXE), Slot.EITHER_HAND,  new ArrayList<>(), EnchantRegistry::syphonExecutable);

    // static function methods
    private static void syphonExecutable(Event e, Integer level, ItemStack item, Player p){
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, level));
        Util.sendMessage(p, "Used Enchantment Syphon!");
    }

    public static void registerEnchants(){
        Util.logToConsole(ChatColor.WHITE + "Registering " + ChatColor.GOLD + ComplexEnchantment.getRegisteredEnchants().size() + ChatColor.WHITE + " enchantments.");
    }
}
