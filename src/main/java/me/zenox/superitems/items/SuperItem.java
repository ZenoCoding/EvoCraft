package me.zenox.superitems.items;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Executable;
import me.zenox.superitems.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SuperItem {
    public static NamespacedKey GLOBAL_ID = new NamespacedKey(SuperItems.getPlugin(), "superitem");
    private String name;
    private String id;
    private Rarity rarity;
    private Type type;
    private ItemAbility ability;
    private Material material;
    private ItemMeta meta;
    private ShapedRecipe recipe;

    public SuperItem(String name, String id, Rarity rarity, Type type, ItemAbility ability, Material material, ItemMeta metadata, ShapedRecipe recipe){
        this.name = name;
        this.id = id;
        this.rarity = rarity;
        this.type = type;
        this.ability = ability;
        this.material = material;
        this.meta = metadata;
        this.recipe = recipe;

        addRecipe(this.recipe);
    }

    private void addRecipe(ShapedRecipe recipe){
        try{
            Bukkit.addRecipe(recipe);
        } catch (Exception e){
            Util.logToConsole(e.toString());
        }
    }

    public ItemStack getItemStack(Integer amount){
        ItemStack item = new ItemStack(this.material);
        item.setItemMeta(this.meta);
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        // Add persistent data
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(GLOBAL_ID, PersistentDataType.STRING, this.id);

        List<String> lore = (meta.getLore() == null) ? new ArrayList() : meta.getLore();
        lore.add("");
        lore.add(ChatColor.GOLD + "Ability: " + this.ability.getAbilityName() + ChatColor.YELLOW + ChatColor.BOLD + "  RIGHT CLICK");
        lore.addAll(this.ability.getAbilityLore());
        lore.add("");
        lore.add(this.rarity.color() + this.rarity.getName() + " " + this.type.getName());
        meta.setLore(lore);
        meta.setDisplayName(this.rarity.color() + this.getName());
        item.setItemMeta(meta);
        return item;
    }

    public String getDisplayName(){
        return this.rarity.color() + this.name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material){
        this.material = material;
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public void setMeta(ItemMeta meta) {
        this.meta = meta;
    }

    public ItemAbility getAbility() {
        return ability;
    }

    public void setAbility(ItemAbility ability) {
        this.ability = ability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShapedRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(ShapedRecipe recipe) {
        this.recipe = recipe;
    }

    enum Rarity{

        COMMON(ChatColor.WHITE, ChatColor.BOLD + "COMMON"), UNCOMMON(ChatColor.GREEN, ChatColor.BOLD + "UNCOMMON"),
        RARE(ChatColor.BLUE, ChatColor.BOLD + "RARE"), EPIC(ChatColor.DARK_PURPLE, ChatColor.BOLD + "EPIC"),
        LEGENDARY(ChatColor.GOLD, ChatColor.BOLD + "LEGENDARY"),
        MYTHIC(ChatColor.LIGHT_PURPLE, ChatColor.MAGIC + "" + ChatColor.BOLD + "D" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "MYTHIC" + ChatColor.MAGIC + "D" + ChatColor.LIGHT_PURPLE);
        //SPECIAL(ChatColor.RED), VERY_SPECIAL(ChatColor.RED);

        private ChatColor color;
        private String name;

        private Rarity(ChatColor color, String name){
            this.color = color;
            this.name = name;
        }

        public ChatColor color(){return color;}

        public String getName(){return name;}

    }

    enum Type {

        SWORD("SWORD"), WAND("WAND"), SUPERITEM("SUPERITEM"), DEPLOYABLE("DEPLOYABLE");

        private String name;

        private Type(String name){
            this.name = name;
        }

        public String getName(){ return name; }
    }
}
