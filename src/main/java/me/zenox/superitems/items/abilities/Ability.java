package me.zenox.superitems.items.abilities;

import com.archyx.aureliumskills.api.AureliumAPI;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.ItemRegistry;
import me.zenox.superitems.util.Util;
import net.minecraft.world.inventory.ContainerAccess;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public abstract class Ability {
    public static List<Ability> registeredAbilities = new ArrayList<>();

    private final String name;
    private final String id;
    private final int manaCost;
    private final int cooldown;
    private final Class<? extends PlayerEvent> eventType;
    private final Slot slot;
    private final List<String> lore = new ArrayList<>();

    public Ability(String name, String id, int manaCost, int cooldown, Class<? extends PlayerEvent> eventType, Slot slot) {
        this.name = name;
        this.cooldown = cooldown;
        this.manaCost = manaCost;
        this.id = id;
        this.eventType = eventType;
        this.slot = slot;

        Ability.registeredAbilities.add(this);
    }

    public String getName() {
        return name;
    }

    public List<String> addLineToLore(String line) {
        lore.add(line);
        return lore;
    }

    public List<String> getLore() {
        return lore;
    }

    protected boolean checkEvent(PlayerEvent e){
        if(!this.eventType.isInstance(e)) {
            return false;
        }
        return true;
    }

    public void useAbility(PlayerEvent e) {
        if(!checkEvent(e)) return;

        PersistentDataContainer container = e.getPlayer().getPersistentDataContainer();
        NamespacedKey cooldownKey = new NamespacedKey(SuperItems.getPlugin(), getId() + "_cooldown");
        Long cooldown = container.get(cooldownKey, PersistentDataType.LONG);


        if (cooldown != null && Math.ceil((cooldown - System.currentTimeMillis()) / 1000) > 0) {
            Util.sendMessage(e.getPlayer(), ChatColor.WHITE + "This ability is on cooldown for " + ChatColor.RED + Math.ceil((cooldown - System.currentTimeMillis()) / 1000) + ChatColor.WHITE + " seconds");
            return;
        }

        if (this.manaCost > 0) {
            int resultingMana = ((int) AureliumAPI.getMana(e.getPlayer())) - manaCost;
            if (resultingMana < 0) {
                Util.sendActionBar(e.getPlayer(), ChatColor.RED + "" + ChatColor.BOLD + "NOT ENOUGH MANA");
                return;
            } else {
                AureliumAPI.setMana(e.getPlayer(), AureliumAPI.getMana(e.getPlayer()) - manaCost);
            }
        }

        AureliumAPI.getPlugin().getActionBar().setPaused(e.getPlayer(), 20);
        String manaMessage = this.manaCost > 0 ? ChatColor.AQUA + "-" + manaCost + " Mana " + "(" + ChatColor.GOLD + name + ChatColor.AQUA + ")" : ChatColor.GOLD + "Used " + name;
        Util.sendActionBar(e.getPlayer(), manaMessage);

        this.runExecutable(e);

        container.set(cooldownKey, PersistentDataType.LONG, System.currentTimeMillis() + (getCooldown() * 1000));
    }

    protected void runExecutable(PlayerEvent e) {
        Util.sendMessage(e.getPlayer(), "Used the " + this.id + " ability");
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public int getManaCost() {
        return this.manaCost;
    }

    public String getId() {
        return this.id;
    }

    public Slot getSlot() {
        return this.slot;
    }

    public enum Slot {
        MAIN_HAND((PlayerInventory inv, Ability ability) -> ItemRegistry.getBasicItemFromItemStack(inv.getItemInMainHand()).getAbilities().contains(ability)),
        OFF_HAND((PlayerInventory inv, Ability ability) -> ItemRegistry.getBasicItemFromItemStack(inv.getItemInOffHand()).getAbilities().contains(ability)),
        EITHER_HAND((PlayerInventory inv, Ability ability) -> MAIN_HAND.predicate.test(inv, ability) || OFF_HAND.predicate.test(inv, ability)),
        HEAD((PlayerInventory inv, Ability ability) -> ItemRegistry.getBasicItemFromItemStack(inv.getHelmet()).getAbilities().contains(ability)),
        CHEST((PlayerInventory inv, Ability ability) -> ItemRegistry.getBasicItemFromItemStack(inv.getChestplate()).getAbilities().contains(ability)),
        LEGS((PlayerInventory inv, Ability ability) -> ItemRegistry.getBasicItemFromItemStack(inv.getLeggings()).getAbilities().contains(ability)),
        BOOTS((PlayerInventory inv, Ability ability) -> ItemRegistry.getBasicItemFromItemStack(inv.getBoots()).getAbilities().contains(ability)),
        ARMOR((PlayerInventory inv, Ability ability) -> { for (ItemStack item : inv.getArmorContents()) if(ItemRegistry.getBasicItemFromItemStack(item).getAbilities().contains(ability)) return true; else return false; return false;}),
        INVENTORY((PlayerInventory inv, Ability ability) -> { for (ItemStack item : inv.getContents()) if(ItemRegistry.getBasicItemFromItemStack(item).getAbilities().contains(ability)) return true; else return false; return false;}),;

        private BiPredicate<PlayerInventory, Ability> predicate;

        Slot(BiPredicate<PlayerInventory, Ability> predicate) {
            this.predicate = predicate;
        }

        public Boolean evaluate(PlayerInventory inv, Ability ability){
            try {
                return this.predicate.test(inv, ability);
            } catch (NullPointerException e){
                return false;
            }
        }
    }


}
