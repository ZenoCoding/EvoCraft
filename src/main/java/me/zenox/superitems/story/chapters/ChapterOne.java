package me.zenox.superitems.story.chapters;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.story.Chapter;
import me.zenox.superitems.util.Romans;
import me.zenox.superitems.util.Util;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChapterOne extends Chapter<PlayerInteractEvent, PlayerInteractEvent> {

    private static ChapterOne instance = null;

    private ChapterOne() {
        super(1, true);
    }

    @Override
    public void onChapterStart(PlayerInteractEvent event) {
        Util.sendTitle(event.getPlayer(), "&b&lChapter " + Romans.encode(this.getId()), "&lThe Guardian's Keep", 10, 40, 10);
        event.getPlayer().teleport(new Location(event.getPlayer().getServer().getWorld("flat"), -346, -54, -510));
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 0.8f);
        // Begin Voiceover
        event.getPlayer().playSound(event.getPlayer().getLocation(), "story.chapter.1.backstory_1", 20f, 1f);
        new BukkitRunnable(){
            @Override
            public void run() {
                SuperItems.getPermissions().playerAdd(event.getPlayer(), "superitems.story.chapter.1.voiceover-finish-1");
            }
        }.runTaskLater(SuperItems.getPlugin(), 440);
    }

    @Override
    public void onChapterEnd(PlayerInteractEvent event) {
        // Clear the player's inventory
        event.getPlayer().getInventory().clear();
    }

    public static ChapterOne getInstance() {
        if(instance == null) instance = new ChapterOne();
        return instance;
    }
}
