package com.mccritz.kmain.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import com.mccritz.kmain.warps.WarpManager;

/**
 * Created by Calvin on 4/22/2015. Project: Legacy
 */
public class CommandGo extends BaseCommand {

    private WarpManager wm = Legacy.getInstance().getWarpManager();

    public CommandGo() {
	super("go", null, CommandUsageBy.PlAYER, "warp");
	setUsage("&cImproper usage! /go");
	setMinArgs(0);
	setMaxArgs(2);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
	Player p = (Player) sender;

	if (args.length == 0) {
	    MessageManager.sendMessage(p, "&7***/go help***");
	    MessageManager.sendMessage(p, "&7/go - &cLists your warps.");
	    MessageManager.sendMessage(p, "&7/go list - &cLists your warps");
	    MessageManager.sendMessage(p, "&7/go set <name> - &cSets a warp at your location.");
	    MessageManager.sendMessage(p, "&7/go del <name> - &cDeletes the warp.");
	    MessageManager.sendMessage(p, "&7/go - &cDisplays this page.");
	    return;
	}

	if (args.length == 1) {
	    if (args[0].equalsIgnoreCase("list")) {
		wm.listWarps(p.getUniqueId());
		return;
	    }

	    if (args[0].equalsIgnoreCase("set")) {
		MessageManager.sendMessage(p, "&cImproper Usage! /go set <name>");
		return;
	    }

	    if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete")
		    || args[0].equalsIgnoreCase("remove")) {
		MessageManager.sendMessage(p, "Improper Usage! /go del <name>");
		return;
	    }

	    wm.warpTeleport(p, args[0]);
	}

	if (args.length == 2) {
	    if (args[0].equalsIgnoreCase("set")) {
		wm.setWarp(p.getUniqueId(), args[1], p.getLocation());
		return;
	    }

	    if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete")
		    || args[0].equalsIgnoreCase("remove")) {
		wm.removeWarp(p.getUniqueId(), args[1]);
	    }
	}
    }
}
