package me.zenox.superitems.abilities;

import com.google.common.primitives.Doubles;
import me.zenox.superitems.SuperItems;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class VoidularRecall extends ItemAbility {
    public VoidularRecall() {
        super("void_recall", AbilityAction.SHIFT_LEFT_CLICK, 250, 0);

        this.addLineToLore(ChatColor.GRAY + "Binds a nearby block to your " + ChatColor.AQUA + "soul.");
        this.addLineToLore(ChatColor.GRAY + "Teleports you back to the " + ChatColor.AQUA + "soulbound" + ChatColor.GRAY + " block.");

    }

    @Override
    public void runExecutable(Event event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        ItemMeta meta = item.getItemMeta();
        Random r = new Random();
        Location locationOfBlock;


        PersistentDataContainer container = p.getPersistentDataContainer();
        NamespacedKey locationKey = new NamespacedKey(SuperItems.getPlugin(), "voidular_recall");
        if (container.has(locationKey, PersistentDataType.STRING)) {
            String locationString = container.get(locationKey, PersistentDataType.STRING);
            List<String> locationStringList = List.of(locationString.split("\\|"));
            container.remove(locationKey);
            locationOfBlock = new Location(p.getServer().getWorld(locationStringList.get(0)), Doubles.tryParse(locationStringList.get(1)), Doubles.tryParse(locationStringList.get(2)), Doubles.tryParse(locationStringList.get(3)));

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
            } catch (Exception error) {

            }


            w.playSound(p.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_DEATH, 2f, 0.7f);
            w.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2f, 0.8f);
        } else {
            if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
                locationOfBlock = p.getLocation().add(0, 1, 0);
            } else {
                locationOfBlock = e.getClickedBlock().getLocation();
            }

            String locationString = locationOfBlock.getWorld().getName() + "|" + locationOfBlock.getX() + "|" + +locationOfBlock.getY() + "|" + locationOfBlock.getZ();
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
            }.runTaskTimer(SuperItems.getPlugin(), 0, 3);

        }


    }
}
