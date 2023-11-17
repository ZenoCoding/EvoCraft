package me.zenox.evocraft.abilities.itemabilities;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ModeledEntity;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import me.zenox.evocraft.Slot;
import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.abilities.AbilitySettings;
import me.zenox.evocraft.abilities.ElementalFlux;
import me.zenox.evocraft.abilities.EventAbility;
import me.zenox.evocraft.abilities.itemabilities.specific.EmberAttune;
import me.zenox.evocraft.item.ComplexItemMeta;
import me.zenox.evocraft.item.ComplexItemStack;
import me.zenox.evocraft.item.ItemRegistry;
import me.zenox.evocraft.persistence.NBTEditor;
import me.zenox.evocraft.util.Geo;
import me.zenox.evocraft.util.TriConsumer;
import me.zenox.evocraft.util.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static me.zenox.evocraft.item.ItemRegistry.TOTEM_POLE;
import static me.zenox.evocraft.util.Util.getNearbyBlocks;

public class ItemAbility extends EventAbility<PlayerInteractEvent> {
    private static final int SHARD_SPEED = 3;
    private static final int SHARD_RADIUS = 3;
    private final AbilityAction action;

    public ItemAbility(AbilitySettings settings) {
        super(settings);
        if(settings.getAbilityAction() == AbilityAction.NONE){
            throw new IllegalArgumentException("Action cannot be NONE");
        }
        this.action = settings.getAbilityAction();
    }

    public ItemAbility(AbilitySettings settings, TriConsumer<PlayerInteractEvent, Player, ItemStack> executable) {
        super(settings, executable);
        if(settings.getAbilityAction() == AbilityAction.NONE){
            throw new IllegalArgumentException("Action cannot be NONE");
        }
        this.action = settings.getAbilityAction();
    }

    public ItemAbility(String id, AbilityAction action, int manaCost, double cooldown) {
        super(id, manaCost, cooldown, Slot.EITHER_HAND);
        this.action = action;
    }

    public ItemAbility(String id, AbilityAction action, int manaCost, double cooldown, Slot slot) {
        super(id, manaCost, cooldown, slot);
        this.action = action;
    }

    public ItemAbility(String id, AbilityAction action, int manaCost, double cooldown, TriConsumer<PlayerInteractEvent, Player, ItemStack> exectuable) {
        super(id, manaCost, cooldown, Slot.EITHER_HAND, exectuable);
        this.action = action;
    }

    @Override
    protected Player getPlayerOfEvent(PlayerInteractEvent e) {
        return e.getPlayer();
    }

    @Override
    protected List<ItemStack> getItem(Player p, PlayerInteractEvent e) {
        return Arrays.stream(new ItemStack[]{e.getItem()}).filter(Objects::nonNull).toList();
    }

    public AbilityAction getAction() {
        return action;
    }

    @Override
    public boolean checkEvent(PlayerInteractEvent event) {
        return action.isAction(event.getAction()) && !event.getPlayer().isSneaking();
    }

