package com.mccritz.kmain.economy.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;

public class CheckBalanceCommand extends BaseCommand {

    private kMain main = kMain.getInstance();
    private ProfileManager pm = main.getProfileManager();

    public CheckBalanceCommand() {
        super("checkbalance", "kmain.checkbalance", CommandUsageBy.ANYONE, "checkbal");
        setUsage("&c/<command> <player>");
        setMinArgs(1);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Profile profile = pm.getProfile(args[0]);

            if (profile == null) {
                MessageManager.message(sender, "&c" + args[0] + " &7could not be found.");
                return;
            }

            MessageManager.message(sender, "&7The of &c" + profile.getName() + " &7is &c" + MessageManager.formatDouble(profile.getGold()) + "&7.");
        }
    }
}
