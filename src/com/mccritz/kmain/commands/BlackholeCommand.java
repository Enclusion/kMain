package com.mccritz.kmain.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.BlockUtils;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BlackholeCommand extends BaseCommand {

    public ArrayList<UUID> blackhole = new ArrayList<>();
    public HashMap<UUID, Material> materialHashMap = new HashMap<>();
    public HashMap<UUID, Integer> TID = new HashMap<>();
    public boolean regenerate = true;

    public BlackholeCommand() {
        super("blackhole", "kmain.blackhole", CommandUsageBy.PlAYER);
        setUsage("&cImproper usage! /blackhole <material>");
        setMinArgs(0);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0) {
            if (!blackhole.contains(p.getUniqueId())) {
                blackhole.add(p.getUniqueId());
                startScheduler(p);
                p.sendMessage(ChatColor.DARK_PURPLE + "Blackhole activated. Inserting dick into server.");
            } else {
                blackhole.remove(p.getUniqueId());
                stopScheduler(p);
                p.sendMessage(ChatColor.DARK_PURPLE + "Blackhole deactivated. Tafari Jumaaneeeee.");
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("clear")) {
                materialHashMap.remove(p.getUniqueId());
                p.sendMessage(ChatColor.GRAY + "BlackHole will now target " + ChatColor.RED + "everything" + ChatColor.GRAY + ".");
                return;
            }

            if (args[0].equalsIgnoreCase("regen")) {
                if (regenerate) {
                    regenerate = false;
                    p.sendMessage(ChatColor.GRAY + "BlackHole will not regenerate blocks.");
                } else {
                    regenerate = true;
                    p.sendMessage(ChatColor.GRAY + "BlackHole will regenerate blocks.");
                }

                return;
            }

            try {
                materialHashMap.put(p.getUniqueId(), Material.valueOf(args[0].toUpperCase()));

                p.sendMessage(ChatColor.GRAY + "BlackHole will now target " + ChatColor.RED + args[0].toUpperCase() + ChatColor.GRAY + ".");
            } catch (Exception e) {
                p.sendMessage(ChatColor.GRAY + "That material does not exist.");
            }
        } else {
            p.sendMessage(ChatColor.RED + getUsage());
        }
    }

    public void startScheduler(final Player p) {
        final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(kMain.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (blackhole.contains(p.getUniqueId())) {
                    for (final Block nearby3 : getNearbyBlocks(p.getLocation(), 10)) {
                        if (nearby3.getType() != Material.AIR) {
                            if (materialHashMap.containsKey(p.getUniqueId())) {
                                if (nearby3.getType() == materialHashMap.get(p.getUniqueId())) {
                                    FallingBlock fb = nearby3.getLocation().getWorld().spawnFallingBlock(nearby3.getLocation(), nearby3.getType(), nearby3.getData());

                                    if (regenerate) {
                                        BlockUtils bu = new BlockUtils(kMain.getInstance());
                                        bu.request(nearby3, 30*20L);
                                    }

                                    nearby3.setType(Material.AIR);
                                    fb.setDropItem(false);
                                }
                            } else {
                                FallingBlock fb = nearby3.getLocation().getWorld().spawnFallingBlock(nearby3.getLocation(), nearby3.getType(), nearby3.getData());

                                if (regenerate) {
                                    BlockUtils bu = new BlockUtils(kMain.getInstance());
                                    bu.request(nearby3, 30*20L);
                                }

                                nearby3.setType(Material.AIR);
                                fb.setDropItem(false);
                            }
                        }
                    }
                    for (Entity near : p.getNearbyEntities(5.0, 5.0, 5.0)) {
                        if (!(near instanceof Player)) {
                            Vector direction2 = near.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                            direction2.setX(direction2.getX() * -5.0);
                            direction2.setZ(direction2.getZ() * -5.0);
                            direction2.setY(direction2.getY() * -5.0);
                            near.setVelocity(direction2);
                        }
                    }
                    for (Entity near : p.getNearbyEntities(10.0, 10.0, 10.0)) {
                        if (!(near instanceof Player)) {
                            Vector direction2 = near.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                            direction2.setX(direction2.getX() * -1.0);
                            direction2.setZ(direction2.getZ() * -1.0);
                            direction2.setY(direction2.getY() * -1.0);
                            near.setVelocity(direction2);
                        }
                    }
                    for (Entity near : p.getNearbyEntities(20.0, 20.0, 20.0)) {
                        if (!(near instanceof Player)) {
                            Vector direction2 = near.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                            direction2.setX(direction2.getX() * -0.5);
                            direction2.setZ(direction2.getZ() * -0.5);
                            direction2.setY(direction2.getY() * -0.5);
                            near.setVelocity(direction2);
                        }
                    }
                    for (Entity near : p.getNearbyEntities(1.0, 1.0, 1.0)) {
                        if (!(near instanceof Player)) {
                            near.remove();
                        }
                    }
                }
            }
        }, 0L, 2L);

        TID.put(p.getUniqueId(), id);
    }

    public void stopScheduler(Player p) {
        if (TID.containsKey(p.getUniqueId())) {
            int id = TID.get(p.getUniqueId());
            Bukkit.getScheduler().cancelTask(id);
            TID.remove(p.getUniqueId());
        }
    }

    public List<Block> getNearbyBlocks(Location center, int radius) {
        List<Location> locs = circle(center, radius, radius, false, true, 0);
        List<Block> blocks = new ArrayList<>();

        for (final Location loc : locs) {
            blocks.add(loc.getBlock());
        }

        return blocks;
    }

    public List<Location> circle(Location loc, int radius, int height, boolean hollow, boolean sphere, int plusY) {
        List<Location> circleblocks = new ArrayList<>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();

        for (int x = cx - radius; x <= cx + radius; ++x) {
            for (int z = cz - radius; z <= cz + radius; ++z) {
                for (int y = sphere ? (cy - radius) : cy; y < (sphere ? (cy + radius) : (cy + height)); ++y) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);

                    if (dist < radius * radius && (!hollow || dist >= (radius - 1) * (radius - 1))) {
                        Location l = new Location(loc.getWorld(), (double) x, (double) (y + plusY), (double) z);
                        circleblocks.add(l);
                    }
                }
            }
        }

        return circleblocks;
    }
}