    /**
     * Represents some static executables
     */
    public static void soulRiftAbility(PlayerInteractEvent event, Player p, ItemStack item) {
        Action action = event.getAction();
        World w = p.getWorld();
        Location loc;
        boolean allowed = true;
        if (EvoCraft.getPlugin().isUsingWorldGuard) {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
            com.sk89q.worldedit.util.Location guardLoc = BukkitAdapter.adapt(p.getLocation());
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            allowed = query.testState(guardLoc, localPlayer, Flags.BUILD);
        }
        if (!allowed) {
            Util.sendMessage(p, "You cannot use this item in a worldguard region!");
            return;
        }

        event.setCancelled(true);

        if (action.equals(Action.RIGHT_CLICK_BLOCK)) loc = Objects.requireNonNull(event.getClickedBlock()).getLocation();
        else if (action.equals(Action.RIGHT_CLICK_AIR)) loc = p.getLocation();
        else return;

        EnderCrystal crystal = (EnderCrystal) w.spawnEntity(loc.add(0, 2, 0), EntityType.ENDER_CRYSTAL);
        crystal.setInvulnerable(true);
        crystal.setMetadata("dmgEnv", new FixedMetadataValue(EvoCraft.getPlugin(), false));

        new BukkitRunnable() {
            final List<Block> blocks = getNearbyBlocks(crystal.getLocation(), 7, 2);
            final List<FallingBlock> fBlocks = new ArrayList<>();
            final List<LivingEntity> entities = new ArrayList<>();
            int count = 0;

            @Override
            public void run() {
                // Particle Magic
                List<Vector> dodecahedron = Geo.makeDodecahedron(loc.toVector(), 2);
                for (Vector v : dodecahedron) {
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 187, 215), 0.5F);
                    w.spawnParticle(Particle.REDSTONE, v.toLocation(w).add(0, 0.5 + Math.sin(count) / 4, 0), 1, dustOptions);
                }


                // At 5 seconds

                if (count == 100) {
                    cancel();
                    crystal.remove();
                    w.createExplosion(crystal.getLocation(), 3, false, false);
                    for (FallingBlock fallingblock : fBlocks) {
                        fallingblock.setGravity(true);
                    }
                    for (LivingEntity entity : entities) {
                        entity.setGravity(true);
                    }

                }

                Random r = new Random();
                Block block = blocks.get(r.nextInt(blocks.size()));

                if (!(block.getType().getBlastResistance() > 1200 || block.getType().equals(Material.PLAYER_HEAD) || block.getType().equals(Material.PLAYER_WALL_HEAD))) {
                    FallingBlock fBlock = w.spawnFallingBlock(block.getLocation(), block.getBlockData());
                    fBlock.setVelocity((fBlock.getLocation().toVector().subtract(crystal.getLocation().toVector()).multiply(-10).normalize()));
                    fBlock.setGravity(false);
                    fBlock.setDropItem(false);
                    fBlock.setHurtEntities(true);
                    fBlock.setMetadata("temporary", new FixedMetadataValue(EvoCraft.getPlugin(), true));

                    fBlocks.add(fBlock);
                }

                for (FallingBlock fallingBlock : fBlocks) {
                    if (r.nextInt(count / 25 + 1) == 0) {
                        fallingBlock.setVelocity((fallingBlock.getLocation().toVector().subtract(crystal.getLocation().add(r.nextDouble() - 0.5, r.nextDouble() - 0.5 + 2, r.nextDouble() - 0.5).toVector()).multiply(-0.5).normalize()));
                    }
                }

                for (LivingEntity entity : entities) {
                    if (r.nextInt(count / 25 + 1) == 0 && !entity.equals(p)) {
                        entity.setVelocity((entity.getLocation().toVector().subtract(crystal.getLocation().add(r.nextDouble() - 0.5, r.nextDouble() - 0.5 + 2, r.nextDouble() - 0.5).toVector()).multiply(-0.5).normalize()));
                        entity.damage((entity.getHealth() * (2f / 3f)) / 10f + 1f);
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
        }.runTaskTimer(EvoCraft.getPlugin(), 0, 0);
    }

    public static void magicMissileAbility(PlayerInteractEvent event, Player p, ItemStack item, Boolean combustion, Integer explosionPower) {
        Random r = new Random();
        World w = p.getWorld();

        LocalPlayer localPlayer;
        com.sk89q.worldedit.util.Location guardLoc;
        RegionContainer container;
        RegionQuery query;

        if (EvoCraft.getPlugin().isUsingWorldGuard) {
            localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
            guardLoc = BukkitAdapter.adapt(p.getLocation());
            container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            query = container.createQuery();

            if (!query.testState(guardLoc, localPlayer, Flags.BUILD)) {
                Util.sendMessage(p, "You cannot use this item in a worldguard region! Flags: [BUILD]");
                return;
            }
        }

        // 20% chance to remove an item from their hand
        if (r.nextInt(5) == 0 && combustion) {
            item.setAmount(event.getItem().getAmount() - 1);
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

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                trident.setVelocity(v2);
                Location loc = trident.getLocation();
                for (Entity entity :
                        trident.getNearbyEntities(2, 2, 2)) {
                    if (entity instanceof Damageable && !entity.equals(p)) {
                        ((Damageable) entity).damage(explosionPower, p);
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

                if (EvoCraft.getPlugin().isUsingWorldGuard) {
                    LocalPlayer localPlayer2 = WorldGuardPlugin.inst().wrapPlayer(p);
                    com.sk89q.worldedit.util.Location guardLoc2 = BukkitAdapter.adapt(trident.getLocation());
                    RegionContainer container2 = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionQuery query2 = container2.createQuery();

                    if (!query2.testState(guardLoc2, localPlayer2, Flags.BUILD)) {
                        trident.remove();
                        Util.sendMessage(p, "You cannot shoot this item into a worldguard region! Flags: [PVP]");
                        cancel();
                    }
                }

                if (trident.isInBlock()) {
                    trident.remove();
                    List<Block> blocks = getNearbyBlocks(loc, explosionPower / 2, explosionPower / 4);
                    for (Block block : blocks) {
                        if (block.getType().getBlastResistance() > 1200 || block.getType().equals(Material.PLAYER_HEAD) || block.getType().equals(Material.PLAYER_WALL_HEAD))
                            continue;
                        if (r.nextDouble() < 0.2) continue;
                        if (EvoCraft.getPlugin().isUsingWorldGuard) {
                            LocalPlayer localPlayer2 = WorldGuardPlugin.inst().wrapPlayer(p);
                            com.sk89q.worldedit.util.Location guardLoc2 = BukkitAdapter.adapt(block.getLocation());
                            RegionContainer container2 = WorldGuard.getInstance().getPlatform().getRegionContainer();
                            RegionQuery query2 = container2.createQuery();


                            if (!query2.testState(guardLoc2, localPlayer2, Flags.BUILD)) {
                                continue;
                            }
                        }
                        FallingBlock fallingBlock = w.spawnFallingBlock(block.getLocation(), block.getBlockData());
                        fallingBlock.setVelocity(fallingBlock.getLocation().toVector().subtract(loc.clone().add(r.nextDouble() * 4 - 2, -2, r.nextDouble() * 4 - 2).toVector()).multiply(explosionPower * 2).normalize());
                        fallingBlock.setDropItem(false);
                        fallingBlock.setHurtEntities(true);
                        fallingBlock.setMetadata("temporary", new FixedMetadataValue(EvoCraft.getPlugin(), true));
                    }
                    try {
                        trident.remove();
                    } catch (Exception ignored) {

                    }
                    w.createExplosion(loc, explosionPower, false, false, p);
                    cancel();
                }
                count++;
                if (count >= 600) {
                    trident.remove();
                    cancel();
                }

            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0, 5);
    }

    public static void centralizeAbility(PlayerInteractEvent event, Player p, ItemStack item, Boolean corrupted, Integer duration) {
        World w = p.getWorld();
        Location loc = p.getLocation();
        if (event.getClickedBlock() != null) loc = event.getClickedBlock().getLocation().add(0.5, 1, 0.5);

        final Random r = new Random();

        ItemMeta meta = item.getItemMeta();

        ArmorStand totem = ((ArmorStand) w.spawnEntity(loc, EntityType.ARMOR_STAND));

        totem.setInvulnerable(false);
        totem.setGravity(false);
        totem.setVisible(false);
        totem.setPersistent(false);

        // NMS-Setting NBT data
        NBTEditor.set(totem, 2039583, "DisabledSlots");

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

        List<Particle> particleList = new ArrayList<>();

        if (corrupted) {
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(EvoCraft.getPlugin(), "centralize_corrupt_uses");

            int uses = 5;

            if (dataContainer.has(key, PersistentDataType.INTEGER)) {
                uses = dataContainer.get(key, PersistentDataType.INTEGER);
            }

            uses--;

            dataContainer.set(key, PersistentDataType.INTEGER, uses);

            if (uses == 0) {
                Location highestblockpos = w.getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getLocation().add(0, 1, 0);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm m spawn -s Demiser 1 " + w.getName() + "," + highestblockpos.getX() + "," + highestblockpos.getY() + "," + highestblockpos.getZ());
                p.getInventory().setItem(event.getHand(), TOTEM_POLE.getItemStack(1));
            } else {
                List<String> lore = meta.getLore();
                for (String loreitem : lore) {
                    if (loreitem.startsWith(ChatColor.LIGHT_PURPLE + "Corrupted Uses Left: ")) {
                        lore.set(lore.indexOf(loreitem), ChatColor.LIGHT_PURPLE + "Corrupted Uses Left: " + ChatColor.DARK_PURPLE + uses);
                        meta.setLore(lore);
                    }
                }
            }

            item.setItemMeta(meta);


            w.playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_DEATH, 1, 1.2f);
            LeatherArmorMeta chestMeta = ((LeatherArmorMeta) chestplate.getItemMeta());
            chestMeta.setColor(Color.PURPLE);
            LeatherArmorMeta leggingsMeta = ((LeatherArmorMeta) leggings.getItemMeta());
            leggingsMeta.setColor(Color.PURPLE);
            LeatherArmorMeta bootsMeta = ((LeatherArmorMeta) boots.getItemMeta());
            bootsMeta.setColor(Color.PURPLE);

            chestplate.setItemMeta(chestMeta);
            leggings.setItemMeta(leggingsMeta);
            boots.setItemMeta(bootsMeta);

            totem.getEquipment().setHelmet(Util.makeSkull(new ItemStack(Material.PLAYER_HEAD), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk3YzI4NmI2ZDE2MjM5YTcxZmYxNjc0OTQ0MTZhZDk0MDcxNzIwNTEwY2Y4YTgyYWIxZjQ1MWZmNGE5MDkxNiJ9fX0="));
            totem.getEquipment().setChestplate(chestplate);
            totem.getEquipment().setLeggings(leggings);
            totem.getEquipment().setBoots(boots);

            particleList.add(Particle.REVERSE_PORTAL);
            particleList.add(Particle.CRIT_MAGIC);
            particleList.add(Particle.END_ROD);

        } else {
            w.playSound(loc, Sound.ITEM_TOTEM_USE, 1, 0);

            LeatherArmorMeta chestMeta = ((LeatherArmorMeta) chestplate.getItemMeta());
            chestMeta.setColor(Color.YELLOW);
            LeatherArmorMeta leggingsMeta = ((LeatherArmorMeta) leggings.getItemMeta());
            leggingsMeta.setColor(Color.YELLOW);
            LeatherArmorMeta bootsMeta = ((LeatherArmorMeta) boots.getItemMeta());
            bootsMeta.setColor(Color.YELLOW);

            chestplate.setItemMeta(chestMeta);
            leggings.setItemMeta(leggingsMeta);
            boots.setItemMeta(bootsMeta);

            totem.getEquipment().setHelmet(Util.makeSkull(new ItemStack(Material.PLAYER_HEAD), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU4MDkxMjhhMGU1YTQ0YzJlMzk1MzJlNmJiYzY4MjUyY2I4YzlkNWVjZDI0NmU1OTY1MDc3YzE0N2M3OTVlNyJ9fX0="));
            totem.getEquipment().setChestplate(chestplate);
            totem.getEquipment().setLeggings(leggings);
            totem.getEquipment().setBoots(boots);

            particleList.add(Particle.TOTEM);
            particleList.add(Particle.CRIT_MAGIC);

        }

        new BukkitRunnable() {
            final int wings = 6;
            final Location loc = totem.getLocation();
            final double startRadius = 4;
            double a = 0;
            int count = 0;
            double x = 0;
            double y = 0;
            double z = 0;
            double radius = startRadius;

            @Override
            public void run() {
                // Spawn Particles
                for (int i = 0; i < wings; i++) {
                    x = Math.cos(a + i * (2 * Math.PI / wings)) * radius;
                    y = Math.sin(a) / 2;
                    z = Math.sin(a + i * (2 * Math.PI / wings)) * radius;

                    Location loc2 = new Location(p.getWorld(), (float) (loc.getX() + x), (float) (loc.getY() + y + 1), (float) (loc.getZ() + z));
                    p.getWorld().spawnParticle(particleList.get(r.nextInt(particleList.size())), loc2, 0);
                    p.getWorld().spawnParticle(particleList.get(r.nextInt(particleList.size())), loc2, 0);

                    a += Math.PI / 150;
                    radius -= 0.025;

                    if (radius <= 0.8) radius = startRadius;

                }

                // Apply effects
                if (count % 60 == 0) {
                    if (corrupted) {
                        if (p.getLocation().distanceSquared(loc) < Math.pow(45, 2)) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 70, 1));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 70, 0));
                        }
                    } else {
                        for (Entity ent : w.getNearbyEntities(loc, 40, 40, 40)) {
                            if (ent.getLocation().distanceSquared(loc) < Math.pow(30, 2)) {
                                if (ent instanceof Player) {
                                    ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 70, 0));
                                    ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 70, 0));
                                }
                            }
                        }
                    }
                }

