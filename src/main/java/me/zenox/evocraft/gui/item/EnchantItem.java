package me.zenox.evocraft.gui.item;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.controlitem.ControlItem;
import me.zenox.evocraft.data.TranslatableList;
import me.zenox.evocraft.data.TranslatableText;
import me.zenox.evocraft.gui.EnchantingGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class EnchantItem extends ControlItem<EnchantingGUI> {

    private final int level;
    private final int skillreq;
    private TranslatableText NAME;
    private TranslatableList LORE;

    public EnchantItem(int level, int skillreq){
        super();
        this.level = level;
        this.skillreq = skillreq;
        this.NAME = new TranslatableText(TranslatableText.Type.GUI + "-enchant-action-title-" + level);
        this.LORE = new TranslatableList(TranslatableText.Type.GUI + "-enchant-action-lore-" + level);
    }

    private EnchantingGUI gui;

    @Override
    public ItemProvider getItemProvider(EnchantingGUI gui) {
        this.gui = gui;
        return new ItemBuilder(Material.EXPERIENCE_BOTTLE).setDisplayName(NAME.toString()).setLegacyLore(LORE.getList()).setAmount(level);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if(EnchantingGUI.enchantValid(gui, level, getXPRequired())) gui.enchantItem(level, getXPRequired());
    }

    public int getXPRequired(){
        switch(level){
            case 1:
                return 20;
            case 2:
                return 30;
            case 3:
                return 50;
            default:
                return 100;
        }
    }
}
