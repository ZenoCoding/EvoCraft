package me.zenox.superitems.item;

import com.archyx.aureliumskills.stats.Stat;
import com.google.common.collect.Multimap;
import me.zenox.superitems.abilities.Ability;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.Serializable;
import java.util.*;

public class ItemSettings {

    private String id;
    private final Boolean unique;
    private Boolean glow;
    private ComplexItem.Rarity rarity;
    private ComplexItem.Type type;
    private Material material;
    private ItemMeta meta;
    private Map<Stat, Double> stats;
    private String skullURL;
    private List<Ability> abilities;
    private HashMap<VariableType, Serializable> variableMap;
    private List<me.zenox.superitems.attribute.AttributeModifier> attributeModifiers;

    public ItemSettings() {
        this.id = "undefined_" + UUID.randomUUID();
        this.unique = false;
        this.glow = false;
        this.rarity = ComplexItem.Rarity.COMMON;
        this.type = ComplexItem.Type.MISC;
        this.material = Material.BARRIER;
        this.meta = new ItemStack(this.material).getItemMeta();
        this.stats = new HashMap<>();
        this.skullURL = "";
        this.abilities = new ArrayList<>();
        this.variableMap = new HashMap<>();
        this.attributeModifiers = new ArrayList<>();
    }

    public ItemSettings(String id, Boolean unique, Boolean glow, ComplexItem.Rarity rarity, ComplexItem.Type type, Material material, ItemMeta meta, Map<Stat, Double> stats, String skullURL, List<Ability> abilities, HashMap<VariableType, Serializable> variableMap, List<me.zenox.superitems.attribute.AttributeModifier> attributeModifiers) {
        this.id = id;
        this.unique = unique;
        this.glow = glow;
        this.rarity = rarity;
        this.type = type;
        this.material = material;
        this.meta = meta;
        this.stats = stats;
        this.skullURL = skullURL;
        this.abilities = abilities;
        this.variableMap = variableMap;
        this.attributeModifiers = attributeModifiers;
    }

    public ItemSettings(String id, Boolean unique, Boolean glow, ComplexItem.Rarity rarity, ComplexItem.Type type, Material material, ItemMeta meta, Map<Stat, Double> stats, String skullURL, List<Ability> abilities){
        this(id, unique, glow, rarity, type, material, meta, stats, skullURL, abilities, new HashMap<>(), new ArrayList<>());
    }


    public static ItemSettings of(ComplexItem item) {
        return new ItemSettings(item.getId(), item.isUnique(), item.doesGlow(), item.getRarity(), item.getType(), item.getMaterial(), item.getMeta(), item.getStats(), item.getSkullURL(), item.getAbilities(), item.getVariableMap(), item.getAttributeModifiers());
    }

    public static ItemSettings of(ItemStack item) {
        return new ItemSettings(item.getType().name(), false, false, ComplexItem.Rarity.COMMON,
                ComplexItem.Type.MISC, item.getType(), item.getItemMeta(), new HashMap<>(),
                (item.getItemMeta() instanceof SkullMeta) ? ((SkullMeta) item.getItemMeta()).getOwnerProfile().getTextures().getSkin().toString() : "", new ArrayList<>());
    }

    public String getId() {
        return id;
    }

    public Boolean isUnique() {
        return unique;
    }

    public Boolean doesGlow() {
        return glow;
    }

    public ComplexItem.Rarity getRarity() {
        return rarity;
    }

    public ComplexItem.Type getType() {
        return type;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public Map<Stat, Double> getStats() {
        return stats;
    }

    public String getSkullURL() {
        return skullURL;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public HashMap<VariableType, Serializable> getVariableMap() {
        return variableMap;
    }

    public List<me.zenox.superitems.attribute.AttributeModifier> getAttributeModifiers() {
        return attributeModifiers;
    }

    public ItemSettings id(String id) {
        this.id = id;
        return this;
    }

    public ItemSettings rarity(ComplexItem.Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public ItemSettings type(ComplexItem.Type type) {
        this.type = type;
        return this;
    }

    public ItemSettings material(Material material) {
        this.material = material;
        return this;
    }

    public ItemSettings meta(ItemMeta meta) {
        this.meta = meta;
        return this;
    }

    public ItemSettings unbreakable() {
        this.meta.setUnbreakable(true);
        this.meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }

    public ItemSettings enchant(Enchantment enchantment, Integer level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemSettings attribute(Attribute attribute, AttributeModifier modifier) {
        this.meta.addAttributeModifier(attribute, modifier);
        return this;
    }

    public ItemSettings attribute(Multimap<Attribute, AttributeModifier> modifiers) {
        this.meta.setAttributeModifiers(modifiers);
        return this;
    }

    public ItemSettings stats(Map<Stat, Double> stats) {
        this.stats = stats;
        return this;
    }

    public ItemSettings stat(Stat stat, Double num) {
        this.stats.put(stat, num);
        return this;
    }

    public ItemSettings skullURL(String skullURL) {
        this.skullURL = skullURL;
        return this;
    }

    public ItemSettings abilities(List<Ability> abilities) {
        this.abilities = abilities;
        return this;
    }

    public ItemSettings abilities(Ability... abilities) {
        this.abilities = List.of(abilities);
        return this;
    }

    public ItemSettings addAbilities(Ability... abilities) {
        this.abilities.addAll(List.of(abilities));
        return this;
    }

    public ItemSettings ability(Ability ability) {
        this.abilities.add(ability);
        return this;
    }

    public ItemSettings glow() {
        this.glow = true;
        return this;
    }

    public ItemSettings setVariableMap(HashMap<VariableType, Serializable> variableMap) {
        this.variableMap = variableMap;
        return this;
    }

    public ItemSettings variable(VariableType type, Serializable value){
        this.variableMap.put(type, value);
        return this;
    }

    public ItemSettings modifier(me.zenox.superitems.attribute.AttributeModifier ... modifiers){
        this.attributeModifiers.addAll(List.of(modifiers));
        return this;
    }

    public ItemSettings setAttributeModifiers(List<me.zenox.superitems.attribute.AttributeModifier> attributeModifiers) {
        this.attributeModifiers = attributeModifiers;
        return this;
    }
}