                if (count / 20 >= duration) {
                    w.playSound(loc, corrupted ? Sound.ENTITY_ELDER_GUARDIAN_DEATH : Sound.ITEM_TOTEM_USE, 1, 1.2f);
                    totem.remove();
                    cancel();
                }

                count++;
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 3, 1);
    }

    public static void obsidianShardAbility(PlayerInteractEvent event, Player p, ItemStack item) {
        World w = p.getWorld();
        shootShard(p, new Location(p.getWorld(), 0, 1.8, 0), 0);
    }

    // Obsidian Shard Helper Method
    private static void shootShard(Player p, Location deltaLoc, double delay) {
        Random r = new Random();
        World w = p.getWorld();

        Arrow arrow = (Arrow) w.spawnEntity(p.getLocation().add(deltaLoc), EntityType.ARROW);
        Vector v = p.getLocation().getDirection().normalize().clone();
        Vector v2 = v.multiply(SHARD_SPEED);
        arrow.setShooter(p);
        arrow.setGravity(false);
        arrow.setPierceLevel(123);
        arrow.getLocation().setDirection(v);
        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        arrow.setInvulnerable(true);
        arrow.setKnockbackStrength(1);
        arrow.setCritical(false);
        arrow.setDamage(0d);

        // Hide the arrow
        PacketContainer destroyArrow = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        destroyArrow.getModifier()
                .write(0, new IntArrayList(new int[]{arrow.getEntityId()}));

        EvoCraft.getPlugin().getProtocolManager().broadcastServerPacket(destroyArrow);

        ModeledEntity shard = ModelEngineAPI.createModeledEntity(arrow);
        shard.addModel(ModelEngineAPI.createActiveModel("obsidianshard"), true);
        shard.getLookController().setBodyYaw(p.getLocation().getYaw());
        shard.getModel("obsidianshard").setLockPitch(true);
        shard.getModel("obsidianshard").setLockYaw(true);

        w.playSound(p.getLocation(), Sound.ENTITY_BEE_HURT, 1, 0.2f);

        new BukkitRunnable() {
            double count = 0;

            @Override
            public void run() {

                Location loc = arrow.getLocation();
                for (Entity entity :
                        arrow.getNearbyEntities(0.5, 2, 0.5)) {
                    if (EvoCraft.getPlugin().isUsingWorldGuard) {
                        LocalPlayer localPlayer2 = WorldGuardPlugin.inst().wrapPlayer(p);
                        com.sk89q.worldedit.util.Location guardLoc2 = BukkitAdapter.adapt(arrow.getLocation());
                        RegionContainer container2 = WorldGuard.getInstance().getPlatform().getRegionContainer();
                        RegionQuery query2 = container2.createQuery();
                        if (!query2.testState(guardLoc2, localPlayer2, Flags.BUILD)) {
                            break;
                        }

                    }
                    if (entity instanceof Damageable && !entity.equals(p)) {
                        ((Damageable) entity).damage(20, p);
                        if (entity instanceof LivingEntity)
                            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20, 1));
                    }
                }

                if (count / 20 == delay) {
                    w.playSound(loc, Sound.ITEM_AXE_WAX_OFF, 1, 1.2f);
                } else if (count / 20 > delay) {
                    arrow.setVelocity(v2);
                    if (count % 20 == 0) {
                        w.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 0.7f, 0.4f);
                    }
                }

                if (count / 20 > 30) {
                    cancel();
                    arrow.remove();
                }

                if (arrow.isInBlock()) {
                    arrow.remove();
                    cancel();
                } else {
                    w.spawnParticle(Particle.REVERSE_PORTAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                    w.spawnParticle(Particle.BLOCK_CRACK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2, Material.OBSIDIAN.createBlockData());
                    w.spawnParticle(Particle.SMOKE_NORMAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                }

                count = count + 1;

            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0, 1);
    }

    public static void tarhelmAbility(PlayerInteractEvent event, Player p, ItemStack item) {
        p.playSound(p.getLocation(), Sound.ENTITY_RAVAGER_ATTACK, 1, 0);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 3));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 400, 0));
    }


    public static void darkcallerAbility(PlayerInteractEvent event, Player p, ItemStack item) {
        World w = p.getWorld();

        LocalPlayer localPlayer;
        com.sk89q.worldedit.util.Location guardLoc;
        RegionContainer container;
        RegionQuery query;

        ArmorStand totem = ((ArmorStand) w.spawnEntity(p.getLocation(), EntityType.ARMOR_STAND));

        totem.setInvulnerable(false);
        totem.setGravity(false);
        totem.setVisible(false);
        totem.setPersistent(false);

        // NMS-Setting NBT data
        NBTEditor.set(totem, 2039583, "DisabledSlots");

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

        List<Particle> particleList = new ArrayList<>();


        LeatherArmorMeta chestMeta = ((LeatherArmorMeta) chestplate.getItemMeta());
        chestMeta.setColor(Color.WHITE);
        LeatherArmorMeta leggingsMeta = ((LeatherArmorMeta) leggings.getItemMeta());
        leggingsMeta.setColor(Color.BLACK);
        LeatherArmorMeta bootsMeta = ((LeatherArmorMeta) boots.getItemMeta());
        bootsMeta.setColor(Color.BLACK);

        chestplate.setItemMeta(chestMeta);
        leggings.setItemMeta(leggingsMeta);
        boots.setItemMeta(bootsMeta);

        totem.getEquipment().setHelmet(Util.makeSkull(new ItemStack(Material.PLAYER_HEAD), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RlMzNjOTVmZWMxYjhkOTg4MjUwZjVmNWIzYTI0ODU3NDI0MzlmYWVhYTc1ZWQ1MDZlYTAxZDc1ZTE3ZjIxIn19fQ==="));
        totem.getEquipment().setChestplate(chestplate);
        totem.getEquipment().setLeggings(leggings);
        totem.getEquipment().setBoots(boots);

    }

    public static void terraStrikeAbility(PlayerInteractEvent event, Player p, ItemStack item) {
        Location loc = p.getLocation();
        Random r = new Random();
        Arrow arr = p.getWorld().spawnArrow(p.getEyeLocation(), loc.getDirection(), 2f, 0);
        arr.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        arr.setPierceLevel(100);

        Giant giant = (Giant) p.getWorld().spawnEntity(loc, EntityType.GIANT);
        giant.setInvisible(true);
        giant.setInvulnerable(true);
        giant.setCollidable(false);
        giant.setCustomName("Dinnerbone");
        giant.setAI(false);
        giant.setSilent(true);
        giant.getEquipment().setItemInMainHand(new ComplexItemStack(ItemRegistry.GREATSWORD_VOLKUMOS).getItem());

        PacketContainer destroyArrow = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        destroyArrow.getModifier()
                .write(0, new IntArrayList(new int[]{arr.getEntityId()}));

        EvoCraft.getPlugin().getProtocolManager().broadcastServerPacket(destroyArrow);

        new BukkitRunnable(){
            boolean contacted = false;
            @Override
            public void run() {
                giant.setCustomName("Dinnerbone");

                Location arrowLocation = arr.getLocation();


                // Calculate the giant's base location to align its hand with the arrow
                Location giantBaseLocation = arrowLocation.clone();

                // Offset the giant's location by moving in the opposite direction of the arrow by 5 blocks (ignoring y axis)
                giantBaseLocation.add(arrowLocation.getDirection().setY(0).normalize().multiply(-5));
                giantBaseLocation.add(arrowLocation.getDirection().rotateAroundY(Math.PI/2).setY(0).normalize().multiply(-1.5));

                // Set the giant's direction to match the arrow's direction
                giantBaseLocation.setDirection(arrowLocation.getDirection());

                // Teleport the giant to the calculated location
                giant.teleport(giantBaseLocation);

                if(arr.isInBlock() && !contacted){
                    contacted = true;
                    arr.setTicksLived(1);

                    p.getWorld().createExplosion(arr.getLocation(), 4, false, false, p);
                    List<Block> blocks = getNearbyBlocks(arr.getLocation(), 4, 2);

                    for (Block block : blocks) {
                        if (block.getType().getBlastResistance() > 1200 || block.getType().equals(Material.PLAYER_HEAD) || block.getType().equals(Material.PLAYER_WALL_HEAD))
                            continue;
                        if (r.nextDouble() < 0.2) {
                            FallingBlock fallingBlock = p.getWorld().spawnFallingBlock(block.getLocation(), block.getBlockData());
                            fallingBlock.setVelocity(fallingBlock.getLocation().toVector().subtract(arr.getLocation().clone().toVector()).multiply(2).normalize());
                            fallingBlock.setDropItem(false);
                            fallingBlock.setHurtEntities(true);
                            fallingBlock.setMetadata("temporary", new FixedMetadataValue(EvoCraft.getPlugin(), true));
                        }
                    }

                    for(Entity entity : arr.getNearbyEntities(4, 2, 4)){
                        if(entity instanceof Damageable && !entity.equals(p)){
                            ((Damageable) entity).damage(50, p);
                        }
                    }
                }
                if((contacted && arr.getTicksLived() > 80) || arr.getTicksLived() > 200){
                    arr.remove();
                    giant.remove();
                    cancel();
                }
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0, 4);
    }

    public static void consumeAbility(PlayerInteractEvent event, Player p, ItemStack item) {
        item.setAmount(item.getAmount()-1);
        Util.sendActionBar(p, ChatColor.GOLD + "" + ChatColor.BOLD + "CONSUMED " + ChatColor.RESET + "" + ChatColor.BLUE + "Gilded Carrot");
        p.setFoodLevel(20);
        p.setSaturation(50);
    }

    public static void voidWarpAbility(PlayerInteractEvent event, Player p, ItemStack item) {
        Random r = new Random();

        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0.6f);

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count >= 20) {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0.8f);

                    Block endblock = p.getTargetBlockExact(75, FluidCollisionMode.NEVER);
                    Vector dir = p.getLocation().getDirection();
                    if (endblock == null) {
                        p.teleport(p.getLocation().add(dir.normalize().multiply(75)).add(0, 1, 0).setDirection(dir));
                    } else {
                        p.teleport(endblock.getLocation().add(0, 1, 0).setDirection(dir));
                    }
                    cancel();
                }

                if (count % 4 == 0) p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_HURT, 1, 1.4f);

                for (int i = 0; i < 4; i++) {
                    Location loc = p.getLocation().clone().add(r.nextDouble() - 0.5, r.nextDouble() + 0.1, r.nextDouble() - 0.5);
                    World w = p.getWorld();
                    w.spawnParticle(Particle.ELECTRIC_SPARK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                    w.spawnParticle(Particle.BLOCK_CRACK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2, Material.OBSIDIAN.createBlockData());
                    w.spawnParticle(Particle.REVERSE_PORTAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                    w.spawnParticle(Particle.BLOCK_CRACK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2, Material.OBSIDIAN.createBlockData());
                    w.spawnParticle(Particle.SMOKE_NORMAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                }
                count++;
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 5, 1);
    }

    public static void voidularRecallAbility(PlayerInteractEvent event, Player p, ItemStack item) {
        Random r = new Random();
        Location locationOfBlock;


        PersistentDataContainer container = p.getPersistentDataContainer();
        NamespacedKey locationKey = new NamespacedKey(EvoCraft.getPlugin(), "voidular_recall");
        if (container.has(locationKey, PersistentDataType.STRING)) {
            String locationString = container.get(locationKey, PersistentDataType.STRING);
            List<String> locationStringList = List.of(locationString.split("\\|"));
            container.remove(locationKey);
            locationOfBlock = new Location(p.getServer().getWorld(locationStringList.get(0)), Double.parseDouble(locationStringList.get(1)), Double.parseDouble(locationStringList.get(2)), Double.parseDouble(locationStringList.get(3)));

            World w = p.getWorld();

            w.playSound(p.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_DEATH, 2f, 0.7f);
            w.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2f, 0.8f);

            for (int i = 0; i < 5; i++) {
                w.spawnParticle(Particle.SCULK_SOUL, locationOfBlock.clone().add(r.nextDouble() - 0.5, 0.5, r.nextDouble() - 0.5), 0, r.nextDouble(), r.nextDouble(), r.nextDouble() - 0.5, 0.2);
                w.spawnParticle(Particle.REVERSE_PORTAL, locationOfBlock.clone().add(r.nextDouble() - 0.5, 0.5, r.nextDouble() - 0.5), 0, r.nextDouble(), r.nextDouble(), r.nextDouble() - 0.5, 0.2);
                w.spawnParticle(Particle.END_ROD, locationOfBlock.clone().add(r.nextDouble() - 0.5, 0.5, r.nextDouble() - 0.5), 0, r.nextDouble(), r.nextDouble(), r.nextDouble() - 0.5, 0.2);
            }

            try {
                p.teleport(locationOfBlock);
            } catch (Exception ignored) {

            }


            w.playSound(p.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_DEATH, 2f, 0.7f);
            w.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2f, 0.8f);
        } else {
            if (event.getAction().equals(Action.LEFT_CLICK_AIR)) {
                locationOfBlock = p.getLocation().add(0, 1, 0);
            } else {
                locationOfBlock = event.getClickedBlock().getLocation();
            }

            String locationString = locationOfBlock.getWorld().getName() + "|" + locationOfBlock.getX() + "|" + locationOfBlock.getY() + "|" + locationOfBlock.getZ();
            container.set(locationKey, PersistentDataType.STRING, locationString);

            // Cosmetic Effects
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_DEATH, 2f, 0.7f);

            new BukkitRunnable() {
                @Override
                public void run() {
                    World w = p.getWorld();
                    w.spawnParticle(Particle.SCULK_SOUL, locationOfBlock.clone().add(r.nextDouble() - 0.5, 0.5, r.nextDouble() - 0.5), 0, r.nextDouble(), r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);

                    if (!container.has(locationKey, PersistentDataType.STRING)) {
                        cancel();
                    }

                }
            }.runTaskTimer(EvoCraft.getPlugin(), 0, 3);

        }

    }

    public static void emberShootAbility(PlayerInteractEvent event, Player p, ItemStack item) {
        ComplexItemMeta complexMeta = ComplexItemStack.of(item).getComplexMeta();

        Location eyeLoc = p.getEyeLocation();

        if(complexMeta.getVariable(EmberAttune.ATTUNEMENT_VARIABLE_TYPE) == null) complexMeta.setVariable(EmberAttune.ATTUNEMENT_VARIABLE_TYPE, EmberAttune.Attunement.BLAZEBORN);

        if (complexMeta.getVariable(EmberAttune.ATTUNEMENT_VARIABLE_TYPE).getValue().equals(EmberAttune.Attunement.BLAZEBORN)) {
            Fireball f = (Fireball) eyeLoc.getWorld().spawnEntity(eyeLoc.add(eyeLoc.getDirection()), EntityType.FIREBALL);
            f.setVelocity(eyeLoc.getDirection().normalize().multiply(Math.min(5, AureliumAPI.getMaxMana(event.getPlayer()) / 75)));
            f.setMetadata("dmgEnv", new FixedMetadataValue(EvoCraft.getPlugin(), false));
            f.setMetadata("knockback", new FixedMetadataValue(EvoCraft.getPlugin(), 2));
            f.setShooter(p);
            f.setYield(((float) Math.sqrt(AureliumAPI.getMaxMana(p))) / 10f);

            // get rid of the fireball after 10 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    f.remove();
                }
            }.runTaskLater(EvoCraft.getPlugin(), 200);

        } else if (complexMeta.getVariable(EmberAttune.ATTUNEMENT_VARIABLE_TYPE).getValue().equals(EmberAttune.Attunement.DARKSOUL)) {
            WitherSkull f = (WitherSkull) eyeLoc.getWorld().spawnEntity(eyeLoc.add(eyeLoc.getDirection()), EntityType.WITHER_SKULL);
            f.setVelocity(eyeLoc.getDirection().normalize().multiply(Math.min(5, AureliumAPI.getMaxMana(event.getPlayer()) / 50)));
            f.setMetadata("dmgEnv", new FixedMetadataValue(EvoCraft.getPlugin(), false));
            f.setShooter(p);
            f.setYield((float) Math.sqrt(AureliumAPI.getMaxMana(p)) / 6f);

            // get rid of the fireball after 10 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    f.remove();
                }
            }.runTaskLater(EvoCraft.getPlugin(), 200);
        }
    }

    public static void smallEmberShootAbility(PlayerInteractEvent event, Player p, ItemStack item) {

        Location eyeLoc = p.getEyeLocation();

        Fireball f = (Fireball) eyeLoc.getWorld().spawnEntity(eyeLoc.add(eyeLoc.getDirection()), EntityType.FIREBALL);
        f.setVelocity(eyeLoc.getDirection().normalize());
        f.setMetadata("dmgEnv", new FixedMetadataValue(EvoCraft.getPlugin(), false));
        f.setYield(2f);
        f.setShooter(p);
    }

    public static void snowShotAbility(PlayerInteractEvent playerInteractEvent, @NotNull Player player, ItemStack itemStack) {
        // Summon a snowball and shoot it, then apply a metadata tag that will be used to check if the snowball is a snowball shot by the player
        Snowball snowball = player.launchProjectile(Snowball.class, player.getLocation().getDirection().multiply(3));
        snowball.setMetadata("super_snowball", new FixedMetadataValue(EvoCraft.getPlugin(), true));
        snowball.setShooter(player);
        // Create particles at the player's location
        player.getWorld().spawnParticle(Particle.SNOWBALL, player.getLocation(), 20, 0.3, 2, 0.3, 0.1);
    }

    public static void crystalSpikeAbility(PlayerInteractEvent event, @NotNull Player player, ItemStack itemStack) {
        // Create an ability that runs a command with different locations in a line where the player is facing
        Location eyeLoc = player.getEyeLocation();
        Location loc = eyeLoc.add(eyeLoc.getDirection().normalize().setY(0).multiply(5));
        new BukkitRunnable() {
            int a = 0;
            @Override
            public void run() {
                if(a >= 7) cancel();

                loc.add(eyeLoc.getDirection().normalize().setY(0).multiply(5));
                // Loop down the y-axis to get the location of a ground block
                while (loc.getBlock().getType() == Material.AIR) {
                    loc.subtract(0, 1, 0);
                }

                // Loop up the y-axis to get the location of a block above the ground block
                while (loc.clone().add(0, 1, 0).getBlock().getType() != Material.AIR) {
                    loc.add(0, 1, 0);
                }

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm m spawn -s ColossusCrystalSpike 1 " + player.getWorld().getName() + "," + loc.getBlockX() + "," + (loc.getBlockY()+1) + "," + loc.getBlockZ());
                // Crystal particle effects
                player.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, 10, 0.3, 0.3, 0.3, 0.1);

                a++;
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0, 3);

    }

    public static void manaBoostAbility(PlayerInteractEvent event, Player player, ItemStack itemStack) {
        // An ability that refuels the player to full mana instantaneously, and grants them 2x mana regeneration for the next 10 seconds
        AureliumAPI.setMana(player, AureliumAPI.getMaxMana(player));
        // Send and action bar
        Util.sendActionBar(player, "&b&lMana Refueled!");
        // Play a sound
        player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1, 2);

        // Consume the item
        itemStack.setAmount(0);

        // Create a task that will run every second for 10 seconds
        new BukkitRunnable() {
            int a = 0;

            @Override
            public void run() {
                a++;
                // If the task has run for 10 seconds, cancel it
                if (a >= 10) {
                    cancel();
                }
                // If the player is not online, cancel the task
                if (!player.isOnline()) {
                    cancel();
                }
                // Get the players current mana regeneration
                double manaRegen = AureliumAPI.getManaRegen(player);
                // Add that amount to the player's mana
                AureliumAPI.setMana(player, Math.min(AureliumAPI.getMaxMana(player), AureliumAPI.getMana(player) + manaRegen));

            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0, 20);

    }

    public static void colossalSweepAbility(PlayerInteractEvent event, Player player, ItemStack itemStack) {
        // An ability that creates a large AOE sweeping damage ability, displaying sweeping particles, and knocking enemies back

    }
    public static void rapidFireAbility(PlayerInteractEvent event, Player player, ItemStack itemStack){
        if (itemStack != null && itemStack.getType() == ItemRegistry.FROSTFIRE_BOW.getMaterial()) {
            if (player.getInventory().getItemInMainHand().equals(itemStack)) {
                // Summon arrow instantly
                Arrow arrow = player.launchProjectile(Arrow.class);
                arrow.setVelocity(player.getEyeLocation().getDirection().multiply(5)); // Adjust the arrow speed as needed
                arrow.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
                ComplexItemMeta meta =  ComplexItemStack.of(itemStack).getComplexMeta();
                ElementalFlux.Flux flux = (ElementalFlux.Flux) meta.getVariable(ElementalFlux.FLUX_VARIABLE_TYPE).getValue();
                if(flux == null){
                    meta.setVariable(ElementalFlux.FLUX_VARIABLE_TYPE, ElementalFlux.Flux.FIRE);
                }
                else if(flux == ElementalFlux.Flux.FIRE){
                    arrow.setFireTicks(100000);
                    arrow.setMetadata("frostfire_fire", new FixedMetadataValue(EvoCraft.getPlugin(), true));
                }
                else if(flux == ElementalFlux.Flux.FROST){
                    arrow.setMetadata("frostfire_frost", new FixedMetadataValue(EvoCraft.getPlugin(), true));
                }
            }
        }
    }
    public static void thunderstrikeAbility(PlayerInteractEvent event, Player player, ItemStack itemStack) {
        /**
         * Thunderstrike Ability
         *
         * Throw a projectile (represented by particles) into the air
         * The player can right click again to strike down lightning at the projectile's position.
         */

        // Check if the player has a projectile in the air
        if (player.getMetadata("thunderstrike_projectile").size() > 0) {
            // Get the projectile from the metadata
            Projectile projectile = (Projectile) player.getServer().getEntity(UUID.fromString(player.getMetadata("thunderstrike_projectile").get(0).asString()));
            if (projectile == null) {
                // If the projectile is null, remove the metadata and return
                player.removeMetadata("thunderstrike_projectile", EvoCraft.getPlugin());
                return;
            }
            // Strike lightning on the ground at the projectile's location
            // also deal AOE damage to nearby entities, and create a small explosion
            // move location to highest ground block below projectile

            Location loc = player.getWorld().getHighestBlockAt(projectile.getLocation()).getLocation();
            player.getWorld().strikeLightning(loc);
            player.getWorld().createExplosion(loc, 2, false, false, player);
            for (Entity entity : loc.getNearbyEntities(2, 2, 2)) {
                if (entity instanceof Damageable && !entity.equals(player)) {
                    ((Damageable) entity).damage(20, player);
                }
            }
            // Remove the projectile
            projectile.remove();
            // Remove the metadata
            player.removeMetadata("thunderstrike_projectile", EvoCraft.getPlugin());
        } else {
            // Create a projectile
            Projectile projectile = player.launchProjectile(Snowball.class, player.getLocation().getDirection().multiply(3));
            // Set the projectile's metadata
            player.setMetadata("thunderstrike_projectile", new FixedMetadataValue(EvoCraft.getPlugin(), projectile.getUniqueId()));

            new BukkitRunnable() {
                int a = 0;

                @Override
                public void run() {
                    a++;
                    // If the projectile is no longer in the air, cancel the task
                    if (!projectile.isValid() || projectile.isDead() || a > 100) {
                        cancel();
                        // Remove the projectile
                        projectile.remove();
                        // Remove the metadata
                        player.removeMetadata("thunderstrike_projectile", EvoCraft.getPlugin());
                    }
                    // Create particles at the projectile's location
                    player.getWorld().spawnParticle(Particle.CRIT_MAGIC, projectile.getLocation(), 10, 0.3, 0.3, 0.3, 0.1);
                    player.getWorld().spawnParticle(Particle.REDSTONE, projectile.getLocation(), 10, 0.3, 0.3, 0.3, 0.1, new Particle.DustOptions(Color.fromRGB(244, 252, 3), 1));
                }
            }.runTaskTimer(EvoCraft.getPlugin(), 0, 3);
        }
    }

    public enum AbilityAction {
        LEFT_CLICK_BLOCK("LEFT CLICK", new Action[]{Action.LEFT_CLICK_BLOCK}),
        LEFT_CLICK_AIR("LEFT CLICK", new Action[]{Action.LEFT_CLICK_AIR}),
        LEFT_CLICK_ALL("LEFT CLICK", new Action[]{Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}),
        RIGHT_CLICK_BLOCK("RIGHT CLICK", new Action[]{Action.RIGHT_CLICK_AIR}),
        RIGHT_CLICK_AIR("RIGHT CLICK", new Action[]{Action.RIGHT_CLICK_BLOCK}),
        RIGHT_CLICK_ALL("RIGHT CLICK", new Action[]{Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR}),

        NONE("", new Action[]{});

        private final String name;
        private final Action[] actionList;

        AbilityAction(String name, Action[] actionList) {
            this.name = name;
            this.actionList = actionList;
        }

        public String getName() {
            return this.name;
        }

        public boolean isAction(Action action) {

            return Arrays.asList(actionList).contains(action);

        }
    }
}
