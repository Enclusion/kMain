package com.mccritz.kmain.utils.teleportation;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.utils.MessageManager;

public class Teleport implements Runnable {

    public Teleport(UUID id) {
	this.id = id;
    }

    public Teleport(UUID id, String warpingMessage, TeleportSetting teleportSetting) {
	this.id = id;
	this.warpingMessage = warpingMessage;
	this.teleportSetting = teleportSetting;
    }

    public Teleport(UUID id, String warpingMessage, String warpedMessage, TeleportSetting teleportSetting) {
	this.id = id;
	this.warpingMessage = warpingMessage;
	this.warpedMessage = warpedMessage;
	this.teleportSetting = teleportSetting;
    }

    public Teleport(UUID id, String warpingMessage, String warpedMessage, Location toLocation,
	    TeleportSetting teleportSetting) {
	this.id = id;
	this.warpingMessage = warpingMessage;
	this.warpedMessage = warpedMessage;
	this.toLocation = toLocation;
	this.teleportSetting = teleportSetting;
    }

    public Teleport(UUID id, String warpingMessage, String warpedMessage, int time, TeleportSetting teleportSetting) {
	this.id = id;
	this.warpingMessage = warpingMessage;
	this.warpedMessage = warpedMessage;
	this.time = time;
	this.teleportSetting = teleportSetting;
    }

    public Teleport(UUID id, String warpingMessage, String warpedMessage, int time, Location toLocation,
	    TeleportSetting teleportSetting) {
	this.id = id;
	this.warpingMessage = warpingMessage;
	this.warpedMessage = warpedMessage;
	this.time = time;
	this.toLocation = toLocation;
	this.teleportSetting = teleportSetting;
    }

    public Teleport(UUID id, String warpingMessage, String warpedMessage, int time, UUID target,
	    TeleportSetting teleportSetting) {
	this.id = id;
	this.warpingMessage = warpingMessage;
	this.warpedMessage = warpedMessage;
	this.time = time;
	this.isTargetTeleport = true;
	this.targetID = target;
	this.teleportSetting = teleportSetting;
    }

    private UUID id;
    private TeleportSetting teleportSetting;
    private String warpedMessage = "";
    private String warpingMessage = "";
    private Location toLocation = null;
    private boolean isTargetTeleport = false;
    private UUID targetID = null;
    private boolean cancelled = false;
    private int time = 0;

    @Override
    public void run() {
	if (cancelled)
	    return;

	if (Bukkit.getPlayer(id) != null) {
	    Player player = Bukkit.getPlayer(id);
	    Profile profile = Legacy.getInstance().getProfileManager().getProfile(id);

	    if (isTargetTeleport) {
		if (targetID != null) {
		    if (Bukkit.getPlayer(targetID) != null) {
			Player target = Bukkit.getPlayer(targetID);

			toLocation = target.getLocation();
			teleportSetting.onTeleport(this, player);
			MessageManager.sendMessage(player, warpedMessage);

			if (profile.getTeleportRunnable() != null) {
			    profile.getTeleportRunnable().cancel();
			}
			profile.setTeleportRunnable(null);
			profile.setTeleportRequester(null);
			profile.setTeleportRequestHere(false);
			profile.setTeleportRequestLocation(null);
			profile.setTeleportRequestTime(0);
		    } else {
			cancel();
		    }
		} else {
		    cancel();
		}
	    } else {
		teleportSetting.onTeleport(this, player);
		MessageManager.sendMessage(player, warpedMessage);

		if (profile.getTeleportRunnable() != null) {
		    profile.getTeleportRunnable().cancel();
		}
		profile.setTeleportRunnable(null);
		profile.setTeleportRequester(null);
		profile.setTeleportRequestHere(false);
		profile.setTeleportRequestLocation(null);
		profile.setTeleportRequestTime(0);
	    }
	} else {
	    cancel();
	}
    }

    public void send() {
	if (Bukkit.getPlayer(id) != null) {
	    Player player = Bukkit.getPlayer(id);

	    if (getTime() > 0) {
		MessageManager.sendMessage(player, warpingMessage);
	    }
	} else {
	    cancel();
	}
    }

    public void setWarpedMessage(String str) {
	this.warpedMessage = str;
    }

    public String getWarpedMessage() {
	return this.warpedMessage;
    }

    public void setWarpingMessage(String str) {
	this.warpingMessage = str;
    }

    public String getWarpingMessage() {
	return this.warpingMessage;
    }

    public void setTime(int i) {
	this.time = i;
    }

    public int getTime() {
	return this.time;
    }

    public void setIsTargetTeleport(boolean b) {
	this.isTargetTeleport = b;
    }

    public boolean isTargetTeleport() {
	return this.isTargetTeleport;
    }

    public void setTargetTeleport(UUID id) {
	this.targetID = id;
    }

    public Location getToLocation() {
	return toLocation;
    }

    public UUID getTarget() {
	return this.targetID;
    }

    public void cancel() {
	this.cancelled = true;
    }

    public boolean isCancelled() {
	return this.cancelled;
    }

    public UUID getID() {
	return id;
    }

}
