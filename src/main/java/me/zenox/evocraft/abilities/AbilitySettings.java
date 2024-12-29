package me.zenox.evocraft.abilities;

import me.zenox.evocraft.Slot;
import me.zenox.evocraft.abilities.itemabilities.ItemAbility;

import java.util.ArrayList;
import java.util.List;
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
    private List<Modifier> modifiers;

    // Ability-Specific Settings
    private ItemAbility.AbilityAction abilityAction;
    private int strength;
    private int charges;
    private int range;
    private int chargeTime;

    public AbilitySettings() {
        this.id = "default" + UUID.randomUUID();
        this.manaCost = 0;
        this.cooldown = 0;
        this.slot = Slot.EITHER_HAND;
        this.isPassive = false;
        this.abilityAction = ItemAbility.AbilityAction.NONE;
        this.modifiers = new ArrayList<>();
        this.strength = 0;
        this.charges = 0;
        this.range = 0;
        this.chargeTime = 0;
    }

    public String getId() {
        return id;
    }

    public AbilitySettings id(String id) {
        this.id = id;
        return this;
    }

    public int getManaCost() {
        return manaCost;
    }

    public AbilitySettings manaCost(int manaCost) {
        this.manaCost = manaCost;
        return this;
    }

    public double getCooldown() {
        return cooldown;
    }

    public AbilitySettings cooldown(double cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public Slot getSlot() {
        return slot;
    }

    public AbilitySettings slot(Slot slot) {
        this.slot = slot;
        return this;
    }

    public boolean isPassive() {
        return isPassive;
    }

    public AbilitySettings passive(boolean passive) {
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

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public AbilitySettings modifiers(List<Modifier> modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public AbilitySettings modifiers(Modifier... modifiers) {
        this.modifiers.addAll(List.of(modifiers));
        return this;
    }

    public AbilitySettings modifier(Modifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    public int getStrength() {
        return strength;
    }

    public AbilitySettings strength(int strength) {
        this.strength = strength;
        return this;
    }

    public int getCharges() {
        return charges;
    }

    public AbilitySettings charges(int charges) {
        this.charges = charges;
        return this;
    }

    public int getRange() {
        return range;
    }

    public AbilitySettings range(int range) {
        this.range = range;
        return this;
    }

    public int getChargeTime() {
        return chargeTime;
    }

    /**
     * Sets the charge time of the ability
     * @param chargeTime Charge time in seconds
     * @return The ability settings
     */
    public AbilitySettings chargeTime(int chargeTime) {
        this.chargeTime = chargeTime;
        return this;
    }
}
