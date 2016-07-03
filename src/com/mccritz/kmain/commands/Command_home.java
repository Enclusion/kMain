package com.mccritz.kmain.commands;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_home extends BaseCommand {

    private Legacy main = Legacy.getInstance();
    private ProfileManager pm = main.getProfileManager();

    public Command_home() {
        super("home", null, CommandUsageBy.PlAYER);
        setUsage("&cImproper usage! /home");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            Profile prof = pm.getProfile(p.getUniqueId());

            if (prof.getHome() != null) {
                prof.homeTeleport(p);
            } else {
                MessageManager.sendMessage(p, "&7You do not have a home set.");
            }
        }
    }
}
