package me.zenox.superitems.items;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Executable;
import me.zenox.superitems.util.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.*;

public class SuperItemRegistry{

    private SuperItems plugin;

    public HashMap<String, SuperItem> registeredItems = new HashMap();

    public SuperItemRegistry(SuperItems plugin){
        this.plugin = plugin;
        addItems();
    }

    private void addItems() {
        registeredItems.put("magic_toy_stick", getNewSuperItemInstance(new ToyStick()));
    }

    // UTIL
    private static SuperItem getNewSuperItemInstance(SuperItemInterface sii){
        return new SuperItem(sii.getName(), sii.getId(), sii.getRarity(), sii.getType(), sii.getItemAbility(), sii.getMaterial(),sii.getItemMeta(), sii.getRecipe());
    }

    @Nullable
    public static SuperItem getSuperItemFromItemStack(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        String id = container.get(SuperItem.GLOBAL_ID, PersistentDataType.STRING);
        return getSuperItemFromId(id);
    }

    public static SuperItem getSuperItemFromId(String id) {
        for (Map.Entry<String, SuperItem> entry : SuperItems.registry.registeredItems.entrySet()) {
            if (id == null) return null;
            if (id.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    //Gets the class of the item
    private class ToyStick implements SuperItemInterface{

        @Override
        public String getName() {
            return "Magic Toy Stick";
        }

        @Override
        public String getId() {
            return "magic_toy_stick";
        }

        @Override
        public SuperItem.Rarity getRarity() {
            return SuperItem.Rarity.LEGENDARY;
        }

        @Override
        public SuperItem.Type getType() {
            return SuperItem.Type.SUPERITEM;
        }

        @Override
        public ItemMeta getItemMeta() {
            ItemMeta itemmeta = new ItemStack(Material.STICK).getItemMeta();
            List<String> lore = new ArrayList();
            lore.add(ChatColor.RED + "" + ChatColor.ITALIC + "Magical.");
            itemmeta.setLore(lore);
            itemmeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
            itemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            return itemmeta;
        }

        @Override
        public Material getMaterial() {
            return Material.STICK;
        }

        @Override
        public ItemAbility getItemAbility() {
            return new ItemAbility("Magic Missile", getItemAbilityLore(), getItemAbilityExecutable());
        }

        @Override
        public List<String> getItemAbilityLore() {
            List<String> abilityLore = new ArrayList<>();
            abilityLore.add(ChatColor.GRAY + "Shoots a magic missile that explodes");
            abilityLore.add(ChatColor.GRAY + "on impact and deals massive" + ChatColor.RED + " damage.");
            abilityLore.add("");
            abilityLore.add(ChatColor.GRAY + "33% chance for the item to " + ChatColor.GOLD + "combust " + ChatColor.GRAY + "and dissapear.");
            return abilityLore;
        }

        @Override
        public Executable getItemAbilityExecutable() {
            Executable exec = (PlayerInteractEvent e) -> {
                if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    Random r = new Random();
                    Player p = e.getPlayer();
                    World w = p.getWorld();

                    // 50% chance to remove an item from their hand
                    if (r.nextInt(3) == 0) {
                        e.getItem().setAmount(e.getItem().getAmount() - 1);
                        Util.sendMessage(p, ChatColor.GOLD + "Woah! Your " + ChatColor.ITALIC + "Magic Toy Stick " + ChatColor.GOLD + "combusted in your hand!", false);
                        p.damage(5, p);
                        p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 0.5F);
                    }


                    Trident trident = (Trident) w.spawnEntity(p.getLocation().add(0, 1.8, 0), EntityType.TRIDENT);
                    Vector v = p.getLocation().getDirection().normalize().clone();
                    Vector v2 = v.multiply(3);
                    trident.setVelocity(v2);
                    trident.setDamage(0);
                    trident.setGravity(false);
                    trident.setPierceLevel(127);

                    int explosionPower = 5;

                    new BukkitRunnable() {
                        int count = 0;

                        @Override
                        public void run() {
                            trident.setVelocity(v2);
                            Location loc = trident.getLocation();
                            for (Entity entity :
                                    trident.getNearbyEntities(2, 2, 2)) {
                                if (entity instanceof Damageable && !entity.equals(p)) {
                                    ((Damageable) entity).damage(explosionPower / 2, p);
                                }
                            }
                            for (int i = 0; i < 5; i++) {
                                w.spawnParticle(Particle.ELECTRIC_SPARK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                                w.spawnParticle(Particle.REVERSE_PORTAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                                w.spawnParticle(Particle.ELECTRIC_SPARK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                                w.spawnParticle(Particle.REVERSE_PORTAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                                w.spawnParticle(Particle.END_ROD, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                                w.spawnParticle(Particle.SMOKE_NORMAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                            }
                            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
                            com.sk89q.worldedit.util.Location guardLoc = BukkitAdapter.adapt(trident.getLocation());
                            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                            RegionQuery query = container.createQuery();

                            if (!query.testState(guardLoc, localPlayer, Flags.BUILD)) {
                                trident.remove();
                                Util.sendMessage(p, "You cannot use this item in a worldguard region!");
                                cancel();
                            }

                            if (trident.isInBlock()) {
                                trident.remove();
                                List<Block> blocks = getNearbyBlocks(loc, explosionPower / 2, explosionPower / 4);
                                for (Block block : blocks) {
                                    if (block.getType().getBlastResistance() > 1200 || block.getType().equals(Material.PLAYER_HEAD) || block.getType().equals(Material.PLAYER_WALL_HEAD))
                                        continue;
                                    com.sk89q.worldedit.util.Location guardLoc2 = BukkitAdapter.adapt(block.getLocation());

                                    if (!query.testState(guardLoc2, localPlayer, Flags.BUILD)) {
                                        continue;
                                    }
                                    FallingBlock fallingBlock = w.spawnFallingBlock(block.getLocation(), block.getBlockData());
                                    fallingBlock.setVelocity(fallingBlock.getLocation().toVector().subtract(loc.toVector()).multiply(explosionPower * 2).normalize());
                                    fallingBlock.setDropItem(false);
                                    fallingBlock.setHurtEntities(true);
                                    block.breakNaturally();
                                }
                                try {
                                    trident.remove();
                                } catch (Exception e) {

                                }
                                w.createExplosion(loc, explosionPower);
                                cancel();
                            }
                            count++;
                            if (count >= 600) {
                                trident.remove();
                                cancel();
                            }

                        }
                    }.runTaskTimer(SuperItems.getPlugin(), 0, 2);
                }
            };
            return exec;
        }

        private List<Block> getNearbyBlocks(Location loc, int radius, int yradius) {
            List<Block> nearbyBlocks = new ArrayList();
            for (int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; x++) {
                for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; z++) {
                    for (int y = loc.getBlockY() + yradius; y >= loc.getBlockY() - yradius; y--) {
                        nearbyBlocks.add(loc.getWorld().getBlockAt(x, y, z));
                    }
                }
            }
            return nearbyBlocks;
        }

        @Override
        public ShapedRecipe getRecipe() {
            NamespacedKey key = new NamespacedKey(SuperItems.getPlugin(), getId());
            ShapedRecipe recipe = new ShapedRecipe(key, getSuperItemFromId(getId()).getItemStack(3));
            recipe.shape("DGD","FSF","TST");
            recipe.setIngredient('D', Material.DRAGON_BREATH);
            recipe.setIngredient('G', Material.GLOWSTONE_DUST);
            recipe.setIngredient('F', Material.FIRE_CHARGE);
            recipe.setIngredient('S', Material.DEBUG_STICK);
            recipe.setIngredient('T', Material.TNT);
            return recipe;
        }
    }
}
