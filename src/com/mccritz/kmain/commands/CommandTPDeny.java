package com.mccritz.kmain.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.utils.Cooldowns;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;

public class CommandTPDeny extends BaseCommand {

    public CommandTPDeny() {
	super("tpdeny", null, CommandUsageBy.PlAYER, "tpno");
	setUsage("&cImproper usage! /tpdeny");
	setMinArgs(0);
	setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
	Player player = (Player) sender;
	Profile profile = Legacy.getInstance().getProfileManager().getProfile(player.getUniqueId());

	if (profile.getTeleportRequester() == null
		|| Cooldowns.calculateRemainder(profile.getTeleportRequestTime()) < 0) {
	    MessageManager.sendMessage(player, "&7No one has requested to teleport to you.");
	    return;
	}

	if (profile.getTeleportRunnable() != null) {
	    profile.getTeleportRunnable().cancel();
	}
	profile.setTeleportRunnable(null);
	profile.setTeleportRequester(null);
	profile.setTeleportRequestHere(false);
	profile.setTeleportRequestLocation(null);
	profile.setTeleportRequestTime(0);

	MessageManager.sendMessage(player, "&7You have denied your last request.");
    }
}