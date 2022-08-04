package me.zenox.superitems.items;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.stats.Stat;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.List;

public class BasicItem {

    public static NamespacedKey GLOBAL_ID = new NamespacedKey(SuperItems.getPlugin(), "superitem");
    private String name;
    private String id;
    private NamespacedKey key;
    private Rarity rarity;
    private Type type;
    private Material material;
    private ItemMeta meta;
    private Map<Stat, Double> stats;

    public BasicItem(String name, String id, Rarity rarity, Type type, Material material, Map<Stat, Double> stats) {
        this.name = name;
        this.id = id;
        this.rarity = rarity;
        this.type = type;
        this.material = material;
        this.meta = new ItemStack(this.material).getItemMeta();
        this.stats = stats;
        this.key = new NamespacedKey(SuperItems.getPlugin(), id);
    }

    public List<Recipe> getRecipes(List<BasicItem> registeredItems){
        return new ArrayList<>();
    }

    public BasicItem() {
    }

    public ItemStack getItemStack(Integer amount) {
        return getItemStackWithData(amount, new ArrayList<>());
    }

    public ItemStack getItemStackWithData(Integer amount, List<String> data) {
        ItemStack item = new ItemStack(this.material);
        item.setItemMeta(this.meta);
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        // Add persistent data
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(GLOBAL_ID, PersistentDataType.STRING, this.getId());

        List<String> lore = new ArrayList<>();

        if(!this.stats.isEmpty()) lore.add(" ");

        if(item.getItemMeta().getLore() != null) lore.addAll(item.getItemMeta().getLore());

        String loreconcat = "";

        for (String loreitem : lore) {
            loreconcat = loreconcat + loreitem + "∫";
        }

        loreconcat.formatted(data);
        List<String> loreformatted = new ArrayList();
        loreformatted.addAll(Arrays.asList(loreconcat.split("∫")));

        writeAbilityLore(loreformatted);

        try {
            if (lore.get(lore.size() - 1).strip() != "") loreformatted.add("");
        } catch(IndexOutOfBoundsException e) {
//            loreformatted.add("");
        }

        loreformatted.add(this.getRarity().color() + this.getRarity().getName() + " " + this.getType().getName());
        meta.setLore(loreformatted);
        meta.setDisplayName(this.getRarity().color() + this.getName());
        item.setItemMeta(meta);

        for(Map.Entry<Stat, Double> entry : this.getStats().entrySet()){
            item = AureliumAPI.addItemModifier(item, entry.getKey(), entry.getValue(), true);
        }

        return item;
    }

    protected void writeAbilityLore(List<String> loreformatted){

    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return this.rarity.color() + this.name;
    }

    public String getId() {
        return id;
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

    public void setMaterial(Material material) {
        this.material = material;
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Map<Stat, Double> getStats() {
        return stats;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public enum Rarity {

        COMMON(ChatColor.WHITE, ChatColor.BOLD + "COMMON"), UNCOMMON(ChatColor.GREEN, ChatColor.BOLD + "UNCOMMON"),
        RARE(ChatColor.BLUE, ChatColor.BOLD + "RARE"), EPIC(ChatColor.DARK_PURPLE, ChatColor.BOLD + "EPIC"),
        LEGENDARY(ChatColor.GOLD, ChatColor.BOLD + "LEGENDARY"),
        MYTHIC(ChatColor.LIGHT_PURPLE, ChatColor.MAGIC + "" + ChatColor.BOLD + "D " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "MYTHIC" + ChatColor.MAGIC + " D" + ChatColor.LIGHT_PURPLE),
        SPECIAL(ChatColor.RED, ChatColor.MAGIC + "" + ChatColor.BOLD + "D " + ChatColor.RED + ChatColor.BOLD + "SPECIAL" + ChatColor.MAGIC + " D" + ChatColor.RED),
        VERY_SPECIAL(ChatColor.RED, ChatColor.MAGIC + "" + ChatColor.BOLD + "| D " + ChatColor.RED + ChatColor.BOLD + "VERY SPECIAL" + ChatColor.MAGIC + " D |" + ChatColor.RED);

        private final ChatColor color;
        private final String name;

        Rarity(ChatColor color, String name) {
            this.color = color;
            this.name = name;
        }

        public ChatColor color() {
            return color;
        }

        public String getName() {
            return name;
        }

    }

    public enum Type {

        SWORD("SWORD"), AXE("AXE"), WAND("WAND"), STAFF("STAFF"), SUPERITEM("SUPERITEM"), DEPLOYABLE("DEPLOYABLE"), MISC(""),
        HELMET("HELMET"), CHESTPLATE("CHESTPLATE"), LEGGINGS("LEGGINGS"), BOOTS("BOOTS");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
