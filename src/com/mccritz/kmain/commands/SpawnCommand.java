package com.mccritz.kmain.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.spawn.SpawnManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends BaseCommand {

    private kMain main = kMain.getInstance();
    private SpawnManager sm = main.getSpawnManager();

    public SpawnCommand() {
        super("spawn", null, CommandUsageBy.PlAYER);
        setUsage("&cImproper usage! /spawn");
        setMinArgs(0);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0) {
            sm.getSpawn().spawnTeleport(p);
            return;
        }

        if (args.length == 1) {
            if (p.hasPermission("kmain.spawn.admin")) {
                if (args[0].equalsIgnoreCase("view")) {
                    if (sm.getSpawn() != null) {
                        MessageManager.message(p, "&7Spawn Radius: &c" + sm.getSpawn().getRadius());
                        MessageManager.message(p, "&7Spawn Height: &c" + sm.getSpawn().getHeight());
                        MessageManager.message(p, "&7Stone Radius: &c" + sm.getSpawn().getStoneRadius());
                        MessageManager.message(p, "&7Stone Height: &c" + sm.getSpawn().getStoneHeight());
                    } else {
                        MessageManager.message(p, "&7The spawn is not set.");
                    }
                } else {
                    MessageManager.message(p, getUsage());
                }
            } else {
                MessageManager.message(p, getUsage());
            }
        }
    }
}
