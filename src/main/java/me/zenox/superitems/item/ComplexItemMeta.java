package me.zenox.superitems.item;

import com.archyx.aureliumskills.modifier.ModifierType;
import com.archyx.aureliumskills.modifier.StatModifier;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.abilities.Ability;
import me.zenox.superitems.abilities.ItemAbility;
import me.zenox.superitems.enchant.ComplexEnchantment;
import me.zenox.superitems.persistence.ArrayListType;
import me.zenox.superitems.persistence.SerializedPersistentType;
import me.zenox.superitems.util.Romans;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

/**
 * ComplexItemMeta Class
 * <p>
 * A "complex" version of ItemMeta that stores more information about stats, upgrades, etc.
 * <p>
 * Converts between PersistentDataStorage and Class, at an abstract level,
 * manipulating normal ItemMeta in an attempt to get rid of functional programming inconsistencies
 */

public class ComplexItemMeta {
    public static final NamespacedKey ABILITY_ID = new NamespacedKey(SuperItems.getPlugin(), "ability");
    public static final String VAR_PREFIX = "var_";
    public static final VariableType<ComplexItem.Rarity> RARITY_VAR = new VariableType<ComplexItem.Rarity>("rarity", new LoreEntry("rarity", List.of("Rarity Lore")), VariableType.Priority.BELOW, (loreEntry, variable) -> loreEntry.setLore(List.of(((ComplexItem.Rarity) variable.getValue()).color() + ((ComplexItem.Rarity) variable.getValue()).getName())));
    public static final VariableType<ComplexItem.Type> TYPE_VAR = new VariableType<ComplexItem.Type>("type", new LoreEntry("type", List.of("Type Lore"), ((loreBuilder, loreEntry) -> {
        loreBuilder.getLoreEntryById(
                RARITY_VAR.name()).get(0).setLore(List.of(loreBuilder.getLoreEntryById(RARITY_VAR.name()).get(0).getLore().get(0) + " " + loreEntry.getLore().get(0)));
        loreEntry.setLore(List.of());
    })), VariableType.Priority.BELOW, (loreEntry, variable) -> loreEntry.setLore(List.of(((ComplexItem.Type) variable.getValue()).getName())));
    private static final NamespacedKey ENCHANT_KEY = new NamespacedKey(SuperItems.getPlugin(), "complexEnchants");
    private List<Ability> abilities;
    private final List<Variable> variableList = new ArrayList<>();
    private HashMap<ComplexEnchantment, Integer> complexEnchantments = new HashMap<>();
    private final ComplexItemStack complexItemStack;

    public ComplexItemMeta(ComplexItemStack complexItemStack, List<Ability> abilities) {
        this.abilities = abilities == null ? new ArrayList<>() : new ArrayList<>(abilities);
        this.complexItemStack = complexItemStack;
        this.read(false);
    }

    public ComplexItemMeta(ComplexItemStack complexItemStack) {
        this(complexItemStack, complexItemStack.getComplexItem().getAbilities());
    }

