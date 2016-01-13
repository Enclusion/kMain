package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.duels.DuelManager;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.spawn.SpawnManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_duel extends BaseCommand {

    private DuelManager dm = Legacy.getInstance().getDuelManager();
    private ProfileManager pm = Legacy.getInstance().getProfileManager();
    private SpawnManager sm = Legacy.getInstance().getSpawnManager();

    public Command_duel() {
        super("duel", null, CommandUsageBy.PlAYER, "1v1", "1vs1", "battle");
        setUsage("&cImproper usage! /duel");
        setMinArgs(0);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0) {
            MessageManager.sendMessage(p, "&7&m------------&r &6&l1v1&r &7&m---------------");
            MessageManager.sendMessage(p, "&6/1v1 &7- &eView this help message.");
            MessageManager.sendMessage(p, "&6/1v1 (player) &7- &eRequest a 1v1.");
            MessageManager.sendMessage(p, "&6/1v1 enable &7- &eEnable 1v1 requests.");
            MessageManager.sendMessage(p, "&6/1v1 disable &7- &eDisable 1v1 requests.");
            MessageManager.sendMessage(p, "&6/accept (player) &7- &eAccept a 1v1.");
            MessageManager.sendMessage(p, "&7&m--------------------------------");
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                MessageManager.sendMessage(p, "&7&m------------&r &6&l1v1&r &7&m---------------");
                MessageManager.sendMessage(p, "&6/1v1 &7- &eView this help message.");
                MessageManager.sendMessage(p, "&6/1v1 (player) &7- &eRequest a 1v1.");
                MessageManager.sendMessage(p, "&6/1v1 enable &7- &eEnable 1v1 requests.");
                MessageManager.sendMessage(p, "&6/1v1 disable &7- &eDisable 1v1 requests.");
                MessageManager.sendMessage(p, "&6/accept (player) &7- &eAccept a 1v1.");
                MessageManager.sendMessage(p, "&7&m--------------------------------");
            } else if (args[0].equalsIgnoreCase("enable")) {
                if (!dm.getNotAcceptingRequests().contains(p.getUniqueId())) {
                    MessageManager.sendMessage(p, "&cYou already have 1v1 requests enabled.");
                    return;
                }

                dm.getNotAcceptingRequests().remove(p.getUniqueId());
                MessageManager.sendMessage(p, "&6You have &aenabled &61v1 requests.");
            } else if (args[0].equalsIgnoreCase("disable")) {
                if (dm.getNotAcceptingRequests().contains(p.getUniqueId())) {
                    MessageManager.sendMessage(p, "&cYou already have 1v1 requests disabled.");
                    return;
                }

                dm.getNotAcceptingRequests().add(p.getUniqueId());
                MessageManager.sendMessage(p, "&6You have &cdisabled &61v1 requests.");
            } else {
                Player t = Bukkit.getPlayer(args[0]);

                if (t == null) {
                    MessageManager.sendMessage(p, "&cPlayer \"" + args[0] + "\" could not be found.");
                    return;
                }

                if (dm.getNotAcceptingRequests().contains(t.getUniqueId())) {
                    MessageManager.sendMessage(p, "&cPlayer \"" + t.getName() + "\" is not accepting 1v1 requests.");
                    return;
                }

                if (!sm.hasSpawnProt(p.getUniqueId())) {
                    MessageManager.sendMessage(p, "&cYou must have spawn protection to request a 1v1!");
                    return;
                }

                if (!sm.hasSpawnProt(t.getUniqueId())) {
                    MessageManager.sendMessage(p.getUniqueId(), "&cPlayer \"" + t.getName() + "\" must have spawn protection to request a 1v1!");
                    return;
                }

                if (p.getLocation().distance(t.getLocation()) > 20) {
                    MessageManager.sendMessage(p, "&cPlayer \"" + t.getName() + "\" is not close enough to request a 1v1!");
                    return;
                }

                if (dm.isInDuel(p)) {
                    MessageManager.sendMessage(p, "&cYou are already in a 1v1.");
                    return;
                }

                if (dm.isInDuel(t)) {
                    MessageManager.sendMessage(p, "&cThat player is already in a 1v1.");
                    return;
                }

                dm.requestDuel(p, t);
            }
        }
    }
}
