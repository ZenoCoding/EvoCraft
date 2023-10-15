package me.zenox.evocraft.story.chapters;

import me.zenox.evocraft.story.Chapter;
import me.zenox.evocraft.util.Romans;
import me.zenox.evocraft.util.Util;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;

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
    }

    @Override
    public void onChapterEnd(PlayerInteractEvent event) {
    }

    public static ChapterOne getInstance() {
        if(instance == null) instance = new ChapterOne();
        return instance;
    }
}