    /**
     * Updates the ItemStack to match the current ComplexItemMeta
     */
    public void updateItem() {
        ItemStack item = complexItemStack.getItem();
        ItemMeta meta = item.getItemMeta();

        // Write Stats
        List<String> statlore = new ArrayList<>();
        List<StatModifier> modifiers = SuperItems.getPlugin().modifiers.getModifiers(ModifierType.ARMOR, item);
        modifiers.addAll(SuperItems.getPlugin().modifiers.getModifiers(ModifierType.ITEM, item));
        modifiers.stream().forEach(statModifier -> statlore.add(ChatColor.GRAY + statModifier.getStat().getDisplayName(Locale.ENGLISH) + ": " + statModifier.getStat().getColor(Locale.ENGLISH) + ((int) statModifier.getValue())));

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        LoreBuilder lore = new LoreBuilder();

        writeVariables(VariableType.Priority.ABOVE_STATS, dataContainer, lore, true);

        // Util.logToConsole("Stat Lore: " + statlore);
        if (!statlore.isEmpty()) {
            lore.entry(new LoreEntry("stat_lore", statlore));
            lore.entry(new LoreEntry("newline", List.of("")));
        }

        writeVariables(VariableType.Priority.ABOVE_LORE, dataContainer, lore, true);


        List<String> trueLore = complexItemStack.getComplexItem().getDefaultLore();

        lore.entry(new LoreEntry("true_lore", trueLore == null ? new ArrayList<>() : trueLore));
        if (trueLore != null && !trueLore.isEmpty()) lore.entry(new LoreEntry("newline", List.of("")));

        writeVariables(VariableType.Priority.ABOVE_ENCHANTS, dataContainer, lore, true);

        // Write Enchants
        for (Map.Entry<Enchantment, Integer> e : meta.getEnchants().entrySet()) {
            ChatColor color = ChatColor.GRAY;
            StringBuilder enchantName = new StringBuilder();
            if (e.getValue() == e.getKey().getMaxLevel()) color = ChatColor.AQUA;
            if (e.getValue() > e.getKey().getMaxLevel()) color = ChatColor.LIGHT_PURPLE;
            Arrays.stream(e.getKey().getKey().getKey().split("_")).forEach((String str) -> enchantName.append(str.substring(0, 1).toUpperCase() + str.substring(1)).append(" "));
            lore.entry(new LoreEntry("enchant_" + e.getKey().getKey().getKey(), List.of(color + enchantName.toString() + Romans.encode(e.getValue()))));
        }

        HashMap<String, Integer> complexEnchMap = new HashMap<>();
        for (Map.Entry<ComplexEnchantment, Integer> entry :
                this.complexEnchantments.entrySet()) {
            complexEnchMap.put(entry.getKey().getId(), entry.getValue());
        }

        // Write ComplexEnchants
        dataContainer.set(ENCHANT_KEY, new SerializedPersistentType<HashMap>(), complexEnchMap);

        for (Map.Entry<ComplexEnchantment, Integer> e : this.complexEnchantments.entrySet()) {
            ChatColor color = ChatColor.GRAY;
            String enchantName = e.getKey().getName().toString();
            if (e.getValue() == e.getKey().getMaxLevel()) color = ChatColor.AQUA;
            if (e.getValue() > e.getKey().getMaxLevel()) color = ChatColor.LIGHT_PURPLE;
            lore.entry(new LoreEntry("enchant_" + e.getKey().getId(), List.of(color + enchantName + " " + Romans.encode(e.getValue()))));
        }

        if (!meta.getEnchants().isEmpty() || !this.complexEnchantments.isEmpty()) lore.entry(new LoreEntry("newline", List.of("")));

        writeVariables(VariableType.Priority.ABOVE_ABILITIES, dataContainer, lore, true);

        // Write Abilities
        writeAbilityLore(lore);
        dataContainer.set(ABILITY_ID, new ArrayListType(), new ArrayList<>(abilities.stream().map(Ability::getId).toList()));

        writeVariables(VariableType.Priority.BELOW_ABILITIES, dataContainer, lore, true);

        writeVariables(VariableType.Priority.BELOW, dataContainer, lore, false);

        meta.setLore(lore.build());

        dataContainer.set(ComplexItem.GLOW_ID, PersistentDataType.INTEGER, complexItemStack.getComplexItem().doesGlow() ? 1 : 0);

        // Finally, set the meta
        item.setItemMeta(meta);
    }

    /**
     * A method to read and get a clone of the ComplexItemMeta of a normal ItemStack
     *
     * @param force whether to force- whether this is a new/unfinished item or a preexisting item
     */
    public void read(Boolean force) {
        ItemStack item = complexItemStack.getItem();
        ItemMeta meta = item.getItemMeta();

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        if (force) {
            this.abilities = (dataContainer
                    .get(ABILITY_ID, new ArrayListType<>()) == null ? new ArrayList<>() : new ArrayList<>(dataContainer.get(ABILITY_ID, new ArrayListType<>()))).stream().map(o -> {
                if (o instanceof String) return Ability.getAbility(((String) o));
                else return Ability.getAbility(((Ability) o).getId());
            }).toList();
        }

        HashMap<String, Integer> complexEnchMap = dataContainer.has(ENCHANT_KEY, new SerializedPersistentType<>()) ? dataContainer.get(ENCHANT_KEY, new SerializedPersistentType<>()) : new HashMap<>();

        for (Map.Entry<String, Integer> entry: complexEnchMap.entrySet()){
            this.complexEnchantments.put(ComplexEnchantment.byId(entry.getKey()), entry.getValue());
        }

        dataContainer.getKeys().stream()
                .filter(namespacedKey -> namespacedKey.getKey().startsWith(VAR_PREFIX))
                .forEach(namespacedKey -> setVariable(VariableType.getVariableByPrefix(namespacedKey.getKey().substring(VAR_PREFIX.length())), dataContainer.get(namespacedKey, new SerializedPersistentType<>())));

        // if the meta doesn't contain rarity or type
        if (this.variableList.stream().filter((variable -> variable.getType().name() == RARITY_VAR.name())).toList().isEmpty())
            this.variableList.add(new Variable(this, RARITY_VAR, ComplexItem.Rarity.UNKNOWN));
        if (this.variableList.stream().filter((variable -> variable.getType().name() == TYPE_VAR.name())).toList().isEmpty())
            this.variableList.add(new Variable(this, TYPE_VAR, ComplexItem.Type.MISC));

        item.setItemMeta(meta);
        updateItem();
    }

