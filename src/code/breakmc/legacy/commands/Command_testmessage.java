package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by Calvin on 4/22/2015.
 * Project: Legacy
 */
public class Command_testmessage extends BaseCommand {

    private Legacy main = Legacy.getInstance();

    public Command_testmessage() {
        super("testmessage", "legacy.testmessage", CommandUsageBy.PlAYER, "tm");
        setUsage("/<command> (message)");
        setMinArgs(1);
        setMaxArgs(100);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 1) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, ' ', 1, args.length)));
        }
    }
}
