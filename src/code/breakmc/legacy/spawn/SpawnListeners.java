package code.breakmc.legacy.spawn;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.listeners.CombatLogListener;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.utils.BlockUtils;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Calvin on 4/27/2015.
 * Project: Legacy
 */
public class SpawnListeners implements Listener {

    private Legacy main = Legacy.getInstance();
    private SpawnManager sm = main.getSpawnManager();
    private ProfileManager pm = main.getProfileManager();
    private BlockUtils butils = Legacy.getInstance().getBlockUtils();

    @EventHandler
    public void onaMove(PlayerMoveEvent e) {
        if (sm.getSpawn() == null) {
            return;
        }

        if (sm.getSpawn().getDontMove().containsKey(e.getPlayer().getUniqueId())) {
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                sm.getSpawn().getDontMove().get(e.getPlayer().getUniqueId()).cancel();
                sm.getSpawn().getDontMove().remove(e.getPlayer().getUniqueId());
                MessageManager.sendMessage(e.getPlayer(), "&cYou moved! Teleportation cancelled.");
            }
        }
    }

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
            if (sm.getSpawn().isInStoneRadius(p.getLocation())) {
                e.setCancelled(true);
                MessageManager.sendMessage(p, "&cYou cannot use this command within " + sm.getSpawn().getStoneHeight() + " blocks of spawn!");
            }
        } else {
            if (e.getMessage().toLowerCase().contains("/sell") || e.getMessage().toLowerCase().contains("/buy")) {
                if (sm.getSpawn().isInStoneRadius(p.getLocation()) && !sm.hasSpawnProt(p.getUniqueId())) {
                    e.setCancelled(true);
                    MessageManager.sendMessage(p, "&cYou cannot use the economy within " + sm.getSpawn().getStoneRadius() + " blocks of spawn without spawn protection!");
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
                        attacker.sendMessage(ChatColor.RED + "This player has spawn protection!");
                        return;
                    }

                    if (sm.hasSpawnProt(attacker.getUniqueId()) && !sm.hasSpawnProt(attacked.getUniqueId())) {
                        attacker.sendMessage(ChatColor.GRAY + "You have lost spawn protection!");
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
                            attacker.sendMessage(ChatColor.RED + "This player has spawn protection!");
                            return;
                        }

                        if (sm.hasSpawnProt(attacker.getUniqueId()) && !sm.hasSpawnProt(attacked.getUniqueId())) {
                            attacker.sendMessage(ChatColor.GRAY + "You have lost spawn protection!");
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

        if (sm.getSpawn().isInStoneRadius(p.getLocation())) {
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
        }

        if (!sm.getSpawn().isInSpawnRadius(e.getPlayer().getLocation()) && sm.hasSpawnProt(p.getUniqueId())) {
            sm.getSpawnProtected().remove(p.getUniqueId());
            MessageManager.sendMessage(p, "&cYou no longer have spawn protection!");
        }
    }

    @EventHandler
    public void onSpawn(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage(null);

        if (sm.getSpawn() == null) {
            return;
        }

        if (!p.hasPlayedBefore()) {
            if (sm.getSpawn().isInSpawnRadius(p.getLocation())) {
                p.teleport(new Location(p.getWorld(), 0.5, sm.getSpawn().getHeight(), 0.5));
                sm.getSpawnProtected().add(p.getUniqueId());
                MessageManager.sendMessage(p, "&7You have regained spawn protection!");
                p.kickPlayer(ChatColor.GREEN + "Your permissions have been updated! You may now rejoin!");
            }
        } else {
            if (sm.getSpawn().isInSpawnRadius(p.getLocation()) && !CombatLogListener.loggers.containsKey(p.getUniqueId()) && CombatLogListener.loggers.get(p.getUniqueId()) == null) {
                sm.getSpawnProtected().add(p.getUniqueId());
                MessageManager.sendMessage(p, "&7You have regained spawn protection!");
            }
        }
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

//    @EventHandler
//    public void onClick(PlayerInteractEntityEvent e) {
//        final Player clicker = e.getPlayer();
//        if (e.getRightClicked() instanceof Player) {
//            final Player clicked = (Player) e.getRightClicked();
//            if (sm.hasSpawnProt(clicked.getUniqueId())) {
//                if (pm.hasProfile(clicked.getUniqueId())) {
//                    if (clicked.hasPermission("youtube.set")) {
//                        if (!pm.getProfile(clicked.getUniqueId()).getYoutubename().equals("")) {
//                            new BukkitRunnable() {
//                                public void run() {
//                                    if (Cooldowns.tryCooldown(clicked.getUniqueId(), "ClickCooldown", 5000)) {
//                                        YoutubeChannel channel = new YoutubeChannel(pm.getProfile(clicked.getUniqueId()).getYoutubename());
//                                        FancyMessage message = new FancyMessage(clicked.getName()).color(ChatColor.AQUA).then("'s ").color(ChatColor.GREEN).then("You").color(ChatColor.DARK_RED).then("Tube").color(ChatColor.WHITE).then(" channel: ").color(ChatColor.GREEN).then(pm.getProfile(clicked.getUniqueId()).getYoutubename()).color(ChatColor.AQUA).link("http://www.youtube.com/" + pm.getProfile(clicked.getUniqueId()).getYoutubename()).tooltip(ChatColor.GREEN + "Click here to go the this channel.").then(" < Click Here").color(ChatColor.GREEN).style(ChatColor.BOLD);
//                                        message.send(clicker);
//                                        MessageManager.sendMessage(clicker, "&aSuscribers: &b" + NumberFormat.getNumberInstance(Locale.US).format(channel.getSubscriberCount()) + "\n&aView Count: &b" + NumberFormat.getNumberInstance(Locale.US).format(channel.getTotalViews()));
//                                    }
//                                }
//                            }.runTaskAsynchronously(main);
//                        } else {
//                            MessageManager.sendMessage(clicker, "&cThis player does not have their &4You&rTube &cchannel setup.");
//                        }
//                    }
//                }
//            }
//        }
//    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (p.getWorld().getEnvironment() == World.Environment.NETHER && p.getLocation().getY() >= 127) {
            e.setTo(e.getFrom());
            MessageManager.sendMessage(p, "&cType /spawn to leave");
        }

        Location standBlock = p.getWorld().getBlockAt(p.getLocation().add(0.0D, -0.1D, 0.0D)).getLocation();
        int xblock = 0;
        double xvel = 0.0D;
        int yblock = -1;
        double yvel = 0.0D;
        int zblock = 0;
        double zvel = 0.0D;
        if (standBlock.getBlock().getTypeId() == 19) {
            while (standBlock.getBlock().getLocation().add(xblock, -1.0D, 0.0D).getBlock().getType().equals(Material.SPONGE)) {
                xblock--;
                xvel += 0.8D;
            }
            while (standBlock.getBlock().getLocation().add(0.0D, yblock, 0.0D).getBlock().getType().equals(Material.SPONGE)) {
                yblock--;
                yvel += 1.0D;
            }
            while (standBlock.getBlock().getLocation().add(0.0D, -1.0D, zblock).getBlock().getType().equals(Material.SPONGE)) {
                zblock--;
                zvel += 0.8D;
            }
            xblock = 0;
            zblock = 0;
            while (standBlock.getBlock().getLocation().add(xblock, -1.0D, 0.0D).getBlock().getType().equals(Material.SPONGE)) {
                xblock++;
                xvel -= 0.8D;
            }
            while (standBlock.getBlock().getLocation().add(0.0D, -1.0D, zblock).getBlock().getType().equals(Material.SPONGE)) {
                zblock++;
                zvel -= 0.8D;
            }
            if (standBlock.getBlock().getLocation().add(0.0D, -1.0D, 0.0D).getBlock().getType().equals(Material.SPONGE)) {
                p.setVelocity(new Vector(xvel, yvel, zvel));
            }
        }
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
}
