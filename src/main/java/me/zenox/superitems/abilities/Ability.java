package me.zenox.superitems.abilities;

import com.archyx.aureliumskills.api.AureliumAPI;
import me.zenox.superitems.Slot;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.data.TranslatableList;
import me.zenox.superitems.data.TranslatableText;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.util.TriConsumer;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public abstract class Ability implements Serializable {
    public static final List<Ability> registeredAbilities = new ArrayList<>();
    public static final List<Class<? extends Event>> registeredEvents = new ArrayList<>();

    private final TranslatableText name;
    private final String id;
    private final int manaCost;
    private final double cooldown;

    private final Class<? extends Event> eventType;
    private final Slot slot;
    private TranslatableList lore;

    private transient TriConsumer<Event, Player, ItemStack> executable;

    protected Ability(String id, int manaCost, double cooldown, Class<? extends Event> eventType, Slot slot) {
        this(id, manaCost, cooldown, eventType, slot, true);
        this.executable = this::runExecutable;
    }

    protected Ability(String id, int manaCost, double cooldown, Class<? extends Event> eventType, Slot slot, TriConsumer<Event, Player, ItemStack> executable) {
        this(id, manaCost, cooldown, eventType, slot, true);
        this.executable = executable;
    }

    /**
     * Internal constructor because exectuable go brr
     *
     * @param id The unique identifier of the ability
     * @param manaCost The mana cost-per usage of the ability
     * @param cooldown The cooldown of the ability, how long before it can be used again
     * @param eventType The EventType that triggers the ability
     * @param slot The slot that the item that contains the ability has to be in, i.e. main hand, head, etc
     * @param internal  parameter that never gets used just to differentiate it from the public variant
     */
    private Ability(String id, int manaCost, double cooldown, Class<? extends Event> eventType, Slot slot, Boolean internal) {
        this.id = id;
        this.name = new TranslatableText(TranslatableText.Type.ABILITY_NAME + "-" + id);
        this.lore = new TranslatableList(TranslatableText.Type.ABILITY_LORE + "-" + id);
        this.cooldown = cooldown;
        this.manaCost = manaCost;
        this.eventType = eventType;
        this.slot = slot;

        for (Ability ability :
                registeredAbilities) {
            if (ability.getId().equalsIgnoreCase(id)) {
                Util.logToConsole("Duplicate Ability ID: " + id + " | Exact Match: " + ability.equals(this));
                throw new IllegalArgumentException("Ability ID cannot be duplicate");
            }
        }

        Ability.registeredAbilities.add(this);
        if(!Ability.registeredEvents.contains(eventType)) Ability.registeredEvents.add(eventType);
    }

    /**
     * method getAbility <br>
     * gets the ability corresponding to the id
     *
     * @param id the id to search for
     * @return the ability- null if not found
     */
    @Nullable
    public static Ability getAbility(String id) {
        for (Ability ability : registeredAbilities) if (ability.getId().equals(id)) return ability;
        return null;
    }

    public String getDisplayName() {
        return name.toString();
    }

    @Deprecated
    public List<String> addLineToLore(String line) {
        return lore.getList();
    }

    public List<String> getLore() {
        return lore.getList();
    }

    protected void setLore(TranslatableList list) {
        this.lore = list;
    }

    public void useAbility(Event e) {
        if (!this.eventType.isInstance(e)) return;
        if (!checkEvent(e)) return;
        Player p = getPlayerOfEvent(e);
        List<ItemStack> items = getItem(p, e);
        ItemStack item = null;

        // Perhaps change this in the future to support passing multiple items to the consumer
        for(ItemStack i : items) {
            if (ComplexItemStack.of(i) == null) continue;
            if (!ComplexItemStack.of(i).getAbilities().contains(this)) continue;
            item = i;
        }

        if(item == null) return;

        PersistentDataContainer container = p.getPersistentDataContainer();
        NamespacedKey cooldownKey = new NamespacedKey(SuperItems.getPlugin(), getId() + "_cooldown");
        Double cooldown;
        try {
            cooldown = container.get(cooldownKey, PersistentDataType.DOUBLE);
        } catch (IllegalArgumentException exception) {
            cooldown = Double.valueOf(container.get(cooldownKey, PersistentDataType.LONG));
            container.remove(cooldownKey);
            container.set(cooldownKey, PersistentDataType.DOUBLE, cooldown);
        }


        if (cooldown != null && Math.ceil((cooldown - System.currentTimeMillis()) / 1000) > 0) {
            Util.sendMessage(p, ChatColor.WHITE + "This ability is on cooldown for " + ChatColor.RED + Math.ceil((cooldown - System.currentTimeMillis()) / 1000) + ChatColor.WHITE + " seconds");
            return;
        }

        if (this.manaCost > 0) {
            int resultingMana = ((int) AureliumAPI.getMana(p)) - manaCost;
            if (resultingMana < 0) {
                Util.sendActionBar(p, ChatColor.RED + "" + ChatColor.BOLD + "NOT ENOUGH MANA");
                return;
            } else {
                AureliumAPI.setMana(p, AureliumAPI.getMana(p) - manaCost);
            }
        }

        AureliumAPI.getPlugin().getActionBar().setPaused(p, 20);
        String manaMessage = this.manaCost > 0 ? ChatColor.AQUA + "-" + manaCost + " Mana " + "(" + ChatColor.GOLD + name + ChatColor.AQUA + ")" : ChatColor.GOLD + "Used " + name;
        Util.sendActionBar(p, manaMessage);

        this.executable.accept(e, p, item);

        container.set(cooldownKey, PersistentDataType.DOUBLE, System.currentTimeMillis() + (getCooldown() * 1000));
    }

    protected void runExecutable(Event e, Player p, ItemStack itemStack) {
        Util.sendMessage(p, "Used the " + this.id + " ability");
    }

    abstract boolean checkEvent(Event e);

    abstract Player getPlayerOfEvent(Event e);

    abstract List<ItemStack> getItem(Player p, Event e);

    public double getCooldown() {
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


}
