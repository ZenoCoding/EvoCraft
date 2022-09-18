package me.zenox.superitems.item;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.modifier.ModifierType;
import com.archyx.aureliumskills.modifier.StatModifier;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.abilities.Ability;
import me.zenox.superitems.item.abilities.ItemAbility;
import me.zenox.superitems.persistence.ArrayListType;
import me.zenox.superitems.util.Romans;
import me.zenox.superitems.persistence.SerializedPersistentType;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

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
    private ItemMeta itemMeta;
    private List<Ability> abilities;
    private List<Variable> variableList = new ArrayList<>();

    private ItemStack item;
    public static final NamespacedKey ABILITY_ID = new NamespacedKey(SuperItems.getPlugin(), "ability");
    public static final String VAR_PREFIX = "var_";
    public static final VariableType RARITY_VAR = new VariableType<ComplexItem.Rarity>("rarity", new LoreEntry("rarity", List.of("Rarity Lore")), VariableType.Priority.BELOW, (loreEntry, variable) -> loreEntry.setLore(List.of(((ComplexItem.Rarity) variable.getValue()).color() + ((ComplexItem.Rarity) variable.getValue()).getName())));
    public static final VariableType TYPE_VAR = new VariableType<ComplexItem.Type>("type", new LoreEntry("type", List.of("Type Lore"), ((loreBuilder, loreEntry) -> {loreBuilder.getLoreEntryById(
            RARITY_VAR.getName()).get(0).setLore(List.of(loreBuilder.getLoreEntryById(RARITY_VAR.getName()).get(0).getLore().get(0) + " " + loreEntry.getLore().get(0)));
            loreEntry.setLore(List.of());})), VariableType.Priority.BELOW, (loreEntry, variable) -> loreEntry.setLore(List.of(((ComplexItem.Type) variable.getValue()).getName())));

    public ComplexItemMeta(ItemMeta itemMeta, List<Ability> abilities, ItemStack item){
        this.itemMeta = itemMeta;
        this.abilities = abilities == null ? new ArrayList<>() : new ArrayList<>(abilities);
        this.item = item;
        this.read(false);
    }

    public ComplexItemMeta(ItemMeta itemMeta, ItemStack item){
        this(itemMeta, new ArrayList<>(), item);
    }

    /**
     * Updates the ItemStack to match the current ComplexItemMeta
     */
    public void updateItem(){
        Util.logToConsole("Variable List: " + variableList);

        // Write Stats
        List<String> statlore = new ArrayList<>();
        List<StatModifier> modifiers = SuperItems.getPlugin().modifiers.getModifiers(ModifierType.ARMOR, item);
        modifiers.addAll(SuperItems.getPlugin().modifiers.getModifiers(ModifierType.ITEM, item));
        modifiers.stream().forEach(statModifier -> statlore.add(ChatColor.GRAY + statModifier.getStat().getDisplayName(Locale.ENGLISH) + ": " + statModifier.getStat().getColor(Locale.ENGLISH) + ((int) statModifier.getValue())));



        for (StatModifier modifier : modifiers) {
            if (((ComplexItem.Type) this.getVariable(TYPE_VAR).getValue()).isWearable()) {
                AureliumAPI.addArmorModifier(item, modifier.getStat(), modifier.getValue(), false);
            } else {
                AureliumAPI.addItemModifier(item, modifier.getStat(), modifier.getValue(), false);
            }
        }

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        LoreBuilder lore = new LoreBuilder();

        writeVariables(VariableType.Priority.ABOVE_STATS, dataContainer, lore, true);


        Util.logToConsole("Stat Lore: " + statlore);
        if(!statlore.isEmpty()) {
            lore.entry(new LoreEntry("stat_lore", statlore));
            lore.entry(new LoreEntry("newline", List.of("")));
        }

        writeVariables(VariableType.Priority.ABOVE_LORE, dataContainer, lore, true);

        lore.entry(new LoreEntry("true_lore", itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore()));
        if (itemMeta.getLore() != null && !itemMeta.getLore().isEmpty()) lore.entry(new LoreEntry("newline", List.of("")));

        writeVariables(VariableType.Priority.ABOVE_ENCHANTS, dataContainer, lore, true);

        // Write Enchants
        for(Map.Entry<Enchantment, Integer> e : itemMeta.getEnchants().entrySet()) {
            ChatColor color = ChatColor.GRAY;
            StringBuilder enchantName = new StringBuilder();
            if(e.getValue() == e.getKey().getMaxLevel()) color = ChatColor.AQUA;
            if(e.getValue() > e.getKey().getMaxLevel()) color = ChatColor.LIGHT_PURPLE;
            Arrays.stream(e.getKey().getKey().getKey().split("_")).forEach((String str) -> {enchantName.append(str.substring(0, 1).toUpperCase() + str.substring(1)).append(" ");});
            lore.entry(new LoreEntry("enchant_" +  e.getKey().getKey().getKey(), List.of(color + enchantName.toString() + Romans.encode(e.getValue()))));
        }

        if(!itemMeta.getEnchants().isEmpty()) lore.entry(new LoreEntry("newline", List.of("")));

        writeVariables(VariableType.Priority.ABOVE_ABILITIES, dataContainer, lore, true);

        // Write Abilities
        writeAbilityLore(lore);
        dataContainer.set(ABILITY_ID, new ArrayListType<Ability>(), new ArrayList(abilities));

        writeVariables(VariableType.Priority.BELOW, dataContainer, lore, false);

        // Finally, set the variables
        ItemMeta copyItemMeta = itemMeta.clone();
        copyItemMeta.setLore(lore.build());
        Util.logToConsole("Item Lore: " + copyItemMeta.getLore().toString());
        item.setItemMeta(copyItemMeta);
    }

    /**
     * A method to read and get a clone of the ComplexItemMeta of a normal ItemStack
     * @param force whether to force- whether this is a new/unfinished item or a preexisting item
     * @return The ComplexItemMeta of the ItemStack
     */
    public void read(Boolean force){
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        if(force) this.abilities = dataContainer.get(ABILITY_ID, new ArrayListType<>()) == null ? new ArrayList<>() : new ArrayList<>(dataContainer.get(ABILITY_ID, new ArrayListType<>()));
        Util.logToConsole("(Read | Keys) Variable List: " + dataContainer.getKeys());
        this.variableList.addAll(dataContainer.getKeys().stream()
                .filter(namespacedKey -> namespacedKey.getKey().startsWith(VAR_PREFIX))
                .map(namespacedKey -> new Variable(this, VariableType.getVariableByPrefix(namespacedKey.getKey().substring(VAR_PREFIX.length())), dataContainer.get(namespacedKey, new SerializedPersistentType<>())))
                .toList());
        Util.logToConsole("(Read | Data | Filtered) Variable List: " + dataContainer.getKeys().stream()
                .filter(namespacedKey -> namespacedKey.getKey().startsWith(VAR_PREFIX)).toList());

        // if the meta doesn't contain rarity or type
        if(this.variableList.stream().filter((variable -> variable.getType().getName() == RARITY_VAR.getName())).toList().isEmpty()) this.variableList.add(new Variable(this, RARITY_VAR, ComplexItem.Rarity.COMMON));
        if(this.variableList.stream().filter((variable -> variable.getType().getName() == TYPE_VAR.getName())).toList().isEmpty()) this.variableList.add(new Variable(this, TYPE_VAR, ComplexItem.Type.MISC));

        updateItem();
    }

    private void writeVariables(VariableType.Priority priority, PersistentDataContainer container, LoreBuilder builder, Boolean newline){
        Stream<Variable> filteredList = variableList.stream().filter(variable -> variable.getType().getPriority() == priority);
        filteredList.forEachOrdered(variable -> {
            try { variable.write(container, builder); } catch (CloneNotSupportedException e){e.printStackTrace();}
        });
        if(!variableList.stream().filter(variable -> variable.getType().getPriority() == priority).toList().isEmpty() && newline) builder.entry(new LoreEntry("newline", List.of("")));

    }

    private void writeAbilityLore(LoreBuilder loreBuilder) {
        for (Ability ability : this.abilities) {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Ability: " + ability.getName() + ChatColor.YELLOW + ChatColor.BOLD + " " + (ability instanceof ItemAbility ? ((ItemAbility) ability).getAction().getName() : ""));
            lore.addAll(ability.getLore());

            if (ability.getManaCost() > 0) lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + ability.getManaCost());
            if (ability.getCooldown() > 0) lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + ability.getCooldown() + "s");
            loreBuilder.entry(new LoreEntry("ability_" + ability.getId(), lore));
            loreBuilder.entry(new LoreEntry("newline", List.of(" ")));
        }
    }

    /**
     * MUST call updateItem() when this ItemMeta is updated.
     */
    public ItemMeta getItemMeta(){
        return itemMeta;
    }

    public void addVariable(Variable var){
        this.variableList.add(var);
        this.updateItem();
    }

    public void setVariable(VariableType type, @NotNull Serializable value){
        variableList.stream().filter(variable -> variable.getType() == type).forEach(variable -> variable.setValue(value));
    }

    // Gets the first variable of that variable type
    public Variable getVariable(VariableType type){
        return variableList.stream().filter(variable -> variable.getType() == type).toList().get(0);
    }

    public List<Variable> getVariableList(){
        return variableList;
    }

}
