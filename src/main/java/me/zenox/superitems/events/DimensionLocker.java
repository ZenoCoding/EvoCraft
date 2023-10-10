package me.zenox.superitems.events;

import me.zenox.superitems.SuperItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class DimensionLocker implements Listener {


    private final SuperItems plugin;

    public static final NamespacedKey DIMENSION_KEY = new NamespacedKey(SuperItems.getPlugin(), "dimension_unlocked");

    public DimensionLocker(SuperItems plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // Dimension lock stuff

    @EventHandler
    public void dimensionSwap(PlayerTeleportEvent e){
        if(e.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) || e.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)){
            PersistentDataContainer container = e.getPlayer().getPersistentDataContainer();
            if (container.has(DIMENSION_KEY, PersistentDataType.INTEGER)){
                if(Dimension.of(e.getTo().getWorld().getEnvironment()).environment == World.Environment.THE_END){
                    e.setCancelled(true);
                    e.getPlayer().getWorld().createExplosion(e.getPlayer().getLocation(), 4, true, true);
                }
            } else {
                container.set(DIMENSION_KEY, PersistentDataType.INTEGER, Dimension.OVERWORLD.value);
                if(Dimension.of(e.getTo().getWorld().getEnvironment()).environment == World.Environment.THE_END){
                    e.setCancelled(true);
                    e.getPlayer().getWorld().createExplosion(e.getPlayer().getLocation(), 4, true, true);
                }
            }
        }
    }

    @EventHandler
    public void useEyeOfEnder(PlayerInteractEvent e){
        if(e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.END_PORTAL_FRAME
                && e.getItem().getType() == Material.ENDER_EYE
                && (e.getAction() == Action.RIGHT_CLICK_BLOCK)){
            e.setCancelled(true);
        }
    }

    enum Dimension {
        OVERWORLD(0, World.Environment.NORMAL), NETHER(1, World.Environment.NETHER), THE_END(2, World.Environment.THE_END), OTHER(-999, World.Environment.CUSTOM);

        private final int value;
        private final World.Environment environment;

        Dimension(int value, World.Environment env){
            this.value = value;
            this.environment = env;
        }

        public static Dimension of(World.Environment env){
            try {
                return ((Dimension) Arrays.stream(Dimension.values()).filter(dimension -> dimension.environment.equals(env)).toArray()[0]);
            } catch (IndexOutOfBoundsException ignored){
                throw new IllegalArgumentException("Environment " + env + " does not have a corresponding Dimension. :/");
            }
        }
    }
}
