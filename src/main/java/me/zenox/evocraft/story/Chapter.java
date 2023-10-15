package me.zenox.evocraft.story;

import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.data.TranslatableText;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * A chapter is a part of the story. It is a collection of actions that are executed when the chapter starts and ends.
 * @param <T> The event that triggers the chapter.
 * @param <U> The event that triggers the end of the chapter (the event that triggers the next chapter).
 */
public abstract class Chapter<T extends Event, U extends Event> implements Listener {
    private final int id;
    private final TranslatableText name;
    /**
     * Whether the chapter is a "solo" chapter. A solo chapter is a chapter that the player has to perform alone, without any other players, hiding them from all other players.
     */
    private final boolean solo;

    public Chapter(int id, boolean solo) {
        this.id = id;
        this.name = new TranslatableText("chapter-" + id);
        this.solo = solo;
    }

    private Type getType(int index) {
        Class<?> clazz = getClass();
        while (clazz != null) {
            Type type = clazz.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                return ((ParameterizedType) type).getActualTypeArguments()[index];
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * Method that detects when a chapter should start and executes
     */
    public abstract void onChapterStart(T event);

    /**
     * Method that executes when a chapter ends
     */
    public abstract void onChapterEnd(U event);

    public void progress(Player player, U event) {
        EvoCraft.getChapterManager().setChapter(player, id + 1);
        onChapterEnd(event);
        EvoCraft.getChapterManager().getChapter(id + 1).onChapterStart(event);
    }

    public int getId() {
        return id;
    }

    public TranslatableText getName() {
        return name;
    }

    public boolean isSolo() {
        return solo;
    }
}
