package me.zenox.superitems.abilities;

import me.zenox.superitems.Slot;

import java.util.UUID;

/**
 * Builder for the Ability class
 */
public class AbilitySettings {
    private String id;
    private int manaCost;
    private double cooldown;
    private Slot slot;
    private boolean isPassive;

    // Ability-Specific Settings
    private ItemAbility.AbilityAction abilityAction;

    public AbilitySettings() {
        this.id = "default" + UUID.randomUUID();
        this.manaCost = 0;
        this.cooldown = 0;
        this.slot = Slot.EITHER_HAND;
        this.isPassive = false;
        this.abilityAction = ItemAbility.AbilityAction.NONE;
    }

    public String getId() {
        return id;
    }

    public AbilitySettings setId(String id) {
        this.id = id;
        return this;
    }

    public int getManaCost() {
        return manaCost;
    }

    public AbilitySettings setManaCost(int manaCost) {
        this.manaCost = manaCost;
        return this;
    }

    public double getCooldown() {
        return cooldown;
    }

    public AbilitySettings setCooldown(double cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public Slot getSlot() {
        return slot;
    }

    public AbilitySettings setSlot(Slot slot) {
        this.slot = slot;
        return this;
    }

    public boolean isPassive() {
        return isPassive;
    }

    public AbilitySettings setPassive(boolean passive) {
        isPassive = passive;
        return this;
    }

    public ItemAbility.AbilityAction getAbilityAction() {
        return abilityAction;
    }

    public AbilitySettings setAbilityAction(ItemAbility.AbilityAction abilityAction) {
        this.abilityAction = abilityAction;
        return this;
    }
}
