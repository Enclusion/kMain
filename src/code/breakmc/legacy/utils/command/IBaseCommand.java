package code.breakmc.legacy.utils.command;

import org.bukkit.command.CommandSender;

public interface IBaseCommand {

    void execute(CommandSender sender, String[] args);
}
