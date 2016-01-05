package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
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
                MessageManager.sendMessage(p, "&cYou do not have a home set.");
            }
        }
    }
}
