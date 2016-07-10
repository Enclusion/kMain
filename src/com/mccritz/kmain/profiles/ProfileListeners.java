package com.mccritz.kmain.profiles;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mccritz.kmain.Legacy;

public class ProfileListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
	Player p = e.getPlayer();

	if (!Legacy.getInstance().getProfileManager().hasProfile(p.getUniqueId())) {
	    Legacy.getInstance().getProfileManager().createProfile(p);
	}
    }
}
