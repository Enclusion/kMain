package code.breakmc.legacy.profiles;

import code.breakmc.legacy.Legacy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Calvin on 5/9/2015.
 */
public class ProfileListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!Legacy.getInstance().getProfileManager().hasProfile(p.getUniqueId())) {
            Legacy.getInstance().getProfileManager().createProfile(p);
        }
    }
}
