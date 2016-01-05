package code.breakmc.legacy.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.BlockUtils;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class Command_clearspawn extends BaseCommand {

    private Legacy main = Legacy.getInstance();

    public Command_clearspawn() {
        super("clearspawn", "legacy.clearspawn", CommandUsageBy.PlAYER);
        setUsage("&cInvalid usage! /clearspawn");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                BlockUtils utils = Legacy.getInstance().getBlockUtils();
                utils.regenAllBlocks(false);
                MessageManager.sendMessage(sender, "&aSpawn has been cleared.");
            }
        }.runTaskAsynchronously(main);
    }
}
