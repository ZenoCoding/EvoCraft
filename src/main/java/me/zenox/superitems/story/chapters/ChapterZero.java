package me.zenox.superitems.story.chapters;

import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.item.ItemRegistry;
import me.zenox.superitems.story.Chapter;
import me.zenox.superitems.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ChapterZero extends Chapter<PlayerJoinEvent, PlayerInteractEvent> {

    private static ChapterZero instance = null;

    private ChapterZero() {
        super(0, true);
    }

    @Override
    public void onChapterStart(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(new Location(event.getPlayer().getServer().getWorld("flat"), -367, 17, -593));
        Util.sendTitle(player, "&b&lChapter Ω", "&d&lPress Start", 10, 150, 10);
        Util.sendMessage(player, "&eNote: &bEvoCraft &fis best experienced with sounds turned on. If you have sounds turned off, you may miss elemental effects, voiceovers, and other important sounds.");
        player.getInventory().setHeldItemSlot(4);
        player.getInventory().setItem(4, new ComplexItemStack(ItemRegistry.START_BUTTON).getItem());
    }

    @Override
    public void onChapterEnd(PlayerInteractEvent event) {
        // Clear the player's inventory
        event.getPlayer().getInventory().clear();
    }

    public static ChapterZero getInstance() {
        if(instance == null) instance = new ChapterZero();
        return instance;
    }
}