    private void writeVariables(VariableType.Priority priority, PersistentDataContainer container, LoreBuilder builder, Boolean newline) {
        Stream<Variable> filteredList = variableList.stream().filter(variable -> variable.getType().priority() == priority);
        filteredList.forEachOrdered(variable -> {
            try {
                variable.write(container, builder);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        });
        if (!variableList.stream().filter(variable -> variable.getType().priority() == priority).toList().isEmpty() && newline)
            builder.entry(new LoreEntry("newline", List.of("")));

    }

    private void writeAbilityLore(LoreBuilder loreBuilder) {
        for (Ability ability : this.abilities) {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Ability: " + ability.getDisplayName() + ChatColor.YELLOW + ChatColor.BOLD + " " + (ability instanceof ItemAbility ? ((ItemAbility) ability).getAction().getName() : ""));
            lore.addAll(ability.getLore());

            if (ability.getManaCost() > 0)
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + ability.getManaCost());
            if (ability.getCooldown() > 0)
                lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + ability.getCooldown() + "s");
            loreBuilder.entry(new LoreEntry("ability_" + ability.getId(), lore));
            loreBuilder.entry(new LoreEntry("newline", List.of(" ")));
        }
    }

    public void addVariable(Variable var) {
        this.variableList.add(var);
        this.updateItem();
    }

    public void setVariable(VariableType type, @NotNull Serializable value) {
        if (variableList.stream().filter(variable -> variable.getType() == type).toList().isEmpty())
            variableList.add(new Variable(this, type, value));
        else
            variableList.stream().filter(variable -> variable.getType() == type).forEach(variable -> variable.setValue(value));
    }

    // Gets the first variable of that variable type
    @Nullable
    public Variable getVariable(VariableType type) {
        try {
            return variableList.stream().filter(variable -> variable.getType() == type).toList().get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public boolean hasVariable(VariableType type){
        return Objects.nonNull(getVariable(type));
    }


    public Map<ComplexEnchantment, Integer> getComplexEnchants(){
        return this.complexEnchantments;
    }

    public Map<Enchantment, Integer> getEnchants(){
        return this.complexItemStack.getItem().getItemMeta().getEnchants();
    }

    public void addEnchantment(Enchantment enchantment, Integer level){
        ItemMeta meta = this.complexItemStack.getItem().getItemMeta();
        meta.addEnchant(enchantment, level, true);
        this.complexItemStack.getItem().setItemMeta(meta);
        updateItem();
    }

    public void addEnchantment(ComplexEnchantment enchantment, Integer level){
        this.complexEnchantments.put(enchantment, level);
        updateItem();
    }

    public void removeEnchantment(Enchantment enchantment){
        ItemMeta meta = this.complexItemStack.getItem().getItemMeta();
        meta.removeEnchant(enchantment);
        this.complexItemStack.getItem().setItemMeta(meta);
        updateItem();
    }

    public void setComplexEnchantments(HashMap<ComplexEnchantment, Integer> complexEnchantments){
        this.complexEnchantments = complexEnchantments;
        updateItem();
    }

    public void removeEnchantment(ComplexEnchantment enchantment){
        this.complexEnchantments.remove(enchantment);
    }

    public List<Variable> getVariableList() {
        return variableList;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }
}
