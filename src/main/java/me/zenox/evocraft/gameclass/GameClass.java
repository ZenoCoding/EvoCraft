package me.zenox.evocraft.gameclass;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.data.TranslatableText;
import me.zenox.evocraft.gui.item.ClassItem;
import me.zenox.evocraft.gui.item.CloseItem;
import me.zenox.evocraft.item.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Represents a class the player may choose
 */
public enum GameClass {
    MAGE("mage", ChatColor.BLUE,
            List.of(ComplexItem.Type.STAFF, ComplexItem.Type.WAND), Material.BLAZE_ROD,
            TreeRegistry.MAGE_TELEPORT_TREE),
    WARRIOR("warrior", ChatColor.RED,
            List.of(ComplexItem.Type.SWORD), Material.IRON_SWORD,
            new AbilityTree()),
    TANK("tank", ChatColor.GREEN,
            List.of(ComplexItem.Type.AXE), Material.IRON_AXE,
            new AbilityTree()),
    ARCHER("archer", ChatColor.YELLOW,
            List.of(ComplexItem.Type.BOW), Material.BOW,
            new AbilityTree());

    private static final NamespacedKey KEY = new NamespacedKey(EvoCraft.getPlugin(), "class");
    private final String id;
    private final String name;
    private final ChatColor color;
    private final String description;
    private final List<ComplexItem.Type> items;
    private final Material icon;
    private final AbilityTree tree;

    GameClass(String id, ChatColor color, List<ComplexItem.Type> items, Material icon, AbilityTree tree) {
        this.id = id;
        this.name = new TranslatableText(TranslatableText.Type.CLASS_NAME + "-" + id).toString();
        this.color = color;
        this.description = new TranslatableText(TranslatableText.Type.CLASS_DESC + "-" + id).toString();
        this.items = items;
        this.icon = icon;
        this.tree = tree;
    }

    public static void setClass(Player player, GameClass gameClass) {
        player.getPersistentDataContainer().set(GameClass.KEY, PersistentDataType.STRING, gameClass.id());
    }

    public static GameClass getClass(Player player) {
        return GameClass.getFromID(player.getPersistentDataContainer().get(GameClass.KEY, PersistentDataType.STRING));
    }

    private static GameClass getFromID(String id) {
        return switch (id) {
            case "mage" -> GameClass.MAGE;
            case "warrior" -> GameClass.WARRIOR;
            case "tank" -> GameClass.TANK;
            case "archer" -> GameClass.ARCHER;
            default -> null;
        };
    }

    public static GUI getGui() {
        return new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(
                        "# # # # # # # # #",
                        "# M # W # T # A #",
                        "# # # # # # # # #"
                )
                .addIngredient('M', new ClassItem(GameClass.MAGE))
                .addIngredient('W', new ClassItem(GameClass.WARRIOR))
                .addIngredient('T', new ClassItem(GameClass.TANK))
                .addIngredient('A', new ClassItem(GameClass.ARCHER))
                .addIngredient('C', new CloseItem())
                .build();
    }

    private String id() {
        return id;
    }

    public ChatColor color() {
        return color;
    }

    public String description() {
        return description;
    }

    public List<ComplexItem.Type> items() {
        return items;
    }

    public Material icon() {
        return icon;
    }

    public AbilityTree tree() {
        return tree;
    }
}
