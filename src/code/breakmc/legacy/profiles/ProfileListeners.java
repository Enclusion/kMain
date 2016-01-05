package code.breakmc.legacy.profiles;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.utils.moon.ScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ProfileListeners implements Listener {

    private ScoreboardManager sbm = Legacy.getInstance().getScoreboardManager();
    private TeamManager tm = Legacy.getInstance().getTeamManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!Legacy.getInstance().getProfileManager().hasProfile(p.getUniqueId())) {
            Legacy.getInstance().getProfileManager().createProfile(p);
        }

//        Legacy.getInstance().getScoreboardManager().setupScoreboard(Legacy.getInstance().getProfileManager().getProfile(p.getUniqueId()));
    }
}
