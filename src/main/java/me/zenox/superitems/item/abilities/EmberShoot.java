package me.zenox.superitems.item.abilities;

import com.archyx.aureliumskills.api.AureliumAPI;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItemMeta;
import me.zenox.superitems.item.ComplexItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EmberShoot extends ItemAbility {

    public EmberShoot() {
        super("Dark Embers", "dark_ember_shoot", AbilityAction.RIGHT_CLICK_ALL, 25, 0);

        this.addLineToLore(ChatColor.GRAY + "Shoot a projectile based on the " + ChatColor.AQUA + "attunement");
        this.addLineToLore(ChatColor.GRAY + "of this weapon. Dark magic is cool, right?");
    }

    @Override
    public void runExecutable(PlayerEvent event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Player p = e.getPlayer();
        ComplexItemMeta complexMeta = ComplexItemStack.of(e.getItem()).getComplexMeta();

        Location eyeLoc = p.getEyeLocation();

        if (complexMeta.getVariable(EmberAttune.ATTUNEMENT_VARIABLE_TYPE).getValue().equals(EmberAttune.Attunement.BLAZEBORN)) {
            Fireball f = (Fireball) eyeLoc.getWorld().spawnEntity(eyeLoc.add(eyeLoc.getDirection()), EntityType.FIREBALL);
            f.setVelocity(eyeLoc.getDirection().normalize().multiply(Math.min(5, AureliumAPI.getMaxMana(e.getPlayer()) / 75)));
            f.setMetadata("dmgEnv", new FixedMetadataValue(SuperItems.getPlugin(), false));
            f.setMetadata("knockback", new FixedMetadataValue(SuperItems.getPlugin(), 1.5));
            f.setShooter(p);
            f.setYield(((float) AureliumAPI.getMaxMana(p))/150f);
        } else if (complexMeta.getVariable(EmberAttune.ATTUNEMENT_VARIABLE_TYPE).getValue().equals(EmberAttune.Attunement.DARKSOUL)) {
            WitherSkull f = (WitherSkull) eyeLoc.getWorld().spawnEntity(eyeLoc.add(eyeLoc.getDirection()), EntityType.WITHER_SKULL);
            f.setVelocity(eyeLoc.getDirection().normalize().multiply(Math.min(5, AureliumAPI.getMaxMana(e.getPlayer()) / 50)));
            f.setMetadata("dmgEnv", new FixedMetadataValue(SuperItems.getPlugin(), false));
            f.setShooter(p);
            f.setYield(((float) AureliumAPI.getMaxMana(p))/100f);
        }
    }
}
