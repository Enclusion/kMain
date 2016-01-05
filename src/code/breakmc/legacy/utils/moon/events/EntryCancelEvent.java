package code.breakmc.legacy.utils.moon.events;

import code.breakmc.legacy.utils.moon.objects.Entry;
import code.breakmc.legacy.utils.moon.objects.Scoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EntryCancelEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Entry entry;
    private Scoreboard scoreboard;
    private Player p;

    public EntryCancelEvent(Entry entry, Scoreboard scoreboard) {
        this.entry = entry;
        this.scoreboard = scoreboard;
        this.p = scoreboard.getPlayer();
    }

    public Entry getEntry() {
        return entry;
    }

    public Player getPlayer() {
        return p;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
