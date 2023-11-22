package me.zenox.evocraft.gui.item;

import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.window.Window;
import me.zenox.evocraft.data.TranslatableList;
import me.zenox.evocraft.data.TranslatableText;
import me.zenox.evocraft.gameclass.GameClass;
import me.zenox.evocraft.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

public class ClassItem extends AbstractItem {
    private final GameClass gameClass;
    private final TranslatableText NAME;
    private final TranslatableList LORE;

    public ClassItem(GameClass gameClass){
        super();
        this.gameClass = gameClass;
        this.NAME = new TranslatableText(TranslatableText.Type.GUI + "-class-action-title-" + gameClass.name().toLowerCase());
        this.LORE = new TranslatableList(TranslatableText.Type.GUI + "-class-action-lore-" + gameClass.name().toLowerCase());
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(gameClass.icon()).setDisplayName(NAME.toString()).setLegacyLore(LORE.getList()).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        // set the class of a player
        Util.sendMessage(player, "You have selected the " + gameClass.name() + " class!");
        GameClass.setClass(player, gameClass);
        for (Window window : getWindows()) {
            if(window.getCurrentViewer().equals(player)){
                window.close();
            }
        }
    }
}

