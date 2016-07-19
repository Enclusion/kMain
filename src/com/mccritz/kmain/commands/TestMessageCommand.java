package com.mccritz.kmain.commands;

import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TestMessageCommand extends BaseCommand {

    public TestMessageCommand() {
        super("testmessage", "kmain.testmessage", CommandUsageBy.PlAYER, "tm");
        setUsage("&c/tm <message>");
        setMinArgs(1);
        setMaxArgs(100);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 1) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, ' ', 0, args.length)));
        }
    }
}
