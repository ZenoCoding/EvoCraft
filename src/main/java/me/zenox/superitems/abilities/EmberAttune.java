package me.zenox.superitems.abilities;

import me.zenox.superitems.item.ComplexItemMeta;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.item.LoreEntry;
import me.zenox.superitems.item.VariableType;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class EmberAttune extends ItemAbility {
    public static final VariableType<Attunement> ATTUNEMENT_VARIABLE_TYPE =
            new VariableType<>("ember_attunement",
                    new LoreEntry("ember_attunement", List.of(ChatColor.AQUA + "Attunement: ")),
                    VariableType.Priority.BELOW_ABILITIES, (loreEntry, variable) ->
                    loreEntry.setLore(List.of(ChatColor.AQUA + "Attunement: " + ((Attunement) variable.getValue()).getName())));

    public EmberAttune() {
        super("dark_ember_attune", AbilityAction.LEFT_CLICK_ALL, 0, 0);
    }

    @Override
    public void runExecutable(Event event, Player p, ItemStack item) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        ItemMeta meta = item.getItemMeta();
        ComplexItemMeta complexMeta = ComplexItemStack.of(item).getComplexMeta();

        Attunement attunement;
        try {
            attunement = (Attunement) complexMeta.getVariable(ATTUNEMENT_VARIABLE_TYPE).getValue();
        } catch (NullPointerException ex) {
            attunement = Attunement.BLAZEBORN;
            complexMeta.setVariable(ATTUNEMENT_VARIABLE_TYPE, attunement);
        }
        if (attunement.equals(Attunement.BLAZEBORN)) {
            complexMeta.setVariable(ATTUNEMENT_VARIABLE_TYPE, Attunement.DARKSOUL);
            meta.setCustomModelData(meta.getCustomModelData() + 1);
        } else {
            complexMeta.setVariable(ATTUNEMENT_VARIABLE_TYPE, Attunement.BLAZEBORN);
            meta.setCustomModelData(meta.getCustomModelData() - 1);
        }
        Util.sendActionBar(p, ((Attunement) complexMeta.getVariable(ATTUNEMENT_VARIABLE_TYPE).getValue()).getName());
        p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 1f);

        item.setItemMeta(meta);
        complexMeta.updateItem();
    }

    public enum Attunement {
        DARKSOUL(ChatColor.DARK_GRAY + "Darksoul"), BLAZEBORN(ChatColor.GOLD + "Blazeborn");

        private final String name;

        Attunement(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
