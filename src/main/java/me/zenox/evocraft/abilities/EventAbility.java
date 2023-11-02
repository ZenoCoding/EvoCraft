package me.zenox.evocraft.abilities;

import me.zenox.evocraft.Slot;
import me.zenox.evocraft.item.ComplexItem;
import me.zenox.evocraft.util.TriConsumer;
import me.zenox.evocraft.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public abstract class EventAbility<T extends Event> extends Ability<T> {

    private final Class<? extends Event> eventType;
    private final Slot slot;

    protected EventAbility(String id, int manaCost, double cooldown, Slot slot) {
        this(id, manaCost, cooldown, slot, false);
        this.executable = this::runExecutable;
    }

    protected EventAbility(String id, int manaCost, double cooldown, Slot slot, TriConsumer<T, Player, ItemStack> executable) {
        this(id, manaCost, cooldown, slot, false);
        this.executable = executable;
    }

    protected EventAbility(@NotNull AbilitySettings settings){
        this(settings.getId(), settings.getManaCost(), settings.getCooldown(), settings.getSlot(), settings.isPassive());
    }

    protected EventAbility(@NotNull AbilitySettings settings, TriConsumer<T, Player, ItemStack> executable){
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
    private EventAbility(String id, int manaCost, double cooldown, Slot slot, boolean isPassive) {
        super(id, manaCost, cooldown, isPassive);
        this.slot = slot;

        this.eventType = (Class<T>) getType();
        if(this.eventType == null) throw new NullPointerException("Event type is null");

        for (Ability<?> ability :
                registeredAbilities) {
            if (ability.getId().equalsIgnoreCase(id)) {
                Util.logToConsole("Duplicate Ability ID: " + id + " | Exact Match: " + ability.equals(this));
                throw new IllegalArgumentException("Ability ID cannot be duplicate");
            }
        }

        Ability.registeredAbilities.add(this);
    }

    @Override
    public void useAbility(Event event) {
        if (!isValidEvent(event)) return;
        T e = (T) event;
        Player p = getPlayerOfEvent(e);
        ItemStack item = getValidItem(p, e);

        if (item == null) return;

        if (isAbilityOnCooldown(p)) {
            sendCooldownMessage(p);
            return;
        }

        if (notEnoughMana(p, this.getManaCost())) {
            sendManaInsufficientMessage(p);
            return;
        }

        deductMana(p, this.getManaCost());
        this.executable.accept(e, p, item);
        setAbilityCooldown(p);
    }

    protected boolean isValidEvent(Event event) {
        if (!this.eventType.isInstance(event)) return false;
        T e = (T) event;
        return checkEvent(e);
    }

    protected ItemStack getValidItem(Player p, T e) {
        List<ItemStack> items = getItem(p, e);
        for (ItemStack i : items) {
            if (i == null || i.getType() == Material.AIR || i.getItemMeta() == null) continue;
            if (!ComplexItem.of(i).getAbilities().contains(this)) continue;
            return i;
        }
        return null;
    }

    protected abstract boolean checkEvent(T e);

    protected abstract Player getPlayerOfEvent(T e);

    protected abstract List<ItemStack> getItem(Player p, T e);

    public Slot getSlot() {
        return this.slot;
    }

    public Class<? extends Event> getEventType() {
        return eventType;
    }

}
