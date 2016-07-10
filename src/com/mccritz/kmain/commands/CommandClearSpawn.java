package com.mccritz.kmain.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.utils.BlockUtils;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;

public class CommandClearSpawn extends BaseCommand {

    private Legacy main = Legacy.getInstance();

    public CommandClearSpawn() {
	super("clearspawn", "kmain.clearspawn", CommandUsageBy.PlAYER);
	setUsage("&cInvalid usage! /clearspawn");
	setMinArgs(0);
	setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
	new BukkitRunnable() {
	    @Override
	    public void run() {
		BlockUtils utils = Legacy.getInstance().getBlockUtils();
		utils.regenAllBlocks(false);
		MessageManager.sendMessage(sender, "&7Spawn has been cleared.");
	    }
	}.runTaskAsynchronously(main);
    }
}
