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

import static me.zenox.superitems.util.Util.getNearbyBlocks;

public class SuperItemRegistry{

    private SuperItems plugin;

    private HashMap<String, SuperItem> registeredItems = new HashMap();

    public SuperItemRegistry(SuperItems plugin){
        this.plugin = plugin;
        ToyStick toyStick = new ToyStick();
        SoulCrystal soulCrystal = new SoulCrystal();
        addItems(toyStick, soulCrystal);
        addRecipes(toyStick.getRecipe());
    }

    private void addItems(SuperItemInterface ... items) {
        for (SuperItemInterface item: items) {
            registeredItems.put(item.getId(), getNewSuperItemInstance(item));
        }
    }

    private void addRecipes(ShapedRecipe ... recipes){
        for (ShapedRecipe recipe :
                recipes) {
            try{
                Bukkit.addRecipe(recipe);
            } catch (IllegalStateException e){
                Util.logToConsole("Found duplicate recipe, re-adding.");
                Bukkit.removeRecipe(recipe.getKey());
                Bukkit.addRecipe(recipe);
            }
        }

    }

    // UTIL
    private static SuperItem getNewSuperItemInstance(SuperItemInterface sii){
        return new SuperItem(sii.getName(), sii.getId(), sii.getRarity(), sii.getType(), sii.getItemAbility(), sii.getMaterial(),sii.getItemMeta());
    }

    @Nullable
    public SuperItem getSuperItemFromItemStack(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        String id = container.get(SuperItem.GLOBAL_ID, PersistentDataType.STRING);
        return this.getSuperItemFromId(id);
    }

    public SuperItem getSuperItemFromId(String id) {
        for (Map.Entry<String, SuperItem> entry : this.registeredItems.entrySet()) {
            if (id == null) return null;
            if (id.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public HashMap<String, SuperItem> getRegisteredItems() {
        return registeredItems;
    }

    public void setRegisteredItems(HashMap<String, SuperItem> registeredItems) {
        this.registeredItems = registeredItems;
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
            return SuperItem.Rarity.EPIC;
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

            List<Action> actions = new ArrayList<>();
            actions.add(Action.RIGHT_CLICK_AIR);
            actions.add(Action.RIGHT_CLICK_BLOCK);
            return new ItemAbility("Magic Missile", "magic_missile", getItemAbilityLore(), actions, getItemAbilityExecutable(), 0);
        }

        @Override
        public List<String> getItemAbilityLore() {
            List<String> abilityLore = new ArrayList<>();
            abilityLore.add(ChatColor.GRAY + "Shoots a magic missile that explodes");
            abilityLore.add(ChatColor.GRAY + "on impact and deals massive" + ChatColor.RED + " damage.");
            abilityLore.add("");
            abilityLore.add(ChatColor.GRAY + "20% chance for the item to " + ChatColor.GOLD + "combust " + ChatColor.GRAY + "and dissapear.");
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
                    if (r.nextInt(5) == 0) {
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

                    int explosionPower = 4;

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

                            LocalPlayer localPlayer;
                            com.sk89q.worldedit.util.Location guardLoc;
                            RegionContainer container;
                            RegionQuery query;
                            if(plugin.isUsingWorldGuard) {
                                localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
                                guardLoc = BukkitAdapter.adapt(trident.getLocation());
                                container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                                query = container.createQuery();

                                if (!query.testState(guardLoc, localPlayer, Flags.BUILD)) {
                                    trident.remove();
                                    Util.sendMessage(p, "You cannot use this item in a worldguard region!");
                                    cancel();
                                }
                            }

                            if (trident.isInBlock()) {
                                trident.remove();
                                List<Block> blocks = getNearbyBlocks(loc, explosionPower / 2, explosionPower / 4);
                                for (Block block : blocks) {
                                    if (block.getType().getBlastResistance() > 1200 || block.getType().equals(Material.PLAYER_HEAD) || block.getType().equals(Material.PLAYER_WALL_HEAD))
                                        continue;
                                    if(plugin.isUsingWorldGuard) {
                                        localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
                                        guardLoc = BukkitAdapter.adapt(block.getLocation());
                                        container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                                        query = container.createQuery();

                                        if (!query.testState(guardLoc, localPlayer, Flags.BUILD)) {
                                            continue;
                                        }
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

    private class SoulCrystal implements SuperItemInterface{

        @Override
        public String getName() {
            return "Soul Crystal";
        }

        @Override
        public String getId() {
            return "soul_crystal";
        }

        @Override
        public SuperItem.Rarity getRarity() {
            return SuperItem.Rarity.LEGENDARY;
        }

        @Override
        public SuperItem.Type getType() {
            return SuperItem.Type.DEPLOYABLE;
        }

        @Override
        public ItemAbility getItemAbility() {
            List<Action> actions = new ArrayList<>();
            actions.add(Action.RIGHT_CLICK_AIR);
            actions.add(Action.RIGHT_CLICK_BLOCK);

            return new ItemAbility("Soul Rift", "soul_rift", getItemAbilityLore(), actions,getItemAbilityExecutable(), 45);
        }

        @Override
        public ItemMeta getItemMeta() {
            ItemStack item = new ItemStack(Material.END_CRYSTAL);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "The void-Sphere of Life was an anti-magic device, which could absorb the");
            lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "special form of magic in the player. It could not be destroyed by magic");
            lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "weapons. The soul crystal was left by an evil spirit of a mighty elf in that");
            lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "sphere. When you wore the orb, your magic power would be raised, while");
            lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "you could control people and objects with you. The soul crystal had a");
            lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "maximum energy level of 3. There are a few Dark elves whose souls were");
            lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "shattered by the sphere, and they tried to absorb the magic from its");
            lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "remains, but they are too weak and starved and died.");
            lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "");
            lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Generated at: https://app.inferkit.com/demo");
            meta.setLore(lore);
            return meta;
        }

