package me.zenox.superitems.enchant;

import me.zenox.superitems.Slot;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class EnchantRegistry {

    public static ComplexEnchantment SYPHON = new ComplexEnchantment("syphon", 5, List.of(ComplexItem.Type.SWORD), Slot.MAIN_HAND,  new ArrayList<>(), EnchantRegistry::syphonExecutable, EntityDamageByEntityEvent.class);

    // static function methods
    private static void syphonExecutable(Event e, Integer level){
        EntityDamageByEntityEvent event = ((EntityDamageByEntityEvent) e);
        ((Player) event.getDamager()).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,  50, 2));
    }

    public static void registerEnchants(){
        Util.logToConsole(ChatColor.WHITE + "Registering " + ChatColor.GOLD + ComplexEnchantment.getRegisteredEnchants().size() + ChatColor.WHITE + " enchantments.");
    }
}
