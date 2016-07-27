package com.mccritz.kmain.spawn;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.utils.BlockUtils;
import com.mccritz.kmain.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class SpawnListeners implements Listener {

    private kMain main = kMain.getInstance();
    private SpawnManager sm = main.getSpawnManager();
    private BlockUtils butils = kMain.getInstance().getBlockUtils();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();

        if (sm.getSpawn() == null) {
            return;
        }

        if (e.getMessage().toLowerCase().contains("/team sethq")
                || e.getMessage().toLowerCase().contains("/team shq")
                || e.getMessage().toLowerCase().contains("/t sethq")
                || e.getMessage().toLowerCase().contains("/t shq")
                || e.getMessage().toLowerCase().contains("/t seth")
                || e.getMessage().toLowerCase().contains("/t setrally")
                || e.getMessage().toLowerCase().contains("/team setrally")
                || e.getMessage().toLowerCase().contains("/sethome")
                || e.getMessage().toLowerCase().contains("/essentials:sethome")
                || e.getMessage().toLowerCase().contains("/esethome")
                || e.getMessage().toLowerCase().contains("/go set")
                || e.getMessage().toLowerCase().contains("/warp set")
                || e.getMessage().toLowerCase().contains("/ewarp set")) {
            if (sm.getSpawn().isIn512Radius(p.getLocation())) {
                e.setCancelled(true);
                MessageManager.message(p, "&7You cannot use that within 512 blocks of spawn.");
            }
        } else {
            if (e.getMessage().toLowerCase().contains("/buy")) {
                if (sm.getSpawn().isIn512Radius(p.getLocation()) && !sm.hasSpawnProt(p.getUniqueId())) {
                    e.setCancelled(true);
                    MessageManager.message(p, "&7You may not use the economy within 512 blocks of spawn.");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageEvent(EntityDamageByEntityEvent e) {
        if (sm.getSpawn() == null) {
            return;
        }

        if (e.getEntity() instanceof Player) {
            Player attacked = (Player) e.getEntity();
            if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                if (e.getDamager() instanceof Player) {
                    Player attacker = (Player) e.getDamager();

                    if (sm.hasSpawnProt(attacked.getUniqueId())) {
                        e.setCancelled(true);
                        return;
                    }

                    if (sm.hasSpawnProt(attacker.getUniqueId()) && !sm.hasSpawnProt(attacked.getUniqueId())) {
                        MessageManager.message(attacker, "&7You no longer have spawn protection.");

                        sm.getSpawnProtected().remove(attacker.getUniqueId());
                        return;
                    }
                }
            }
            if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                if (e.getDamager() instanceof Projectile) {
                    Projectile a = (Projectile) e.getDamager();
                    if (a.getShooter() instanceof Player) {
                        Player attacker = (Player) a.getShooter();
                        if (sm.hasSpawnProt(attacked.getUniqueId())) {
                            e.setCancelled(true);
                            return;
                        }

                        if (sm.hasSpawnProt(attacker.getUniqueId()) && !sm.hasSpawnProt(attacked.getUniqueId())) {
                            MessageManager.message(attacker, "&7You no longer have spawn protection.");

                            sm.getSpawnProtected().remove(attacker.getUniqueId());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (sm.getSpawn() == null) {
            return;
        }

        if (!sm.getSpawn().isInSpawnRadius(e.getPlayer().getLocation()) && sm.hasSpawnProt(p.getUniqueId())) {
            sm.getSpawnProtected().remove(p.getUniqueId());

            MessageManager.message(p, "&7You no longer have spawn protection.");
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (sm.getSpawn().isInStoneRadius(p.getLocation())) {
                    p.removePotionEffect(PotionEffectType.INVISIBILITY);
                }
            }
        }.runTaskAsynchronously(kMain.getInstance());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage(null);

        if (sm.getSpawn() == null) {
            return;
        }

        if (!p.hasPlayedBefore()) {
            if (sm.getSpawn().isInSpawnRadius(p.getLocation())) {
                p.teleport(new Location(p.getWorld(), 0.5, sm.getSpawn().getHeight(), 0.5));
                sm.getSpawnProtected().add(p.getUniqueId());
                MessageManager.message(p, "&7You cannot attack for 10 seconds.");
            }
        } else {
            if (sm.getSpawn().isInSpawnRadius(p.getLocation())) {
                sm.getSpawnProtected().add(p.getUniqueId());
                MessageManager.message(p, "&7You cannot attack for 10 seconds.");
            }
        }
    }

    @EventHandler()
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }

    @EventHandler
    public void onspw(CreatureSpawnEvent e) {
        if (e.getEntity().getType() == EntityType.SQUID) {
            e.setCancelled(true);
            return;
        }

        if (sm.getSpawn() == null) {
            return;
        }

        if (sm.getSpawn().isInStoneRadius(e.getLocation()) && e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CHUNK_GEN && e.getEntity().getType() != EntityType.VILLAGER && e.getEntity() instanceof Monster) {
            e.setCancelled(true);
            return;
        }

        if (sm.getSpawn().isInStoneRadius(e.getLocation()) && e.getEntity().getType() != EntityType.VILLAGER) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                return;
            }

            Player p = (Player) e.getEntity();

            if (sm.getSpawn() == null) {
                return;
            }

            if (sm.hasSpawnProt(p.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (sm.getSpawn() == null) {
            return;
        }

        if (sm.getSpawn().isInSpawnRadius(e.getBlock().getLocation())) {
            if (!e.getPlayer().hasPermission("spawn.build")) {
                e.setCancelled(true);
                return;
            }
        }

        if (e.getBlock().getWorld().getEnvironment() == World.Environment.NETHER) {
            if (e.getBlock().getY() >= 127) {
                e.setCancelled(true);
                return;
            }
        }

        if (e.getPlayer().isOp()) {
            return;
        }

        if (e.getBlock().getLocation().getY() <= sm.getSpawn().getStoneHeight() && sm.getSpawn().isInStoneRadius(e.getBlock().getLocation()) || e.getBlock().getLocation().getY() >= (sm.getSpawn().getStoneHeight() + 15) && sm.getSpawn().isInStoneRadius(e.getBlock().getLocation())) {
            e.setCancelled(true);
            return;
        }

        if (sm.getSpawn().isInStoneRadius(e.getBlock().getLocation()) && !sm.getSpawn().isInSpawnRadius(e.getBlock().getLocation())) {
            butils.onBlockBreak(new BlockBreakEvent(e.getBlock(), e.getPlayer()), 600 * 20);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (sm.getSpawn() == null) {
            return;
        }

        if (sm.getSpawn().isInSpawnRadius(e.getBlock().getLocation())) {
            if (!e.getPlayer().hasPermission("spawn.build")) {
                e.setCancelled(true);
                return;
            }
        }

        if (e.getBlock().getWorld().getEnvironment() == World.Environment.NETHER) {
            if (e.getBlock().getY() >= 127) {
                e.setCancelled(true);
                return;
            }
        }

        if (e.getPlayer().isOp()) {
            return;
        }

        if (e.getBlock().getLocation().getY() <= sm.getSpawn().getStoneHeight() && sm.getSpawn().isInStoneRadius(e.getBlock().getLocation()) || e.getBlock().getLocation().getY() >= (sm.getSpawn().getStoneHeight() + 15) && sm.getSpawn().isInStoneRadius(e.getBlock().getLocation())) {
            e.setCancelled(true);
            return;
        }

        if (sm.getSpawn().isInStoneRadius(e.getBlock().getLocation()) && !sm.getSpawn().isInSpawnRadius(e.getBlock().getLocation())) {
            if (e.getBlock().getType() == Material.BED || e.getBlock().getType() == Material.SAND || e.getBlock().getType() == Material.GRAVEL || e.getBlock().getType() == Material.IRON_DOOR_BLOCK || e.getBlock().getType() == Material.IRON_DOOR || e.getBlock().getType() == Material.WOOD_DOOR || e.getBlock().getType() == Material.WOODEN_DOOR || e.getBlock().getType() == Material.BED_BLOCK) {
                e.setCancelled(true);
            }

            butils.onBlockPlace(new BlockPlaceEvent(e.getBlock(), e.getBlockReplacedState(), e.getBlockAgainst(), e.getItemInHand(), e.getPlayer(), true), 600 * 20);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (sm.getSpawn() == null) {
            return;
        }

        for (Block b : new ArrayList<>(e.blockList())) {
            if (sm.getSpawn().isInSpawnRadius(b.getLocation())) {
                e.blockList().remove(b);
            } else if (sm.getSpawn().isInStoneRadius(b.getLocation())) {
                if (b.getY() <= sm.getSpawn().getStoneHeight()) {
                    e.blockList().remove(b);
                } else {
                    butils.request(b, 600 * 20);
                }
            }
        }
    }

    @EventHandler
    public void onPistonPush(BlockPistonExtendEvent event) {
        if (sm.getSpawn() == null) {
            return;
        }

        List<Block> blockList = event.getBlocks();
        boolean cancel = false;
        BlockFace face = event.getDirection();
        for (Block block : blockList) {
            Location location = block.getLocation();
            if (face.equals(BlockFace.NORTH)) {
                location = block.getLocation().subtract(0, 0, 1);
            } else if (face.equals(BlockFace.SOUTH)) {
                location = block.getLocation().add(0, 0, 1);
            } else if (face.equals(BlockFace.WEST)) {
                location = block.getLocation().subtract(1, 0, 0);
            } else if (face.equals(BlockFace.EAST)) {
                location = block.getLocation().add(1, 0, 0);
            }

            if (sm.getSpawn().isInStoneRadius(location)) {
                cancel = true;
            }
        }
        event.setCancelled(cancel);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (event.getPlayer().getBedSpawnLocation() != null) {
            return;
        }

        event.setRespawnLocation(new Location(Bukkit.getWorld("world"), 0.5, sm.getSpawn().getHeight(), 0.5));

        if (sm.getSpawn() == null) {
            return;
        }

        sm.getSpawnProtected().add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onChange(BlockFadeEvent e) {
        if (sm.getSpawn() == null) {
            return;
        }

        if (sm.getSpawn().isInStoneRadius(e.getBlock().getLocation()) || sm.getSpawn().isInSpawnRadius(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (sm.getSpawn() == null) {
            return;
        }

        if (e.getAction() == Action.PHYSICAL) {
            if (e.hasBlock()) {
                if (e.getClickedBlock().getType() == Material.SOIL) {
                    if (sm.getSpawn().isInSpawnRadius(e.getClickedBlock().getLocation())) {
                        e.setUseInteractedBlock(Event.Result.DENY);
                        e.setCancelled(true);
                        e.getClickedBlock().setType(Material.SOIL);
                        e.getClickedBlock().setData(e.getClickedBlock().getData());
                    }
                }
            }
        }

        if (e.hasItem()) {
            if (e.getItem().getType() == Material.BUCKET || e.getItem().getType() == Material.LAVA_BUCKET || e.getItem().getType() == Material.WATER_BUCKET) {
                if (sm.getSpawn().isInStoneRadius(p.getLocation())) {
                    e.setCancelled(true);
                }

                if (e.hasBlock()) {
                    if (sm.getSpawn().isInStoneRadius(e.getClickedBlock().getLocation())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (p.getWorld().getEnvironment() == World.Environment.NETHER && p.getLocation().getY() >= 127) {
            e.setTo(e.getFrom());
            MessageManager.message(p, "&7You cannot go on top of the nether. Type /spawn to leave.");
        }

//        Location standBlock = p.getWorld().getBlockAt(p.getLocation().add(0.0D, -0.1D, 0.0D)).getLocation();
//        int xblock = 0;
//        double xvel = 0.0D;
//        int yblock = -1;
//        double yvel = 0.0D;
//        int zblock = 0;
//        double zvel = 0.0D;
//
//        if (standBlock.getBlock().getTypeId() == 19) {
//            while (standBlock.getBlock().getLocation().add(xblock, -1.0D, 0.0D).getBlock().getType().equals(Material.SPONGE)) {
//                xblock--;
//                xvel += 0.8D;
//            }
//
//            while (standBlock.getBlock().getLocation().add(0.0D, yblock, 0.0D).getBlock().getType().equals(Material.SPONGE)) {
//                yblock--;
//                yvel += 1.0D;
//            }
//
//            while (standBlock.getBlock().getLocation().add(0.0D, -1.0D, zblock).getBlock().getType().equals(Material.SPONGE)) {
//                zblock--;
//                zvel += 0.8D;
//            }
//
//            xblock = 0;
//            zblock = 0;
//
//            while (standBlock.getBlock().getLocation().add(xblock, -1.0D, 0.0D).getBlock().getType().equals(Material.SPONGE)) {
//                xblock++;
//                xvel -= 0.8D;
//            }
//
//            while (standBlock.getBlock().getLocation().add(0.0D, -1.0D, zblock).getBlock().getType().equals(Material.SPONGE)) {
//                zblock++;
//                zvel -= 0.8D;
//            }
//
//            if (standBlock.getBlock().getLocation().add(0.0D, -1.0D, 0.0D).getBlock().getType().equals(Material.SPONGE)) {
//                p.setVelocity(new Vector(xvel, yvel, zvel));
//            }
//        }
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent e) {
        if (sm.getSpawn() == null) {
            return;
        }

        for (Block b : e.getBlocks()) {
            if (b.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                if (sm.getSpawn().isInNetherRadius(b.getLocation())) {
                    e.setCancelled(true);
                }
            } else if (b.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                if (sm.getSpawn().isInStoneRadius(b.getLocation())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent e) {
        if (sm.getSpawn() == null) {
            return;
        }

        if (sm.getSpawn().isInStoneRadius(e.getToBlock().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent e) {
        if (sm.getSpawn() == null) {
            return;
        }

        e.getBlocks().stream().filter(b -> sm.getSpawn().isInStoneRadius(b.getLocation())).forEach(b -> {
            e.setCancelled(true);
        });
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent e) {
        if (sm.getSpawn() == null) {
            return;
        }

        if (sm.getSpawn().isInStoneRadius(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDispense(BlockDispenseEvent e) {
        if (sm.getSpawn() == null) {
            return;
        }

        if (sm.getSpawn().isInStoneRadius(e.getBlock().getLocation()) && e.getItem().getType() == Material.LAVA_BUCKET || e.getItem().getType() == Material.WATER_BUCKET) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSplash(PotionSplashEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player player = (Player) e.getEntity().getShooter();

            if (sm.getSpawnProtected().contains(player.getUniqueId())) {
                boolean spawnprot = true;

                for (Entity entity : e.getAffectedEntities()) {
                    if (entity instanceof Player) {
                        Player affected = (Player) entity;

                        if (!sm.getSpawnProtected().contains(affected.getUniqueId())) {
                            spawnprot = false;
                        }
                    }
                }

                if (!spawnprot) {
                    sm.getSpawnProtected().remove(player.getUniqueId());
                    MessageManager.message(player, "&7You no longer have spawn protection.");
                }
            }
        }
    }
}