        @Override
        public Material getMaterial() {
            return Material.END_CRYSTAL;
        }

        @Override
        public List<String> getItemAbilityLore() {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Deploys a" + ChatColor.AQUA + " soul crystal" + ChatColor.GRAY + ", which lasts for" + ChatColor.GREEN + " 5s" + ChatColor.GRAY + " and");
            lore.add(ChatColor.GRAY + "pulls entities in, dealing " + ChatColor.RED + "6 true damage" + ChatColor.GRAY + " per second.");
            return lore;
        }

        @Override
        public Executable getItemAbilityExecutable() {
            Executable exec = (PlayerInteractEvent e) -> {
                Action action = e.getAction();
                Player p = e.getPlayer();
                World w = p.getWorld();
                Location loc;
                if(action.equals(Action.RIGHT_CLICK_BLOCK)){
                    loc = e.getClickedBlock().getLocation();
                } else if (action.equals(Action.RIGHT_CLICK_AIR)){
                    loc = p.getLocation();
                } else {
                    return;
                }

                EnderCrystal crystal = (EnderCrystal) w.spawnEntity(loc.add(0, 2, 0), EntityType.ENDER_CRYSTAL);
                crystal.setInvulnerable(true);

                new BukkitRunnable(){
                    int count = 0;
                    List<Block> blocks = getNearbyBlocks(crystal.getLocation(), 7, 2);
                    List<FallingBlock> fBlocks = new ArrayList<>();
                    List<LivingEntity> entities = new ArrayList<>();

                    @Override
                    public void run() {
                        // At 15 seconds

                        if(count == 100){
                            cancel();
                            crystal.remove();
                            w.createExplosion(crystal.getLocation(), 3);
                            for (FallingBlock fallingblock: fBlocks) {
                                fallingblock.setGravity(true);
                            }
                            for (LivingEntity entity : entities){
                                entity.setGravity(true);
                            }

                        }

                        Random r = new Random();
                        Block block = blocks.get(r.nextInt(blocks.size() - 0) + 0);
                        Boolean allowed = true;

                        if(plugin.isUsingWorldGuard){
                            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
                            com.sk89q.worldedit.util.Location guardLoc = BukkitAdapter.adapt(block.getLocation());
                            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                            RegionQuery query = container.createQuery();
                            allowed = query.testState(guardLoc, localPlayer, Flags.BUILD);
                        }
                        if (!(block.getType().getBlastResistance() > 1200 || block.getType().equals(Material.PLAYER_HEAD) || block.getType().equals(Material.PLAYER_WALL_HEAD)) && allowed) {
                            FallingBlock fBlock = w.spawnFallingBlock(block.getLocation(), block.getBlockData());
                            fBlock.setVelocity((fBlock.getLocation().toVector().subtract(crystal.getLocation().toVector()).multiply(-10).normalize()));
                            fBlock.setGravity(false);
                            fBlock.setDropItem(false);
                            fBlock.setHurtEntities(true);

                            fBlocks.add(fBlock);
                        }

                        for (FallingBlock fallingBlock: fBlocks) {
                            if(r.nextInt(count/25+1) == 0){
                                fallingBlock.setVelocity((fallingBlock.getLocation().toVector().subtract(crystal.getLocation().add(r.nextDouble()-0.5, r.nextDouble()-0.5+2, r.nextDouble()-0.5).toVector()).multiply(-0.5).normalize()));
                            }
                        }

                        //TODO: Uncomment to allow deployer to bypass
                        for (LivingEntity entity: entities) {
                            if(r.nextInt(count/25+1) == 0 && !entity.equals(p)){
                                entity.setVelocity((entity.getLocation().toVector().subtract(crystal.getLocation().add(r.nextDouble()-0.5, r.nextDouble()-0.5+2, r.nextDouble()-0.5).toVector()).multiply(-0.5).normalize()));
                                entity.damage((entity.getHealth()*(2/3))/15+1);
                            }
                        }


                        if (count == 0) {
                            for (Entity entity : crystal.getNearbyEntities(7.5, 5, 7.5)) {
                                if (entity instanceof LivingEntity && !entity.equals(p)) {
                                    entity.setVelocity((entity.getLocation().toVector().subtract(crystal.getLocation().add(0, 2, 0).toVector()).multiply(-10).normalize()));
                                    entity.setGravity(false);
                                    entities.add((LivingEntity) entity);
                                }
                            }
                        }

                        count++;
                    }
                }.runTaskTimer(SuperItems.getPlugin(), 0, 0);


            };
            return exec;
        }
    }
}
