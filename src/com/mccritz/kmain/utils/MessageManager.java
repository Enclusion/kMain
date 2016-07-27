package com.mccritz.kmain.utils;


import com.mccritz.kmain.commands.DebugCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.UUID;

public class MessageManager {

    public static void message(Player p, String message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void message(UUID id, String message) {
        if (Bukkit.getPlayer(id) != null) {
            Bukkit.getPlayer(id).sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public static void message(CommandSender s, String message) {
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void broadcast(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void debug(String message) {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

        for (UUID id : DebugCommand.getDisplayList()) {
            message(id, message);
        }
    }

    public static String formatDouble(double d) {
        DecimalFormat df2 = new DecimalFormat("#,##0.00");
        return df2.format(d);
    }
}
