package com.mccritz.kmain.commands;

import org.bukkit.command.CommandSender;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.spawn.Spawn;
import com.mccritz.kmain.spawn.SpawnManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;

/**
 * Created by Calvin on 4/22/2015. Project: Legacy
 */
public class CommandSetSpawn extends BaseCommand {

    private Legacy main = Legacy.getInstance();
    private SpawnManager sm = main.getSpawnManager();

    public CommandSetSpawn() {
	super("setspawn", "kmain.setspawn", CommandUsageBy.ANYONE);
	setUsage("&cImproper usage! /setspawn <spawnradius, spawnheight, stoneradius, stoneheight>");
	setMinArgs(4);
	setMaxArgs(4);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
	try {
	    Spawn spawn = new Spawn(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]),
		    Integer.parseInt(args[3]));

	    sm.setSpawn(spawn);

	    MessageManager.sendMessage(sender,
		    "&7Successfully set spawn radius to &c" + args[0] + "&7, spawn height to &c" + args[1]
			    + "&7, stone radius to &c" + args[2] + "&7, and stone height to &c" + args[3] + "&7.");
	} catch (NumberFormatException e) {
	    MessageManager.sendMessage(sender, "&4You must enter valid numbers.");
	}
    }
}