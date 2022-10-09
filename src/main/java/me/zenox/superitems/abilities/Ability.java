package me.zenox.superitems.abilities;

import com.archyx.aureliumskills.api.AureliumAPI;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.data.TranslatableList;
import me.zenox.superitems.data.TranslatableText;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;


public abstract class Ability implements Serializable {
    public static List<Ability> registeredAbilities = new ArrayList<>();

    private final TranslatableText name;
    private final String id;
    private final int manaCost;
    private final double cooldown;

    private final Class<? extends Event> eventType;
    private final Slot slot;
    private TranslatableList lore;

    private transient Consumer<Event> executable;

    public Ability(String id, int manaCost, double cooldown, Class<? extends Event> eventType, Slot slot) {
        this(id, manaCost, cooldown, eventType, slot, true);
        this.executable = this::runExecutable;
    }

    public Ability(String id, int manaCost, double cooldown, Class<? extends Event> eventType, Slot slot, Consumer<Event> executable) {
        this(id, manaCost, cooldown, eventType, slot, true);
        this.executable = executable;
    }

    /**
     * Internal constructor because exectuable go brr
     *
     * @param id
     * @param manaCost
     * @param cooldown
     * @param eventType
     * @param slot
     * @param internal  parameter that never gets used just to differentiate it from the public variant
     */
    private Ability(String id, int manaCost, double cooldown, Class<? extends Event> eventType, Slot slot, Boolean internal) {
        this.id = id;
        this.name = new TranslatableText(TranslatableText.TranslatableType.ABILITY_NAME + "-" + id);
        this.lore = new TranslatableList(TranslatableText.TranslatableType.ABILITY_LORE + "-" + id);
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
        for (Ability ability : registeredAbilities) if (ability.getId() == id) return ability;
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

    protected boolean checkEvent(Event e) {
        return this.eventType.isInstance(e);
    }

    public void useAbility(Event e) {
        if (!checkEvent(e)) return;
        Player p = getPlayerOfEvent(e);

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

        this.executable.accept(e);

        container.set(cooldownKey, PersistentDataType.DOUBLE, System.currentTimeMillis() + (getCooldown() * 1000));
    }

    protected void runExecutable(Event e) {
        if (e instanceof PlayerEvent)
            Util.sendMessage(((PlayerEvent) e).getPlayer(), "Used the " + this.id + " ability");
        else if (e instanceof EntityEvent)
            Util.sendMessage(((EntityEvent) e).getEntity(), "Used the " + this.id + " ability");
        else
            Util.logToConsole("Attempted to use ability " + this.id + "but was unable to find the corresponding entity.");
    }

    private Player getPlayerOfEvent(Event e) {
        if (e instanceof PlayerEvent) return ((PlayerEvent) e).getPlayer();
        else if (e instanceof EntityDamageByEntityEvent) return ((Player) ((EntityDamageByEntityEvent) e).getDamager());
            // create implemenations for every event registered
        else
            Util.logToConsole("Ability#getPlayerOfEvent: Attempted to use ability " + this.id + "but was unable to find the corresponding entity.");
        return null;
    }

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

    public enum Slot {
        MAIN_HAND((PlayerInventory inv, Ability ability) -> ComplexItemStack.of(inv.getItemInMainHand()).getAbilities().contains(ability), (PlayerInventory inv) -> inv.getItemInMainHand()),
        OFF_HAND((PlayerInventory inv, Ability ability) -> ComplexItemStack.of(inv.getItemInOffHand()).getAbilities().contains(ability), (PlayerInventory inv) -> inv.getItemInOffHand()),
        EITHER_HAND((PlayerInventory inv, Ability ability) -> MAIN_HAND.predicate.test(inv, ability) || OFF_HAND.predicate.test(inv, ability), (PlayerInventory inv) -> inv.getItemInMainHand()),
        HEAD((PlayerInventory inv, Ability ability) -> ComplexItemStack.of(inv.getHelmet()).getAbilities().contains(ability), (PlayerInventory inv) -> inv.getHelmet()),
        CHEST((PlayerInventory inv, Ability ability) -> ComplexItemStack.of(inv.getChestplate()).getAbilities().contains(ability), (PlayerInventory inv) -> inv.getChestplate()),
        LEGS((PlayerInventory inv, Ability ability) -> ComplexItemStack.of(inv.getLeggings()).getAbilities().contains(ability), (PlayerInventory inv) -> inv.getLeggings()),
        BOOTS((PlayerInventory inv, Ability ability) -> ComplexItemStack.of(inv.getBoots()).getAbilities().contains(ability), (PlayerInventory inv) -> inv.getBoots()),
        ARMOR((PlayerInventory inv, Ability ability) -> {
            for (ItemStack item : inv.getArmorContents())
                if (ComplexItemStack.of(item).getAbilities().contains(ability)) return true;
            return false;
        }, (PlayerInventory inv) -> inv.getHelmet()),
        INVENTORY((PlayerInventory inv, Ability ability) -> {
            for (ItemStack item : inv.getContents())
                if (ComplexItemStack.of(item).getAbilities().contains(ability)) return true;
            return false;
        }, (PlayerInventory inv) -> inv.getItemInMainHand()),
        ;

        private final BiPredicate<PlayerInventory, Ability> predicate;

        //groups that apply to multiple items will return the first item applicable
        private final Function<PlayerInventory, ItemStack> getItem;

        Slot(BiPredicate<PlayerInventory, Ability> predicate, Function<PlayerInventory, ItemStack> getItem) {
            this.predicate = predicate;
            this.getItem = getItem;
        }

        /**
         * Gets all the unique abilities of a player's equipped gear.
         *
         * @param p The player
         * @return A list of unique abilities.
         */
        public static List<Ability> uniqueEquipped(Player p) {
            List<ItemStack> items = List.of(MAIN_HAND.item(p), OFF_HAND.item(p), HEAD.item(p), CHEST.item(p), LEGS.item(p), BOOTS.item(p));
            List<Ability> abilities = new ArrayList<>();
            items.stream()
                    .map(ComplexItemStack::of)
                    .filter(Objects::nonNull)
                    .forEach((complexItemStack) -> abilities.addAll(complexItemStack.getAbilities()));
            return abilities.stream().distinct().toList();
        }

        public Boolean evaluate(PlayerInventory inv, Ability ability) {
            try {
                return this.predicate.test(inv, ability);
            } catch (NullPointerException e) {
                return false;
            }
        }

        public ItemStack item(PlayerInventory inv) {
            return this.getItem.apply(inv);
        }

        public ItemStack item(Player p) {
            return this.getItem.apply(p.getInventory());
        }
    }


}
