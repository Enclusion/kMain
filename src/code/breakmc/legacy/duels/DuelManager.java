package code.breakmc.legacy.duels;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.EntityHider;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.PlayerUtility;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class DuelManager {

    private Legacy main = Legacy.getInstance();
    private EntityHider entityHider;
    private HashSet<DuelMatch> duels = new HashSet<>();
    private HashMap<UUID, UUID> duelRequests = new HashMap<>();
    private ArrayList<UUID> notAcceptingRequests = new ArrayList<>();

    public DuelManager() {
        entityHider = new EntityHider(main, EntityHider.Policy.BLACKLIST);
    }

    public void createDuel(Player p1, Player p2) {
        MessageManager.sendMessage(p2, "&e" + p1.getName() + " &6has accepted the 1v1.");

        DuelMatch d = new DuelMatch(p1, p2);
        duels.add(d);

        duelRequests.remove(p1.getUniqueId());
        duelRequests.remove(p2.getUniqueId());
    }

    public void requestDuel(Player requester, Player requested) {
        if (duelRequests.containsKey(requester.getUniqueId())) {
            if (duelRequests.get(requester.getUniqueId()).equals(requested.getUniqueId())) {
                MessageManager.sendMessage(requester, "&cYou have already requested a 1v1 with " + requested.getName());
                return;
            }
        }

        duelRequests.put(requester.getUniqueId(), requested.getUniqueId());

        MessageManager.sendMessage(requester, "&6Your 1v1 request has been sent.");

        FancyMessage fm = new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&e" + requester.getName() + " &6has requested a 1v1! &6Click "))
                .then(ChatColor.YELLOW + "here").tooltip(ChatColor.GREEN + "Accept now!").command("/accept " + requester.getName())
                .then(ChatColor.GOLD + " to accept.");
        fm.send(requested);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (duelRequests.containsKey(requester.getUniqueId())) {
                    if (duelRequests.get(requester.getUniqueId()).equals(requested.getUniqueId())) {
                        if (!isInDuel(requested) && !isInDuel(requester)) {
                            duelRequests.remove(requester.getUniqueId());
                            MessageManager.sendMessage(requester, "&e" + requested.getName() + " &6has denied your 1v1 request.");
                        } else {
                            if (isInDuel(requested) || isInDuel(requester)) {
                                if (getDuel(requested) != getDuel(requester)) {
                                    duelRequests.remove(requester.getUniqueId());
                                    MessageManager.sendMessage(requester, "&e" + requested.getName() + " &6has denied your 1v1 request.");
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskLater(main, 15*20L);
    }

    public void handleStartVisibility(DuelMatch d) {
        for (Player p : d.getPlayers()) {
            for (Player all : PlayerUtility.getOnlinePlayers()) {
                if (!d.getPlayers().contains(all)) {
                    p.hidePlayer(all);

                    if (!all.hasPermission("legacy.duelbypass")) {
                        all.hidePlayer(p);
                    }
                }
            }
        }
    }

    public void handleFinishVisibility(DuelMatch d) {
        for (Player p : d.getPlayers()) {
            if (d.isWinner(p)) {
                for (Player all : PlayerUtility.getOnlinePlayers()) {
                    p.hidePlayer(all);

                    if (!all.hasPermission("legacy.duelbypass")) {
                        all.hidePlayer(p);
                    }
                }
            } else {
                for (Player all : PlayerUtility.getOnlinePlayers()) {
                    if (!d.isWinner(all) && !isInDuel(all)) {
                        all.showPlayer(p);
                        p.showPlayer(all);
                    }
                }
            }
        }
    }

    public DuelMatch getDuel(Player p) {
        for (DuelMatch d : duels) {
            if (d.getPlayers().contains(p)) {
                return d;
            }
        }

        return null;
    }

    public boolean isInDuel(Player p) {
        for (DuelMatch d : duels) {
            if (d.getPlayers().contains(p)) {
                return true;
            }
        }

        return false;
    }

    public HashSet<DuelMatch> getDuels() {
        return duels;
    }

    public HashMap<UUID, UUID> getDuelRequests() {
        return duelRequests;
    }

    public ArrayList<UUID> getNotAcceptingRequests() {
        return notAcceptingRequests;
    }

    public EntityHider getEntityHider() {
        return entityHider;
    }
}
