package me.zenox.superitems.story;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.story.chapters.ChapterOne;
import me.zenox.superitems.story.chapters.ChapterTwo;
import me.zenox.superitems.story.chapters.ChapterZero;
import me.zenox.superitems.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChapterManager implements Listener {
    public static NamespacedKey CHAPTER_KEY = new NamespacedKey(SuperItems.getPlugin(), "chapter");

    private SuperItems plugin;

    public List<Chapter> chapters = new ArrayList<>();

    static {
        // Runnable that hides players for solo chapters
        new BukkitRunnable(){
            @Override
            public void run() {
                ChapterManager manager = SuperItems.getPlugin().getChapterManager();
                for(Player p : Bukkit.getOnlinePlayers()){
                    try {
                        if (manager.getChapter(p).isSolo()) {
                            for (Player p2 : Bukkit.getOnlinePlayers()) {
                                if (p != p2) {
                                    p.hidePlayer(SuperItems.getPlugin(), p2);
                                    if (!p2.getEffectivePermissions().contains("superitems.admin"))
                                        p2.hidePlayer(SuperItems.getPlugin(), p);
                                }
                            }
                        } else {
                            for (Player p2 : Bukkit.getOnlinePlayers()) {
                                p.showPlayer(SuperItems.getPlugin(), p2);
                                p2.showPlayer(SuperItems.getPlugin(), p);
                            }
                        }
                    } catch (NullPointerException e){
                        Util.logToConsole("Player " + p.getName() + " has no chapter data!");
                        manager.setChapter(p, manager.getChapter(2));
                        //p.kickPlayer("You have no chapter data! Please rejoin.");
                    }
                }
            }
        }.runTaskTimer(SuperItems.getPlugin(), 1, 10);
    }

    public ChapterManager(SuperItems plugin) {
        this.plugin = plugin;
        registerChapters();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Chapter chapter = getChapter(player);
        Util.logToConsole("Player " + player.getName() + " joined with chapter " + chapter.getId());
        if(chapter == null) {
            chapter = ChapterZero.getInstance();
            player.getPersistentDataContainer().set(CHAPTER_KEY, PersistentDataType.INTEGER, chapter.getId());
        }

        if(chapter.equals(ChapterZero.getInstance())) chapter.onChapterStart(event);
    }

    @Nullable
    public Chapter getChapter(@NotNull Player player){
        return player.getPersistentDataContainer().has(CHAPTER_KEY, PersistentDataType.INTEGER) ? chapters.get(player.getPersistentDataContainer().get(CHAPTER_KEY, PersistentDataType.INTEGER)) : null;
    }

    @Nullable
    public Chapter getChapter(int id){
        return chapters.get(id);
    }

    public void setChapter(Player p, Chapter chapter){
        p.getPersistentDataContainer().set(CHAPTER_KEY, PersistentDataType.INTEGER, chapter.getId());
    }

    public void setChapter(Player p, int id){
        Util.sendMessage(p, "Setting chapter of player %s to %s.".formatted(p.getName(), id));
        try{
            setChapter(p, chapters.get(id));
        } catch (IndexOutOfBoundsException e){
            throw(new IndexOutOfBoundsException("Chapter with id " + id + " does not exist!"));
        }
    }

    public void registerChapters() {
        // Register all chapters here
        chapters.add(ChapterZero.getInstance());
        chapters.add(ChapterOne.getInstance());
        chapters.add(ChapterTwo.getInstance());
    }
}
