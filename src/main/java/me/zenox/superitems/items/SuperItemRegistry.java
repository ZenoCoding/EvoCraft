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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.*;

import static me.zenox.superitems.util.Util.getNearbyBlocks;

public class SuperItemRegistry{

    private SuperItems plugin;

    private HashMap<String, BasicItem> registeredItems = new HashMap();

    public SuperItemRegistry(SuperItems plugin){
        this.plugin = plugin;
        ToyStick toyStick = new ToyStick();
        SoulCrystal soulCrystal = new SoulCrystal();
        addSuperItems(toyStick, soulCrystal, new FieryEmberStaff(),new DarkEmberStaff());
        addBasicItems(new GardenerSapling(new String[]{"bobderaa238", ChatColor.RED + "[ADMIN] Zqnqx", "Friday, July 22"}));
        addRecipes(toyStick.getRecipe());
    }

    private void addBasicItems(BasicItemInterface ... items) {
        for (BasicItemInterface item: items) {
            registeredItems.put(item.getId(), getNewBasicItemInstance(item));
        }
    }

    private void addSuperItems(SuperItemInterface ... items) {
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
    private static BasicItem getNewBasicItemInstance(BasicItemInterface bii){
        return new BasicItem(bii.getName(), bii.getId(), bii.getRarity(), bii.getType(), bii.getMaterial(), bii.getItemMeta());
    }

    private static SuperItem getNewSuperItemInstance(SuperItemInterface sii){
        return new SuperItem(sii.getName(), sii.getId(), sii.getRarity(), sii.getType(), sii.getItemAbilities(), sii.getMaterial(),sii.getItemMeta());
    }

    @Nullable
    public BasicItem getBasicItemFromItemStack(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        String id = container.get(SuperItem.GLOBAL_ID, PersistentDataType.STRING);
        return this.getBasicItemFromId(id);
    }

    public BasicItem getBasicItemFromId(String id) {
        for (Map.Entry<String, BasicItem> entry : this.registeredItems.entrySet()) {
            if (id == null) return null;
            if (id.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public HashMap<String, BasicItem> getRegisteredItems() {
        return registeredItems;
    }

    private class GardenerSapling implements BasicItemInterface{

        private String issuedTo;
        private String issuedFrom;
        private String date;

        private GardenerSapling(String ... args){
            this.issuedTo = args[0];
            this.issuedFrom = args[1];
            this.date = args[2];
        }

        private GardenerSapling(){
            this.issuedTo = "N/A";
            this.issuedFrom = "N/A";
            this.date = "N/A";
        }

        @Override
        public String getName() {
            return "Gardener's Sapling";
        }

        @Override
        public String getId() {
            return "gardener_sapling";
        }

        @Override
        public BasicItem.Rarity getRarity() {
            return BasicItem.Rarity.VERY_SPECIAL;
        }

        @Override
        public SuperItem.Type getType() {
            return BasicItem.Type.MISC;
        }

        @Override
        public ItemMeta getItemMeta() {
            ItemStack item = new ItemStack(Material.OAK_SAPLING);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "A sapling, that can when cared for");
            lore.add(ChatColor.GRAY + "can grow into something beautiful.");
            lore.add("");
            lore.add(ChatColor.GRAY + "A thanks to the beta testers who helped along the way.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Issued to: " + ChatColor.YELLOW + this.issuedTo);
            lore.add(ChatColor.GRAY + "Issued From: " + ChatColor.YELLOW + this.issuedFrom);
            lore.add(ChatColor.GRAY + "Date Issued: " + ChatColor.YELLOW + this.date);
            meta.setLore(lore);
            meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            //meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            return null;
        }

        @Override
        public Material getMaterial() {
            return Material.OAK_SAPLING;
        }
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
        public BasicItem.Rarity getRarity() {
            return BasicItem.Rarity.EPIC;
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
        public List<ItemAbility> getItemAbilities() {
            List<ItemAbility> abilities = new ArrayList<>();
            abilities.add(new ItemAbility("Magic Missile", "magic_missile", getItemAbilityLore(), ItemAbility.AbilityAction.RIGHT_CLICK_ALL, getItemAbilityExecutable(), 0, 0));
            return abilities;
        }

        public List<String> getItemAbilityLore() {
            List<String> abilityLore = new ArrayList<>();
            abilityLore.add(ChatColor.GRAY + "Shoots a magic missile that explodes");
            abilityLore.add(ChatColor.GRAY + "on impact and deals massive" + ChatColor.RED + " damage.");
            abilityLore.add("");
            abilityLore.add(ChatColor.GRAY + "20% chance for the item to " + ChatColor.GOLD + "combust " + ChatColor.GRAY + "and dissapear.");
            return abilityLore;
        }

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

                    int explosionPower = 7;

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
            ShapedRecipe recipe = new ShapedRecipe(key, ((SuperItem) getBasicItemFromId(getId())).getItemStack(3));
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
        public BasicItem.Rarity getRarity() {
            return BasicItem.Rarity.LEGENDARY;
        }

        @Override
        public SuperItem.Type getType() {
            return SuperItem.Type.DEPLOYABLE;
        }

        @Override
        public List<ItemAbility> getItemAbilities() {
            List<ItemAbility> abilities = new ArrayList<>();
            abilities.add(new ItemAbility("Soul Rift", "soul_rift", getItemAbilityLore(), ItemAbility.AbilityAction.RIGHT_CLICK_ALL, getItemAbilityExecutable(), 30, 30));

            return abilities;
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

        public List<String> getItemAbilityLore() {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Deploys a" + ChatColor.AQUA + " soul crystal" + ChatColor.GRAY + ", which lasts for" + ChatColor.GREEN + " 5s" + ChatColor.GRAY + " and");
            lore.add(ChatColor.GRAY + "pulls entities in, dealing " + ChatColor.RED + "6 true damage" + ChatColor.GRAY + " per second.");
            return lore;
        }

        public Executable getItemAbilityExecutable() {
            Executable exec = (PlayerInteractEvent e) -> {
                Action action = e.getAction();
                Player p = e.getPlayer();
                World w = p.getWorld();
                Location loc;
                boolean allowed = true;
                if(plugin.isUsingWorldGuard){
                    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
                    com.sk89q.worldedit.util.Location guardLoc = BukkitAdapter.adapt(p.getLocation());
                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionQuery query = container.createQuery();
                    allowed = query.testState(guardLoc, localPlayer, Flags.BUILD);
                }
                if(!allowed){
                    Util.sendMessage(p, "You cannot use this item in a worldguard region!");
                    return;
                }

                if(action.equals(Action.RIGHT_CLICK_BLOCK)){
                    loc = e.getClickedBlock().getLocation();
                } else if (action.equals(Action.RIGHT_CLICK_AIR)){
                    loc = p.getLocation();
                } else {
                    return;
                }

                e.setCancelled(true);

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
                                boolean allowed = true;
                                if(plugin.isUsingWorldGuard){
                                    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
                                    com.sk89q.worldedit.util.Location guardLoc = BukkitAdapter.adapt(crystal.getLocation());
                                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                                    RegionQuery query = container.createQuery();
                                    allowed = query.testState(guardLoc, localPlayer, Flags.BUILD);
                                }
                                if(!allowed){
                                    fallingblock.remove();
                                } else {
                                    fallingblock.setGravity(true);
                                }
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

                            block.breakNaturally();

                            fBlocks.add(fBlock);
                        }

                        for (FallingBlock fallingBlock: fBlocks) {
                            if(r.nextInt(count/25+1) == 0){
                                fallingBlock.setVelocity((fallingBlock.getLocation().toVector().subtract(crystal.getLocation().add(r.nextDouble()-0.5, r.nextDouble()-0.5+2, r.nextDouble()-0.5).toVector()).multiply(-0.5).normalize()));
                            }
                        }

                        for (LivingEntity entity: entities) {
                            if(r.nextInt(count/25+1) == 0 && !entity.equals(p)){
                                entity.setVelocity((entity.getLocation().toVector().subtract(crystal.getLocation().add(r.nextDouble()-0.5, r.nextDouble()-0.5+2, r.nextDouble()-0.5).toVector()).multiply(-0.5).normalize()));
                                entity.damage((entity.getHealth()*(2/3))/10+1);
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

    private class FieryEmberStaff implements SuperItemInterface{

        @Override
        public String getName() {
            return "Fiery Ember Staff";
        }

        @Override
        public String getId() {
            return "fiery_ember_staff";
        }

        @Override
        public BasicItem.Rarity getRarity() {
            return BasicItem.Rarity.UNCOMMON;
        }

        @Override
        public SuperItem.Type getType() {
            return BasicItem.Type.STAFF;
        }

        @Override
        public List<ItemAbility> getItemAbilities() {
            List<ItemAbility> abilities = new ArrayList<>();
            abilities.add(new ItemAbility("Fiery Embers", "fiery_ember_shoot", getAbilityLore(1), ItemAbility.AbilityAction.RIGHT_CLICK_ALL, getShootExecutable(), 5, 0));
            return abilities;
        }

        private Executable getShootExecutable() {
            Executable exec = (PlayerInteractEvent e) -> {
                Player p = e.getPlayer();
//                World w = p.getWorld();
//                Location loc;
//
//                boolean allowed = true;
//                if(plugin.isUsingWorldGuard){
//                    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
//                    com.sk89q.worldedit.util.Location guardLoc = BukkitAdapter.adapt(p.getLocation());
//                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
//                    RegionQuery query = container.createQuery();
//                    allowed = query.testState(guardLoc, localPlayer, Flags.BUILD);
//                }
//                if(!allowed){
//                    Util.sendMessage(p, "You cannot use this item in a worldguard region!");
//                    return;
//                }

                Location eyeLoc = p.getEyeLocation();

                Fireball f = (Fireball) eyeLoc.getWorld().spawnEntity(eyeLoc.add(eyeLoc.getDirection()), EntityType.FIREBALL);
                f.setVelocity(eyeLoc.getDirection().normalize());
                f.setMetadata("dmgEnv", new FixedMetadataValue(plugin, false));
                f.setYield(1f);


            };
            return exec;
        }

        private List<String> getAbilityLore(int arg){
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Shoot a " + ChatColor.RED + "fireball" + ChatColor.GRAY + " that explodes");
            lore.add(ChatColor.GRAY + "on impact. Fiery Explosions!");
            return lore;
        }

        @Override
        public ItemMeta getItemMeta() {
            ItemStack item = new ItemStack(getMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "A fiery rod, taken from " + ChatColor.RED + "Blaziel" + ChatColor.GRAY + " himself.");
            meta.setLore(lore);


            return meta;
        }


        @Override
        public Material getMaterial() {
            return Material.BLAZE_ROD;
        }
    }

    private class DarkEmberStaff implements SuperItemInterface{

        @Override
        public String getName() {
            return "Dark Ember Staff";
        }

        @Override
        public String getId() {
            return "dark_ember_staff";
        }

        @Override
        public BasicItem.Rarity getRarity() {
            return BasicItem.Rarity.RARE;
        }

        @Override
        public SuperItem.Type getType() {
            return BasicItem.Type.STAFF;
        }

        @Override
        public List<ItemAbility> getItemAbilities() {
            List<ItemAbility> abilities = new ArrayList<>();
            abilities.add(new ItemAbility("Attune", "dark_ember_attune", getAbilityLore(0), ItemAbility.AbilityAction.LEFT_CLICK_ALL, getSwapExecutable(), 0, 0));
            abilities.add(new ItemAbility("Dark Embers", "dark_ember_shoot", getAbilityLore(1), ItemAbility.AbilityAction.RIGHT_CLICK_ALL, getShootExecutable(), 10, 0));
            return abilities;
        }

        private Executable getSwapExecutable() {
            Executable exec = (PlayerInteractEvent e) -> {
                Player p = e.getPlayer();
                ItemStack item = e.getItem();
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                String attunement = dataContainer.get(new NamespacedKey(plugin, "ember_attunement"), PersistentDataType.STRING);
                String message = ChatColor.RED + "Unknown Attunement";
                String newAttunement = "attunement_unknown";
                if(attunement.equalsIgnoreCase("blazeborn")){
                    message = ChatColor.DARK_GRAY + "Darksoul";
                    newAttunement = "darksoul";
                } else if (attunement.equalsIgnoreCase("darksoul")){
                    message = ChatColor.GOLD + "Blazeborn";
                    newAttunement = "blazeborn";
                }

                dataContainer.set(new NamespacedKey(plugin, "ember_attunement"), PersistentDataType.STRING, newAttunement);

                Util.sendActionBar(p, message);
                p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 1f);

                // Update lore
                List<String> lore = item.getItemMeta().getLore();
                for (String lorestring: lore) {
                    if(lorestring.startsWith(ChatColor.AQUA + "Attunement: ")){
                        lore.set(lore.indexOf(lorestring), ChatColor.AQUA + "Attunement: " + message);
                    }
                }
                meta.setLore(lore);
                item.setItemMeta(meta);

            };
            return exec;
        }

        private Executable getShootExecutable() {
            Executable exec = (PlayerInteractEvent e) -> {
                Player p = e.getPlayer();
                World w = p.getWorld();
                Location loc;
//
//                boolean allowed = true;
//                if(plugin.isUsingWorldGuard){
//                    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
//                    com.sk89q.worldedit.util.Location guardLoc = BukkitAdapter.adapt(p.getLocation());
//                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
//                    RegionQuery query = container.createQuery();
//                    allowed = query.testState(guardLoc, localPlayer, Flags.BUILD);
//                }
//                if(!allowed){
//                    Util.sendMessage(p, "You cannot use this item in a worldguard region!");
//                    return;
//                }

                Location eyeLoc = p.getEyeLocation();

                ItemStack item = e.getItem();
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                String attunement = dataContainer.get(new NamespacedKey(plugin, "ember_attunement"), PersistentDataType.STRING);
                if(attunement.equalsIgnoreCase("blazeborn")){
                    Fireball f = (Fireball) eyeLoc.getWorld().spawnEntity(eyeLoc.add(eyeLoc.getDirection()), EntityType.FIREBALL);
                    f.setVelocity(eyeLoc.getDirection().normalize());
                    f.setMetadata("dmgEnv", new FixedMetadataValue(plugin, false));
                    f.setYield(2f);
                } else if (attunement.equalsIgnoreCase("darksoul")){
                    WitherSkull f = (WitherSkull) eyeLoc.getWorld().spawnEntity(eyeLoc.add(eyeLoc.getDirection()), EntityType.WITHER_SKULL);
                    f.setVelocity(eyeLoc.getDirection().normalize());
                    f.setMetadata("dmgEnv", new FixedMetadataValue(plugin, false));
                    f.setYield(2f);
                }


            };
            return exec;
        }

        private List<String> getAbilityLore(int arg){
            List<String> lore = new ArrayList<>();
            if(arg == 0){
                lore.add(ChatColor.GRAY + "Changes the " + ChatColor.AQUA + "attunement" + ChatColor.GRAY + " of this staff,");
                lore.add(ChatColor.GRAY + "changing the domain it draws power from.");
                lore.add("");
                lore.add(ChatColor.GRAY + "Swap between " + ChatColor.GOLD + "Blazeborn" + ChatColor.GRAY + " and " + ChatColor.DARK_GRAY + "Darksoul");
            } else {
                lore.add(ChatColor.GRAY + "Shoot a projectile based on the " + ChatColor.AQUA + "attunement");
                lore.add(ChatColor.GRAY + "of this weapon. Dark magic is cool, right?");
                lore.add("");
                lore.add(ChatColor.AQUA + "Attunement: " + ChatColor.GOLD + "Blazeborn");
            }
            return lore;
        }

        @Override
        public ItemMeta getItemMeta() {
            ItemStack item = new ItemStack(getMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "A fiery rod, taken from " + ChatColor.RED + "Blaziel" + ChatColor.GRAY + " himself.");
            meta.setLore(lore);

            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(new NamespacedKey(plugin, "ember_attunement"), PersistentDataType.STRING, "blazeborn");

            return meta;
        }


        @Override
        public Material getMaterial() {
            return Material.BLAZE_ROD;
        }
    }
}
