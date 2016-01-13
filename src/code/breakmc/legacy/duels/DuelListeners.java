package code.breakmc.legacy.duels;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.PlayerUtility;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class DuelListeners implements Listener {

    private DuelManager dm = Legacy.getInstance().getDuelManager();

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (dm.isInDuel(e.getPlayer())) {
            MessageManager.sendMessage(e.getPlayer(), "&cYou can not break blocks in a 1v1!");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (dm.isInDuel(e.getPlayer())) {
            MessageManager.sendMessage(e.getPlayer(), "&cYou can not place blocks in a 1v1!");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();

        if (!e.getMessage().toLowerCase().startsWith("/msg")
                && !e.getMessage().toLowerCase().startsWith("/message")
                && !e.getMessage().toLowerCase().startsWith("/tell")
                && !e.getMessage().toLowerCase().startsWith("/w")
                && !e.getMessage().toLowerCase().startsWith("/whisper")
                && !e.getMessage().toLowerCase().startsWith("/report")
                && !e.getMessage().toLowerCase().startsWith("/help")
                && !e.getMessage().toLowerCase().startsWith("/reply")
                && !e.getMessage().toLowerCase().startsWith("/r")) {
            if (dm.isInDuel(p) && !dm.getDuel(p).hasEnded()) {
                e.setCancelled(true);
                MessageManager.sendMessage(p, "&cYou cannot use this command in a 1v1!");
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player killed = e.getEntity();

        if (dm.isInDuel(killed)) {
            DuelMatch d = dm.getDuel(killed);

            if (killed == d.getP1()) {
                d.setWinner(d.getP2());
            } else {
                d.setWinner(d.getP1());
            }

            d.endDuel(d);

            for (ItemStack i : e.getDrops()) {
                Entity ent = killed.getLocation().getWorld().dropItemNaturally(killed.getLocation(), i);
                d.getLoot().add(ent);
                d.hideLoot();
            }

            e.getDrops().clear();

            return;
        }

        for (ItemStack i : e.getDrops()) {
            Entity ent = killed.getLocation().getWorld().dropItemNaturally(killed.getLocation(), i);

            for (Player all : PlayerUtility.getOnlinePlayers()) {
                if (dm.isInDuel(all)) {
                    if (dm.getDuel(all) != dm.getDuel(killed)) {
                        dm.getEntityHider().hideEntity(all, ent);
                    }
                }
            }
        }

        e.getDrops().clear();
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();

        if (dm.isInDuel(p)) {
            DuelMatch d = dm.getDuel(p);

            d.getDrops().add(e.getItemDrop());
            d.hideDrops();
        } else {
            for (Player all : PlayerUtility.getOnlinePlayers()) {
                if (dm.isInDuel(all) && dm.getDuel(all) != dm.getDuel(p)) {
                    dm.getEntityHider().hideEntity(all, e.getItemDrop());
                }
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();

        if (!dm.getEntityHider().canSee(p, e.getItem())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!p.hasPermission("legacy.duelbypass")) {
            for (Player all : PlayerUtility.getOnlinePlayers()) {
                if (dm.isInDuel(all)) {
                    DuelMatch d = dm.getDuel(all);

                    if (d.isStarted() && !d.hasEnded()) {
                        dm.handleStartVisibility(d);
                    } else {
                        dm.handleFinishVisibility(d);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        Profile prof = Legacy.getInstance().getProfileManager().getProfile(p.getUniqueId());

        if (dm.isInDuel(p)) {
            prof.setSafeLogged(true);
            p.setHealth(0);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player hit = (Player) e.getEntity();
            Player hitter = (Player) e.getDamager();

            if (dm.isInDuel(hit) && dm.isInDuel(hitter)) {
                if (dm.getDuel(hit) == dm.getDuel(hitter)) {
                    if (!dm.getDuel(hitter).isStarted()) {
                        e.setCancelled(true);
                        MessageManager.sendMessage(hitter, "&cYou cannot attack until the 1v1 has started!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (e.getTo().getBlockX() != e.getFrom().getBlockX() || e.getTo().getBlockY() != e.getFrom().getBlockY() || e.getTo().getBlockZ() != e.getFrom().getBlockZ()) {
            if (!Legacy.getInstance().getSpawnManager().getSpawn().isInStoneRadius(e.getTo()) && dm.isInDuel(p)) {
                e.setTo(e.getFrom());
                MessageManager.sendMessage(p, "&cYou cannot leave spawn while in a 1v1.");
            }
        }
    }
}
