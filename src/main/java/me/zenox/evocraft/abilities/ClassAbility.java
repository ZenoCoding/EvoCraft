package me.zenox.evocraft.abilities;

import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.data.PlayerData;
import me.zenox.evocraft.data.PlayerDataManager;
import me.zenox.evocraft.util.Geo;
import me.zenox.evocraft.util.TriConsumer;
import me.zenox.evocraft.util.Util;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ClassAbility extends Ability<PlayerInteractEvent> {
    final List<Modifier> modifiers;
    @SaveState
    protected TriConsumer<PlayerInteractEvent, Player, ClassAbility> executable;

    @SaveState
    private int strength;
    @SaveState
    private int charges;
    @SaveState
    private int chargeTime;
    @SaveState
    private int range;

    private final Map<String, Object> baseState;
    private boolean modified = false;

    public ClassAbility(AbilitySettings settings, TriConsumer<PlayerInteractEvent, Player, ClassAbility> executable) {
        super(settings.getId(), settings.getManaCost(), settings.getCooldown(), settings.isPassive());
        this.executable = executable;
        this.modifiers = settings.getModifiers();

        // stats
        this.strength = settings.getStrength();
        this.charges = settings.getCharges();
        this.chargeTime = settings.getChargeTime();
        this.range = settings.getRange();

        // save basestate snapshot
        this.baseState = getState();
    }

    public Map<String, Object> getState(){
        Map<String, Object> map = new HashMap<>();
        for (Class<?> clazz = this.getClass(); clazz != null; clazz = clazz.getSuperclass()){
            for (Field f : clazz.getDeclaredFields()){
                if (f.isAnnotationPresent(SaveState.class)){
                    try {
                        f.setAccessible(true);
                        map.put(f.getName(), f.get(this));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return map;
    }

    @Override
    public void useAbility(Event event) {
        try {
            PlayerInteractEvent e = (PlayerInteractEvent) event;
            Player p = e.getPlayer();

            PlayerData data = PlayerDataManager.getInstance().getPlayerData(p.getUniqueId());

            int level = data.getPathLevel(data.getPlayerClass().tree().path(this));
            if (level == -1) {
                Util.sendActionBar(p, "&cABILITY %s LOCKED!".formatted(this.getId().replace('_', ' ').toUpperCase()));
                return; // ability is not unlocked
            }

            Util.sendMessage(p, "&aUsing ability %s at level %d".formatted(this.getId(), level));

            applyModifiers(level); // apply modifiers to the ability

            // Check if ability is on cooldown
            if (isAbilityOnCooldown(p)) {
                sendCooldownMessage(p);
                return;
            }

            if (this.getCharges() > 0 && noCharges(p)) {
                sendNoChargesMessage(p);
                return;
            }

            if (notEnoughMana(p, this.getManaCost())) {
                sendManaInsufficientMessage(p);
                return;
            }

            deductMana(p, this.getManaCost());
            int chargesLeft = Ability.cooldownManager.consumeCharge(p, this);

            String chargeMsg = "&6(&e%d&6/%d) (-1)".formatted(chargesLeft, this.getCharges());
            showMessage(p, chargeMsg);

            this.executable.accept(e, p, this);
            setAbilityCooldown(p);
        } finally {
            reset();
        }

    }

    protected boolean noCharges(Player p) {
        return cooldownManager.isOnCooldown(p, this);
    }

    protected void sendNoChargesMessage(Player p) {
        Util.sendActionBar(p, "&c&lNO CHARGES LEFT");
    }



    /**
     * Execute a code block with the context of the ability's modifiers applied
     * @param level the level of the ability
     * @param code the code to execute
     */
    public void executeWithLevel(int level, @NotNull Consumer<ClassAbility> code){
        applyModifiers(level);
        try {
            code.accept(this);
        } finally {
            reset();
        }
    }

    /**
     * Execute a code block with the context of the ability's modifiers applied
     * @param p the player to fetch the level from
     * @param code the code to execute
     */
    public void executeAsPlayer(@NotNull Player p, Consumer<ClassAbility> code){
        PlayerData data = PlayerDataManager.getInstance().getPlayerData(p.getUniqueId());
        int level = data.getPathLevel(data.getPlayerClass().tree().path(this));
        executeWithLevel(level, code);
    }

    public void applyModifiers(int level){
        if (modified) throw new IllegalStateException("You may not modify the ability while it is already being modified.");
        for(int i = 0; i < level; i++){
            modifiers.get(i).modify(this);
        }
        modified = true;
    }

    // Recreate the ability to its original state
    public void reset(){
        for (Class<?> clazz = this.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            for (Field f : clazz.getDeclaredFields()) {
                if (f.isAnnotationPresent(SaveState.class)) {
                    try {
                        f.setAccessible(true);
                        f.set(this, baseState.get(f.getName()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        modified = false;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }

    public int getChargeTime() {
        return chargeTime;
    }

    public ClassAbility setChargeTime(int chargeTime) {
        this.chargeTime = chargeTime;
        return this;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public TriConsumer<PlayerInteractEvent, Player, ClassAbility> getExecutable() {
        return this.executable;
    }

    public void setExecutable(TriConsumer<PlayerInteractEvent, Player, ClassAbility> executable) {
        this.executable = executable;
    }

    /*
     * Ability Executables
     */
    private static void teleport(Player player, int distance) {
        Location currentLocation = player.getLocation();
        Vector direction = currentLocation.getDirection().normalize();

        // This will check each block along the path, up to the distance
        for (int i = 1; i <= distance; i++) {
            Location targetLocation = currentLocation.clone().add(direction.clone().multiply(1)); // Move 1 block at a time in the direction
            // Check for solid blocks at the player's feet or head level
            if (targetLocation.getBlock().getType().isSolid() || targetLocation.clone().add(0, 1, 0).getBlock().getType().isSolid()) {
                // If a solid block is found, stop and teleport the player to the last safe location
                break;
            }
            // Update currentLocation to the last checked location
            currentLocation = targetLocation;
        }

        // Perform the actual teleportation
        player.teleport(currentLocation.add(0, 0.1, 0)); // Slightly raise the Y value to prevent suffocation
    }

    private static void teleportEffect(Location startLoc, Location endLoc, Color color, Player player, int damage, double width) {
        List<Vector> tracedPath = Geo.lerpEdges(List.of(startLoc.toVector(), endLoc.toVector()), (int) (1.3 * startLoc.distance(endLoc)));
        Vector direction = endLoc.toVector().subtract(startLoc.toVector()).normalize();

        new BukkitRunnable() {
            int a = 0;
            @Override
            public void run() {
                if (a >= tracedPath.size()/2) {
                    cancel();
                    return;
                }

                Location currentLoc = tracedPath.get(a).toLocation(player.getWorld());
                Vector perpendicular = new Vector(-direction.getZ(), 0, direction.getX()).normalize().multiply(width / 2);

                for (double i = -width / 2; i <= width / 2; i += width / 10) {
                    Location effectLoc = currentLoc.clone().add(perpendicular.clone().multiply(i));
                    player.getWorld().getNearbyEntities(effectLoc, 0.5, 0.5, 0.5).forEach(entity -> {
                        if (entity instanceof Damageable && !entity.equals(player)) {
                            ((Damageable) entity).damage(damage);
                        }
                    });
                    player.getWorld().spawnParticle(Particle.REDSTONE, effectLoc, 1, 0.1, 0, 0.1, new Particle.DustOptions(color, 1));
                }

                a++;
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0, 1);
    }


    public static void teleportAbility(PlayerInteractEvent event, Player player, ClassAbility ability) {
        Location prev = player.getLocation();
        teleport(player, ability.getRange());

        // Create particles along the path
        teleportEffect(prev, player.getLocation(), Color.fromRGB(0, 123, 255), player, 0, 1);
    }

    /**
     * Dark Teleport Executable that damages entities along the path
     */
    public static void darkTeleport(PlayerInteractEvent event, Player player, ClassAbility ability) {
        Location prev = player.getLocation();

        // Teleport the player using the helper method
        teleport(player, ability.getRange());

        // Create particles along the path and damage entities using the helper method
        teleportEffect(prev, player.getLocation(), org.bukkit.Color.fromRGB(0, 12, 89), player, 5 * ability.getStrength(), 1);
    }


    public static void surgeTeleport(PlayerInteractEvent event, Player player, ClassAbility ability) {
        Location prev = player.getLocation();

        teleport(player, ability.getRange());

        teleportEffect(prev, player.getLocation(), org.bukkit.Color.fromRGB(102, 0, 126), player, 10 * ability.getStrength(), 2);

    }

    public static void arcaneTeleport(PlayerInteractEvent event, Player player, ClassAbility ability) {
        
        darkTeleport(event, player, ability);
    }

    public static void manaBallAbility(PlayerInteractEvent event, Player player, ClassAbility ability) {
        // Create the projectile
        Snowball projectile = player.launchProjectile(Snowball.class);
        projectile.setGravity(false);

        // Set projectile properties
        projectile.setVelocity(player.getLocation().getDirection().multiply(1.5)); // Speed of the projectile

        // Apply metadata to the projectile so we can identify it later as a mana projectile
        projectile.setMetadata("mana_projectile", new FixedMetadataValue(EvoCraft.getPlugin(), ability.getStrength()));

        new BukkitRunnable(){
            int a = 0;
            final Location start = player.getLocation().clone();
            final int range = ability.getRange();
            @Override
            public void run() {
                if (a >= 20 || start.distance(projectile.getLocation()) > range || projectile.isDead()) {
                    projectile.remove();
                    cancel();
                    return;
                }
                // spawn some end rod particles and some teal colored "mana" redstone particles

                player.getWorld().spawnParticle(Particle.END_ROD, projectile.getLocation(), 1, 0.1, 0, 0.1, 0);
                player.getWorld().spawnParticle(Particle.REDSTONE, projectile.getLocation(), 1, 0, 0.1, 0,
                        new Particle.DustOptions(Color.fromRGB(0, 255, 255), 1));
                a++;
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0, 2);
    }

    public static void manaBallDamage(@NotNull ProjectileHitEvent event, int strength){
        // Logic to deal damage
        if (event.getHitEntity() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) event.getHitEntity();
            target.damage(5.0*strength, (Entity) event.getEntity().getShooter()); // Deal 5 points of damage, customize as needed
            // Additional effects can be added here, like knockback or status effects
        }
        // Remove the projectile upon impact
        event.getEntity().remove();
    }

    public static void riftBeamAbility(PlayerInteractEvent event, Player player, ClassAbility ability) {

    }

    public static void runeShieldAbility(PlayerInteractEvent event, Player player, ClassAbility ability) {

    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface SaveState {
    }
}