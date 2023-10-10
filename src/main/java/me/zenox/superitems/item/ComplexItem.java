package me.zenox.superitems.item;

import com.archyx.aureliumskills.stats.Stat;
import com.google.common.primitives.Ints;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.abilities.Ability;
import me.zenox.superitems.abilities.ItemAbility;
import me.zenox.superitems.attribute.AttributeModifier;
import me.zenox.superitems.data.TranslatableList;
import me.zenox.superitems.data.TranslatableText;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class ComplexItem {

    public static final List<ComplexItem> itemRegistry = new ArrayList<>();

    public static final NamespacedKey GLOBAL_ID = new NamespacedKey(SuperItems.getPlugin(), "superitem");
    public static final NamespacedKey GLOW_ID = new NamespacedKey(SuperItems.getPlugin(), "glow");
    public static final NamespacedKey UUID_ID = new NamespacedKey(SuperItems.getPlugin(), "uuid");
    private final boolean unique;
    private final String id;
    private final NamespacedKey key;
    private final TranslatableText name;
    private final TranslatableList lore;
    private final int customModelData;
    private boolean glow;
    private final Rarity rarity;
    private final Type type;
    private final Material material;
    private final ItemMeta meta;
    private final Map<Stat, Double> stats;
    private final List<Ability> abilities;
    private final HashMap<VariableType, Serializable> variableMap = new HashMap<>();
    private final List<AttributeModifier> attributeModifiers;

    private String skullURL;


    public ComplexItem(String id, Boolean unique, Boolean glow, Rarity rarity, Type type, Material material, Map<Stat, Double> stats, List<Ability> abilities, HashMap<VariableType, Serializable> variableMap, List<AttributeModifier> attributeModifiers) {
        this.id = id;
        this.name = new TranslatableText(TranslatableText.Type.ITEM_NAME + "-" + id);
        this.lore = new TranslatableList(TranslatableText.Type.ITEM_LORE + "-" + id);
        this.key = new NamespacedKey(SuperItems.getPlugin(), id);
        this.attributeModifiers = attributeModifiers;
        String str = String.valueOf(Math.abs(id.hashCode()));
        this.customModelData = Ints.tryParse(str.substring(0, Math.min(7, str.length())));
        this.unique = unique;
        this.glow = glow;
        this.rarity = rarity;
        this.type = type;
        this.material = material;
        this.meta = new ItemStack(this.material).getItemMeta();
        this.stats = stats;
        this.skullURL = "";
        this.abilities = abilities;
        this.variableMap.putAll(variableMap);

        register(false);
    }

    public ComplexItem(String id, Boolean unique, Rarity rarity, Type type, Material material, Map<Stat, Double> stats, List<Ability> abilities) {
        this(id, unique, false, rarity, type, material, stats, abilities, new HashMap<>(), new ArrayList<>());
    }

    public ComplexItem(String id, Rarity rarity, Type type, Material material, Map<Stat, Double> stats, List<Ability> abilities) {
        this(id, false, rarity, type, material, stats, abilities);
    }

    public ComplexItem(String id, Rarity rarity, Type type, Material material, Map<Stat, Double> stats) {
        this(id, false, rarity, type, material, stats, List.of());
    }

    public ComplexItem(String id, Rarity rarity, Type type, Material material) {
        this(id, false, rarity, type, material, Map.of(), List.of());
    }

    public ComplexItem(ItemSettings settings) {
        this.name = new TranslatableText(TranslatableText.Type.ITEM_NAME + "-" + settings.getId());
        this.id = settings.getId();
        this.lore = new TranslatableList(TranslatableText.Type.ITEM_LORE + "-" + id);
        this.key = new NamespacedKey(SuperItems.getPlugin(), id);
        String str = String.valueOf(Math.abs(id.hashCode()));
        this.customModelData = Ints.tryParse(str.substring(0, Math.min(7, str.length())));
        this.unique = settings.isUnique();
        this.glow = settings.doesGlow();
        this.rarity = settings.getRarity();
        this.type = settings.getType();
        this.material = settings.getMaterial();
        this.meta = settings.getMeta();
        this.stats = settings.getStats();
        this.skullURL = "";
        this.abilities = settings.getAbilities() == null ? new ArrayList<>() : new ArrayList<>(settings.getAbilities());
        this.variableMap.putAll(settings.getVariableMap());
        this.attributeModifiers = settings.getAttributeModifiers();

        register(false);
    }

    protected ComplexItem(ItemSettings settings, boolean override) {
        this.name = new TranslatableText(TranslatableText.Type.ITEM_NAME + "-" + settings.getId());
        this.id = settings.getId();
        this.lore = new TranslatableList(TranslatableText.Type.ITEM_LORE + "-" + id);
        this.key = new NamespacedKey(SuperItems.getPlugin(), id);
        String str = String.valueOf(Math.abs(id.hashCode()));
        this.customModelData = Ints.tryParse(str.substring(0, Math.min(7, str.length())));
        this.unique = settings.isUnique();
        this.glow = settings.doesGlow();
        this.rarity = settings.getRarity();
        this.type = settings.getType();
        this.material = settings.getMaterial();
        this.meta = settings.getMeta();
        this.stats = settings.getStats();
        this.skullURL = "";
        this.abilities = settings.getAbilities() == null ? new ArrayList<>() : new ArrayList<>(settings.getAbilities());
        this.variableMap.putAll(settings.getVariableMap());
        this.attributeModifiers = settings.getAttributeModifiers();

        register(true);
    }

    private void register(boolean override){
        for (ComplexItem item:
                new ArrayList<>(itemRegistry)) {
            if (item.getId().equalsIgnoreCase(id)) {
                if (!override) {
                    Util.logToConsole("Duplicate ComplexItem ID: " + id + " | Exact Match: " + item.equals(this));
                    throw new IllegalArgumentException("ComplexItem ID cannot be duplicate");
                } else {
                    itemRegistry.remove(item);
                    break;
                }
            }
        }

        itemRegistry.add(this);
    }


    public List<Recipe> getRecipes() {
        return new ArrayList<>();
    }

    @Deprecated
    public ItemStack getItemStack(Integer amount) {
        return new ComplexItemStack(this, amount).getItem();
    }

    protected void writeAbilityLore(List<String> lore) {
        for (Ability ability : this.abilities) {
            lore.add("");
            lore.add(ChatColor.GOLD + "Ability: " + ability.getDisplayName() + ChatColor.YELLOW + ChatColor.BOLD + " " + (ability instanceof ItemAbility ? ((ItemAbility) ability).getAction().getName() : ""));
            lore.addAll(ability.getLore());

            if (ability.getManaCost() > 0)
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + ability.getManaCost());
            if (ability.getCooldown() > 0)
                lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + ability.getCooldown() + "s");

        }
    }

    public TranslatableText getName() {
        return name;
    }

    public String getDisplayName() {
        return this.rarity.color() + this.name.toString();
    }

    public List<String> getDefaultLore() {
        return lore.getList();
    }

    public String getId() {
        return id;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public Boolean isUnique() {
        return unique;
    }

    public boolean doesGlow() {
        return glow;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public Type getType() {
        return this.type;
    }

    public Map<Stat, Double> getStats() {
        return stats;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public String getSkullURL() {
        return this.skullURL;
    }

    public void setSkullURL(String URL) {
        this.skullURL = URL;
    }

    public List<Ability> getAbilities() {
        return this.abilities;
    }

    public HashMap<VariableType, Serializable> getVariableMap() {
        return variableMap;
    }

    public List<AttributeModifier> getAttributeModifiers() {
        return attributeModifiers;
    }

    public enum Rarity {
        // Game Rarities
        COMMON(ChatColor.WHITE, "&f&lCOMMON&f&l"),
        UNCOMMON(ChatColor.GREEN, "&a&lUNCOMMON&a&l"),
        RARE(ChatColor.BLUE, "&9&lRARE&9&l"),
        EPIC(ChatColor.AQUA, "&b&lEPIC&b&l"),
        LEGENDARY(ChatColor.GOLD, "&6&lLEGENDARY&6&l"),

        // One-of-a-kind Rarities
        MYTHIC(ChatColor.LIGHT_PURPLE, "&d&k&lD &d&lMYTHIC&k D&d&l"),

        // Quest rarity pertaining to the story
        OMEGA(ChatColor.GOLD, "&6&k&lD &6&lOMEGA&k D&6&l"),

        // Reserved for symbol items
        VERY_SPECIAL(ChatColor.RED, "&c&k&l| D &c&lVERY SPECIAL &kD |&c&l"),

        // Reserved for unobtainable/unknown items
        UNOBTAINABLE(ChatColor.DARK_AQUA, "&3&k&lD &3&lUNOBTAINABLE&k D&3&l"),
        UNKNOWN(ChatColor.RED, "&c&k& D &c&lUNKNOWN &kD&c&l");

        private final ChatColor color;
        private final String name;

        Rarity(ChatColor color, String name) {
            this.color = color;
            this.name = ChatColor.translateAlternateColorCodes('&', name);
        }

        public ChatColor color() {
            return color;
        }

        public String getName() {
            return name;
        }

    }

    public enum Type {

        SWORD("SWORD", false), AXE("AXE", false), BOW("BOW", false), CROSSBOW("CROSSBOW", false),
        TRIDENT("TRIDENT", false),
        PICKAXE("PICKAXE", false), FISHING_ROD("FISHING ROD", false), SHOVEL("SHOVEL", false),
        SHIELD("SHIELD", false), HOE("HOE", false),
        WAND("WAND", false), STAFF("STAFF", false), SUPERITEM("SUPERITEM", false),
        DEPLOYABLE("DEPLOYABLE", false), MISC("", false), ENCHANTING_FUEL("ENCHANTING FUEL", false),
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
