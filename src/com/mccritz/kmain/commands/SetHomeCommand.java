package com.mccritz.kmain.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand extends BaseCommand {

    private kMain main = kMain.getInstance();
    private ProfileManager pm = main.getProfileManager();

    public SetHomeCommand() {
        super("sethome", null, CommandUsageBy.PlAYER, "shome");
        setUsage("&cImproper usage! /sethome");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            Profile prof = pm.getProfile(p.getUniqueId());
            prof.setHome(p.getLocation());

            MessageManager.message(p, "&7Your home has been set.");
        }
    }
}
