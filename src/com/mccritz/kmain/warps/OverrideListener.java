package com.mccritz.kmain.warps;

import com.mccritz.kmain.Legacy;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by Calvin on 5/9/2015.
 */
public class OverrideListener implements Listener {

    private Legacy main = Legacy.getInstance();
    private WarpManager wm = main.getWarpManager();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String[] message = e.getMessage().split("\\s+");

        if (message[0].equalsIgnoreCase("/yes")) {
            e.setCancelled(true);

            if (wm.getOverriding().containsKey(p.getUniqueId())) {
                WarpManager.TempWarp warp = (WarpManager.TempWarp) p.getMetadata("warpToOverride").get(0).value();

                Warp w = null;

                for (Warp ws : wm.getWarps().get(p.getUniqueId())) {
                    if (ws.getName().equalsIgnoreCase(warp.getWarp().getName())) {
                        w = ws;
                    }
                }

                if (w == null) {
                    MessageManager.sendMessage(p, "&7The warp no longer exists.");
                    return;
                }

                wm.getWarps().get(p.getUniqueId()).remove(w);
                warp.getWarp().setLocation(warp.getLoc());
                wm.getWarps().get(p.getUniqueId()).add(warp.getWarp());

                wm.getOverriding().get(p.getUniqueId()).cancel();
                wm.getOverriding().remove(p.getUniqueId());

                p.removeMetadata("warpToOverride", main);

                MessageManager.sendMessage(p, "You have overrided warp &c" + warp.getWarp().getName() + "&7.");
            } else {
                MessageManager.sendMessage(p, "&7You are not overwriting any warps.");
            }
        } else if (message[0].equalsIgnoreCase("/no")) {
            e.setCancelled(true);

            if (wm.getOverriding().containsKey(p.getUniqueId())) {
                wm.getOverriding().get(p.getUniqueId()).cancel();
                wm.getOverriding().remove(p.getUniqueId());
                p.removeMetadata("warpToOverride", main);

                MessageManager.sendMessage(p, "&7You have &ccancelled &7the override.");
            } else {
                MessageManager.sendMessage(p, "&7You are not overwriting any warps.");
            }
        }
    }
}
