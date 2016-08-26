package com.mccritz.kmain.utils;

import com.mccritz.kmain.commands.HudCommand;
import com.mccritz.kmain.events.EventManager;
import com.mccritz.kmain.events.end.EndEvent;
import com.mccritz.kmain.kMain;
import com.mccritz.kmain.spawn.SpawnManager;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.glaedr.scoreboards.Entry;
import com.mccritz.kmain.utils.glaedr.scoreboards.PlayerScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PlayerUtility {

    private static TeamManager tm = kMain.getInstance().getTeamManager();
    private static SpawnManager sm = kMain.getInstance().getSpawnManager();
    private static EventManager em = kMain.getInstance().getEventManager();

    public static double getHealth(Player p) {
        return p.getHealth();
    }

    public static double getMaxHealth(Player p) {
        return p.getMaxHealth();
    }

    public static void setHealth(Player p, double health) {
        p.setHealth(health);
    }

    public static double getDamage(EntityDamageByEntityEvent e) {
        return e.getDamage();
    }

    public static Player[] getOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }

    public static boolean hasInventorySpace(Inventory inventory, ItemStack is) {
        Inventory inv = Bukkit.createInventory(null, inventory.getSize());

        for (int i = 0; i < inv.getSize(); i++) {
            if (inventory.getItem(i) != null) {
                ItemStack item = inventory.getItem(i).clone();
                inv.setItem(i, item);
            }
        }

        return inv.addItem(new ItemStack[]{is.clone()}).size() <= 0;

    }

    public static int checkSlotsAvailable(Inventory inv) {
        ItemStack[] items = inv.getContents();
        int emptySlots = 0;

        for (ItemStack is : items) {
            if (is == null) {
                emptySlots = emptySlots + 1;
            }
        }

        return emptySlots;
    }

    public static List<String> toList(Player[] array) {
        List<String> list = new ArrayList<>();

        for (Player t : array) {
            list.add(t.getName());
        }

        return list;
    }

    public static void updateScoreboard(Player p) {
        PlayerScoreboard scoreboard = PlayerScoreboard.getScoreboard(p);
        EndEvent endEvent = kMain.getInstance().getEventManager().getEndEvent();

        if (HudCommand.getDisplayList().contains(p.getUniqueId())) {
            if (scoreboard != null) {
                if (scoreboard.getEntry("spacer1") != null) {
                    scoreboard.getEntry("spacer1")
                            .setText("&7&m--------------------")
                            .send();
                } else {
                    new Entry("spacer1", scoreboard)
                            .setText("&7&m--------------------")
                            .send();
                }

                if (scoreboard.getEntry("spawn") != null) {
                    scoreboard.getEntry("spawn")
                            .setText("&bSpawn Protection: " + (sm.hasSpawnProt(p.getUniqueId()) ? "&aYes" : "&cNo"))
                            .send();
                } else {
                    new Entry("spawn", scoreboard)
                            .setText("&bSpawn Protection: " + (sm.hasSpawnProt(p.getUniqueId()) ? "&aYes" : "&cNo"))
                            .send();
                }

                if (scoreboard.getEntry("gold") != null) {
                    scoreboard.getEntry("gold")
                            .setText("&bGold: &6" + MessageManager.formatDouble(kMain.getInstance().getProfileManager().getProfile(p.getUniqueId()).getGold()))
                            .send();
                } else {
                    new Entry("gold", scoreboard)
                            .setText("&bGold: &6" + MessageManager.formatDouble(kMain.getInstance().getProfileManager().getProfile(p.getUniqueId()).getGold()))
                            .send();
                }

                if (endEvent != null && endEvent.isStarted() && p.getWorld().getEnvironment() == World.Environment.THE_END) {
                    if (endEvent.getGameTime() <= 660 && endEvent.getGameTime() > 360) {
                        if (scoreboard.getEntry("endevent") != null) {
                            scoreboard.getEntry("endevent")
                                    .setText("&bEnd Exit: &5" + DateUtil.readableTime(BigDecimal.valueOf(endEvent.getEndExitTime())))
                                    .send();
                        } else {
                            new Entry("endevent", scoreboard)
                                    .setText("&bEnd Exit: &5" + DateUtil.readableTime(BigDecimal.valueOf(endEvent.getEndExitTime())))
                                    .send();
                        }
                    } else if (endEvent.getGameTime() <= 360 && endEvent.getGameTime() > 240) {
                        if (scoreboard.getEntry("endevent") != null) {
                            scoreboard.getEntry("endevent")
                                    .setText("&bChest Refill: &5" + DateUtil.readableTime(BigDecimal.valueOf(endEvent.getChestRefillTime())))
                                    .send();
                        } else {
                            new Entry("endevent", scoreboard)
                                    .setText("&bChest Refill: &5" + DateUtil.readableTime(BigDecimal.valueOf(endEvent.getChestRefillTime())))
                                    .send();
                        }
                    } else if (endEvent.getGameTime() <= 60 && endEvent.getGameTime() > 0) {
                        if (scoreboard.getEntry("endevent") != null) {
                            scoreboard.getEntry("endevent")
                                    .setText("&bEnd Over: &5" + DateUtil.readableTime(BigDecimal.valueOf(endEvent.getEndOverTime())))
                                    .send();
                        } else {
                            new Entry("endevent", scoreboard)
                                    .setText("&bEnd Over: &5" + DateUtil.readableTime(BigDecimal.valueOf(endEvent.getEndOverTime())))
                                    .send();
                        }
                    } else {
                        if (scoreboard.getEntry("endevent") != null) {
                            scoreboard.getEntry("endevent").cancel();
                        }
                    }
                } else {
                    if (scoreboard.getEntry("endevent") != null) {
                        scoreboard.getEntry("endevent").cancel();
                    }
                }
//
//                if (p.getWorld().getEnvironment() == World.Environment.NORMAL || p.getWorld().getEnvironment() == World.Environment.NETHER) {
//                    Bukkit.broadcastMessage(Cooldowns.getCooldown(p.getUniqueId(), "deathban") + "");
//                    if (Cooldowns.getCooldown(p.getUniqueId(), "deathban") > 0) {
//                        if (scoreboard.getEntry("endevent") != null) {
//                            scoreboard.getEntry("endevent")
//                                    .setText("&bDeathban: &5" + DateUtil.readableTime(BigDecimal.valueOf(Cooldowns.getCooldown(p.getUniqueId(), "deathban"))))
//                                    .send();
//                        } else {
//                           new Entry("endevent", scoreboard)
//                                   .setText("&bDeathban: &5" + DateUtil.readableTime(BigDecimal.valueOf(Cooldowns.getCooldown(p.getUniqueId(), "deathban"))))
//                                   .send();
//                        }
//                    } else {
//                        if (scoreboard.getEntry("endevent") != null) {
//                            scoreboard.getEntry("endevent").cancel();
//                        }
//                    }
//                }

                if (scoreboard.getEntry("team") != null) {
                    scoreboard.getEntry("team")
                            .setText("&bTeam: &c" + (tm.hasTeam(p.getUniqueId()) ? tm.getTeam(p.getUniqueId()).getName() : "&cNone."))
                            .send();
                } else {
                    new Entry("team", scoreboard)
                            .setText("&bTeam: &c" + (tm.hasTeam(p.getUniqueId()) ? tm.getTeam(p.getUniqueId()).getName() : "&cNone."))
                            .send();
                }

                if (scoreboard.getEntry("spacer2") != null) {
                    scoreboard.getEntry("spacer2")
                            .setText("&7&m--------------------")
                            .send();
                } else {
                    new Entry("spacer2", scoreboard)
                            .setText("&7&m--------------------")
                            .send();
                }
            }
        } else {
            if (scoreboard != null) {
                scoreboard.getEntries().clear();
            }
        }
    }
}
