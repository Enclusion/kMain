package code.breakmc.legacy.teams;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.MessageManager;
import com.breakmc.pure.Pure;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Team implements Listener {

    String name;
    List<UUID> managers;
    List<UUID> members;
    Location hq;
    Location rally;
    boolean friendlyFireEnabled;
    String password;
    int valorPoints = 0;
    HashMap<UUID, BukkitRunnable> dontMove = new HashMap<>();

    public Team(String name, List<UUID> managers, List<UUID> members, Location hq, Location rally, boolean friendlyFireEnabled, String password, int valorPoints) {
        this.name = name;
        this.managers = managers;
        this.members = members;
        this.hq = hq;
        this.rally = rally;
        this.friendlyFireEnabled = friendlyFireEnabled;
        this.password = password;
        this.valorPoints = valorPoints;

        Bukkit.getServer().getPluginManager().registerEvents(this, Legacy.getInstance());
    }

    public boolean isManager(UUID id) {
        return managers.contains(id);
    }

    public void sendMessage(String message) {
        for (UUID id : getManagers()) {
            MessageManager.sendMessage(id, message);
        }
        for (UUID id : getMembers()) {
            MessageManager.sendMessage(id, message);
        }
    }

    public List<Player> getOnlinePlayers() {
        List<Player> players = new ArrayList<>();
        for (UUID id : managers) {
            if (Bukkit.getPlayer(id) != null) {
                players.add(Bukkit.getPlayer(id));
            }
        }
        for (UUID id : members) {
            if (Bukkit.getPlayer(id) != null) {
                players.add(Bukkit.getPlayer(id));
            }
        }

        return players;
    }

    public void getExtraInformation(UUID id) {
        MessageManager.sendMessage(id, "&3&m***&r &b" + getName() + " &3&m***&r");
        MessageManager.sendMessage(id, "&3Valor Points&7: &b" + getValorPoints());
        MessageManager.sendMessage(id, "&3Password&7: " + (!getPassword().equalsIgnoreCase("") ? "&b" + getPassword() : "&cUnset"));
        MessageManager.sendMessage(id, "&3Team HQ&7: " + (getHq() != null ? "&aSet" : "&cUnset"));
        MessageManager.sendMessage(id, "&3Team Rally&7: " + (getRally() != null ? "&aSet" : "&cUnset"));
        MessageManager.sendMessage(id, "&3Friendly Fire&7: " + (!isFriendlyFireEnabled() ? "&aOff" : "&cOn"));
        MessageManager.sendMessage(id, "&3Members &7(" + (getMembers().size() + getManagers().size()) + "/30):");

        for (UUID ids : getManagers()) {
            if (Bukkit.getPlayer(ids) != null) {
                MessageManager.sendMessage(id, "&a\u2022 &b" + Bukkit.getPlayer(ids).getName() + formatHealth(Bukkit.getPlayer(ids).getHealth()));
            } else {
                MessageManager.sendMessage(id, "&c\u2022 &b" + Bukkit.getOfflinePlayer(ids).getName());
            }
        }

        for (UUID ids : getMembers()) {
            if (Bukkit.getPlayer(ids) != null) {
                MessageManager.sendMessage(id, "&a\u2022 &7" + Bukkit.getPlayer(ids).getName() + formatHealth(Bukkit.getPlayer(ids).getHealth()));
            } else {
                MessageManager.sendMessage(id, "&c\u2022 &7" + Bukkit.getOfflinePlayer(ids).getName());
            }
        }
    }

    public void getInformation(UUID id) {
        MessageManager.sendMessage(id, "&3&m***&r &b" + getName() + " &3&m***&r");
        MessageManager.sendMessage(id, "&3Valor Points&7: &b" + getValorPoints());
        MessageManager.sendMessage(id, "&3Members &7(" + (getMembers().size() + getManagers().size()) + "/30):");

        for (UUID ids : getManagers()) {
            if (Bukkit.getPlayer(ids) != null) {
                MessageManager.sendMessage(id, "&a\u2022 &b" + Bukkit.getPlayer(ids).getName() + formatHealth(Bukkit.getPlayer(ids).getHealth()));
            } else {
                MessageManager.sendMessage(id, "&c\u2022 &b" + Bukkit.getOfflinePlayer(ids).getName());
            }
        }

        for (UUID ids : getMembers()) {
            if (Bukkit.getPlayer(ids) != null) {
                MessageManager.sendMessage(id, "&a\u2022 &7" + Bukkit.getPlayer(ids).getName() + formatHealth(Bukkit.getPlayer(ids).getHealth()));
            } else {
                MessageManager.sendMessage(id, "&c\u2022 &7" + Bukkit.getOfflinePlayer(ids).getName());
            }
        }
    }

    String formatHealth(double health) {
        double hearts = health / 2;
        DecimalFormat format = new DecimalFormat("#");

        if (hearts <= 10 && hearts >= 7) {
            return String.format(" &a%s \u2764", format.format(hearts));
        } else if (hearts <= 7 && hearts >= 4) {
            return String.format(" &e%s \u2764", format.format(hearts));
        } else {
            return String.format(" &c%s \u2764", format.format(hearts));
        }
    }

    public void teleport(final Player p, final String locName) {
        if (locName.equalsIgnoreCase("hq")) {
            if (Legacy.getInstance().getSpawnManager().getSpawn().isInSpawnRadius(p.getLocation())) {
                MessageManager.sendMessage(p, "&cYou cannot warp within spawn");
                return;
            }

            if (hq == null) {
                MessageManager.sendMessage(p, "&cThe team's HQ is not set!");
                return;
            }

            if (canTeleport(p)) {
                p.teleport(hq);
                MessageManager.sendMessage(p, "&3You have teleported to the team's HQ!\n&7You can not attack for 5 seconds.");
                return;
            }

            if (dontMove.containsKey(p.getUniqueId())) {
                dontMove.get(p.getUniqueId()).cancel();
            }

            dontMove.put(p.getUniqueId(), new BukkitRunnable() {
                public void run() {
                    p.teleport(hq);
                    dontMove.remove(p.getUniqueId());
                    MessageManager.sendMessage(p, "&3You have teleported to the team's HQ!\n&7You can not attack for 5 seconds.");
                }
            });

            dontMove.get(p.getUniqueId()).runTaskLater(Legacy.getInstance(), 10 * 20);

            MessageManager.sendMessage(p, "&7Someone is nearby! Warping in 10 seconds, do not move!");
        }

        if (locName.equalsIgnoreCase("rally")) {
            if (Legacy.getInstance().getSpawnManager().hasSpawnProt(p.getUniqueId())) {
                MessageManager.sendMessage(p, "&cYou cannot warp within spawn");
                return;
            }

            if (rally == null) {
                MessageManager.sendMessage(p, "&cThe team's rally point is not set!");
                return;
            }

            if (canTeleport(p)) {
                p.teleport(rally);
                MessageManager.sendMessage(p, "&3You have teleported to the team's rally point!\n&7You can not attack for 5 seconds.");
                return;
            }

            if (dontMove.containsKey(p.getUniqueId())) {
                dontMove.get(p.getUniqueId()).cancel();
            }

            dontMove.put(p.getUniqueId(), new BukkitRunnable() {
                public void run() {
                    p.teleport(rally);
                    dontMove.remove(p.getUniqueId());
                    MessageManager.sendMessage(p, "&3You have teleported to the team's rally point!\n&7You can not attack for 5 seconds.");
                }
            });

            dontMove.get(p.getUniqueId()).runTaskLater(Legacy.getInstance(), 10 * 20);

            MessageManager.sendMessage(p, "&7Someone is nearby! Warping in 10 seconds, do not move!");
        }
    }

    public boolean canTeleport(Player p) {
        TeamManager tm = Legacy.getInstance().getTeamManager();
        boolean canTeleport = true;
        for (Entity ent : p.getNearbyEntities(40, 20, 40)) {
            if (ent instanceof Player) {
                Player near = (Player) ent;

                if (near.equals(p)) continue;

                if (Pure.getInstance().getPunishmentManager().isVanished(near)) continue;

                if (tm.getTeam(near.getUniqueId()) != null) {
                    if (!this.equals(tm.getTeam(near.getUniqueId()))) {
                        canTeleport = false;
                    }
                } else {
                    canTeleport = false;
                }
            }
        }
        return canTeleport;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (dontMove.containsKey(e.getPlayer().getUniqueId())) {
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                dontMove.get(e.getPlayer().getUniqueId()).cancel();
                dontMove.remove(e.getPlayer().getUniqueId());
                MessageManager.sendMessage(e.getPlayer(), "&cYou moved! Teleportation cancelled.");
            }
        }
    }
}
