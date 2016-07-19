package com.mccritz.kmain.utils.glaedr.events;

import com.mccritz.kmain.utils.glaedr.scoreboards.Entry;
import com.mccritz.kmain.utils.glaedr.scoreboards.PlayerScoreboard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is called when an entry is abruptly cancelled by entry#cancel
 */

@Getter
@Setter
public class EntryTickEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Entry entry;
    private PlayerScoreboard scoreboard;
    private Player player;

    public EntryTickEvent(Entry entry, PlayerScoreboard scoreboard) {
	this.entry = entry;
	this.scoreboard = scoreboard;
	this.player = scoreboard.getPlayer();
    }

    @Override
    public HandlerList getHandlers() {
	return handlers;
    }

    public static HandlerList getHandlerList() {
	return handlers;
    }
}