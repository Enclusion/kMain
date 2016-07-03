package com.mccritz.kmain.utils.glaedr.listeners;

import com.mccritz.kmain.utils.glaedr.scoreboards.PlayerScoreboard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ScoreboardListeners implements Listener {

    /**
     * EventHandler that creates player scoreboards on join
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerScoreboard playerScoreboard = PlayerScoreboard.getScoreboard(player);
        if (playerScoreboard == null) {
            long startTime = System.currentTimeMillis();
            new PlayerScoreboard(player);
            long endTime = System.currentTimeMillis();
            player.sendMessage(ChatColor.GREEN + "Scoreboard created in " + (endTime - startTime) + "ms.");
        } else {
            player.setScoreboard(playerScoreboard.getScoreboard());
        }
    }
}