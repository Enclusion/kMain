package com.mccritz.kmain.utils.command;

import org.bukkit.command.CommandSender;

public interface IBaseCommand {

    void execute(CommandSender sender, String[] args);
}
