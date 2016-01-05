package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.PlayerUtility;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Command_homeas extends BaseCommand {

    private ProfileManager pm = Legacy.getInstance().getProfileManager();

    public Command_homeas() {
        super("homeas", "legacy.homeas", CommandUsageBy.PlAYER, "has");
        setUsage("&cImproper usage! /homeas help");
        setMinArgs(0);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0) {
            MessageManager.sendMessage(p, "&7&m--------------&r &bAdmin &7&m--------------");
            MessageManager.sendMessage(p, "&a/homeas [player] &8- &7Warps you to their home.");
            MessageManager.sendMessage(p, "&7&m----------------------------------");
        }

        if (args.length == 1) {
            Profile tprof = pm.getProfile(args[0]);

            if (tprof == null) {
                MessageManager.sendMessage(p, "&cPlayer \"" + args[0] + "\" could not be found.");
                return;
            }

            if (tprof.getHome() != null) {
                p.teleport(tprof.getHome());
                Legacy.getInstance().getLogger().log(Level.INFO, "[Admin Teleport]: " + p.getName() + " to " + tprof.getName() + "'s home");
            } else {
                MessageManager.sendMessage(p, "&c" + tprof.getName() + " does not have a home set.");
            }
        }
    }

    public List<String> tabComplete(String[] args, CommandSender sender) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                List<String> list2return = toList(PlayerUtility.getOnlinePlayers()).stream().filter(opt -> opt.toLowerCase().startsWith(args[1])).collect(Collectors.toList());

                Collections.sort(list2return);

                return list2return;
            }
        }

        return new ArrayList<>();
    }

    public List<String> toList(Player[] array) {
        List<String> list = new ArrayList<>();
        for (Player t : array) {
            list.add(t.getName());
        }
        return list;
    }
}