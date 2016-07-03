package com.mccritz.kmain.utils.teleportation;

import org.bukkit.entity.Player;

public abstract class TeleportSetting {

    public abstract void onTeleport(Teleport teleport, Player player);

    public abstract void onStartup(Teleport teleport, Player player);
}
