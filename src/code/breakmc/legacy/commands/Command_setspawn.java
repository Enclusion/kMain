package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.spawn.Spawn;
import code.breakmc.legacy.spawn.SpawnManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;

/**
 * Created by Calvin on 4/22/2015.
 * Project: Legacy
 */
public class Command_setspawn extends BaseCommand {

    private Legacy main = Legacy.getInstance();
    private SpawnManager sm = main.getSpawnManager();

    public Command_setspawn() {
        super("setspawn", "legacy.setspawn", CommandUsageBy.ANYONE);
        setUsage("&cImproper usage! /setspawn (spawnradius, spawnheight, stoneradius, stoneheight)");
        setMinArgs(4);
        setMaxArgs(4);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            Spawn spawn = new Spawn(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));

            sm.setSpawn(spawn);

            MessageManager.sendMessage(sender, "&7Successfully set spawn radius to &b" + args[0] + "&7, spawn height to &b" + args[1] + "&7, stone radius to &b" + args[2] + "&7, and stone height to &b" + args[3] + "&7.");
        } catch (NumberFormatException e) {
            MessageManager.sendMessage(sender, "&4You must enter valid numbers.");
        }
    }
}
