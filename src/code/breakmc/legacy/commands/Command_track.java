package code.breakmc.legacy.commands;

import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.TrackingUtils;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Calvin on 4/22/2015.
 * Project: Legacy
 */
public class Command_track extends BaseCommand {

    public Command_track() {
        super("track", null, CommandUsageBy.PlAYER);
        setUsage("&cImproper usage! /track (player;all)");
        setMinArgs(1);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (p.getWorld().getEnvironment().equals(World.Environment.NETHER) || p.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
            String env = p.getWorld().getEnvironment().equals(World.Environment.NETHER) ? "the nether." : "the end.";
            MessageManager.sendMessage(p, "&cTracking is disabled in " + env);
            return;
        }

        TrackingUtils track = new TrackingUtils();

        if (args[0].equalsIgnoreCase("all")) {
            track.setLoc(p.getLocation().getBlockX(), p.getLocation().getBlockY() - 1, p.getLocation().getBlockZ());
            track.TrackAll(p, null);
        } else {
            Player tracked = Bukkit.getPlayer(args[0]);

            if (tracked == null) {
                MessageManager.sendMessage(p, "&cPlayer \"" + args[0] + "\" could not be found.");
            } else {
                track.setLoc(p.getLocation().getBlockX(), p.getLocation().getBlockY() - 1, p.getLocation().getBlockZ());
                track.Track(p, tracked);
            }
        }
    }
}
