package code.breakmc.legacy.duels;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.spawn.SpawnManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.PlayerUtility;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DuelMatch {

    private DuelManager dm = Legacy.getInstance().getDuelManager();
    private SpawnManager sm = Legacy.getInstance().getSpawnManager();
    private Player p1;
    private Player p2;
    private Player winner;
    private int preCountdown = 10;
    private int postCountdown = 20;
    private boolean started;
    private boolean ended;
    private List<Entity> loot = new ArrayList<>();
    private List<Entity> drops = new ArrayList<>();

    public DuelMatch(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.started = false;

        startCountdown();
    }

    public void startCountdown() {
        dm.handleStartVisibility(this);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (p1 == null || p2 == null) {
                    if (p1 != null)
                        MessageManager.sendMessage(p1, "&cThe 1v1 has been cancelled.");
                    if (p2 != null)
                        MessageManager.sendMessage(p2, "&cThe 1v1 has been cancelled.");

                    cancel();
                }

                if (ended) {
                    if (p1 != null)
                        MessageManager.sendMessage(p1, "&cThe 1v1 has been cancelled.");
                    if (p2 != null)
                        MessageManager.sendMessage(p2, "&cThe 1v1 has been cancelled.");

                    cancel();
                }

                if (preCountdown == 0) {
                    MessageManager.sendMessage(p1, "&6Fight!");
                    MessageManager.sendMessage(p2, "&6Fight!");
                    started = true;
                    sm.getSpawnProtected().remove(p1.getUniqueId());
                    sm.getSpawnProtected().remove(p2.getUniqueId());
                    cancel();
                }

                if (preCountdown > 0) {
                    MessageManager.sendMessage(p1, "&61v1 commencing in &e" + preCountdown + " &6seconds.");
                    MessageManager.sendMessage(p2, "&61v1 commencing in &e" + preCountdown + " &6seconds.");
                    preCountdown--;
                }
            }
        }.runTaskTimer(Legacy.getInstance(), 0L, 20L);
    }

    public void endDuel(DuelMatch d) {
        d.setStarted(false);
        d.setEnded(true);

        dm.handleFinishVisibility(this);

        if (p1 != null && p1 == winner) {
            MessageManager.sendMessage(winner, "&6You have &awon &6the 1v1 against &e" + p2.getName() + "&6!");
            MessageManager.sendMessage(p2, "&6You have &clost &6the 1v1 against &e" + winner.getName() + "&6!");
        }

        if (p2 != null && p2 == winner) {
            MessageManager.sendMessage(winner, "&6You have &awon &6the 1v1 against &e" + p1.getName() + "&6!");
            MessageManager.sendMessage(p1, "&6You have &clost &6the 1v1 against &e" + winner.getName() + "&6!");
        }

        MessageManager.sendMessage(winner, "&cYou have " + postCountdown + " seconds of invisiblity! Warp home!");

        new BukkitRunnable() {
            public void run() {
                if (postCountdown > 0) {
                    postCountdown--;
                    MessageManager.sendMessage(winner, "&c" + postCountdown + "..");
                }

                if (postCountdown == 1) {
                    dm.getDuels().remove(d);
                    MessageManager.sendMessage(winner, "&cYour invisibility has expired, you may now be attacked!");

                    for (Player all : PlayerUtility.getOnlinePlayers()) {
                        if (!all.canSee(winner)) {
                            all.showPlayer(winner);
                        }

                        if (!winner.canSee(all)) {
                            winner.showPlayer(all);
                        }
                    }

                    cancel();
                }
            }
        }.runTaskTimer(Legacy.getInstance(), 0L, 20L);
    }

    public void hideLoot() {
        for (Player all : PlayerUtility.getOnlinePlayers()) {
            if (all != winner) {
                for (Entity ent : loot) {
                    dm.getEntityHider().hideEntity(all, ent);
                }
            }
        }
    }

    public void hideDrops() {
        for (Player all : PlayerUtility.getOnlinePlayers()) {
            if (all != p1 && all != p2) {
                for (Entity ent : drops) {
                    dm.getEntityHider().hideEntity(all, ent);
                }
            }
        }
    }

    public List<Player> getPlayers() {
        return Arrays.asList(p1, p2);
    }

    public boolean isWinner(Player p) { return winner.getUniqueId() == p.getUniqueId(); }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean hasEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public List<Entity> getLoot() {
        return loot;
    }

    public List<Entity> getDrops() {
        return drops;
    }
}
