package com.mccritz.kmain.economy.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import com.mccritz.kmain.utils.slack.SlackAPI;
import com.mccritz.kmain.utils.slack.SlackMessage;
import com.mccritz.kmain.utils.slack.SlackTeam;
import org.bukkit.command.CommandSender;

public class ClearBalanceCommand extends BaseCommand {

    private kMain main = kMain.getInstance();
    private ProfileManager pm = main.getProfileManager();

    public ClearBalanceCommand() {
        super("clearbalance", "kmain.clearbalance", CommandUsageBy.ANYONE, "clearbal");
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

            profile.setGold(0.0);

            MessageManager.message(sender, "&7You have cleared the balance of &c" + profile.getName() + "&7.");

            SlackAPI.getInstance().sendMessage(SlackTeam.MAIN, "#gold-eco", new SlackMessage("kMain", ":robot_face:", "" + sender.getName() + " has cleared " + profile.getName() + "'s balance"), false);
        }
    }
}
