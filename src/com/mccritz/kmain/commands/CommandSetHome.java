package com.mccritz.kmain.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;

public class CommandSetHome extends BaseCommand {

    private Legacy main = Legacy.getInstance();
    private ProfileManager pm = main.getProfileManager();

    public CommandSetHome() {
	super("sethome", null, CommandUsageBy.PlAYER, "shome");
	setUsage("&cImproper usage! /sethome");
	setMinArgs(0);
	setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
	if (sender instanceof Player) {
	    Player p = (Player) sender;

	    Profile prof = pm.getProfile(p.getUniqueId());
	    prof.setHome(p.getLocation());

	    MessageManager.sendMessage(p, "&7Your home has been set.");
	}
    }
}
