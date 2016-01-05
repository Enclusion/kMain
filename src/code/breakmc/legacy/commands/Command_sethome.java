package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_sethome extends BaseCommand {

    private Legacy main = Legacy.getInstance();
    private ProfileManager pm = main.getProfileManager();

    public Command_sethome() {
        super("sethome", null, CommandUsageBy.PlAYER, "shome");
        setUsage("&cInvalid usage! /sethome");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            Profile prof = pm.getProfile(p.getUniqueId());
            prof.setHome(p.getLocation());

            MessageManager.sendMessage(p, "&7Home has been set.");
        }
    }
}
