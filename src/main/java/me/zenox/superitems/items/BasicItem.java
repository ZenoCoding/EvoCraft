package me.zenox.superitems.items;

import me.zenox.superitems.SuperItems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class BasicItem {

    public static NamespacedKey GLOBAL_ID = new NamespacedKey(SuperItems.getPlugin(), "superitem");
    private String name;
    private String id;
    private Rarity rarity;
    private Type type;
    private Material material;
    private ItemMeta meta;

    public BasicItem(String name, String id, Rarity rarity, Type type, Material material, ItemMeta metadata){
        this.name = name;
        this.id = id;
        this.rarity = rarity;
        this.type = type;
        this.material = material;
        this.meta = metadata;
    }

    public ItemStack getItemStack(Integer amount){
        ItemStack item = new ItemStack(this.material);
        item.setItemMeta(this.meta);
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        // Add persistent data
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(GLOBAL_ID, PersistentDataType.STRING, this.getId());

        List<String> lore = (meta.getLore() == null) ? new ArrayList() : meta.getLore();

        lore.add("");
        lore.add(this.getRarity().color() + this.getRarity().getName() + " " + this.getType().getName());
        meta.setLore(lore);
        meta.setDisplayName(this.getRarity().color() + this.getName());
        item.setItemMeta(meta);
        return item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    enum Rarity{

        COMMON(ChatColor.WHITE, ChatColor.BOLD + "COMMON"), UNCOMMON(ChatColor.GREEN, ChatColor.BOLD + "UNCOMMON"),
        RARE(ChatColor.BLUE, ChatColor.BOLD + "RARE"), EPIC(ChatColor.DARK_PURPLE, ChatColor.BOLD + "EPIC"),
        LEGENDARY(ChatColor.GOLD, ChatColor.BOLD + "LEGENDARY"),
        MYTHIC(ChatColor.LIGHT_PURPLE, ChatColor.MAGIC + "" + ChatColor.BOLD + "D " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "MYTHIC" + ChatColor.MAGIC + " D" + ChatColor.LIGHT_PURPLE),
        SPECIAL(ChatColor.RED, ChatColor.MAGIC + "" + ChatColor.BOLD + "D " + ChatColor.RED + ChatColor.BOLD + "SPECIAL" + ChatColor.MAGIC + " D" + ChatColor.RED),
        VERY_SPECIAL(ChatColor.RED, ChatColor.MAGIC + "" + ChatColor.BOLD + "| D " + ChatColor.RED + ChatColor.BOLD + "VERY SPECIAL" + ChatColor.MAGIC + " D |" + ChatColor.RED);

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

        SWORD("SWORD"), WAND("WAND"), STAFF("STAFF"), SUPERITEM("SUPERITEM"), DEPLOYABLE("DEPLOYABLE"), MISC("");

        private String name;

        private Type(String name){
            this.name = name;
        }

        public String getName(){ return name; }
    }
}
