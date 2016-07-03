package com.mccritz.kmain.profiles;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.spawn.SpawnManager;
import com.mccritz.kmain.teams.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ProfileListeners implements Listener {

    private TeamManager tm = Legacy.getInstance().getTeamManager();
    private SpawnManager sm = Legacy.getInstance().getSpawnManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!Legacy.getInstance().getProfileManager().hasProfile(p.getUniqueId())) {
            Legacy.getInstance().getProfileManager().createProfile(p);
        }
    }
}
