package me.zenox.evocraft.abilities;

import com.archyx.aureliumskills.api.AureliumAPI;
import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.data.TranslatableList;
import me.zenox.evocraft.data.TranslatableText;
import me.zenox.evocraft.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static me.zenox.evocraft.abilities.ClassAbility.SaveState;

public abstract class Ability<T extends Event> {
    public static final List<Ability<?>> registeredAbilities = new ArrayList<>();
    public static final CooldownManager cooldownManager = new CooldownManager();

    private final TranslatableText name;
    private final String id;
    @SaveState
    private int manaCost;
    @SaveState
    private double cooldown;
    private final boolean isPassive;
    private TranslatableList lore;

    public Ability(String id, int manaCost, double cooldown, boolean isPassive) {
        this.name = new TranslatableText(TranslatableText.Type.ABILITY_NAME + "-" + id);
        this.id = id;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.lore = new TranslatableList(TranslatableText.Type.ABILITY_LORE + "-" + id);
        this.isPassive = isPassive;
    }

    /**
     * method getAbility <br>
     * gets the ability corresponding to the id
     *
     * @param id the id to search for
     * @return the ability- null if not found
     */
    @Nullable
    public static Ability<?> getAbility(String id) {
        for (Ability<?> ability : registeredAbilities) if (ability.getId().equals(id)) return ability;
        return null;
    }

    protected Type getType() {
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

    public abstract void useAbility(Event event);

    protected boolean isAbilityOnCooldown(@NotNull Player p) {
        PersistentDataContainer container = p.getPersistentDataContainer();
        NamespacedKey cooldownKey = new NamespacedKey(EvoCraft.getPlugin(), getId() + "_cooldown");
        Double cooldown;
        try {
            cooldown = container.get(cooldownKey, PersistentDataType.DOUBLE);
        } catch (IllegalArgumentException exception) {
            cooldown = Double.valueOf(container.get(cooldownKey, PersistentDataType.LONG));
            container.remove(cooldownKey);
            container.set(cooldownKey, PersistentDataType.DOUBLE, cooldown);
        }

        return cooldown != null && Math.ceil((cooldown - System.currentTimeMillis()) / 1000) > 0;
    }

    protected void sendCooldownMessage(Player p) {
        if (!isPassive) {
            Util.sendActionBar(p, ChatColor.WHITE + "" + ChatColor.BOLD + "ABILITY ON COOLDOWN (" + ChatColor.RED + Math.ceil((getCooldownEndTime(p) - System.currentTimeMillis()) / 100) / 10 + "s" + ChatColor.WHITE + ")");
        }
    }

    protected double getCooldownEndTime(@NotNull Player p) {
        PersistentDataContainer container = p.getPersistentDataContainer();
        NamespacedKey cooldownKey = new NamespacedKey(EvoCraft.getPlugin(), getId() + "_cooldown");
        return container.get(cooldownKey, PersistentDataType.DOUBLE);
    }

    protected boolean notEnoughMana(Player p, int requiredMana) {
        return ((int) AureliumAPI.getMana(p)) < requiredMana;
    }

    protected void sendManaInsufficientMessage(Player p) {
        if (!isPassive) Util.sendActionBar(p, ChatColor.RED + "" + ChatColor.BOLD + "NOT ENOUGH MANA");
    }

    protected void deductMana(Player p, int manaCost) {
        AureliumAPI.setMana(p, AureliumAPI.getMana(p) - manaCost);
    }

    protected void showMessage(Player p, String msg) {
        String manaMessage = manaCost > 0 ? ChatColor.AQUA + "-" + manaCost + " Mana " + "(" + ChatColor.GOLD + name + ChatColor.AQUA + ")" : ChatColor.GOLD + "Used " + name;
        if (!isPassive) Util.sendActionBar(p, manaMessage + msg);
    }

    protected void setAbilityCooldown(Player p) {
        PersistentDataContainer container = p.getPersistentDataContainer();
        NamespacedKey cooldownKey = new NamespacedKey(EvoCraft.getPlugin(), getId() + "_cooldown");
        container.set(cooldownKey, PersistentDataType.DOUBLE, System.currentTimeMillis() + (getCooldown() * 1000));
    }

    protected void runExecutable(T e, Player p, ItemStack itemStack) {
        Util.sendMessage(p, "Used the " + this.id + " ability");
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

    public boolean isPassive() {
        return isPassive;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

}
