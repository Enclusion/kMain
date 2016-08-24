package com.mccritz.kmain.events.end.commands;

import com.mccritz.kmain.events.EventManager;
import com.mccritz.kmain.events.end.EndEvent;
import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.Cooldowns;
import com.mccritz.kmain.utils.DateUtil;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class EndEventCommand extends BaseCommand {

    private kMain main = kMain.getInstance();
    private EventManager em = main.getEventManager();
    private List<String> helpLines;

    public EndEventCommand() {
        super("endevent", "kmain.endevent", CommandUsageBy.ANYONE);
        setUsage("&cImproper usage! /endevent help");
        setMinArgs(0);
        setMaxArgs(2);

        helpLines = Arrays.asList(
                "&7**&5End Event&7**",
                "&5/endevent &7- Displays this page.",
                "&5/endevent start &7- Starts the end event.",
                "&5/endevent forcestop &7- Stops the end event.",
                "&5/endevent refillchest &7- Refills all chests.",
                "&5/endevent unban <player> &7- Removes the deathban on the player.",
                "&5/endevent setloot <tier> &7- Edit the loot for a specific tier.",
                "&5/endevent setspawn &7- Sets the spawn point.",
                "&5/endevent status &7- Shows the end event status.");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                sendHelp(sender);
            } else if (args[0].equalsIgnoreCase("start")) {
                if (!sender.hasPermission("endevent.start")) {
                    MessageManager.message(sender, "&cYou are not allowed to do this.");
                    return;
                }

                EndEvent endEvent = em.getEndEvent();

                if (endEvent == null) {
                    MessageManager.message(sender, "&7The end event could not be loaded.");
                    return;
                }

                if (endEvent.isStarted()) {
                    MessageManager.message(sender, "&7The end event is already started.");
                    return;
                }

                endEvent.startEvent();

                MessageManager.message(sender, "&7You have started the end event.");
            } else if (args[0].equalsIgnoreCase("forcestop")) {
                if (!sender.hasPermission("endevent.forcestop")) {
                    MessageManager.message(sender, "&cYou are not allowed to do this.");
                    return;
                }

                EndEvent endEvent = em.getEndEvent();

                if (endEvent == null) {
                    MessageManager.message(sender, "&7The end event could not be loaded.");
                    return;
                }

                if (!endEvent.isStarted()) {
                    MessageManager.message(sender, "&7The end event is not running.");
                    return;
                }

                endEvent.cancelEvent();

                MessageManager.message(sender, "&7You have cancelled the end event.");

                MessageManager.broadcast("&c[&5End Event&c] The event has been force stopped by an administrator.");
            } else if (args[0].equalsIgnoreCase("refillchest")) {
                if (!sender.hasPermission("endevent.refillchest")) {
                    MessageManager.message(sender, "&cYou are not allowed to do this.");
                    return;
                }

                EndEvent endEvent = em.getEndEvent();

                if (endEvent == null) {
                    MessageManager.message(sender, "&7The end event could not be loaded.");
                    return;
                }

                endEvent.fillChests();

                MessageManager.message(sender, "&7The chests have been refilled in the end event.");
            } else if (args[0].equalsIgnoreCase("setspawn")) {
                if (!sender.hasPermission("endevent.setspawn")) {
                    MessageManager.message(sender, "&cYou are not allowed to do this.");
                    return;
                }

                if (!(sender instanceof Player)) {
                    MessageManager.message(sender, "&cYou can only execute this command ingame.");
                    return;
                }

                Player p = (Player) sender;

                EndEvent endEvent = em.getEndEvent();

                if (endEvent == null) {
                    MessageManager.message(sender, "&7The end event could not be loaded.");
                    return;
                }

                endEvent.setSpawnLocation(p.getLocation());
                endEvent.saveData();

                MessageManager.message(sender, "&7The spawn location in the end event has been set.");
            } else if (args[0].equalsIgnoreCase("status")) {
                if (!sender.hasPermission("endevent.status")) {
                    MessageManager.message(sender, "&cYou are not allowed to do this.");
                    return;
                }

                EndEvent endEvent = em.getEndEvent();

                if (endEvent == null) {
                    MessageManager.message(sender, "&7The end event could not be loaded.");
                    return;
                }

                MessageManager.message(sender, "&7End Event Status:");
                MessageManager.message(sender, "&7Started - " + (endEvent.isStarted() ? "&atrue" : "&cfalse"));
                MessageManager.message(sender, "&7Can Leave End - " + (endEvent.isCanLeaveEnd() ? "&atrue" : "&cfalse"));
                MessageManager.message(sender, "&7Spawn Location - " + (endEvent.getSpawnLocation() != null ? "&5X: " + endEvent.getSpawnLocation().getBlockX() + " Y: " + endEvent.getSpawnLocation().getBlockY() + " Z: " + endEvent.getSpawnLocation().getBlockZ() : "&cnot set"));
                MessageManager.message(sender, "&7Game Time - " + (endEvent.isStarted() ? "&5" + DateUtil.readableTime((endEvent.getGameTime() * 1000)) : "&cnot started"));
                MessageManager.message(sender, "&7Death Bans - " + (endEvent.isStarted() ? "&5" + endEvent.getDeathBannedSize() : "&cnot started"));
            } else {
                MessageManager.message(sender, "&c" + getUsage());
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("unban")) {
                if (!sender.hasPermission("endevent.unban")) {
                    MessageManager.message(sender, "&cYou are not allowed to do this.");
                    return;
                }

                if (Bukkit.getPlayer(args[1]) == null) {
                    MessageManager.message(sender, "&cThe specified player was not found.");
                    return;
                }

                Player target = Bukkit.getPlayer(args[1]);

                EndEvent endEvent = em.getEndEvent();

                if (endEvent == null) {
                    MessageManager.message(sender, "&7The end event could not be loaded.");
                    return;
                }

                if (Cooldowns.getCooldown(target.getUniqueId(), "deathban") <= 0) {
                    MessageManager.message(sender, "&7That player is not death banned.");
                    return;
                }

                Cooldowns.removeCooldowns(target.getUniqueId());
                MessageManager.message(sender, "&7You have removed the deathban from &5" + target.getName() + "&7.");
            } else if (args[0].equalsIgnoreCase("setloot")) {
                if (!(sender instanceof Player)) {
                    MessageManager.message(sender, "&cOnly players can execute this command.");
                    return;
                }

                Player player = (Player) sender;

                if (!sender.hasPermission("endevent.setloot")) {
                    MessageManager.message(sender, "&cYou are not allowed to do this.");
                    return;
                }

                EndEvent endEvent = em.getEndEvent();

                if (endEvent == null) {
                    MessageManager.message(sender, "&7The end event could not be loaded.");
                    return;
                }

                if (args[1].equalsIgnoreCase("1")) {
                    player.openInventory(endEvent.getTier1Inventory());
                } else if (args[1].equalsIgnoreCase("2")) {
                    player.openInventory(endEvent.getTier2Inventory());
                } else if (args[1].equalsIgnoreCase("3")) {
                    player.openInventory(endEvent.getTier3Inventory());
                } else {
                    MessageManager.message(sender, "&c" + getUsage());
                }
            } else {
                MessageManager.message(sender, "&c" + getUsage());
            }
        } else {
            MessageManager.message(sender, "&c" + getUsage());
        }
    }

    public void sendHelp(CommandSender sender) {
        for (String line : helpLines) {
            MessageManager.message(sender, line);
        }
    }
}
