package code.breakmc.legacy.utils.moon;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.kits.Kit;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.utils.DateUtil;
import code.breakmc.legacy.utils.PlayerUtility;
import code.breakmc.legacy.utils.moon.objects.Entry;
import code.breakmc.legacy.utils.moon.objects.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class ScoreboardManager {

    public ScoreboardManager() {
        registerAll();
    }

    private HashSet<Scoreboard> scoreboards = new HashSet<>();
    private TeamManager tm = Legacy.getInstance().getTeamManager();
    private ProfileManager pm = Legacy.getInstance().getProfileManager();

    public void createScoreboard(Player p, String title) {
        if (getScoreboard(p) == null) {
            Scoreboard scoreboard = new Scoreboard(p, title);
            scoreboards.add(scoreboard);
        }
    }

    public void setupScoreboard(Profile profile) {
        Player p = Bukkit.getPlayer(profile.getUniqueId());

        if (p != null) {
            createScoreboard(p, ChatColor.translateAlternateColorCodes('&', Legacy.getInstance().getConfig().getString("scoreboard.title")));

            Scoreboard board = getScoreboard(p);

            if (tm.hasTeam(p.getUniqueId())) {
                Team team = tm.getTeam(p.getUniqueId());

                Entry entry1 = new Entry(board, "team")
                        .setText("&3Team&7: &b" + tm.getTeam(p.getUniqueId()).getName())
                        .setup();
                entry1.send();

                Entry entry2 = new Entry(board, "valor")
                        .setText("&3Valor Points&7: &b" + tm.getTeam(p.getUniqueId()).getValorPoints())
                        .setup();
                entry2.send();

                Entry entry4 = new Entry(board, "members")
                        .setText("&3Team Members&7: &b" + (team.getMembers().size() + team.getManagers().size()) + "&7/&b20")
                        .setup();
                entry4.send();
            }

            Entry entry3 = new Entry(board, "balance")
                    .setText("&3Balance&7: &b" + profile.getBalance())
                    .setup();
            entry3.send();

            Kit k = null;

            for (Kit ks : Legacy.getInstance().getKitManager().getKits()) {
                if (ks.getName().equalsIgnoreCase("PvP")) {
                    k = ks;
                }
            }

            if (k != null && k.getNextUse(profile) > 0L) {
                Entry entry5 = new Entry(board, "cooldown")
                        .setText("&3Kit &bPvP &3cooldown&7: &b" + DateUtil.formatSimplifiedDateDiff(k.getNextUse(profile)))
                        .setup();
                entry5.send();
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    updateScoreboard(profile);
                }
            }.runTaskTimerAsynchronously(Legacy.getInstance(), 0L, 20L);
        }
    }

    public void updateScoreboard(Profile profile) {
        Player p = Bukkit.getPlayer(profile.getUniqueId());

        if (p != null) {
            Scoreboard board = getScoreboard(p);

            if (board != null) {
                for (Entry entry : board.getEntries().keySet()) {
                    if (tm.hasTeam(profile.getUniqueId())) {
                        Team team = tm.getTeam(profile.getUniqueId());

                        if (entry.getID().contains("team")) {
                            entry.setText("&3Team&7: &b" + tm.getTeam(p.getUniqueId()).getName());
                            entry.setUpdate(true);
                        } else {
                            Entry entry1 = new Entry(board, "team")
                                    .setText("&3Team&7: &b" + tm.getTeam(p.getUniqueId()).getName())
                                    .setup();
                            entry1.send();
                        }

                        if (entry.getID().contains("valor")) {
                            entry.setText("&3Valor Points&7: &b" + tm.getTeam(p.getUniqueId()).getValorPoints());
                            entry.setUpdate(true);
                        } else {
                            Entry entry2 = new Entry(board, "valor")
                                    .setText("&3Valor Points&7: &b" + tm.getTeam(p.getUniqueId()).getValorPoints())
                                    .setup();
                            entry2.send();
                        }

                        if (entry.getID().contains("members")) {
                            entry.setText("&3Team Members&7: &b" + (team.getMembers().size() + team.getManagers().size()) + "&7/&b20");
                            entry.setUpdate(true);
                        } else {
                            Entry entry4 = new Entry(board, "members")
                                    .setText("&3Team Members&7: &b" + (team.getMembers().size() + team.getManagers().size()) + "&7/&b20")
                                    .setup();
                            entry4.send();
                        }
                    } else {
                        if (entry.getID().contains("team")) {
                            entry.cancel();
                        }

                        if (entry.getID().contains("valor")) {
                            entry.cancel();
                        }

                        if (entry.getID().contains("members")) {
                            entry.cancel();
                        }
                    }

                    if (entry.getID().contains("balance")) {
                        entry.setText("&3Balance&7: &b" + profile.getBalance());
                        entry.setUpdate(true);
                    } else {
                        Entry entry3 = new Entry(board, "balance")
                                .setText("&3Balance&7: &b" + profile.getBalance())
                                .setup();
                        entry3.send();
                    }

                    Kit k = null;

                    for (Kit ks : Legacy.getInstance().getKitManager().getKits()) {
                        if (ks.getName().equalsIgnoreCase("PvP")) {
                            k = ks;
                        }
                    }

                    if (entry.getID().contains("cooldown")) {
                        if (k != null && k.getNextUse(profile) > 0L) {
                            entry.setText("&3Kit &bPvP &3cooldown&7: &b" + DateUtil.formatSimplifiedDateDiff(k.getNextUse(profile)));
                            entry.setUpdate(true);
                        } else {
                            entry.cancel();
                        }
                    } else {
                        if (k != null && k.getNextUse(profile) > 0L) {
                            Entry entry5 = new Entry(board, "cooldown")
                                    .setText("&3Kit &bPvP &3cooldown&7: &b" + DateUtil.formatSimplifiedDateDiff(k.getNextUse(profile)))
                                    .setup();
                            entry5.send();
                        }
                    }
                }
            }
        }
    }

    public Scoreboard getScoreboard(Player p) {
        for (Scoreboard scoreboard : getScoreboards()) {
            if (scoreboard.getPlayer().getUniqueId() == p.getUniqueId()) {
                return scoreboard;
            }
        }

        return null;
    }

    public void registerAll() {
        for (Player player : PlayerUtility.getOnlinePlayers()) {
            createScoreboard(player, Legacy.getInstance().getConfig().getString("scoreboard.title"));
        }
    }

    public HashSet<Scoreboard> getScoreboards() {
        return scoreboards;
    }
}
