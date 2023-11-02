package me.zenox.evocraft.abilities;

import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.util.Geo;
import me.zenox.evocraft.util.TriConsumer;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
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
    private int strength;

    @SaveState
    private int charges;

    @SaveState
    private int range;

    private final Map<String, Object> baseState;

    public ClassAbility(AbilitySettings settings, TriConsumer<PlayerInteractEvent, Player, ItemStack> executable) {
        super(settings.getId(), settings.getManaCost(), settings.getCooldown(), settings.isPassive(), executable);
        this.modifiers = settings.getModifiers();
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

        // TODO: fetch level from player's ability tree
        applyModifiers(modifiers.size()); // apply modifiers to the ability

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
        this.runExecutable(null, p, null);
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

    public static void teleportAbility(PlayerInteractEvent event, Player player, ItemStack itemStack) {
        // Teleport the player up to 5 blocks forward, but less if it's a block
        int maxDistance = 5;
        int distance = 0;
        while (distance < maxDistance) {
            if (player.getLocation().clone().add(player.getLocation().getDirection().multiply(distance)).getBlock().getType().isSolid()) {
                break;
            }
            distance++;
        }
        player.teleport(player.getLocation().clone().add(player.getLocation().getDirection().multiply(distance)));

        // Create particles along the path
        List<Vector> tracedPath = Geo.lerpEdges(List.of(player.getLocation().toVector(), player.getLocation().clone().add(player.getLocation().getDirection().multiply(distance)).toVector()), 15);
        new BukkitRunnable(){
            int a = 0;
            @Override
            public void run() {
                if (a > tracedPath.size()) {
                    cancel();
                    return;
                }
                player.getWorld().spawnParticle(Particle.REDSTONE, tracedPath.get(a).toLocation(player.getWorld()), 5, 0, 1.8, 0, 0, new Particle.DustOptions(org.bukkit.Color.fromRGB(0, 123, 255), 3));
                a++;
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0, 1);
    }

    /**
     * Dark Teleport Executable that damages entities along the path
     * @param event
     * @param player
     * @param itemStack
     */
    public static void darkTeleport(PlayerInteractEvent event, Player player, ItemStack itemStack) {
        // Teleport the player up to 5 blocks forward, but less if it's a block
        int maxDistance = 5;
        int distance = 0;
        while (distance < maxDistance) {
            if (player.getLocation().clone().add(player.getLocation().getDirection().multiply(distance)).getBlock().getType().isSolid()) {
                break;
            }
            distance++;
        }
        player.teleport(player.getLocation().clone().add(player.getLocation().getDirection().multiply(distance)));

        // Create particles along the path
        List<Vector> tracedPath = Geo.lerpEdges(List.of(player.getLocation().toVector(), player.getLocation().clone().add(player.getLocation().getDirection().multiply(distance)).toVector()), 15);
        new BukkitRunnable(){
            int a = 0;
            @Override
            public void run() {
                if (a > tracedPath.size()) {
                    cancel();
                    return;
                }
                player.getWorld().spawnParticle(Particle.REDSTONE, tracedPath.get(a).toLocation(player.getWorld()), 5, 0, 1.8, 0, 0, new Particle.DustOptions(org.bukkit.Color.fromRGB(0, 123, 255), 3));
                a++;
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0, 1);

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface SaveState {
    }
}