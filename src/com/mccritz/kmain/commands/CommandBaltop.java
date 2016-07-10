package com.mccritz.kmain.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.economy.EconomyManager;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;

/**
 * Created by Calvin on 4/22/2015. Project: Legacy
 */
public class CommandBaltop extends BaseCommand {

    private Legacy main = Legacy.getInstance();
    private ProfileManager pm = main.getProfileManager();
    private EconomyManager eco = main.getEconomyManager();

    public CommandBaltop() {
	super("balancetop", null, CommandUsageBy.ANYONE, "baltop", "moneytop", "dinerotop");
	setUsage("&cImproper usage! /baltop");
	setMinArgs(0);
	setMaxArgs(0);
    }

    @Override
    public void execute(final CommandSender sender, String[] args) {
	new BukkitRunnable() {
	    @Override
	    public void run() {
		HashMap<String, Double> map = new HashMap<>();
		for (Profile profs : pm.getLoadedProfiles()) {
		    map.put(profs.getName(), profs.getBalance());
		}

		MessageManager.sendMessage(sender, "&7&m***&r &3Top 10 Players &7&m***");

		Object[] a = map.entrySet().toArray();
		Arrays.sort(a, (o1, o2) -> ((Map.Entry<String, Double>) o2).getValue()
			.compareTo(((Map.Entry<String, Double>) o1).getValue()));

		int topten = 0;
		for (Object e : a) {
		    if (topten <= 9) {
			MessageManager.sendMessage(sender,
				"&7" + (topten + 1) + " - &b" + ((Map.Entry<String, Double>) e).getKey() + "&7: &c"
					+ eco.formatDouble(((Map.Entry<String, Double>) e).getValue()));
		    }
		    topten++;
		}
	    }
	}.runTaskAsynchronously(main);
    }
}
