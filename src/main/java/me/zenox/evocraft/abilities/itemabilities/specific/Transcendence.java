package me.zenox.evocraft.abilities.itemabilities.specific;

import me.zenox.evocraft.abilities.itemabilities.ItemAbility;
import me.zenox.evocraft.item.ComplexItemMeta;
import me.zenox.evocraft.item.ComplexItemStack;
import me.zenox.evocraft.item.LoreEntry;
import me.zenox.evocraft.item.VariableType;
import me.zenox.evocraft.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Transcendence extends ItemAbility {


    public static final VariableType<EmberAttune.Attunement> AGONY_PAGES_VARIABLE_TYPE =
            new VariableType<>("agony_pages",
                    new LoreEntry("agony_pages", List.of(ChatColor.AQUA + "Pages: ")),
                    VariableType.Priority.BELOW_ABILITIES, (loreEntry, variable) ->
                    loreEntry.setLore(List.of(ChatColor.AQUA + "Pages: " + ChatColor.LIGHT_PURPLE + variable.getValue())));
    public Transcendence() {
        super("dimensional_travel", AbilityAction.RIGHT_CLICK_ALL, 350, 0);
    }


    @Override
    public void runExecutable(PlayerInteractEvent event, Player p, ItemStack item) {
        ComplexItemMeta complexMeta = ComplexItemStack.of(item).getComplexMeta();

        int pages = (int) complexMeta.getVariable(AGONY_PAGES_VARIABLE_TYPE).getValue() - 1;

        complexMeta.setVariable(AGONY_PAGES_VARIABLE_TYPE, pages);
        complexMeta.updateItem();

        if (pages == 0) {
            item.setAmount(item.getAmount() - 1);
            Util.sendMessage(p, ChatColor.WHITE + "Your " + ChatColor.DARK_PURPLE + "[Dimensional Journal]" + ChatColor.WHITE + " ran out of pages and exploded!");
        }

        List<World> worldList = List.of(p.getServer().getWorld("world_nether"), p.getServer().getWorld("world"), p.getServer().getWorld("world_the_end"));

        if (worldList.contains(p.getWorld())) {
            int index = worldList.indexOf(p.getWorld());
            double pitch = p.getLocation().getPitch();
            if (pitch >= 0) {
                if (worldList.size() - 1 == index) p.teleport(worldList.get(0).getSpawnLocation());
                else p.teleport(worldList.get(index + 1).getSpawnLocation());
            } else {
                if (index == 0) {
                    p.teleport(worldList.get(worldList.size() - 1).getSpawnLocation());
                } else p.teleport(worldList.get(index - 1).getSpawnLocation());
            }
        } else {
            p.teleport(worldList.get(1).getSpawnLocation());
        }

        p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 0f);
        p.playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.7f, 1.2f);
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.2f);


    }
}
