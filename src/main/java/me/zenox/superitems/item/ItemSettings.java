package me.zenox.superitems.item;

import com.archyx.aureliumskills.stats.Stat;
import me.zenox.superitems.item.abilities.Ability;
import me.zenox.superitems.item.abilities.ItemAbility;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class ItemSettings {

    private String id;
    private Boolean unique;
    private ComplexItem.Rarity rarity;
    private ComplexItem.Type type;
    private Material material;
    private ItemMeta meta;
    private Map<Stat, Double> stats;
    private String skullURL;

    private List<Ability> abilities;

    public ItemSettings(){
        this.id = "undefined_" + UUID.randomUUID();
        this.unique = false;
        this.rarity = ComplexItem.Rarity.COMMON;
        this.type = ComplexItem.Type.MISC;
        this.material = Material.BARRIER;
        this.meta = new ItemStack(this.material).getItemMeta();
        this.stats = new HashMap<>();
        this.skullURL = "";
        this.abilities = new ArrayList<>();
    }

    public ItemSettings(String id, Boolean unique, ComplexItem.Rarity rarity, ComplexItem.Type type, Material material, ItemMeta meta, Map<Stat, Double> stats, String skullURL, List<Ability> abilities){
        this.id = id;
        this.unique = unique;
        this.rarity = rarity;
        this.type = type;
        this.material = material;
        this.meta = meta;
        this.stats = stats;
        this.skullURL = skullURL;
        this.abilities = abilities;
    }

    public static ItemSettings of(ComplexItem item){
        return new ItemSettings(item.getId(), item.isUnique() ,item.getRarity(), item.getType(), item.getMaterial(), item.getMeta(), item.getStats(), item.getSkullURL(), new ArrayList<>());
    }

    public static ItemSettings of(ItemStack item){
        return new ItemSettings(item.getType().name(), false, ComplexItem.Rarity.COMMON,
                ComplexItem.Type.MISC, item.getType(), item.getItemMeta(), new HashMap(),
                (item.getItemMeta() instanceof SkullMeta) ? ((SkullMeta) item.getItemMeta()).getOwnerProfile().getTextures().getSkin().toString() : "", new ArrayList());
    }

    public String getId() {
        return id;
    }

    public Boolean isUnique(){ return unique;}

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

    public ItemSettings id(String id){
        this.id = id;
        return this;
    }

    public ItemSettings rarity(ComplexItem.Rarity rarity){
        this.rarity = rarity;
        return this;
    }

    public ItemSettings type(ComplexItem.Type type){
        this.type = type;
        return this;
    }

    public ItemSettings material(Material material){
        this.material = material;
        return this;
    }

    public ItemSettings meta(ItemMeta meta){
        this.meta = meta;
        return this;
    }

    public ItemSettings stats(Map<Stat, Double> stats){
        this.stats = stats;
        return this;
    }

    public ItemSettings stat(Stat stat, Double num){
        this.stats.put(stat, num);
        return this;
    }

    public ItemSettings skullURL(String skullURL){
        this.skullURL = skullURL;
        return this;
    }

    public ItemSettings abilities(List<Ability> abilities){
        this.abilities = abilities;
        return this;
    }

    public ItemSettings abilities(ItemAbility ... abilities){
        this.abilities = List.of(abilities);
        return this;
    }

    public ItemSettings addAbilities(ItemAbility ... abilities){
        this.abilities.addAll(List.of(abilities));
        return this;
    }

    public ItemSettings ability(ItemAbility ability){
        this.abilities.add(ability);
        return this;
    }

    public ItemSettings glow(){
        ItemMeta meta = new ItemStack(this.getMaterial()).getItemMeta();
        meta.addEnchant(this.getMaterial() == Material.BOW ? Enchantment.PROTECTION_ENVIRONMENTAL : Enchantment.ARROW_INFINITE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.meta = meta;
        return this;
    }
}
