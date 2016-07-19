package com.mccritz.kmain.utils;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.UUID;

public class MessageManager {

    public static void sendMessage(Player p, String message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendMessage(UUID id, String message) {
        if (Bukkit.getPlayer(id) != null) {
            Bukkit.getPlayer(id).sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public static void sendMessage(CommandSender s, String message) {
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static String formatDouble(double d) {
        DecimalFormat df2 = new DecimalFormat("#,##0.00");
        return df2.format(d);
    }
}
