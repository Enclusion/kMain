package code.breakmc.legacy.commands;

import code.breakmc.legacy.utils.LagTask;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Command_lag extends BaseCommand {

    public Command_lag() {
        super("lag", null, CommandUsageBy.ANYONE);
        setUsage("&cImproper usage! /lag");
        setMinArgs(0);
        setMaxArgs(12311);

    }

    public void execute(CommandSender sender, String[] args) {
        DecimalFormat f = new DecimalFormat("##.0");
        MessageManager.sendMessage(sender, "&cServer TPS: " + f.format(BigDecimal.valueOf(LagTask.getTPS())));
        MessageManager.sendMessage(sender, "&cServer Lag: " + Math.round((1.0D - LagTask.getTPS() / 20.0D) * 100.0D) + "%");
    }
}
