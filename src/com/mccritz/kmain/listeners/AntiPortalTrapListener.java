package com.mccritz.kmain.listeners;

import com.mccritz.kmain.kMain;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class AntiPortalTrapListener implements Listener {

    private kMain main = kMain.getInstance();
    private HashMap<Player, HashMap<String, ArrayList<Double>>> portalLocations = new HashMap<>();
    private HashMap<Player, Location> startLocation = new HashMap<>();
    private ArrayList<Integer> allowedBottomIds = new ArrayList<>();

    public AntiPortalTrapListener() {
        allowedBottomIds.add(0);
        allowedBottomIds.add(6);
        allowedBottomIds.add(7);
        allowedBottomIds.add(10);
        allowedBottomIds.add(30);
        allowedBottomIds.add(31);
        allowedBottomIds.add(32);
        allowedBottomIds.add(37);
        allowedBottomIds.add(38);
        allowedBottomIds.add(39);
        allowedBottomIds.add(40);
        allowedBottomIds.add(50);
        allowedBottomIds.add(51);
        allowedBottomIds.add(55);
        allowedBottomIds.add(59);
        allowedBottomIds.add(63);
        allowedBottomIds.add(65);
        allowedBottomIds.add(68);
        allowedBottomIds.add(69);
        allowedBottomIds.add(66);
        allowedBottomIds.add(70);
        allowedBottomIds.add(72);
        allowedBottomIds.add(75);
        allowedBottomIds.add(76);
        allowedBottomIds.add(77);
        allowedBottomIds.add(83);
        allowedBottomIds.add(90);
        allowedBottomIds.add(104);
        allowedBottomIds.add(105);
        allowedBottomIds.add(106);
        allowedBottomIds.add(115);
        allowedBottomIds.add(131);
        allowedBottomIds.add(132);
        allowedBottomIds.add(141);
        allowedBottomIds.add(142);
        allowedBottomIds.add(143);
        allowedBottomIds.add(147);
        allowedBottomIds.add(148);
        allowedBottomIds.add(157);
        allowedBottomIds.add(171);
        allowedBottomIds.add(175);
    }

    @EventHandler
    public void onPortalEvent(PlayerPortalEvent portal) {
        Player p = portal.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                Location l = p.getLocation();
                Location nblock = p.getLocation();
                nblock.setZ(p.getLocation().getZ() - 1.0);
                Location midnblock = p.getLocation();
                midnblock.setZ(p.getLocation().getZ() - 1.0);
                midnblock.setY(p.getLocation().getY() + 1.0);
                Location topnblock = p.getLocation();
                topnblock.setZ(p.getLocation().getZ() - 1.0);
                topnblock.setY(p.getLocation().getY() + 2.0);
                Location sblock = p.getLocation();
                sblock.setZ(p.getLocation().getZ() + 1.0);
                Location midsblock = p.getLocation();
                midsblock.setZ(p.getLocation().getZ() + 1.0);
                midsblock.setY(p.getLocation().getY() + 1.0);
                Location topsblock = p.getLocation();
                topsblock.setZ(p.getLocation().getZ() + 1.0);
                topsblock.setY(p.getLocation().getY() + 2.0);
                Location eblock = p.getLocation();
                eblock.setX(p.getLocation().getX() + 1.0);
                Location mideblock = p.getLocation();
                mideblock.setX(p.getLocation().getX() + 1.0);
                mideblock.setY(p.getLocation().getY() + 1.0);
                Location topeblock = p.getLocation();
                topeblock.setX(p.getLocation().getX() + 1.0);
                topeblock.setY(p.getLocation().getY() + 2.0);
                Location wblock = p.getLocation();
                wblock.setX(p.getLocation().getX() - 1.0);
                Location midwblock = p.getLocation();
                midwblock.setX(p.getLocation().getX() - 1.0);
                midwblock.setY(p.getLocation().getY() + 1.0);
                Location topwblock = p.getLocation();
                topwblock.setX(p.getLocation().getX() - 1.0);
                topwblock.setY(p.getLocation().getY() + 2.0);

                while (nblock.getBlock().getType() == Material.PORTAL) {
                    nblock.setZ(nblock.getZ() - 1.0);
                    midnblock.setZ(nblock.getZ());
                    topnblock.setZ(nblock.getZ());
                }

                while (sblock.getBlock().getType() == Material.PORTAL) {
                    sblock.setZ(sblock.getZ() + 1.0);
                    midsblock.setZ(sblock.getZ());
                    topsblock.setZ(sblock.getZ());
                }

                while (eblock.getBlock().getType() == Material.PORTAL) {
                    eblock.setX(eblock.getX() + 1.0);
                    mideblock.setX(eblock.getX());
                    topeblock.setX(eblock.getX());
                }

                while (wblock.getBlock().getType() == Material.PORTAL) {
                    wblock.setX(wblock.getX() - 1.0);
                    midwblock.setX(wblock.getX());
                    topwblock.setX(wblock.getX());
                }

                if ((!allowedBottomIds.contains(nblock.getBlock().getTypeId())
                        || (!allowedBottomIds.contains(sblock.getBlock().getTypeId())
                        || (!allowedBottomIds.contains(eblock.getBlock().getTypeId())
                        || (!allowedBottomIds.contains(wblock.getBlock().getTypeId())
                        || (!allowedBottomIds.contains(midnblock.getBlock().getTypeId())
                        || (!allowedBottomIds.contains(midsblock.getBlock().getTypeId())
                        || (!allowedBottomIds.contains(mideblock.getBlock().getTypeId())
                        || (!allowedBottomIds.contains(midwblock.getBlock().getTypeId())))))))))) {

                    HashMap<String, ArrayList<Double>> coordloc = new HashMap<>();
                    ArrayList<Double> xloc = new ArrayList<>();
                    ArrayList<Double> zloc = new ArrayList<>();
                    startLocation.put(p, l);
                    xloc.add(l.getX() - 2.0);
                    xloc.add(l.getX() - 1.0);
                    xloc.add(l.getX());
                    xloc.add(l.getX() + 1.0);
                    xloc.add(l.getX() + 2.0);
                    coordloc.put("x: ", xloc);
                    zloc.add(l.getZ() - 2.0);
                    zloc.add(l.getZ() - 1.0);
                    zloc.add(l.getZ());
                    zloc.add(l.getZ() + 1.0);
                    zloc.add(l.getZ() + 2.0);
                    coordloc.put("z: ", zloc);
                    portalLocations.put(p, coordloc);
                    Block block = p.getLocation().getWorld().getBlockAt(p.getLocation());
                    block.setType(Material.AIR);
                }
            }
        }.runTaskLater(main, 1L);
    }

    @EventHandler
    public void onPortalTpLeave(PlayerTeleportEvent portaltpleave) {
        Player p = portaltpleave.getPlayer();

        if (!portalLocations.containsKey(p)) {
            return;
        }

        HashMap<String, ArrayList<Double>> coordloc = portalLocations.get(p);
        ArrayList<Double> xloc = coordloc.get("x: ");
        ArrayList<Double> zloc = coordloc.get("z: ");

        if (!xloc.contains(portaltpleave.getTo().getX()) && !zloc.contains(portaltpleave.getTo().getZ())) {
            portaltpleave.setCancelled(true);
        }
    }

    @EventHandler
    public void onPortalLeave(PlayerMoveEvent portalleave) {
        Player p = portalleave.getPlayer();

        if (!portalLocations.containsKey(p)) {
            return;
        }

        HashMap<String, ArrayList<Double>> coordloc = portalLocations.get(p);
        ArrayList<Double> xloc = coordloc.get("x: ");
        ArrayList<Double> zloc = coordloc.get("z: ");

        if (xloc.get(0) > portalleave.getTo().getX() || xloc.get(4) < portalleave.getTo().getX() || zloc.get(0) > portalleave.getTo().getZ() || zloc.get(4) < portalleave.getTo().getZ()) {
            Location l = startLocation.get(p);
            Block block = l.getWorld().getBlockAt(l);
            block.setType(Material.FIRE);
            coordloc.clear();
            portalLocations.remove(p);
            startLocation.remove(p);
        }
    }
}
