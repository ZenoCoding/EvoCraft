package me.zenox.superitems.items.abilities;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.NBTEditor;
import me.zenox.superitems.util.Util;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.zenox.superitems.items.ItemRegistry.TOTEM_POLE;

public class Centralize extends ItemAbility implements Listener {
    private boolean corrupted;
    private int duration;

    public Centralize(boolean corrupted, int duration) {
        super("Centralize", "totem_centralize", AbilityAction.RIGHT_CLICK_ALL, corrupted ? 150 : 125, 30);
        this.corrupted = corrupted;
        this.duration = duration;

        if(this.corrupted) {
            this.addLineToLore(ChatColor.GRAY + "Place down a " + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Totem" + ChatColor.GRAY + " which temporarily");
            this.addLineToLore(ChatColor.GRAY + "grants " + ChatColor.RED + "Resistance II" + ChatColor.GRAY + " and " + ChatColor.GOLD + "Fire Resistance I");
            this.addLineToLore(ChatColor.GRAY + "to you, yourself in a" + ChatColor.GREEN + " 45 block" + ChatColor.GRAY + " radius.");
            this.addLineToLore("");
            this.addLineToLore(ChatColor.LIGHT_PURPLE + "Corrupted Uses Left: " + ChatColor.DARK_PURPLE + "5");
        } else {
            this.addLineToLore(ChatColor.GRAY + "Place down a " + ChatColor.YELLOW + "" + ChatColor.BOLD + "Totem" + ChatColor.GRAY + " which temporarily");
            this.addLineToLore(ChatColor.GRAY + "grants " + ChatColor.RED + "Resistance I" + ChatColor.GRAY + " and " + ChatColor.GOLD + "Fire Resistance I");
            this.addLineToLore(ChatColor.GRAY + "to all players in a" + ChatColor.GREEN + " 30 block" + ChatColor.GRAY + " radius.");
        }

        Bukkit.getPluginManager().registerEvents(this, SuperItems.getPlugin());
    }

    @Override
    public void runExecutable(PlayerEvent event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Player p = e.getPlayer();
        World w = p.getWorld();
        Location loc = p.getLocation();
        if(e.getClickedBlock() != null) loc = e.getClickedBlock().getLocation().add(0.5, 1, 0.5);

        final Random r = new Random();

        ItemStack item = e.getItem();
        ItemMeta meta = item.getItemMeta();

        ArmorStand totem = ((ArmorStand) w.spawnEntity(loc, EntityType.ARMOR_STAND));

        totem.setInvulnerable(false);
        totem.setGravity(false);
        totem.setVisible(false);
        totem.setPersistent(false);

        // NMS-Setting NBT data
        NBTEditor.set(totem, 2039583, "DisabledSlots");

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

        List<Particle> particleList = new ArrayList();

        if(this.corrupted){
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(SuperItems.getPlugin(), "centralize_corrupt_uses");

            int uses = 5;

            if (dataContainer.has(key, PersistentDataType.INTEGER)) {
                uses = dataContainer.get(key, PersistentDataType.INTEGER);
            }

            uses--;

            dataContainer.set(key, PersistentDataType.INTEGER, uses);

            if (uses == 0) {
                Location highestblockpos = w.getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getLocation().add(0, 1, 0);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm m spawn -s Demiser 1 " + w.getName() + "," + highestblockpos.getX() + "," + highestblockpos.getY() + "," + highestblockpos.getZ());
                p.getInventory().setItem(e.getHand(), TOTEM_POLE.getItemStack(1));
            } else {
                List<String> lore = meta.getLore();
                for (String loreitem : lore) {
                    if (loreitem.startsWith(ChatColor.LIGHT_PURPLE + "Corrupted Uses Left: ")) {
                        lore.set(lore.indexOf(loreitem), ChatColor.LIGHT_PURPLE + "Corrupted Uses Left: " + ChatColor.DARK_PURPLE + uses);
                        meta.setLore(lore);
                    }
                }
            }

            item.setItemMeta(meta);


            w.playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_DEATH, 1, 1.2f);
            LeatherArmorMeta chestMeta = ((LeatherArmorMeta) chestplate.getItemMeta());
            chestMeta.setColor(Color.PURPLE);
            LeatherArmorMeta leggingsMeta = ((LeatherArmorMeta) leggings.getItemMeta());
            leggingsMeta.setColor(Color.PURPLE);
            LeatherArmorMeta bootsMeta = ((LeatherArmorMeta) boots.getItemMeta());
            bootsMeta.setColor(Color.PURPLE);

            chestplate.setItemMeta(chestMeta);
            leggings.setItemMeta(leggingsMeta);
            boots.setItemMeta(bootsMeta);

            totem.getEquipment().setHelmet(Util.makeSkull(new ItemStack(Material.PLAYER_HEAD), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk3YzI4NmI2ZDE2MjM5YTcxZmYxNjc0OTQ0MTZhZDk0MDcxNzIwNTEwY2Y4YTgyYWIxZjQ1MWZmNGE5MDkxNiJ9fX0="));
            totem.getEquipment().setChestplate(chestplate);
            totem.getEquipment().setLeggings(leggings);
            totem.getEquipment().setBoots(boots);

            particleList.add(Particle.REVERSE_PORTAL);
            particleList.add(Particle.CRIT_MAGIC);
            particleList.add(Particle.END_ROD);

        } else {
            w.playSound(loc, Sound.ITEM_TOTEM_USE, 1, 0);

            LeatherArmorMeta chestMeta = ((LeatherArmorMeta) chestplate.getItemMeta());
            chestMeta.setColor(Color.YELLOW);
            LeatherArmorMeta leggingsMeta = ((LeatherArmorMeta) leggings.getItemMeta());
            leggingsMeta.setColor(Color.YELLOW);
            LeatherArmorMeta bootsMeta = ((LeatherArmorMeta) boots.getItemMeta());
            bootsMeta.setColor(Color.YELLOW);

            chestplate.setItemMeta(chestMeta);
            leggings.setItemMeta(leggingsMeta);
            boots.setItemMeta(bootsMeta);

            totem.getEquipment().setHelmet(Util.makeSkull(new ItemStack(Material.PLAYER_HEAD), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU4MDkxMjhhMGU1YTQ0YzJlMzk1MzJlNmJiYzY4MjUyY2I4YzlkNWVjZDI0NmU1OTY1MDc3YzE0N2M3OTVlNyJ9fX0="));
            totem.getEquipment().setChestplate(chestplate);
            totem.getEquipment().setLeggings(leggings);
            totem.getEquipment().setBoots(boots);

            particleList.add(Particle.TOTEM);
            particleList.add(Particle.CRIT_MAGIC);
        }

        new BukkitRunnable() {
            double a = 0;
            int count = 0;

            double x = 0;
            double y = 0;
            double z = 0;

            final int wings = 6;

            Location loc = totem.getLocation();

            double startradius = 4;
            double radius = startradius;

            @Override
            public void run() {
                for (int i = 0; i < wings; i++) {
                    x = Math.cos(a + i * (2 * Math.PI / wings)) * radius;
                    y = Math.sin(a)/2;
                    z = Math.sin(a + i * (2 * Math.PI / wings)) * radius;

                    Location loc2 = new Location(p.getWorld(), (float) (loc.getX() + x), (float) (loc.getY() + y + 1), (float) (loc.getZ() + z));
                    p.getWorld().spawnParticle(particleList.get(r.nextInt(particleList.size())), loc2, 0);
                    p.getWorld().spawnParticle(particleList.get(r.nextInt(particleList.size())), loc2, 0);

                    a += Math.PI / 150;
                    radius -= 0.025;

                    if(radius <= 0.8) radius = startradius;

                }

                if(count%60 == 0){
                    if(corrupted){
                        try {
                            if (p.getLocation().distanceSquared(loc) < Math.pow(45, 2)) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 70, 1));
                                p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 70, 0));
                            }
                        } catch (IllegalArgumentException e){

                        }
                    } else {
                        for(Entity ent : w.getNearbyEntities(loc, 40, 40, 40)){
                            if(ent.getLocation().distanceSquared(loc) < Math.pow(30, 2)) {
                                if(ent instanceof Player) {
                                    ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 70, 0));
                                    ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 70, 0));
                                }
                            }
                        }
                    }
                }

                if(count/20 >= duration){
                    w.playSound(loc, corrupted ? Sound.ENTITY_ELDER_GUARDIAN_DEATH : Sound.ITEM_TOTEM_USE, 1, 1.2f);
                    totem.remove();
                    cancel();
                }

                count++;
            }
        }.runTaskTimer(SuperItems.getPlugin(), 3, 1);
    }
}
