package me.zenox.superitems.items.basicitems;


import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.ComplexItem;
import org.bukkit.*;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static me.zenox.superitems.items.ItemRegistry.CORRUPT_PEARL;

public class CorruptPearl extends ComplexItem implements Listener {

    public CorruptPearl() {
        super("Corrupt Pearl", "corrupt_pearl", Rarity.RARE, Type.MISC, Material.PLAYER_HEAD, Map.of());
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

        this.getMeta().setLore(List.of(ChatColor.GRAY + "A corrupt soul."));

        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDU0ZjJiYzNlY2Y1MDc1ZDBkZGU4YTRhNDgyZjVhZGEyMWQzYTY4NTZiYzdiZmFkNTFiYmJjMDAzYzg2YzU0MyJ9fX0=");

        Bukkit.getPluginManager().registerEvents(this, SuperItems.getPlugin());
    }

    @Override
    public List<Recipe> getRecipes() {
        return super.getRecipes();
    }

    @EventHandler
    public void dropEvent(EntityDeathEvent event){
        if(!(event.getEntity().getType() == EntityType.ENDERMAN)){
            return;
        }

        Random r = new Random();

        Enderman entity = ((Enderman) event.getEntity());
        World w = entity.getWorld();
        Location loc = entity.getLocation();

        if(r.nextDouble() < 0.01){

            Enderman specialEnderman = (Enderman) w.spawnEntity(loc.clone().add(Math.sin(r.nextDouble(2)*Math.PI)*r.nextDouble(2), 1, Math.sin(r.nextDouble(2)*Math.PI)*r.nextDouble(2)), EntityType.ENDERMAN);

            specialEnderman.setCarriedBlock(Material.END_PORTAL_FRAME.createBlockData());
            specialEnderman.setMetadata("corrupted", new FixedMetadataValue(SuperItems.getPlugin(), true));
        }

        List<MetadataValue> values = entity.getMetadata("corrupted");
        if (!values.isEmpty() && values.get(0).asBoolean() == true) {
            event.getDrops().removeIf((ItemStack item) -> item.getType().equals(Material.END_PORTAL_FRAME));
            w.dropItemNaturally(loc, CORRUPT_PEARL.getItemStack(1));
        }

    }

}
