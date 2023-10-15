package me.zenox.evocraft.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.GUIContext;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;


/**
 * hhahahh enchant gui code go brr
 */
public class EnchantGUIBuilder<G extends GUI> extends GUIBuilder {

    private final EnchantGUIType guiType;
    private final GUIContext context;
    private final Player p;
    private final Block block;

    public EnchantGUIBuilder(@NotNull EnchantGUIType guiType, Player p, Block block) {
        super(guiType);
        this.guiType = guiType;
        this.context = new GUIContext();
        try {
            Field f = getClass().getSuperclass().getDeclaredField("context");
            f.setAccessible(true);
            f.set(this, context);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        this.p = p;
        this.block = block;
    }

    @Override
    public EnchantingGUI build() {
        if (context.getStructure() == null) throw new IllegalStateException("GUIContext has not been set yet.");
        return guiType.createGUI(context, p, block);
    }

}

