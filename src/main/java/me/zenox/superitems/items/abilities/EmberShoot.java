package me.zenox.superitems.items.abilities;

import com.archyx.aureliumskills.api.AureliumAPI;
import me.zenox.superitems.SuperItems;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EmberShoot extends ItemAbility {

    public EmberShoot() {
        super("Dark Embers", "dark_ember_shoot", AbilityAction.RIGHT_CLICK_ALL, 10, 0);

        this.addLineToLore(ChatColor.GRAY + "Shoot a projectile based on the " + ChatColor.AQUA + "attunement");
        this.addLineToLore(ChatColor.GRAY + "of this weapon. Dark magic is cool, right?");
        this.addLineToLore("");
        this.addLineToLore(ChatColor.AQUA + "Attunement: " + ChatColor.GOLD + "Blazeborn");
    }

    @Override
    public void runExecutable(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        World w = p.getWorld();
        Location loc;

        Location eyeLoc = p.getEyeLocation();

        ItemStack item = e.getItem();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        String attunement = dataContainer.get(new NamespacedKey(SuperItems.getPlugin(), "ember_attunement"), PersistentDataType.STRING);
        if (attunement.equalsIgnoreCase("blazeborn")) {
            Fireball f = (Fireball) eyeLoc.getWorld().spawnEntity(eyeLoc.add(eyeLoc.getDirection()), EntityType.FIREBALL);
            f.setVelocity(eyeLoc.getDirection().normalize().multiply(Math.min(5, AureliumAPI.getMaxMana(e.getPlayer()) / 75)));
            f.setMetadata("dmgEnv", new FixedMetadataValue(SuperItems.getPlugin(), false));
            f.setMetadata("knockback", new FixedMetadataValue(SuperItems.getPlugin(), 1.5));
            f.setShooter(p);
            f.setYield(2f);
        } else if (attunement.equalsIgnoreCase("darksoul")) {
            WitherSkull f = (WitherSkull) eyeLoc.getWorld().spawnEntity(eyeLoc.add(eyeLoc.getDirection()), EntityType.WITHER_SKULL);
            f.setVelocity(eyeLoc.getDirection().normalize().multiply(Math.min(5, AureliumAPI.getMaxMana(e.getPlayer()) / 50)));
            f.setMetadata("dmgEnv", new FixedMetadataValue(SuperItems.getPlugin(), false));
            f.setShooter(p);
            f.setYield(3f);
        }
    }
}
