package me.zenox.superitems.network;

import com.comphenix.protocol.ProtocolManager;
import me.zenox.superitems.item.ComplexItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class GlowFilter extends ClientItemFilter{

    public GlowFilter(JavaPlugin plugin, ProtocolManager manager){
        super(plugin, manager);
    }

    @Override
    void filterItem(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(container.get(ComplexItem.GLOW_ID, PersistentDataType.INTEGER) == 1)
            meta.addEnchant(itemStack.getType() == Material.BOW ? Enchantment.PROTECTION_ENVIRONMENTAL : Enchantment.ARROW_INFINITE, 1, true);
        itemStack.setItemMeta(meta);
    }
}
