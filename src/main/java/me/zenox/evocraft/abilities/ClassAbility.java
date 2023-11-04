package me.zenox.evocraft.abilities;

import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.data.PlayerData;
import me.zenox.evocraft.data.PlayerDataManager;
import me.zenox.evocraft.util.Geo;
import me.zenox.evocraft.util.TriConsumer;
import me.zenox.evocraft.util.Util;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassAbility extends Ability<PlayerInteractEvent> {
    final List<Modifier> modifiers;
    @SaveState
    protected TriConsumer<PlayerInteractEvent, Player, ClassAbility> executable;

    @SaveState
    private int strength;
    @SaveState
    private int charges;
    @SaveState
    private int range;

    private final Map<String, Object> baseState;

    public ClassAbility(AbilitySettings settings, TriConsumer<PlayerInteractEvent, Player, ClassAbility> executable) {
        super(settings.getId(), settings.getManaCost(), settings.getCooldown(), settings.isPassive());
        this.executable = executable;
        this.modifiers = settings.getModifiers();

        // stats
        this.strength = settings.getStrength();
        this.charges = settings.getCharges();
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


    // Override the useAbility method to consider the modified mana cost, actually dont use this for now
    // TODO: should abilities only be used through the directUseAbility method?
    @Override
    public void useAbility(Event event) {
        PlayerInteractEvent e = (PlayerInteractEvent) event;
        Player p = e.getPlayer();

        PlayerData data = PlayerDataManager.getInstance().getPlayerData(p.getUniqueId());

        // TODO: fetch level from player's ability tree
        int level = data.getPathLevel(data.getPlayerClass().tree().path(this));
        if(level == -1) {
            Util.sendMessage(p, "&cYou have not unlocked ability %s yet!".formatted(this.getId()));
            return; // ability is not unlocked
        }

        Util.sendMessage(p, "&aUsing ability %s at level %d".formatted(this.getId(), level));

        applyModifiers(level); // apply modifiers to the ability

        // Check if ability is on cooldown
        if (isAbilityOnCooldown(p)) {
            sendCooldownMessage(p);
            return;
        }

        if (notEnoughMana(p, this.getManaCost())) {
            sendManaInsufficientMessage(p);
            return;
        }

        deductMana(p, this.getManaCost());
        this.executable.accept(e, p, this);
        setAbilityCooldown(p);

        reset(); // reset the ability to its original state

    }

    public void applyModifiers(int level){
        for(int i = 0; i < level; i++){
            modifiers.get(i).modify(this);
        }
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

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public static void teleportAbility(PlayerInteractEvent event, Player player, ClassAbility ability) {
        // Teleport the player up to 5 blocks forward, but less if it's a block
        int maxDistance = ability.getRange();
        Util.sendMessage(player, "&aTeleporting %d blocks".formatted(maxDistance));
        int distance = 0;
        Location prev = player.getLocation();

        while (distance < maxDistance) {
            if (player.getLocation().clone().add(player.getLocation().getDirection().multiply(distance)).getBlock().getType().isSolid()) {
                break;
            }
            distance++;
        }
        player.teleport(player.getLocation().clone().add(player.getLocation().getDirection().multiply(distance)));

        // Create particles along the path
        List<Vector> tracedPath = Geo.lerpEdges(List.of(prev.toVector(), player.getLocation().toVector()), (int) (1.3*ability.getRange()));
        new BukkitRunnable(){
            int a = 0;
            @Override
            public void run() {
                if (a >= tracedPath.size()/2) {
                    cancel();
                    return;
                }
                player.getWorld().spawnParticle(Particle.REDSTONE, tracedPath.get(a).toLocation(player.getWorld()),
                        5, 0, 0.5, 0, 0,
                        new Particle.DustOptions(org.bukkit.Color.fromRGB(0, 123, 255), 2));
                a++;
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0, 1);
    }

    /**
     * Dark Teleport Executable that damages entities along the path
     */
    public static void darkTeleport(PlayerInteractEvent event, Player player, ClassAbility ability) {
        // Teleport the player up to 5 blocks forward, but less if it's a block
        int maxDistance = ability.getRange();
        Util.sendMessage(player, "&aTeleporting %d blocks".formatted(maxDistance));
        int distance = 0;
        Location prev = player.getLocation();

        while (distance < maxDistance) {
            if (player.getLocation().clone().add(player.getLocation().getDirection().multiply(distance)).getBlock().getType().isSolid()) {
                break;
            }
            distance++;
        }
        player.teleport(player.getLocation().clone().add(player.getLocation().getDirection().multiply(distance)));

        // Create particles along the path
        List<Vector> tracedPath = Geo.lerpEdges(List.of(prev.toVector(), player.getLocation().toVector()), (int) (1.3*ability.getRange()));
        new BukkitRunnable(){
            int a = 0;
            @Override
            public void run() {
                if (a >= tracedPath.size()/2) {
                    cancel();
                    return;
                }
                player.getWorld().getNearbyEntities(tracedPath.get(a).toLocation(player.getWorld()), 1, 1, 1).forEach(entity -> {
                    if (entity instanceof Damageable && !entity.equals(player)) {
                        ((Damageable) entity).damage(5 * ability.getStrength());
                    }
                });
                player.getWorld().spawnParticle(Particle.REDSTONE, tracedPath.get(a).toLocation(player.getWorld()), 5, 0, 0.5, 0, 0, new Particle.DustOptions(org.bukkit.Color.fromRGB(0, 12, 89), 3));
                a++;
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0, 1);

    }


    public static void surgeTeleport(PlayerInteractEvent event, Player player, ClassAbility ability) {
        darkTeleport(event, player, ability);
    }

    public static void arcaneTeleport(PlayerInteractEvent event, Player player, ClassAbility ability) {
        
        darkTeleport(event, player, ability);
    }

    public TriConsumer<PlayerInteractEvent, Player, ClassAbility> getExecutable() {
        return this.executable;
    }

    public void setExecutable(TriConsumer<PlayerInteractEvent, Player, ClassAbility> executable) {
        this.executable = executable;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface SaveState {
    }
}