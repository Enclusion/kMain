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

public class SetBalanceCommand extends BaseCommand {

    private kMain main = kMain.getInstance();
    private ProfileManager pm = main.getProfileManager();

    public SetBalanceCommand() {
        super("setbalance", "kmain.setbalance", CommandUsageBy.ANYONE, "setbal");
        setUsage("&c/<command> <player> <amount>");
        setMinArgs(2);
        setMaxArgs(2);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 2) {
            Profile profile = pm.getProfile(args[0]);

            if (profile == null) {
                MessageManager.message(sender, "&c" + args[0] + " &7could not be found.");
                return;
            }

            try {
                profile.setGold(Double.valueOf(args[1]));

                MessageManager.message(sender, "&7You have set the balance of &c" + profile.getName() + " &7to &c" + MessageManager.formatDouble(profile.getGold()) + "&7.");

                SlackAPI.getInstance().sendMessage(SlackTeam.MAIN, "#gold-eco", new SlackMessage("kMain", ":robot_face:", "" + sender.getName() + " has set " + profile.getName() + "'s balance to " + MessageManager.formatDouble(profile.getGold())), false);
            } catch (Exception ignored) {
                MessageManager.message(sender, "&cThe arguments specified is not a valid balance.");
            }
        }
    }
}
