package com.mccritz.kmain.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;

/**
 * Created by Calvin on 4/22/2015. Project: Legacy
 */
public class CommandTestMessage extends BaseCommand {

    public CommandTestMessage() {
	super("testmessage", "kmain.testmessage", CommandUsageBy.PlAYER, "tm");
	setUsage("&c/tm <message>");
	setMinArgs(1);
	setMaxArgs(100);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
	if (args.length >= 1) {
	    Bukkit.broadcastMessage(
		    ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, ' ', 0, args.length)));
	}
    }
}
