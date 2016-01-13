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

public class Command_accept extends BaseCommand {

    private DuelManager dm = Legacy.getInstance().getDuelManager();
    private ProfileManager pm = Legacy.getInstance().getProfileManager();
    private SpawnManager sm = Legacy.getInstance().getSpawnManager();

    public Command_accept() {
        super("accept", null, CommandUsageBy.PlAYER);
        setUsage("&cImproper usage! /accept (player)");
        setMinArgs(1);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 1) {
            Player t = Bukkit.getPlayer(args[0]);

            if (t == null) {
                MessageManager.sendMessage(p, "&cPlayer \"" + args[0] + "\" could not be found.");
                return;
            }

            if (!dm.getDuelRequests().containsKey(t.getUniqueId())) {
                MessageManager.sendMessage(p, "&cThat player has not sent you a 1v1 request.");
                return;
            }

            if (!dm.getDuelRequests().get(t.getUniqueId()).equals(p.getUniqueId())) {
                MessageManager.sendMessage(p, "&cThat player has not sent you a 1v1 request.");
                return;
            }

            if (!sm.hasSpawnProt(p.getUniqueId())) {
                MessageManager.sendMessage(p, "&cYou must have spawn protection to accept a 1v1 request!");
                return;
            }

            if (!sm.hasSpawnProt(t.getUniqueId())) {
                MessageManager.sendMessage(p.getUniqueId(), "&cThat player must have spawn protection to accept his 1v1 request!");
                return;
            }

            if (p.getLocation().distance(t.getLocation()) > 20) {
                MessageManager.sendMessage(p, "&cThat player is not close enough to accept his 1v1 request!");
                return;
            }

            if (dm.isInDuel(t)) {
                MessageManager.sendMessage(p, "&cThat player is already in a 1v1.");
                return;
            }

            if (dm.isInDuel(p)) {
                MessageManager.sendMessage(p, "&cYou are already in a 1v1.");
                return;
            }

            dm.createDuel(p, t);
        }
    }
}
