package com.mccritz.kmain.commands;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.spawn.SpawnManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.PlayerUtility;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Calvin on 4/22/2015.
 * Project: Legacy
 */
public class Command_spawn extends BaseCommand {

    private Legacy main = Legacy.getInstance();
    private SpawnManager sm = main.getSpawnManager();

    public Command_spawn() {
        super("spawn", null, CommandUsageBy.PlAYER);
        setUsage("&cImproper usage! /spawn");
        setMinArgs(0);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0) {
            PlayerUtility.updateScoreboard(p);

            sm.getSpawn().spawnTeleport(p);
            return;
        }

        if (args.length == 1) {
            if (p.hasPermission("kmain.spawn.admin")) {
                if (args[0].equalsIgnoreCase("view")) {
                    if (sm.getSpawn() != null) {
                        MessageManager.sendMessage(p, "&7Spawn Radius: &c" + sm.getSpawn().getRadius());
                        MessageManager.sendMessage(p, "&7Spawn Height: &c" + sm.getSpawn().getHeight());
                        MessageManager.sendMessage(p, "&7Stone Radius: &c" + sm.getSpawn().getStoneRadius());
                        MessageManager.sendMessage(p, "&7Stone Height: &c" + sm.getSpawn().getStoneHeight());
                    } else {
                        MessageManager.sendMessage(p, "&7The spawn is not set.");
                    }
                } else {
                    MessageManager.sendMessage(p, getUsage());
                }
            } else {
                MessageManager.sendMessage(p, getUsage());
            }
        }
    }
}
