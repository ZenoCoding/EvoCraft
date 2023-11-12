package me.zenox.evocraft.abilities;

import me.zenox.evocraft.item.ComplexItemMeta;
import me.zenox.evocraft.item.ComplexItemStack;
import me.zenox.evocraft.item.LoreEntry;
import me.zenox.evocraft.item.VariableType;
import me.zenox.evocraft.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ElementalFlux extends ItemAbility {
    public static final VariableType<Flux> FLUX_VARIABLE_TYPE =
            new VariableType<>("elemental_flux",
                    new LoreEntry("elemental_flux", List.of(ChatColor.AQUA + "Flux: ")),
                    VariableType.Priority.BELOW_ABILITIES, (loreEntry, variable) ->
                    loreEntry.setLore(List.of(ChatColor.AQUA + "Flux: " + ((Flux) variable.getValue()).getName())));

    public ElementalFlux() {
        super("elemental_flux", AbilityAction.LEFT_CLICK_ALL, 0, 0);
    }

    @Override
    public void runExecutable(PlayerInteractEvent event, Player p, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        ComplexItemMeta complexMeta = ComplexItemStack.of(item).getComplexMeta();

        Flux flux;
        try {
            flux = (Flux) complexMeta.getVariable(FLUX_VARIABLE_TYPE).getValue();
        } catch (NullPointerException ex) {
            flux = Flux.FROST;
            complexMeta.setVariable(FLUX_VARIABLE_TYPE, flux);
        }
        if (flux.equals(Flux.FROST)) {
            complexMeta.setVariable(FLUX_VARIABLE_TYPE, Flux.FIRE);
            meta.setCustomModelData(meta.getCustomModelData() + 1);
        } else {
            complexMeta.setVariable(FLUX_VARIABLE_TYPE, Flux.FROST);
            meta.setCustomModelData(meta.getCustomModelData() - 1);
        }
        Util.sendActionBar(p, ((Flux) complexMeta.getVariable(FLUX_VARIABLE_TYPE).getValue()).getName());
        p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 1f);

        item.setItemMeta(meta);
        complexMeta.updateItem();
    }

    public enum Flux {
        FIRE(ChatColor.GOLD + "Fire"), FROST(ChatColor.AQUA + "Frost");

        private final String name;

        Flux(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
