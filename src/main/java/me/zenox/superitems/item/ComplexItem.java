package me.zenox.superitems.item;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.stats.Stat;
import com.google.common.primitives.Ints;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.data.TranslatableList;
import me.zenox.superitems.data.TranslatableText;
import me.zenox.superitems.abilities.Ability;
import me.zenox.superitems.abilities.ItemAbility;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComplexItem {

    public static final NamespacedKey GLOBAL_ID = new NamespacedKey(SuperItems.getPlugin(), "superitem");
    private String id;
    private NamespacedKey key;
    private TranslatableText name;
    private TranslatableList lore;
    private int customModelData;
    private final boolean unique;
    private Rarity rarity;
    private Type type;
    private Material material;
    private ItemMeta meta;
    private Map<Stat, Double> stats;
    private List<Ability> abilities;

    private String skullURL = "";

    public ComplexItem(String id, Boolean unique, Rarity rarity, Type type, Material material, Map<Stat, Double> stats, List<Ability> abilities) {
        this.id = id;
        this.name = new TranslatableText(TranslatableText.TranslatableType.ITEM_NAME + "-" + id);
        this.lore = new TranslatableList(TranslatableText.TranslatableType.ITEM_LORE + "-" + id);
        this.key = new NamespacedKey(SuperItems.getPlugin(), id);
        this.customModelData = Ints.tryParse(String.valueOf(Math.abs(id.hashCode())).substring(0, 7));
        this.unique = unique;
        this.rarity = rarity;
        this.type = type;
        this.material = material;
        this.meta = new ItemStack(this.material).getItemMeta();
        this.stats = stats;
        this.skullURL = "";
        this.abilities = abilities;

    }

    public ComplexItem(String id, Rarity rarity, Type type, Material material, Map<Stat, Double> stats, Boolean unique){
        this(id, unique, rarity, type, material, stats, List.of());
    }

    public ComplexItem(String id, Rarity rarity, Type type, Material material, Map<Stat, Double> stats, List<Ability> abilities){
        this(id, false, rarity, type, material, stats, abilities);
    }

    public ComplexItem(String id, Rarity rarity, Type type, Material material, Map<Stat, Double> stats){
        this(id, false, rarity, type, material, stats, List.of());
    }

    public ComplexItem(ItemSettings settings){
        this.name = new TranslatableText(TranslatableText.TranslatableType.ITEM_NAME + "-" + settings.getId());
        this.id = settings.getId();
        this.lore = new TranslatableList(TranslatableText.TranslatableType.ITEM_LORE + "-" + id);
        this.key = new NamespacedKey(SuperItems.getPlugin(), id);
        this.customModelData = Ints.tryParse(String.valueOf(Math.abs(id.hashCode())).substring(0, 7));
        this.unique = settings.isUnique();
        this.rarity = settings.getRarity();
        this.type = settings.getType();
        this.material = settings.getMaterial();
        this.meta = settings.getMeta();
        this.stats = settings.getStats();
        this.skullURL = "";
        this.abilities = settings.getAbilities() == null ? new ArrayList<>() : new ArrayList<>(settings.getAbilities());
    }

    public List<Recipe> getRecipes() {
        return new ArrayList<>();
    }

    @Deprecated
    public ItemStack getItemStack(Integer amount) {
        return new ComplexItemStack(this, 2).getItem();
    }

    @Deprecated
    public ItemStack getItemStackWithData(Integer amount) {
        ItemStack item = new ItemStack(this.material);
        item.setItemMeta(this.meta);
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        // Add persistent data
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(GLOBAL_ID, PersistentDataType.STRING, this.getId());

        List<String> lore = new ArrayList<>();

        if (!this.stats.isEmpty()) lore.add(" ");

        if (item.getItemMeta().getLore() != null) lore.addAll(item.getItemMeta().getLore());


        writeAbilityLore(lore);

        try {
            if (lore.get(lore.size() - 1).strip() != "") lore.add("");
        } catch (IndexOutOfBoundsException e) {
        }

        lore.add(this.getRarity().color() + this.getRarity().getName() + " " + this.getType().getName());
        meta.setLore(lore);
        meta.setDisplayName(this.getRarity().color() + this.getName().toString());
        item.setItemMeta(meta);

        for (Map.Entry<Stat, Double> entry : this.getStats().entrySet()) {
            item = this.getType().isWearable() ? AureliumAPI.addArmorModifier(item, entry.getKey(), entry.getValue(), true) : AureliumAPI.addItemModifier(item, entry.getKey(), entry.getValue(), true);

        }

        ItemStack skullItem = Util.makeSkull(item, this.skullURL);

        item = this.skullURL.isBlank() ? item : skullItem;

        return item;
    }

    protected void writeAbilityLore(List<String> lore) {
        for (Ability ability : this.abilities) {
            lore.add("");
            lore.add(ChatColor.GOLD + "Ability: " + ability.getDisplayName() + ChatColor.YELLOW + ChatColor.BOLD + " " + (ability instanceof ItemAbility ? ((ItemAbility) ability).getAction().getName() : ""));
            lore.addAll(ability.getLore());

            if (ability.getManaCost() > 0) lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + ability.getManaCost());
            if (ability.getCooldown() > 0) lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + ability.getCooldown() + "s");

        }
    }

    public TranslatableText getName() {
        return name;
    }

    public String getDisplayName() {
        return this.rarity.color() + this.name.toString();
    }

    public List<String> getDefaultLore(){
        return lore.getList();
    }

    public String getId() {
        return id;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public Boolean isUnique() {return unique;}

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

    public void setSkullURL(String URL) {
        this.skullURL = URL;
    }

    public String getSkullURL() {
        return this.skullURL;
    }

    public List<Ability> getAbilities() {
        return this.abilities;
    }

    public enum Rarity {

        COMMON(ChatColor.WHITE, ChatColor.BOLD + "COMMON"), UNCOMMON(ChatColor.GREEN, ChatColor.BOLD + "UNCOMMON"),
        RARE(ChatColor.BLUE, ChatColor.BOLD + "RARE"), EPIC(ChatColor.DARK_PURPLE, ChatColor.BOLD + "EPIC"),
        LEGENDARY(ChatColor.GOLD, ChatColor.BOLD + "LEGENDARY"),
        MYTHIC(ChatColor.LIGHT_PURPLE, ChatColor.MAGIC + "" + ChatColor.BOLD + "D " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "MYTHIC" + ChatColor.MAGIC + " D" + ChatColor.LIGHT_PURPLE),
        SPECIAL(ChatColor.RED, ChatColor.MAGIC + "" + ChatColor.BOLD + "D " + ChatColor.RED + ChatColor.BOLD + "SPECIAL" + ChatColor.MAGIC + " D" + ChatColor.RED),
        VERY_SPECIAL(ChatColor.RED, ChatColor.MAGIC + "" + ChatColor.BOLD + "| D " + ChatColor.RED + ChatColor.BOLD + "VERY SPECIAL" + ChatColor.MAGIC + " D |" + ChatColor.RED),
        UNKNOWN(ChatColor.RED, ChatColor.MAGIC + "" + ChatColor.BOLD + "D " + ChatColor.RED + ChatColor.BOLD + "UNKNOWN" + ChatColor.MAGIC + " D" + ChatColor.RED);

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

        SWORD("SWORD", false), AXE("AXE", false), WAND("WAND", false), STAFF("STAFF", false), SUPERITEM("SUPERITEM", false), DEPLOYABLE("DEPLOYABLE", false), MISC("", false),
        HELMET("HELMET", true), CHESTPLATE("CHESTPLATE", true), LEGGINGS("LEGGINGS", true), BOOTS("BOOTS", true);

        private final String name;
        private final Boolean wearable;

        Type(String name, Boolean wearable) {
            this.name = name;
            this.wearable = wearable;
        }

        public String getName() {
            return name;
        }

        public Boolean isWearable() {
            return wearable;
        }

    }
}
