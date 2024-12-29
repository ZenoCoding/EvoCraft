package me.zenox.evocraft.abilities;

import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the cooldowns and charges for abilities
 */
public class CooldownManager implements Listener {
    private final Map<UUID, Map<ClassAbility, Integer>> chargeMap = new HashMap<>();
    private final Map<UUID, Map<ClassAbility, BukkitRunnable>> rechargeTasks = new HashMap<>();

    public CooldownManager() {
        Bukkit.getPluginManager().registerEvents(this, EvoCraft.getPlugin());
    }

    public boolean isOnCooldown(Player player, ClassAbility ability) {
        return getCharges(player, ability) == 0;
    }

    public int getCharges(@NotNull Player player, ClassAbility ability) {
        chargeMap.putIfAbsent(player.getUniqueId(), new HashMap<>());
        return chargeMap.get(player.getUniqueId()).getOrDefault(ability, ability.getCharges());
    }

    /**
     * Consumes a charge of the ability and returns the amount of charges left
     * @param player
     * @param ability
     * @return the amount of charges left
     */
    public int consumeCharge(@NotNull Player player, ClassAbility ability) {
        if (isOnCooldown(player, ability))
            throw new IllegalStateException("You cannot use an ability when you have zero charges left.");
        Map<ClassAbility, Integer> playerCharges = chargeMap.get(player.getUniqueId());
        playerCharges.put(ability, getCharges(player, ability) - 1);
        rechargeAbility(player, ability);
        return getCharges(player, ability);
    }

    public void rechargeAbility(Player player, @NotNull ClassAbility ability) {
        UUID playerId = player.getUniqueId();
        chargeMap.putIfAbsent(playerId, new HashMap<>());
        rechargeTasks.putIfAbsent(playerId, new HashMap<>());

        if (rechargeTasks.get(playerId).containsKey(ability)) {
            // A recharge task is already running for this ability
            return;
        }

        int chargeTime = ability.getChargeTime() * 20;
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                ability.executeAsPlayer(player, (ability) -> {
                    if (getCharges(player, ability) < ability.getCharges()) {
                        chargeMap.get(playerId).put(ability, getCharges(player, ability) + 1);
                        Util.sendActionBar(player, " &6(+1 %s charge) (%d/%d)".formatted(ability.getDisplayName(), getCharges(player, ability), ability.getCharges()));
                    } else {
                        cancel();
                        rechargeTasks.get(playerId).remove(ability);
                    }

                    if (ability.getChargeTime() * 20 != chargeTime) {
                        cancel();
                        rechargeTasks.get(playerId).remove(ability);
                        this.runTaskTimer(EvoCraft.getPlugin(), ability.getChargeTime(), ability.getChargeTime());
                    }
                });
            }
        };

        rechargeTasks.get(playerId).put(ability, task);
        task.runTaskTimer(EvoCraft.getPlugin(), chargeTime, chargeTime);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        UUID playerId = e.getPlayer().getUniqueId();
        chargeMap.remove(playerId);
        if (rechargeTasks.containsKey(playerId)) {
            for (BukkitRunnable task : rechargeTasks.get(playerId).values()) {
                task.cancel(); // Cancel any running tasks
            }
            rechargeTasks.remove(playerId);
        }
    }
}
