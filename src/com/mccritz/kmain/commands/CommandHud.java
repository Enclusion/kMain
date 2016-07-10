package com.mccritz.kmain.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import com.mccritz.kmain.utils.glaedr.scoreboards.PlayerScoreboard;

/**
 * Created by Calvin on 4/22/2015. Project: Legacy
 */
public class CommandHud extends BaseCommand {

    private static ArrayList<UUID> displayList = new ArrayList<>();

    public CommandHud() {
	super("hud", null, CommandUsageBy.PlAYER, "display", "togglesc", "togglescoreboard");
	setUsage("&cImproper usage! /hud");
	setMinArgs(0);
	setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
	Player p = (Player) sender;

	if (displayList.contains(p.getUniqueId())) {
	    MessageManager.sendMessage(p, "&7HUD has been &cdisabled&7.");

	    PlayerScoreboard.getScoreboards().remove(PlayerScoreboard.getScoreboard(p));
	    p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

	    displayList.remove(p.getUniqueId());
	} else {
	    MessageManager.sendMessage(p, "&7HUD has been &aenabled&7.");

	    displayList.add(p.getUniqueId());

	    new PlayerScoreboard(p);
	}
    }

    public static ArrayList<UUID> getDisplayList() {
	return displayList;
    }
}
