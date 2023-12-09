package me.zenox.evocraft.abilities;

import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.data.PlayerData;
import me.zenox.evocraft.data.PlayerDataManager;
import me.zenox.evocraft.util.Geo;
import me.zenox.evocraft.util.TriConsumer;
import me.zenox.evocraft.util.Util;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
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
                Util.sendActionBar(p, "&c&l%s LOCKED!".formatted(this.getDisplayName().toUpperCase()));
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

            String chargeMsg = "";
            if (this.getCharges() > 0) {
                int chargesLeft = Ability.cooldownManager.consumeCharge(p, this);
                chargeMsg = " &6(&e%d&6/%d) (-1)".formatted(chargesLeft, this.getCharges());
            }
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

    // Constants for the homing behavior
    private static final double ACCELERATION_RATE = 0.2;
    private static final double MAX_TURN_RATE = Math.toRadians(7);

    public static void manaBallAbility(PlayerInteractEvent event, Player player, ClassAbility ability) {
        Snowball projectile = createProjectile(player, ability);
        startParticleTask(player, projectile, ability.getRange()).runTaskTimer(EvoCraft.getPlugin(), 0, 2);
    }

    public static void homingManaBall(PlayerInteractEvent event, Player player, ClassAbility ability) {
        Snowball projectile = createProjectile(player, ability);
        startHomingTask(player, projectile, ability.getRange(), false).runTaskTimer(EvoCraft.getPlugin(), 0, 2);
    }

    public static void multishotManaBall(PlayerInteractEvent event, Player player, ClassAbility ability) {
        // Here you can define how many projectiles you want to shoot
        int numberOfProjectiles = 3;
        for (int i = 0; i < numberOfProjectiles; i++) {
            Snowball projectile = createProjectile(player, ability);
            // Let's keep the projectiles straight but instead shoot them in parallel with offsets
            Vector direction = projectile.getLocation().getDirection().setY(0).normalize();
            Vector perpendicular = direction.rotateAroundY(Math.toRadians(90));
            Vector offset = perpendicular.multiply(((i - numberOfProjectiles/2)*1.5));
            projectile.teleport(projectile.getLocation().add(offset));
            startHomingTask(player, projectile, ability.getRange(), false).runTaskTimer(EvoCraft.getPlugin(), 0, 2);
        }
    }

    private static Snowball createProjectile(Player player, ClassAbility ability) {
        Snowball projectile = player.launchProjectile(Snowball.class);
        projectile.setGravity(false);
        projectile.setVelocity(player.getLocation().getDirection().multiply(1.5));
        projectile.setMetadata("mana_projectile", new FixedMetadataValue(EvoCraft.getPlugin(), ability.getStrength()));
        return projectile;
    }

    private static BukkitRunnable startParticleTask(Player player, Snowball projectile, int range) {
        return new BukkitRunnable() {
            final Location start = player.getLocation().clone();
            @Override
            public void run() {
                if (start.distance(projectile.getLocation()) > range || !projectile.isValid()) {
                    projectile.remove();
                    cancel();
                    return;
                }
                spawnParticles(projectile.getLocation());
            }
        };
    }

    private static BukkitRunnable startHomingTask(Player player, Snowball projectile, int range, boolean down) {
        return new BukkitRunnable() {
            final Location start = player.getLocation().clone();
            @Override
            public void run() {
                if (start.distance(projectile.getLocation()) > range || !projectile.isValid()) {
                    projectile.remove();
                    cancel();
                    return;
                }
                LivingEntity target = findNearestTarget(projectile, player);
                if (target != null) {
                    adjustProjectileVelocity(projectile, target, down);
                }
                spawnParticles(projectile.getLocation());
            }
        };
    }

    private static void spawnParticles(Location location) {
        location.getWorld().spawnParticle(Particle.END_ROD, location, 1, 0.1, 0, 0.1, 0);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 0, 0.1, 0,
                new Particle.DustOptions(Color.fromRGB(0, 255, 255), 1));
    }

    private static void adjustProjectileVelocity(Snowball projectile, LivingEntity target, boolean downwards) {
        Vector currentVelocity = projectile.getVelocity();
        Vector targetDirection = target.getEyeLocation().toVector().subtract(projectile.getLocation().toVector());
        double currentHorizontalSpeed = Math.sqrt(currentVelocity.getX() * currentVelocity.getX() + currentVelocity.getZ() * currentVelocity.getZ());

        Vector velocityAdjustment = targetDirection.clone().subtract(currentVelocity).normalize();
        Vector adjustedVelocity = currentVelocity.clone().add(velocityAdjustment);

        if (downwards) {
            // Keep the horizontal velocity components the same to maintain horizontal speed
            double adjustedY = adjustedVelocity.getY() - ACCELERATION_RATE * 1.1; // Slightly increase the downward acceleration
            adjustedVelocity = new Vector(currentVelocity.getX(), adjustedY, currentVelocity.getZ());
        } else {
            if (currentVelocity.angle(adjustedVelocity) > MAX_TURN_RATE) {
                adjustedVelocity = rotateVectorTowards(currentVelocity, targetDirection.normalize(), MAX_TURN_RATE);
            }
            adjustedVelocity = adjustedVelocity.normalize().multiply(currentHorizontalSpeed).setY(adjustedVelocity.getY());
        }

        projectile.setVelocity(adjustedVelocity);
    }


    private static Vector rotateVectorTowards(Vector source, Vector target, double maxAngle) {
        double angle = source.angle(target);
        if (angle < maxAngle) return target; // No need to rotate if within max angle

        double theta = Math.min(angle, maxAngle) / angle;
        return source.clone().multiply(1 - theta).add(target.clone().multiply(theta)).normalize();
    }

    private static LivingEntity findNearestTarget(Entity projectile, Player caster) {
        Location location = projectile.getLocation();
        double minDistance = Double.MAX_VALUE;
        LivingEntity nearest = null;

        for (Entity entity : location.getWorld().getNearbyEntities(location, 10, 20, 10)
                 .stream()
                 .filter(entity -> entity instanceof LivingEntity && ((LivingEntity) entity).hasLineOfSight(projectile)).toList()) {
            if (entity instanceof LivingEntity && !entity.equals(caster)) {
                double distance = entity.getLocation().distance(location);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = (LivingEntity) entity;
                }
            }
        }
        return nearest;
    }

    public static void manaBallDamage(@NotNull ProjectileHitEvent event, int strength){
        // Logic to deal damage
        if (event.getHitEntity() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) event.getHitEntity();
            target.damage(5.0*strength, (Entity) event.getEntity().getShooter()); // Deal 5 points of damage, customize as needed
            target.setNoDamageTicks(0);
            target.setFreezeTicks(target.getFreezeTicks()+strength*20);
        }
        // Remove the projectile upon impact
        event.getEntity().remove();
    }

    public static void multishotArcaneSingularity(PlayerInteractEvent event, Player player, ClassAbility ability) {
        int numberOfProjectiles = 3;
        double spreadAngle = 15; // Angle between each projectile

        Vector baseDirection = player.getLocation().getDirection().normalize();
        double basePitch = baseDirection.getY(); // Store the original pitch

        for (int i = 0; i < numberOfProjectiles; i++) {
            // Calculate the yaw offset by spreading the projectiles evenly
            double yawOffsetDegrees = spreadAngle * (i - (numberOfProjectiles - 1) / 2.0);
            Vector direction = rotateVectorAroundY(baseDirection.setY(0).normalize(), yawOffsetDegrees);
            direction.setY(basePitch); // Set the original pitch back to the direction vector
            direction.normalize(); // Normalize the vector after adjusting pitch

            Snowball projectile = player.launchProjectile(Snowball.class);
            projectile.setVelocity(direction.multiply(3.0));
            projectile.setMetadata("arcane_singularity", new FixedMetadataValue(EvoCraft.getPlugin(), true));
            Location prev = projectile.getLocation().clone();

            new BukkitRunnable() {
                final int abilityRange = ability.getRange();
                @Override
                public void run() {
                    if (projectile.isDead() || projectile.isOnGround() || reachMaxRange(projectile, prev, abilityRange)) {
                        createSingularity(projectile.getLocation(), ability, player);
                        this.cancel(); // Stop the task after the singularity has been created
                    }
                    spawnParticles(projectile.getLocation());
                }
            }.runTaskTimer(EvoCraft.getPlugin(), 0L, 1L);

            startHomingTask(player, projectile, ability.getRange(), true).runTaskTimer(EvoCraft.getPlugin(), 0, 2);
        }
    }

    private static Vector rotateVectorAroundY(Vector vector, double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;
        return new Vector(x, vector.getY(), z);
    }

    private static boolean reachMaxRange(Projectile projectile, Location initial, int range) {
        return projectile.getLocation().distance(initial) >= range;
    }

    private static void createSingularity(Location location, ClassAbility ability, Player player) {
        // Create a singularity effect at the location
        int range = ability.getRange()/3;
        int duration = ability.getRange()*20/3;
        spawnSingularityParticles(location, duration, range);
        applySingularityEffects(location, duration, range, player);

        // Schedule to end the singularity effect after its lifetime
        Bukkit.getScheduler().runTaskLater(EvoCraft.getPlugin(), () -> {
            endSingularity(location, ability);
        }, duration); // Duration based on ability settings
    }

    private static void spawnSingularityParticles(Location loc, int duration, int range) {
        // Spawn particles to indicate the singularity
        // Placeholder for actual particle effect code
        final double RADIUS = range;
        final double DELTA_ANGLE = Math.PI / 16;

        Location location = loc.clone().add(0, 1, 0).setDirection(new Vector(0, 0, 0));
        new BukkitRunnable(){
            double angle = 0;
            int a = 0;
            @Override
            public void run(){
                if (a > duration) {
                    cancel();
                    return;
                }
                spawnParticles(location.clone().add(Math.cos(angle) * RADIUS, 0, Math.sin(angle) * RADIUS));
                spawnParticles(location.clone().add(Math.cos(angle + Math.PI) * RADIUS, 0, Math.sin(angle + Math.PI) * RADIUS));
                spawnParticles(location.clone().add(0, RADIUS + Math.sin(angle) * RADIUS / 2, 0));
                angle += DELTA_ANGLE;

                if (angle % (Math.PI / 2) < DELTA_ANGLE)
                    location.getWorld().playSound(location.clone().add(0, RADIUS + Math.sin(angle) * RADIUS / 2, 0), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 0);
                a++;
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0L, 1L);
    }


    private static void applySingularityEffects(Location location, int duration, int range, Player p) {
        final int radius = range;
        final double pullStrength = 0.2; // Example pull strength
        final double gravityEffect = 0.3; // Example additional gravity effect

        new BukkitRunnable() {
            int a = 0;
            @Override
            public void run() {
                if (a > duration) {
                    cancel();
                    return;
                }
                location.getWorld().getNearbyEntities(location, radius, radius, radius).forEach(entity -> {
                    if (entity instanceof LivingEntity livingEntity && !entity.equals(p)) {
                        double distance = location.distance(livingEntity.getLocation());

                        // Apply slowness effect
                        int effectStrength = (int) (distance / radius * 4); // Scale from 0 to 4
                        if (effectStrength > 0) {
                            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, effectStrength, false, false, false));
                        }

                        livingEntity.setFreezeTicks(livingEntity.getFreezeTicks()+15);

                        // Apply a grounding effect
                        if (livingEntity.isOnGround()) {
                            // Player is on the ground, possibly apply a minor pull
                            if (distance > radius) {
                                Vector towardsCenter = location.toVector().subtract(livingEntity.getLocation().toVector()).normalize();
                                livingEntity.setVelocity(towardsCenter.multiply(pullStrength));
                            }
                        } else {
                            // Player is in the air, apply downward force to simulate increased gravity
                            Vector currentVelocity = livingEntity.getVelocity();
                            livingEntity.setVelocity(new Vector(currentVelocity.getX(),
                                    Math.max(-gravityEffect, currentVelocity.getY() - gravityEffect),
                                    currentVelocity.getZ()));
                        }
                    }
                });
                a+= 5;
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0L, 5L); // Repeat every second
    }


    private static void endSingularity(Location location, ClassAbility ability) {
        // Logic to remove the singularity effects and clean up
        // For example, removing potion effects from players in the area
        location.getWorld().getNearbyEntities(location, ability.getRange(), ability.getRange(), ability.getRange())
                .stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .forEach(player -> player.removePotionEffect(PotionEffectType.SLOW));
    }

    /**
     * Casts the base Rift Beam ability.
     * The beam damages and pierces through enemies in a straight line from the player's position.
     *
     * @param event   The player interaction event.
     * @param player  The player casting the ability.
     * @param ability The class ability information.
     */
    public static void riftBeamAbility(PlayerInteractEvent event, Player player, ClassAbility ability) {
        // 1. Calculate the beam trajectory from the player's eye location in the direction they are looking.
        Location start = player.getEyeLocation();
        Location end = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(ability.getRange()));
        // 2. Spawn particles along this line to simulate the beam effect.
        // 3. Check for entities in the beam's path and apply damage to them.
        alongPath(start, end, location -> {
            location.getWorld().spawnParticle(Particle.END_ROD, location, 1, 0.1, 0, 0.1, 0);
            player.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0, 0, 0,
                    new Particle.DustOptions(Color.fromRGB(144, 0, 255), 1));
            player.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5).forEach(entity -> {
                if (entity instanceof Damageable && !entity.equals(player)) {
                    ((Damageable) entity).damage(ability.getStrength()*5);
                }
            });
        });
    }

    private static void alongPath(Location start, Location end, Consumer<Location> loc){
        List<Vector> tracedPath = Geo.lerpEdges(List.of(start.toVector(), end.toVector()), (int) (1.3 * start.distance(end)));
        tracedPath = tracedPath.subList(0, tracedPath.size()/2);
        tracedPath.forEach(vector -> {
            loc.accept(vector.toLocation(start.getWorld()));
        });
    }

    /**
     * Applies a mark to enemies hit by the Rift Beam.
     * Marked enemies take increased damage from all sources for a short duration.
     *
     * @param event   The player interaction event.
     * @param player  The player casting the ability.
     * @param ability The class ability information.
     */
    public static void riftBeamMark(PlayerInteractEvent event, Player player, ClassAbility ability) {

        Location start = player.getEyeLocation();
        Location end = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(ability.getRange()));
        // Spawn particles along this line to simulate the beam effect.
        // Check for entities in the beam's path and apply damage to them.
        alongPath(start, end, location -> {
            location.getWorld().spawnParticle(Particle.END_ROD, location, 1, 0.1, 0, 0.1, 0);
            player.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0, 0, 0,
                    new Particle.DustOptions(Color.fromRGB(144, 0, 255), 1));
            player.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5).forEach(entity -> {
                if (entity instanceof LivingEntity lEntity && !entity.equals(player)) {
                    lEntity.damage(ability.getStrength()*5);

                    // Apply a potion effect to the target
                    lEntity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20*ability.getStrength(), 0));

                }
            });
        });
    }

    /**
     * Chains the Rift Beam to additional nearby enemies after hitting the initial target.
     *
     * @param event   The player interaction event.
     * @param player  The player casting the ability.
     * @param ability The class ability information.
     */
    public static void riftBeamChain(PlayerInteractEvent event, Player player, ClassAbility ability) {
        double distance = ability.getRange();
        Set<LivingEntity> explored = new HashSet<>();

        Location start = player.getEyeLocation();
        Vector direction = start.getDirection().normalize();

        Optional<LivingEntity> hitEntity = findFirstEntity(start, direction, distance, player);

        if (hitEntity.isPresent()) {
            LivingEntity target = hitEntity.get();
            target.damage(ability.getStrength() * 5);
            explored.add(target);
            createParticlePath(start, target.getLocation(), player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    chainToClosestEntities(target, player, ability, explored, distance, 1);
                }
            }.runTaskLater(EvoCraft.getPlugin(), 5L);

        // create visual particle path to show that it was casted, but nothing was hit
        } else {
            Location end = findBeamHitLocation(start, direction, distance);
            createParticlePath(start, findBeamHitLocation(start, direction, distance), player);
            end.getWorld().spawnParticle(Particle.REDSTONE, end, 10, 0.2, 0.2, 0.2,
                    new Particle.DustOptions(Color.fromRGB(144, 0, 255), 1));
            end.getWorld().spawnParticle(Particle.END_ROD, end, 10, 0.2, 0.2, 0.2);
        }

    }

    private static Optional<LivingEntity> findFirstEntity(Location start, Vector direction, double distance, Player player) {
        double stepSize = 0.5;
        Vector stepVector = direction.clone().normalize().multiply(stepSize);

        for (double i = 0; i <= distance; i += stepSize) {
            Location currentLoc = start.clone().add(stepVector.clone().multiply(i));

            // Check if a block is hit
            if (currentLoc.getBlock().getType() != Material.AIR) {
                return Optional.empty();
            }

            List<LivingEntity> nearbyEntities = (List<LivingEntity>) currentLoc.getNearbyLivingEntities(1);
            for (LivingEntity entity : nearbyEntities) {
                if (!entity.equals(player)) {
                    return Optional.of(entity);
                }
            }
        }
        return Optional.empty();
    }

    private static Location findBeamHitLocation(Location start, Vector direction, double distance) {
        double stepSize = 0.5;
        Vector stepVector = direction.clone().normalize().multiply(stepSize);
        Location currentLoc = start.clone();

        for (double i = 0; i <= distance; i += stepSize) {
            currentLoc.add(stepVector);
            if (currentLoc.getBlock().getType() != Material.AIR) {
                return currentLoc;
            }
        }
        return start.clone().add(direction.multiply(distance));
    }

    private static void chainToClosestEntities(LivingEntity initialTarget, Player player, ClassAbility ability, Set<LivingEntity> explored, double remainingDistance, double damageMultiplier) {

        double closestDistanceSquared = Double.MAX_VALUE;
        LivingEntity closestEntity = null;


        // Find the closest entity within the remaining distance
        for (LivingEntity entity : initialTarget.getLocation().getWorld().getNearbyLivingEntities(initialTarget.getLocation(), remainingDistance)) {
            if (!entity.equals(player) && !explored.contains(entity)) {
                double distanceSquared = entity.getLocation().distanceSquared(initialTarget.getLocation());
                if (distanceSquared < closestDistanceSquared) {
                    closestDistanceSquared = distanceSquared;
                    closestEntity = entity;
                }
            }
        }

        // If a valid closest entity is found
        if (closestEntity != null && initialTarget.hasLineOfSight(closestEntity)) {
            // Apply reduced damage to the closest entity
            closestEntity.damage(ability.getStrength() * 5 * damageMultiplier);
            explored.add(closestEntity);

            // Create particle effects from the last target to the new one
            createParticlePath(initialTarget.getEyeLocation(), closestEntity.getEyeLocation(), player);
            final LivingEntity finalClosestEntity = closestEntity;

            // Calculate remaining distance and continue chaining if possible
            double distanceToNextTarget = Math.sqrt(closestDistanceSquared);
            if (remainingDistance - distanceToNextTarget > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        chainToClosestEntities(finalClosestEntity, player, ability, explored, remainingDistance - distanceToNextTarget, damageMultiplier * 0.8); // reduce damage by 10% each chain
                    }
                }.runTaskLater(EvoCraft.getPlugin(), 5L);
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
            }
        }.runTaskLater(EvoCraft.getPlugin(), 5L); // 20L represents a delay of 1 second (20 ticks)
    }

    private static void createParticlePath(Location start, Location end, Player player) {
        alongPath(start, end, location -> {

            location.getWorld().spawnParticle(Particle.END_ROD, location, 1, 0.1, 0, 0.1, 0);
            location.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0, 0, 0,
                    new Particle.DustOptions(Color.fromRGB(144, 0, 255), 1));
        });
    }

    /**
     * The ultimate upgrade of the Rift Beam ability.
     * Charges up to unleash a devastating blast with one main beam.
     *
     * @param event   The player interaction event.
     * @param player  The player casting the ability.
     * @param ability The class ability information.
     */
    public static void riftBeamApex(PlayerInteractEvent event, Player player, ClassAbility ability) {
        // Start charging
        startCharge(player);

        // Schedule task to check for charge completion
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isFullyCharged(player)) {
                    // On full charge, release the beams
                    releaseMainBeam(player, ability);

                    this.cancel(); // Stop the task
                }
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0L, 1L); // Check every tick
    }

    public static void startCharge(Player player) {
        // Display charging particles or effects around the player.
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 10, 0.5, 0.5, 0.5, 0);
        // Record the start time of the charge.
        player.setMetadata("charge_start_time", new FixedMetadataValue(EvoCraft.getPlugin(), System.currentTimeMillis()));

        // Start a task that increases the pitch of the sound and sends a message to the action bar as the charge progresses
        new BukkitRunnable() {
            float pitch = 0.5f; // Start pitch
            @Override
            public void run() {
                if (!isFullyCharged(player)) {
                    // Increase the pitch and play the sound
                    pitch += 0.05f; // Increase pitch
                    player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, pitch);

                    // Send a message to the action bar
                    player.sendActionBar(ChatColor.GREEN + "Charging... " + Math.round(pitch * 100) + "%");
                } else {
                    // Stop the task when the charge is complete
                    this.cancel();
                }
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0L, 20L); // Run every second (20 ticks)
    }

    public static boolean isFullyCharged(Player player) {
        // Check if the required charge time has elapsed.
        long chargeStartTime = player.getMetadata("charge_start_time").get(0).asLong();
        long chargeDuration = System.currentTimeMillis() - chargeStartTime;
        // Return true if fully charged, false otherwise.
        return chargeDuration >= 5000; // Assuming 5 seconds as the required charge time
    }

    public static void releaseMainBeam(Player player, ClassAbility ability) {
        // Create and launch the main beam.
        Location start = player.getEyeLocation();
        Location end = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(ability.getRange() * 2)); // Assuming the main beam has twice the range

        new BukkitRunnable() {
            int ticks = 0; // Count the number of ticks
            @Override
            public void run() {
                if (ticks < 20) { // Run for 20 ticks (1 second)
                    alongPath(start, end, location -> {
                        // Spawn particles for visual effects
                        location.getWorld().spawnParticle(Particle.END_ROD, location, 10, 0.5, 0.5, 0.5, 0); // Increase the count and speed for a bigger beam
                        location.getWorld().spawnParticle(Particle.SQUID_INK, location, 10, 0.5, 0.5, 0.5, 0); // Use SQUID_INK particles for sonic effect

                        // Deal damage to nearby entities
                        player.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5).forEach(entity -> {
                            if (entity instanceof Damageable && !entity.equals(player)) {
                                ((Damageable) entity).damage(ability.getStrength() * 10); // Assuming the main beam deals 10 times the ability's strength as damage
                            }
                        });
                    });
                    ticks++;
                } else {
                    // Stop the task after 1 second
                    this.cancel();
                }
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0L, 1L); // Run every tick (1/20th of a second)
    }


    public static void runeShieldAbility(PlayerInteractEvent event, Player player, ClassAbility ability) {

    }
    public static void bloodlustAbility(PlayerInteractEvent event, Player player, ClassAbility ability){
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 1) );
    }

    public static void tripleSlashAbility(PlayerInteractEvent event, Player player, ClassAbility ability) {

    }

    public static void bullRushAbility(PlayerInteractEvent event, Player player, ClassAbility ability) {

    }
    public static ArrayList<Player> counterStrikeActive = new ArrayList<>();
    public static void counterstrikeAbility(PlayerInteractEvent event, Player player, ClassAbility ability) {
        counterStrikeActive.add(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 200));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 50, 200));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 50, 200));
        new BukkitRunnable(){
            @Override
            public void run() {
                counterStrikeActive.remove(player);
            }
        }.runTaskLater(EvoCraft.getPlugin(), 50);
    }

    public static void agilityBloodlust(PlayerInteractEvent event, Player player, ClassAbility ability) {

    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface SaveState {
    }
}