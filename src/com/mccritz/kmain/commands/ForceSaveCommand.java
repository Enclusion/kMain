package com.mccritz.kmain.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class ForceSaveCommand extends BaseCommand {

    private kMain main = kMain.getInstance();

    public ForceSaveCommand() {
        super("clearspawn", "kmain.forcesave", CommandUsageBy.ANYONE);
        setUsage("&cInvalid usage! /forcesave");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                main.getEconomyManager().saveSales(true);
                main.getProfileManager().saveProfiles(true);
                main.getTeamManager().saveTeams(true);
                main.getWarpManager().saveWarps(true);
                main.getSpawnManager().saveSpawn();
            }
        }.runTaskAsynchronously(main);
    }
}
