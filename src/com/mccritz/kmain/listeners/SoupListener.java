package com.mccritz.kmain.listeners;

import com.mccritz.kmain.utils.PlayerUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SoupListener implements Listener {

    @EventHandler
    public void onSoup(PlayerInteractEvent e) {
        Action action = e.getAction();
        Player p = e.getPlayer();
        if (action.name().contains("RIGHT")) {
            if (PlayerUtility.getHealth(p) < PlayerUtility.getMaxHealth(p)) {
                if (e.getItem() != null && e.getItem().getType() == Material.MUSHROOM_SOUP) {
                    e.setCancelled(true);
                    p.setHealth(PlayerUtility.getHealth(p) + 7 > PlayerUtility.getMaxHealth(p) ? PlayerUtility.getMaxHealth(p) : PlayerUtility.getHealth(p) + 7);
                    e.getItem().setType(Material.BOWL);
                }
            } else {
                if (p.getFoodLevel() < 20) {
                    if (e.getItem() != null && e.getItem().getType() == Material.MUSHROOM_SOUP) {
                        e.setCancelled(true);
                        p.setFoodLevel(p.getFoodLevel() + 7 > 20 ? 20 : p.getFoodLevel() + 7);
                        e.getItem().setType(Material.BOWL);
                    }
                }
            }
        }
    }
}
