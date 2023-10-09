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
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public abstract class Ability<T extends Event> implements Serializable {
    public static final List<Ability> registeredAbilities = new ArrayList<>();

    private final TranslatableText name;
    private final String id;
    private final int manaCost;
    private final double cooldown;

    private final Class<? extends Event> eventType;
    private final Slot slot;
    private TranslatableList lore;
    private final boolean isPassive;

    private TriConsumer<T, Player, ItemStack> executable;

    protected Ability(String id, int manaCost, double cooldown, Slot slot) {
        this(id, manaCost, cooldown, slot, false);
        this.executable = this::runExecutable;
    }

    protected Ability(String id, int manaCost, double cooldown, Slot slot, TriConsumer<T, Player, ItemStack> executable) {
        this(id, manaCost, cooldown, slot, false);
        this.executable = executable;
    }

    protected Ability(AbilitySettings settings){
        this(settings.getId(), settings.getManaCost(), settings.getCooldown(), settings.getSlot(), settings.isPassive());
    }

    protected Ability(AbilitySettings settings, TriConsumer<T, Player, ItemStack> executable){
        this(settings.getId(), settings.getManaCost(), settings.getCooldown(), settings.getSlot(), settings.isPassive());
        this.executable = executable;
    }

    /**
     * Internal constructor because exectuable go brr
     *
     * @param id       The unique identifier of the ability
     * @param manaCost The mana cost-per usage of the ability
     * @param cooldown The cooldown of the ability, how long before it can be used again
     * @param slot     The slot that the item that contains the ability has to be in, i.e. main hand, head, etc
     * @param isPassive Whether the ability is passive
     */
    private Ability(String id, int manaCost, double cooldown, Slot slot, boolean isPassive) {
        this.id = id;
        this.name = new TranslatableText(TranslatableText.Type.ABILITY_NAME + "-" + id);
        this.lore = new TranslatableList(TranslatableText.Type.ABILITY_LORE + "-" + id);
        this.cooldown = cooldown;
        this.manaCost = manaCost;
        this.slot = slot;
        this.isPassive = isPassive;

        this.eventType = (Class<T>) getType();
        if(this.eventType == null) throw new NullPointerException("Event type is null");

        for (Ability ability :
                registeredAbilities) {
            if (ability.getId().equalsIgnoreCase(id)) {
                Util.logToConsole("Duplicate Ability ID: " + id + " | Exact Match: " + ability.equals(this));
                throw new IllegalArgumentException("Ability ID cannot be duplicate");
            }
        }

        Ability.registeredAbilities.add(this);
    }

    private Type getType() {
        Class<?> clazz = getClass();
        while (clazz != null) {
            Type type = clazz.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                return ((ParameterizedType) type).getActualTypeArguments()[0];
            }
            clazz = clazz.getSuperclass();
        }
        return null;
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

    public void useAbility(Event event) {
        if (!this.eventType.isInstance(event)) return;
        T e = (T) event;
        if (!checkEvent(e)) return;
        Player p = getPlayerOfEvent(e);
        List<ItemStack> items = getItem(p, e);
        ItemStack item = null;

        // Perhaps change this in the future to support passing multiple items to the consumer
        for(ItemStack i : items) {
            if (i == null || i.getType() == Material.AIR || i.getItemMeta() == null) continue;
            if (!ComplexItemStack.of(i).getAbilities().contains(this)) continue;
            item = i;
            break;
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

        // If the ability is on cooldown, return
        if (cooldown != null && Math.ceil((cooldown - System.currentTimeMillis()) / 1000) > 0) {
            if (!isPassive) Util.sendActionBar(p, ChatColor.WHITE + "" + ChatColor.BOLD + "ABILITY ON COOLDOWN (" + ChatColor.RED + Math.ceil((cooldown - System.currentTimeMillis()) / 100)/10 + "s" + ChatColor.WHITE + ")");
            return;
        }

        if (this.manaCost > 0) {
            int resultingMana = ((int) AureliumAPI.getMana(p)) - manaCost;
            if (resultingMana < 0) {
                if (!isPassive) Util.sendActionBar(p, ChatColor.RED + "" + ChatColor.BOLD + "NOT ENOUGH MANA");
                return;
            } else {
                AureliumAPI.setMana(p, AureliumAPI.getMana(p) - manaCost);
            }
        }

        AureliumAPI.getPlugin().getActionBar().setPaused(p, 20);
        String manaMessage = this.manaCost > 0 ? ChatColor.AQUA + "-" + manaCost + " Mana " + "(" + ChatColor.GOLD + name + ChatColor.AQUA + ")" : ChatColor.GOLD + "Used " + name;
        if(!isPassive) Util.sendActionBar(p, manaMessage);

        this.executable.accept(e, p, item);

        container.set(cooldownKey, PersistentDataType.DOUBLE, System.currentTimeMillis() + (getCooldown() * 1000));
    }

    protected void runExecutable(T e, Player p, ItemStack itemStack) {
        Util.sendMessage(p, "Used the " + this.id + " ability");
    }

    abstract boolean checkEvent(T e);

    abstract Player getPlayerOfEvent(T e);

    abstract List<ItemStack> getItem(Player p, T e);

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

    public boolean isPassive() {
        return isPassive;
    }

    public Class<? extends Event> getEventType() {
        return eventType;
    }
}
