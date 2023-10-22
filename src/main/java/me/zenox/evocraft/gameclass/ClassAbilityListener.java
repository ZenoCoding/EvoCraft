package me.zenox.evocraft.gameclass;

import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.item.ComplexItemStack;
import me.zenox.evocraft.util.Util;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ClassAbilityListener implements Listener {
    private final EvoCraft plugin;
    // null = SHIFT, l = LEFT_CLICK, r = RIGHT_CLICK
    private static final HashMap<Player, Character> actionMap = new HashMap<>();

    public ClassAbilityListener(EvoCraft plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClassSneak(PlayerToggleSneakEvent event) {

        if (event.isSneaking()){
            // initiator
            Player player = event.getPlayer();

            // filter by player's class
            if (GameClass.getClass(player) == null) return;
            if (!GameClass.getClass(player).items().contains(ComplexItemStack.of(player.getEquipment().getItemInMainHand()).getComplexItem().getType())) return;
            // Prime the ability action bar for PlayerInteractEvent
            Util.sendActionBar(event.getPlayer(), "&bSHIFT&7-&b_&7-&b_");
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 0.5f);
            actionMap.put(player, null);

            // remove after 1 second unless another action is taken
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (actionMap.get(player) == null)
                        actionMap.remove(player);
                }
            }.runTaskLater(plugin, 20);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        // filter by player's class
        if (GameClass.getClass(player) == null) return;
        if (!GameClass.getClass(player).items().contains(ComplexItemStack.of(event.getItem()).getComplexItem().getType())) return;

        if (actionMap.containsKey(player) && (action.isLeftClick() || action.isRightClick())) {
            if (actionMap.get(player) != null){
                // Second Action, has first action
                // Effects
                player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 1.5f);
                player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 2, 1);

                Character firstAction = actionMap.get(player);
                Util.sendActionBar(event.getPlayer(), "&bSHIFT&7-&b%s&7-&b%s&r".formatted(firstAction, action.toString().charAt(0)));
                Util.sendTitle(player, "", "&7Class Ability: &b&l%s&f-&b&l%s&r".formatted(firstAction, action.toString().charAt(0)), 10, 20, 5);
                actionMap.remove(player); // Clear the action after processing the combo
            } else {
                // First Action

                player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 1f);

                Util.sendActionBar(event.getPlayer(), "&bSHIFT&7-&b%s&7-&b_".formatted(action.toString().charAt(0)));
                actionMap.put(player, action.toString().charAt(0));

                // remove after 1 second
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        actionMap.remove(player);
                    }
                }.runTaskLater(plugin, 20);
            }
        }
    }

    /**
     * Whether a player has initiated the casting of a class ability
     * @param player The player to check
     * @return Whether the player has initiated the casting of a class ability
     */
    public static boolean playerInitiated(Player player){
        return actionMap.containsKey(player);
    }

}
