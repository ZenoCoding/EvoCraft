package me.zenox.evocraft.abilities.itemabilities.specific;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.stats.Stats;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.zenox.evocraft.Slot;
import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.abilities.itemabilities.ItemAbility;
import me.zenox.evocraft.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Psychic extends ItemAbility implements Listener {

    public Psychic() {
        super("psychic", AbilityAction.RIGHT_CLICK_ALL, 0, 0, Slot.MAIN_HAND);

        this.addLineToLore(ChatColor.GRAY + "Understanding " + ChatColor.WHITE + "thyself" + ChatColor.GRAY + " requires lots of patience.");
        this.addLineToLore(ChatColor.GRAY + "Or a magic orb! Gain" + ChatColor.AQUA + " +50 Wisdom");
        this.addLineToLore("");
        this.addLineToLore(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Capped at " + ChatColor.AQUA + "+50 Wisdom");

        Bukkit.getPluginManager().registerEvents(this, EvoCraft.getPlugin());
    }

    @Override
    public void runExecutable(PlayerInteractEvent event, Player p, ItemStack item) {
        event.setCancelled(true);
        PersistentDataContainer dataContainer = p.getPersistentDataContainer();

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
        Location guardLoc = BukkitAdapter.adapt(p.getLocation());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        if (query.testState(guardLoc, localPlayer, Flags.INVINCIBILITY)) {
            Util.sendMessage(p, "You cannot use this item inside a worldguard region!");
            return;
        }

        event.getItem().setAmount(event.getItem().getAmount() - 1);

        NamespacedKey key = new NamespacedKey(EvoCraft.getPlugin(), "psychic_wisdom_buff");
        int count = 0;
        if (dataContainer.has(key, PersistentDataType.INTEGER))
            count = dataContainer.get(key, PersistentDataType.INTEGER);
        count++;

        if (count >= 6) {
            List<String> stringList = List.of(ChatColor.GRAY + "Your brain starts to hurt.",
                    ChatColor.GRAY + "You start to wonder, that despite all your greatness, if this was a good idea.",
                    ChatColor.GRAY + "You have an immense headache.",
                    ChatColor.GRAY + "You start hallucinating, slowly losing your mind.",
                    ChatColor.GRAY + "Your brain expands past the limits of the human body, expanding...");
            new BukkitRunnable() {
                int dialogcount = 0;

                @Override
                public void run() {
                    if (dialogcount + 1 > stringList.size()) {
                        killPlayer(p, key, dataContainer);
                        cancel();
                        return;
                    }
                    Util.sendMessage(p, stringList.get(dialogcount), false);
                    if (dialogcount == 3)
                        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, Integer.MAX_VALUE, 9));
                    dialogcount++;
                }
            }.runTaskTimer(EvoCraft.getPlugin(), 10, 40);

        } else {
            List<String> stringList = List.of(ChatColor.GRAY + "You feel your mind expanding, your knowledge deepens." + ChatColor.AQUA + " +50 Wisdom (" + count + "/5)",
                    ChatColor.GRAY + "Your understanding of the world is greater than anyone ever before." + ChatColor.AQUA + " +50 Wisdom (" + count + "/5)",
                    ChatColor.GRAY + "You now understand the deepest and most complex concepts, your IQ among Albert Einstein and the greats." + ChatColor.AQUA + " +50 Wisdom (" + count + "/5)",
                    ChatColor.GRAY + "Your brain is physically bigger than most, many praise you around the world for your prodigy." + ChatColor.AQUA + " +50 Wisdom (" + count + "/5)",
                    ChatColor.GRAY + "You have achieved true meaning, understanding things so profound and taking leaps for humanity." + ChatColor.AQUA + " +50 Wisdom (" + count + "/5)");
            Util.sendMessage(p, stringList.get(count - 1), false);
        }

        dataContainer.set(key, PersistentDataType.INTEGER, count);
        p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1, 1.2f);
        AureliumAPI.addStatModifier(p, "evocraft:psychic_wisdom_buff_" + count, Stats.WISDOM, 50d);
    }

    private void killPlayer(Player p, NamespacedKey key, PersistentDataContainer dataContainer) {
        dataContainer.set(key, PersistentDataType.INTEGER, 0);
        p.setMetadata("evocraft:death_psychic", new FixedMetadataValue(EvoCraft.getPlugin(), true));
        p.setHealth(0);
        p.damage(10, p);
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                p.setHealth(0);
                p.damage(10, p);
                List<MetadataValue> values = p.getMetadata("evocraft:death_psychic");
                if (!values.isEmpty() && !values.get(0).asBoolean()) cancel();
                count++;
            }
        }.runTaskTimer(EvoCraft.getPlugin(), 0, 20);
        for (int i = 0; i < 20; i++) {
            AureliumAPI.removeStatModifier(p, "evocraft:psychic_wisdom_buff_" + i);
        }
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {

        Player entity = e.getEntity().getPlayer();
        List<MetadataValue> values = entity.getMetadata("evocraft:death_psychic");
        if (!values.isEmpty() && values.get(0).asBoolean()) {
            e.setDeathMessage(entity.getDisplayName() + "'s brain exploded.");
        }
        e.getEntity().getPlayer().setMetadata("evocraft:death_psychic", new FixedMetadataValue(EvoCraft.getPlugin(), false));
    }
}
