package code.breakmc.legacy.utils.moon.objects;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.moon.events.EntryCancelEvent;
import code.breakmc.legacy.utils.moon.events.EntryFinishEvent;
import code.breakmc.legacy.utils.moon.events.EntryTickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Entry {

    private Scoreboard scoreboard;
    private String ID;
    private String text;
    private boolean useMinutes;
    private boolean countdown;
    private Team team;
    private BigDecimal time;
    private boolean finished;
    private boolean update;

    private int minutes;
    private int seconds;

    public Entry(Scoreboard scoreboard, String ID) {
        this.scoreboard = scoreboard;
        this.ID = ID;
        this.time = BigDecimal.valueOf(0);
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public Entry setSeconds(int seconds) {
        this.seconds = seconds;
        return this;
    }

    public Entry setMinutes(int minutes) {
        this.minutes = minutes;
        return this;
    }

    public Entry setup() {
        if (scoreboard.getScoreboard().getTeam(ID) == null) {
            team = scoreboard.getScoreboard().registerNewTeam(ID);
        } else {
            scoreboard.getScoreboard().getTeam(ID).unregister();
            team = scoreboard.getScoreboard().registerNewTeam(ID);
        }

        return this;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Entry setTime(double decimal) {
        time = BigDecimal.valueOf(decimal);
        return this;
    }

    public BigDecimal getTime() {
        return time;
    }

    public Entry setText(String text) {
        this.text = ChatColor.translateAlternateColorCodes('&', text);
        return this;
    }

    public Entry setUpdate(boolean update) {
        this.update = update;
        return this;
    }

    public String getID() {
        return ID;
    }

    public Entry setMinutes(boolean b) {
        useMinutes = b;
        return this;
    }

    public Entry setCountdown(boolean b) {
        this.countdown = b;
        return this;
    }

    public void send() {
        if (scoreboard.getScoreboard().getTeam(ID) != null) {
            scoreboard.getScoreboard().getTeam(ID).unregister();
        }

        team = scoreboard.getScoreboard().registerNewTeam(ID);

        if (!countdown) {
            scoreboard.show(this);
        } else {
            final Entry entry = this;

            if (update) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!update) {
                            this.cancel();
                            return;
                        }

                        setText(text);
                        scoreboard.show(entry);
                    }
                }.runTaskTimerAsynchronously(Legacy.getInstance(), 0L, 15L);
            }

            if (!useMinutes) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (finished) {
                            this.cancel();
                            return;
                        }

                        String text = getText();
                        setText(text + time + "s");
                        scoreboard.show(entry);
                        setText(text);
                        setTime(time.subtract(BigDecimal.valueOf(0.1)).doubleValue());

                        if (time.equals(BigDecimal.valueOf(0.0)) && !finished) {
                            finished = true;
                            this.cancel();
                            team.unregister();
                            scoreboard.remove(entry);
                            Bukkit.getPluginManager().callEvent(new EntryFinishEvent(entry, scoreboard));
                            Bukkit.getPluginManager().callEvent(new EntryTickEvent(entry, scoreboard));
                        }
                    }
                }.runTaskTimer(Legacy.getInstance(), 0L, 2);
            } else {
                for (int x = 0; x < minutes * 60 + 1; x++) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (finished) {
                                this.cancel();
                                return;
                            }

                            final DecimalFormat formatter = new DecimalFormat("00");
                            String sString = formatter.format(seconds);
                            String text = getText();
                            setText(text + minutes + ":" + sString);

                            if (seconds == -1) {
                                minutes -= 1;
                                seconds = 59;
                                setText(text + minutes + ":59");
                            }

                            scoreboard.show(entry);
                            setText(text);

                            seconds -= 1;
                            Bukkit.getPluginManager().callEvent(new EntryTickEvent(entry, scoreboard));

                            if (minutes == 0 && seconds == 0) {
                                finished = true;
                                scoreboard.remove(entry);
                                team.unregister();
                                Bukkit.getPluginManager().callEvent(new EntryFinishEvent(entry, scoreboard));
                            }
                        }
                    }.runTaskLater(Legacy.getInstance(), 20L * x);
                }
            }
        }
    }

    public void cancel() {
        scoreboard.remove(this);
        finished = true;
        Bukkit.getPluginManager().callEvent(new EntryCancelEvent(this, scoreboard));
    }

    public boolean isFinished() {
        return finished;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public String getText() {
        return text;
    }

    public boolean isUsingMinutes() {
        return useMinutes;
    }

    public boolean isCountdown() {
        return countdown;
    }
}
