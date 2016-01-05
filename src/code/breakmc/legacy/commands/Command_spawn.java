package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.spawn.SpawnManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Calvin on 4/22/2015.
 * Project: Legacy
 */
public class Command_spawn extends BaseCommand {

    private Legacy main = Legacy.getInstance();
    private SpawnManager sm = main.getSpawnManager();

    public Command_spawn() {
        super("spawn", null, CommandUsageBy.PlAYER);
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0) {
            sm.getSpawn().spawnTeleport(p);
            return;
        }

        if (args.length == 1) {
            if (p.hasPermission("legacy.spawn.admin")) {
                if (args[0].equalsIgnoreCase("view")) {
                    if (sm.getSpawn() != null) {
                        MessageManager.sendMessage(p, "&7Spawn Radius: &b" + sm.getSpawn().getRadius());
                        MessageManager.sendMessage(p, "&7Spawn Height: &b" + sm.getSpawn().getHeight());
                        MessageManager.sendMessage(p, "&7Stone Radius: &b" + sm.getSpawn().getStoneRadius());
                        MessageManager.sendMessage(p, "&7Stone Height: &b" + sm.getSpawn().getStoneHeight());
                    } else {
                        MessageManager.sendMessage(p, "&cThe spawn is not set.");
                    }
                } else {
                    MessageManager.sendMessage(p, getUsage());
                }
            } else {
                MessageManager.sendMessage(p, getUsage());
            }
        }
    }
}
