package com.mccritz.kmain.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.utils.Cooldowns;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import com.mccritz.kmain.utils.teleportation.Teleport;
import com.mccritz.kmain.utils.teleportation.TeleportSetting;

public class CommandTPAccept extends BaseCommand {

    public CommandTPAccept() {
	super("tpaccept", null, CommandUsageBy.PlAYER, "tpyes");
	setUsage("&cImproper usage! /tpaccept");
	setMinArgs(0);
	setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
	Player player = (Player) sender;
	Profile profile = Legacy.getInstance().getProfileManager().getProfile(player.getUniqueId());

	if (profile.getTeleportRequester() == null) {
	    MessageManager.sendMessage(player, "&7No one has requested to teleport to you.");
	    return;
	}

	if (Cooldowns.calculateRemainder(profile.getTeleportRequestTime()) < 0) {
	    MessageManager.sendMessage(player, "&7No one has requested to teleport to you.");
	    profile.setTeleportRequester(null);
	    return;
	}

	if (profile.isTeleportRequestHere()) {
	    final Player target = Bukkit.getPlayer(profile.getTeleportRequester());

	    if (target == null) {
		MessageManager.sendMessage(player, "&7The player that made the last request is offline.");
		return;
	    }

	    Profile targetProfile = Legacy.getInstance().getProfileManager().getProfile(target.getUniqueId());

	    final Teleport teleportRunnable = new Teleport(target.getUniqueId(),
		    "&7You are now being teleported to &c" + player.getName() + "&7.\nWait 5 seconds. Do not move.",
		    "&7You have teleported to &c" + player.getDisplayName() + "&7.", 10, player.getUniqueId(),
		    new TeleportSetting() {
			@Override
			public void onTeleport(Teleport teleport, Player player) {
			    target.teleport(teleport.getToLocation());
			}

			@Override
			public void onStartup(Teleport teleport, Player player) {

			}
		    });
	    targetProfile.createTeleportRunnable(teleportRunnable);

	    MessageManager.sendMessage(player,
		    "&7You have accepted the request for &c" + target.getName() + " &7to teleport to you.");

	    profile.setTeleportRequester(null);
	    profile.setTeleportRequestHere(false);
	    profile.setTeleportRequestLocation(null);
	    profile.setTeleportRequestTime(0);
	} else {
	    final Player teleportTo = Bukkit.getPlayer(profile.getTeleportRequester());

	    if (teleportTo == null) {
		MessageManager.sendMessage(player, "&7The player that made the last request is offline.");
		return;
	    }

	    final Teleport teleportRunnable = new Teleport(player.getUniqueId(),
		    "&7You are now being teleported to &c" + teleportTo.getName() + "&7.\nWait 5 seconds. Do not move.",
		    "&7You have teleported to &c" + teleportTo.getName() + "&7.", 10, teleportTo.getUniqueId(),
		    new TeleportSetting() {
			@Override
			public void onTeleport(Teleport teleport, Player player) {
			    player.teleport(teleport.getToLocation());
			}

			@Override
			public void onStartup(Teleport teleport, Player player) {

			}
		    });
	    profile.createTeleportRunnable(teleportRunnable);
	    MessageManager.sendMessage(teleportTo,
		    "&c" + player.getName() + " &7has accepted your request to teleport to them.");

	    profile.setTeleportRequester(null);
	    profile.setTeleportRequestHere(false);
	    profile.setTeleportRequestLocation(null);
	    profile.setTeleportRequestTime(0);
	}

    }

}
