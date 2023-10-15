package me.zenox.evocraft.gui.item;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.controlitem.ControlItem;
import me.zenox.evocraft.gui.EnchantingGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Item that represents a boolean and changes based on boolean
 */
public class BooleanItem extends ControlItem<EnchantingGUI> {

    private Predicate<EnchantingGUI> supplier;
    private ItemProvider trueItem = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName("§r");
    private ItemProvider falseItem = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("§r");

    public BooleanItem(Predicate<EnchantingGUI> supplier, ItemProvider trueItem, ItemProvider falseItem){
        super();
        this.supplier = supplier;
        this.trueItem = trueItem;
        this.falseItem = falseItem;
    }

    public BooleanItem(Predicate<EnchantingGUI> supplier, ItemProvider trueItem){
        super();
        this.supplier = supplier;
        this.trueItem = trueItem;
    }

    public BooleanItem(Predicate<EnchantingGUI> supplier){
        super();
        this.supplier = supplier;
    }

    private EnchantingGUI gui;

    @Override
    public ItemProvider getItemProvider(EnchantingGUI gui) {
        this.gui = gui;
        return supplier.test(gui) ? trueItem : falseItem;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {

    }
}
