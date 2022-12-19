package me.zenox.superitems.story.chapters;

import me.zenox.superitems.story.Chapter;
import me.zenox.superitems.util.Romans;
import me.zenox.superitems.util.Util;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChapterTwo extends Chapter<PlayerInteractEvent, PlayerInteractEvent> {
    private static ChapterTwo instance = null;

    private ChapterTwo() {
        super(2, true);
    }

    @Override
    public void onChapterStart(PlayerInteractEvent event) {
        Util.sendTitle(event.getPlayer(), "&b&lChapter " + Romans.encode(this.getId()), "&lHumble Beginnings", 10, 40, 10);
        event.getPlayer().teleport(new Location(event.getPlayer().getServer().getWorld("flat"), -134, 78, -466));
        event.getPlayer().setBedSpawnLocation(new Location(event.getPlayer().getServer().getWorld("flat"), -134, 78, -466), true);
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1, 1.5f);
        // Begin Voiceover

    }

    @Override
    public void onChapterEnd(PlayerInteractEvent event) {
        // Clear the player's inventory
        event.getPlayer().getInventory().clear();
    }

    public static ChapterTwo getInstance() {
        if(instance == null) instance = new ChapterTwo();
        return instance;
    }
}
