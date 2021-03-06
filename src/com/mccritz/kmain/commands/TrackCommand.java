package com.mccritz.kmain.commands;

import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.TrackingUtils;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrackCommand extends BaseCommand {

    public TrackCommand() {
        super("track", null, CommandUsageBy.PlAYER);
        setUsage("&cImproper usage! /track <player;all>");
        setMinArgs(1);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (p.getWorld().getEnvironment().equals(World.Environment.NETHER) || p.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
            String env = p.getWorld().getEnvironment().equals(World.Environment.NETHER) ? "the nether." : "the end.";
            MessageManager.message(p, "&7Tracking is disabled in " + env);
            return;
        }

        TrackingUtils track = new TrackingUtils();

        if (args[0].equalsIgnoreCase("all")) {
            track.setLoc(p.getLocation().getBlockX(), p.getLocation().getBlockY() - 1, p.getLocation().getBlockZ());
            track.TrackAll(p, null);
        } else {
            Player tracked = Bukkit.getPlayer(args[0]);

            if (tracked == null) {
                MessageManager.message(p, "&c" + args[0] + " &7could not be found.");
            } else {
                track.setLoc(p.getLocation().getBlockX(), p.getLocation().getBlockY() - 1, p.getLocation().getBlockZ());
                track.Track(p, tracked);
            }
        }
    }
}
