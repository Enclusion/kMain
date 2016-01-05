package code.breakmc.legacy.utils.moon.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class Scoreboard {

    private Player player;
    private String title;
    private org.bukkit.scoreboard.Scoreboard scoreboard;
    private Objective objective;
    private HashMap<Entry, String> entries;
    private HashMap<Entry, Integer> scores;
    private boolean hidden;

    public Scoreboard(Player player, String title) {
        this.player = player;
        this.title = title;
        this.hidden = false;
        this.entries = new HashMap<>();
        this.scores = new HashMap<>();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective(player.getName().toLowerCase(), "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(title);
    }

    public int getScore(Entry entry) {
        for (int scoreint = 0; scoreint < entries.size() + 1; scoreint++) {
            if (scores.containsKey(entry)) {
                if (scores.get(entry) == scoreint + 1) {
                    if (!scores.values().contains(scoreint) && scoreint != 0) {
                        scores.put(entry, scoreint);
                        return scoreint;
                    }

                    return scoreint + 1;
                }
            } else {
                if (!scores.isEmpty()) {
                    if (!scores.values().contains(scoreint + 1)) {
                        scores.put(entry, scoreint + 1);
                        return scoreint + 1;
                    }
                } else {
                    scores.put(entry, scoreint + 1);
                    return scoreint + 1;
                }
            }
        }

        return 0;
    }

    public void remove(Entry entry) {
        if (entries.containsKey(entry)) {
            scoreboard.resetScores(entries.get(entry));
            scores.remove(entry);
        }
    }


    public void show(Entry entry) {
        if (entry.isFinished()) {
            return;
        }

        if (hidden) {
            return;
        }

        boolean reset = false;
        String toreset = null;

        if (entries.containsKey(entry)) {
            reset = true;
            toreset = entries.get(entry);
        }

        String text;
        String prefix = null;
        String suffix;

        if (entry.getText().length() > 16) {
            prefix = entry.getText().substring(0, 15);

            if (entry.getText().length() > 32) {
                if (entry.getText().length() > 48) {
                    text = " must be under";
                    prefix = "Error, ";
                    suffix = " 48 characters";
                } else {
                    text = entry.getText().substring(15, 31);
                    suffix = entry.getText().substring(31, entry.getText().length());
                }
            } else {
                text = entry.getText().substring(15, entry.getText().length());
                suffix = "        ";
            }
        } else {
            text = entry.getText();
            suffix = "        ";
        }

        entries.put(entry, text);

        if (entries.containsKey(entry)) {
            Team team = entry.getTeam();

            if (team != null && scoreboard.getTeams().contains(team)) {
                team.addEntry(text);

                if (prefix != null) {
                    team.setPrefix(prefix);
                }

                team.setSuffix(suffix);

                Score score = objective.getScore(text);
                score.setScore(getScore(entry));

                if (reset && toreset != null) {
                    scoreboard.resetScores(toreset);
                }

                player.setScoreboard(scoreboard);
            }
        }
    }

    public HashMap<Entry, Integer> getScores() {
        return scores;
    }

    public Entry getEntry(String id) {
        for (Entry entry : getEntries().keySet()) {
            if (entry.getID().equalsIgnoreCase(id)) {
                if (!entry.isFinished()) {
                    return entry;
                }
            }
        }

        return null;
    }

    public org.bukkit.scoreboard.Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getObjective() {
        return objective;
    }

    public String getTitle() {
        return title;
    }

    public Player getPlayer() {
        return player;
    }

    public HashMap<Entry, String> getEntries() {
        return entries;
    }

    public boolean isHidden() {
        return hidden;
    }
}
