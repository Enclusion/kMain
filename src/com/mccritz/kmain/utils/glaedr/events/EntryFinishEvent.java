package com.mccritz.kmain.utils.glaedr.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.mccritz.kmain.utils.glaedr.scoreboards.Entry;
import com.mccritz.kmain.utils.glaedr.scoreboards.PlayerScoreboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * This event is called once a countdown entry reaches 0 seconds
 */
public class EntryFinishEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Entry entry;
    private PlayerScoreboard scoreboard;
    private Player player;

    public EntryFinishEvent(Entry entry, PlayerScoreboard scoreboard) {
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
