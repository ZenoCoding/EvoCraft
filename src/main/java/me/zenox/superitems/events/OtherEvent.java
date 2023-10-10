package me.zenox.superitems.events;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.enchant.ComplexEnchantment;
import me.zenox.superitems.gui.EnchantingGUI;
import me.zenox.superitems.item.ComplexItemMeta;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.item.VanillaItem;
import me.zenox.superitems.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class OtherEvent implements Listener {

    private final SuperItems plugin;

    public OtherEvent(SuperItems plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void tileEntityInteract(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            switch(e.getClickedBlock().getType()){
                case ENCHANTING_TABLE -> new SimpleWindow(e.getPlayer(), "Enchantment Table", EnchantingGUI.getGui(e.getPlayer(), e.getClickedBlock()), true, true).show();
                default -> {
                    return;
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void projectileExplode(EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        List<MetadataValue> values = entity.getMetadata("dmgEnv");
        if (!values.isEmpty() && !values.get(0).asBoolean()) {
            if (entity instanceof Explosive explosive) {
                e.setCancelled(true);
                e.getEntity().remove();
                entity.getWorld().createExplosion(entity.getLocation(), explosive.getYield(), false, false, entity instanceof Projectile ? (Entity) ((Projectile) entity).getShooter() : entity);
            }
        }

        List<MetadataValue> kbValues = entity.getMetadata("knockback");
        if (!kbValues.isEmpty()) {
            for (Entity nearbyEntity : entity.getLocation().getWorld().getNearbyEntities(entity.getLocation(), 3, 3, 3)) {
                if (!Util.isInvulnerable(nearbyEntity)) nearbyEntity.setVelocity(nearbyEntity.getVelocity().add(nearbyEntity.getLocation().toVector().subtract(entity.getLocation().add(0, -0.3, 0).toVector()).normalize().multiply(kbValues.get(0).asInt())));
            }
        }
    }

    @EventHandler
    public void projectileCollide(ProjectileHitEvent e){
        Projectile entity = e.getEntity();
        if(Objects.isNull(entity)) return;
        if(Objects.isNull(e.getHitEntity())) return;
        List<MetadataValue> snowballValues = entity.getMetadata("super_snowball");
        if (!snowballValues.isEmpty()) {
            Entity hitEntity = e.getHitEntity();
            if (!Util.isInvulnerable(hitEntity)){
                hitEntity.setVelocity(hitEntity.getVelocity().add(hitEntity.getLocation().toVector().subtract(entity.getLocation().add(0, -0.3, 0).toVector()).normalize().multiply(0.5)));
                hitEntity.setFreezeTicks(Math.max(0, hitEntity.getFreezeTicks()) + 20);
                if (hitEntity instanceof Player player) player.damage(1);
            }

        }
    }

    @EventHandler
    public void fallingBlockLand(EntityChangeBlockEvent e) {
        Entity entity = e.getEntity();
        List<MetadataValue> values = entity.getMetadata("temporary");
        if (!values.isEmpty() && values.get(0).asBoolean()) {
            entity.remove();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        // Check if the item is not a vanilla item, if it isn't, cancel the event
        if (VanillaItem.byItem(item) == null) e.setCancelled(true);
    }

    @EventHandler
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent e){

        // Store a table of the last time all entities hit the player (in meta-data, so it isn't persistent)
        // For Dark Fury Ability
        e.getEntity().setMetadata("last_damaged", new FixedMetadataValue(SuperItems.getPlugin(), System.currentTimeMillis()));
    }

    @EventHandler
    public void villagerLevelUp(VillagerAcquireTradeEvent e){
        MerchantRecipe recipe = e.getRecipe();
        ItemStack stack = recipe.getResult();
        if(stack.getType().equals(Material.ENCHANTED_BOOK)) e.setCancelled(true);
//        ItemStack newStack = new ItemStack(Material.ENCHANTED_BOOK);
//        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) newStack.getItemMeta();
//        Enchantment enchantmentToApply = ComplexEnchantment.getRegisteredEnchants().get(new Random().nextInt(ComplexEnchantment.getRegisteredEnchants().size())).getVanillaEnchant();
//        if(enchantmentToApply == null) {
//            meta.addStoredEnchant(enchantmentToApply, 1, true);
//            newStack.setItemMeta(meta);
//        }
//        else newStack = new ItemStack(Material.BOOK);
//        // Clone the previous merchant recipe but with the newStack as a result
//        e.setRecipe(new MerchantRecipe(newStack, recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward(), recipe.getVillagerExperience(), recipe.getPriceMultiplier()));
    }

    // If a grindstone is used on an item, remove all ComplexEnchantments from it
    @EventHandler(priority= EventPriority.HIGHEST)
    public void grindstoneUse(PrepareResultEvent e){
        if (e.getResult() == null) return;
        ComplexItemStack resultStack = ComplexItemStack.of(e.getResult());
        ComplexItemMeta resultMeta = resultStack.getComplexMeta();
        if(e.getInventory().getType() == InventoryType.GRINDSTONE) {
            resultMeta.setComplexEnchantments(new HashMap<>());
            resultMeta.updateItem();
            e.setResult(resultStack.getItem());
        } else if(e.getInventory().getType() == InventoryType.ANVIL){
            AnvilInventory inventory = (AnvilInventory) e.getInventory();
            // Get the enchantmaps of both of the items in the AnvilInventory
            HashMap<ComplexEnchantment, Integer> firstEnchants = (HashMap<ComplexEnchantment, Integer>) ComplexItemStack.of(inventory.getFirstItem()).getComplexMeta().getComplexEnchants();
            HashMap<ComplexEnchantment, Integer> secondEnchants = (HashMap<ComplexEnchantment, Integer>) ComplexItemStack.of(inventory.getSecondItem()).getComplexMeta().getComplexEnchants();
            resultMeta.setComplexEnchantments(EnchantingGUI.combineEnchantMaps(firstEnchants, secondEnchants));
            resultMeta.updateItem();
            e.setResult(resultStack.getItem());

            plugin.getServer().getScheduler().runTask(plugin, () -> inventory.setRepairCost(0));
        }
    }


}
