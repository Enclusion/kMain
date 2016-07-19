package com.mccritz.kmain.utils.glaedr;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.PlayerUtility;
import com.mccritz.kmain.utils.glaedr.scoreboards.Entry;
import com.mccritz.kmain.utils.glaedr.scoreboards.PlayerScoreboard;
import com.mccritz.kmain.utils.glaedr.scoreboards.Wrapper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
@Getter
public class Glaedr implements Listener {

    private String title;
    private boolean hook, overrideTitle, scoreCountUp;
    private List<String> bottomWrappers = new ArrayList<>();
    private List<String> topWrappers = new ArrayList<>();

    public Glaedr(String title, boolean hook, boolean overrideTitle, boolean scoreCountUp) {
        this.title = ChatColor.translateAlternateColorCodes('&', title);
        this.hook = hook;
        this.overrideTitle = overrideTitle;
        this.scoreCountUp = scoreCountUp;

        Bukkit.getPluginManager().registerEvents(this, kMain.getInstance());
    }

    public Glaedr(String title) {
        this(title, false, true, false);

        registerPlayers();
    }

    public void registerPlayers() {
        for (Player player : PlayerUtility.getOnlinePlayers()) {
            new PlayerScoreboard(this, player);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerScoreboard playerScoreboard = PlayerScoreboard.getScoreboard(player);

        if (playerScoreboard == null) {
            new PlayerScoreboard(this, player);
        } else {
            if (player.getScoreboard() != playerScoreboard.getScoreboard()) {
                if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null) {
                    playerScoreboard.setObjective(player.getScoreboard().getObjective(DisplaySlot.SIDEBAR));
                } else {
                    Objective objective = player.getScoreboard().registerNewObjective(player.getName(), "dummy");
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    objective.setDisplayName(title);
                    playerScoreboard.setObjective(objective);
                }

                playerScoreboard.setScoreboard(player.getScoreboard());

                for (Entry entry : playerScoreboard.getEntries()) {
                    entry.setup();
                }

                for (Wrapper wrapper : playerScoreboard.getWrappers()) {
                    wrapper.setup();
                }
            }

            player.setScoreboard(playerScoreboard.getScoreboard());
        }
    }
}