package code.breakmc.legacy.utils;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.spawn.SpawnManager;
import mkremins.fanciful.FancyMessage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class TrackingUtils {

    private SpawnManager sm = Legacy.getInstance().getSpawnManager();
    int mx;
    int my;
    int mz;

    public void setLoc(int x, int y, int z) {
        this.mx = x;
        this.my = y;
        this.mz = z;
    }

    public boolean checkPlayer(Player pl, int x, int z) {
        int num;
        if (x == 0) {
            int plz = pl.getLocation().getBlockZ();
            num = Math.abs(z);
            if (Math.abs(this.mz - plz) < num) {
                if (z < 0) {
                    if (plz <= this.mz)
                        return true;
                } else if (plz >= this.mz)
                    return true;
            }
        } else if (z == 0) {
            int plz = pl.getLocation().getBlockX();
            num = Math.abs(x);
            if (Math.abs(this.mx - plz) < num) {
                if (x < 0) {
                    if (plz <= this.mx)
                        return true;
                } else if (plz >= this.mx)
                    return true;
            }
        }
        return false;
    }

    public void TrackDir(Player player, int x, int z, Player player2) {
        String compass = "north";
        int num = Math.abs(x) + Math.abs(z);

        boolean can = checkPlayer(player2, x, z);
        if (z < 0) {
            compass = "north";
        } else if (z > 0) {
            compass = "south";
        }
        if (x < 0) {
            compass = "west";
        } else if (x > 0) {
            compass = "east";
        }
        if (player == player2) {
            if (player2.hasPermission("tracking.donator")) {
                MessageManager.sendMessage(player, "&b" + player2.getName() + " &aIS &7within &b" + num + " &7blocks " + compass + " of here.");
            } else {
                MessageManager.sendMessage(player, "&7" + player2.getName() + " &aIS &7within &b" + num + " &7blocks " + compass + " of here.");
            }
        } else {
            if (player2.hasPermission("trackers.donator")) {
                MessageManager.sendMessage(player, "&b" + player2.getName() + (can ? " &aIS " : " &cIS NOT ") + "&7within &b" + num + " &7blocks " + compass + " of here");
            } else {
                MessageManager.sendMessage(player, "&7" + player2.getName() + (can ? " &aIS " : " &cIS NOT ") + "&7within &b" + num + " &7blocks " + compass + " of here");
            }
        }
    }

    public int findBlock(World world, int x, int z, Material mat1, Material mat2) {
        boolean hasmat = true;
        int length = 0;
        for (int i = 1; i < 1000; i++) {
            Block block = world.getBlockAt(this.mx + x * i, this.my, this.mz + z * i);
            Material bmat = block.getType();
            if (bmat == mat1) {
                length++;
                if (mat1 == Material.COBBLESTONE) {
                    block.setType(Material.AIR);
                    block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
                }
            } else {
                if (bmat == mat2) {
                    hasmat = false;
                    length++;
                    if (mat1 != Material.COBBLESTONE) {
                        break;
                    }
                    block.setType(Material.AIR);
                    block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
                    break;
                }
                return 0;
            }

        }
        if ((length > 0) && (!hasmat))
            return length;
        return 0;
    }

    public void Track(Material mat1, Material mat2, Player player, Player player2) {

        Block block = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()).getBlock();
        boolean northDists = findEnd(player.getWorld(), 0, -1, mat2);
        boolean southDists = findEnd(player.getWorld(), 0, 1, mat2);
        boolean eastDists = findEnd(player.getWorld(), -1, 0, mat2);
        boolean westDists = findEnd(player.getWorld(), 1, 0, mat2);
        int northDist = findBlock(player.getWorld(), 0, -1, mat1, mat2);
        int southDist = findBlock(player.getWorld(), 0, 1, mat1, mat2);
        int eastDist = findBlock(player.getWorld(), -1, 0, mat1, mat2);
        int westDist = findBlock(player.getWorld(), 1, 0, mat1, mat2);

        if (northDists && northDist > 0) {
            TrackDir(player, 0, -northDist * 25, player2);
        }
        if (eastDists && eastDist > 0) {
            TrackDir(player, -eastDist * 25, 0, player2);
        }
        if (southDists && southDist > 0) {
            TrackDir(player, 0, southDist * 25, player2);
        }
        if (westDists && westDist > 0) {
            TrackDir(player, westDist * 25, 0, player2);
        }
        if (block.getType() == Material.OBSIDIAN) {
            block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
            block.breakNaturally(new ItemStack(Material.AIR));
        }
    }

    public void Track(Player player, Player player2) {
        Block block = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()).getBlock();
        if (block.getType() == Material.DIAMOND_BLOCK && sm.getSpawn().isInSpawnRadius(block.getLocation())) {
            TrackSpawn(player, player2);
        } else if (block.getType() == Material.DIAMOND_BLOCK && isPerm(block)) {
            Track(Material.OBSIDIAN, Material.GOLD_BLOCK, player, player2);
        } else if (block.getType() == Material.OBSIDIAN && isTemp(block)) {
            Track(Material.COBBLESTONE, Material.STONE, player, player2);
        } else {
            MessageManager.sendMessage(player, "&cYou must be on a tracker to use this command.");
        }
    }

    public void TrackSpawn(Player player, Player player2) {
        Block block = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()).getBlock();

        TrackDir(player, 0, -300, player2);
        TrackDir(player, -300, 0, player2);
        TrackDir(player, 0, 300, player2);
        TrackDir(player, 300, 0, player2);

        if (block.getType() == Material.OBSIDIAN) {
            block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
            block.breakNaturally(new ItemStack(Material.AIR));
        }
    }

    public boolean isTemp(Block b1) {
        boolean istemp = false;

        if (b1.getType() == Material.OBSIDIAN) {
            double left = b1.getLocation().getX() + 1;
            double right = b1.getLocation().getX() - 1;
            double front = b1.getLocation().getZ() - 1;
            double back = b1.getLocation().getZ() + 1;
            Block leftb = Bukkit.getWorld(b1.getWorld().getName()).getBlockAt(new Location(b1.getWorld(), left, b1.getLocation().getY(), b1.getLocation().getZ()));
            Block rightb = Bukkit.getWorld(b1.getWorld().getName()).getBlockAt(new Location(b1.getWorld(), right, b1.getLocation().getY(), b1.getLocation().getZ()));
            Block frontb = Bukkit.getWorld(b1.getWorld().getName()).getBlockAt(new Location(b1.getWorld(), b1.getLocation().getX(), b1.getLocation().getY(), front));
            Block backb = Bukkit.getWorld(b1.getWorld().getName()).getBlockAt(new Location(b1.getWorld(), b1.getLocation().getX(), b1.getLocation().getY(), back));

            istemp = leftb.getType() == Material.STONE || leftb.getType() == Material.COBBLESTONE || rightb.getType() == Material.STONE || rightb.getType() == Material.COBBLESTONE || frontb.getType() == Material.STONE || frontb.getType() == Material.COBBLESTONE || backb.getType() == Material.STONE || backb.getType() == Material.COBBLESTONE;
        }
        return istemp;
    }

    public boolean isPerm(Block b1) {
        boolean isperm = false;

        if (b1.getType() == Material.DIAMOND_BLOCK) {
            double left = b1.getLocation().getX() + 1;
            double right = b1.getLocation().getX() - 1;
            double front = b1.getLocation().getZ() - 1;
            double back = b1.getLocation().getZ() + 1;
            Block leftb = Bukkit.getWorld(b1.getWorld().getName()).getBlockAt(new Location(b1.getWorld(), left, b1.getLocation().getY(), b1.getLocation().getZ()));
            Block rightb = Bukkit.getWorld(b1.getWorld().getName()).getBlockAt(new Location(b1.getWorld(), right, b1.getLocation().getY(), b1.getLocation().getZ()));
            Block frontb = Bukkit.getWorld(b1.getWorld().getName()).getBlockAt(new Location(b1.getWorld(), b1.getLocation().getX(), b1.getLocation().getY(), front));
            Block backb = Bukkit.getWorld(b1.getWorld().getName()).getBlockAt(new Location(b1.getWorld(), b1.getLocation().getX(), b1.getLocation().getY(), back));

            isperm = leftb.getType() == Material.GOLD_BLOCK || leftb.getType() == Material.OBSIDIAN || rightb.getType() == Material.GOLD_BLOCK || rightb.getType() == Material.OBSIDIAN || frontb.getType() == Material.GOLD_BLOCK || frontb.getType() == Material.OBSIDIAN || backb.getType() == Material.GOLD_BLOCK || backb.getType() == Material.OBSIDIAN;
        }
        return isperm;
    }

    public void TrackDirAll(Player player, int x, int z, Player player2) {
        String compass;
        int in = 0;
        int num = Math.abs(x) + Math.abs(z);
        FancyMessage m1 = new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&r"));
        FancyMessage m2 = new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&r"));

        if (player2 == null) {
            for (int i = 0; i < PlayerUtility.getOnlinePlayers().length; i++) {
                if (i < PlayerUtility.getOnlinePlayers().length - 1) {
                    Player pl = PlayerUtility.getOnlinePlayers()[i];
                    boolean can = checkPlayer(pl, x, z);

                    if (can) {
                        in ++;
                        if (pl.hasPermission("tracking.donator")) {
                            m2.then(pl.getName()).command("/track " + pl.getName()).color(ChatColor.AQUA).tooltip(ChatColor.translateAlternateColorCodes('&', "&aDonator"), " ", ChatColor.translateAlternateColorCodes('&', getEnvironmentName(pl.getWorld().getEnvironment()))).then(", ").color(ChatColor.GRAY);
                        } else {
                            m2.then(pl.getName()).command("/track " + pl.getName()).color(ChatColor.GRAY).tooltip(ChatColor.translateAlternateColorCodes('&', "&7Non-Donator"), " ", ChatColor.translateAlternateColorCodes('&', getEnvironmentName(pl.getWorld().getEnvironment()))).then(", ").color(ChatColor.GRAY);
                        }
                    }
                } else {
                    Player pl = PlayerUtility.getOnlinePlayers()[i];
                    boolean can = checkPlayer(pl, x, z);

                    if (can) {
                        in ++;
                        if (pl.hasPermission("tracking.donator")) {
                            m2.then(pl.getName()).command("/track " + pl.getName()).color(ChatColor.AQUA).tooltip(ChatColor.translateAlternateColorCodes('&', "&aDonator"), " ", ChatColor.translateAlternateColorCodes('&', getEnvironmentName(pl.getWorld().getEnvironment())));
                        } else {
                            m2.then(pl.getName()).command("/track " + pl.getName()).color(ChatColor.GRAY).tooltip(ChatColor.translateAlternateColorCodes('&', "&7Non-Donator"), " ", ChatColor.translateAlternateColorCodes('&', getEnvironmentName(pl.getWorld().getEnvironment())));
                        }
                    }
                }
            }
        } else {
            boolean can = checkPlayer(player2, x, z);

            if (can) {
                in ++;
            }
        }

        if (z < 0) {
            compass = "North";
            if (in == 0) {
                m1.then(compass).color(ChatColor.DARK_AQUA).then(" (").color(ChatColor.GRAY).then("" + num).color(ChatColor.AQUA).then("): ").color(ChatColor.GRAY);
                m1.send(player);
            } else {
                m1.then(compass).color(ChatColor.DARK_AQUA).then(" (").color(ChatColor.GRAY).then("" + num).color(ChatColor.AQUA).then("): ").color(ChatColor.GRAY);
                m1.send(player);
                m2.send(player);
            }
        } else if (x > 0) {
            compass = "East";
            if (in == 0) {
                m1.then(compass).color(ChatColor.DARK_AQUA).then(" (").color(ChatColor.GRAY).then("" + num).color(ChatColor.AQUA).then("): ").color(ChatColor.GRAY);
                m1.send(player);
            } else {
                m1.then(compass).color(ChatColor.DARK_AQUA).then(" (").color(ChatColor.GRAY).then("" + num).color(ChatColor.AQUA).then("): ").color(ChatColor.GRAY);
                m1.send(player);
                m2.send(player);
            }
        } else if (z > 0) {
            compass = "South";
            if (in == 0) {
                m1.then(compass).color(ChatColor.DARK_AQUA).then(" (").color(ChatColor.GRAY).then("" + num).color(ChatColor.AQUA).then("): ").color(ChatColor.GRAY);
                m1.send(player);
            } else {
                m1.then(compass).color(ChatColor.DARK_AQUA).then(" (").color(ChatColor.GRAY).then("" + num).color(ChatColor.AQUA).then("): ").color(ChatColor.GRAY);
                m1.send(player);
                m2.send(player);
            }
        } else if (x < 0) {
            compass = "West";
            if (in == 0) {
                m1.then(compass).color(ChatColor.DARK_AQUA).then(" (").color(ChatColor.GRAY).then("" + num).color(ChatColor.AQUA).then("): ").color(ChatColor.GRAY);
                m1.send(player);
            } else {
                m1.then(compass).color(ChatColor.DARK_AQUA).then(" (").color(ChatColor.GRAY).then("" + num).color(ChatColor.AQUA).then("): ").color(ChatColor.GRAY);
                m1.send(player);
                m2.send(player);
            }
        }
    }

    public void TrackAll(final Material mat1, final Material mat2, final Player player, final Player player2) {

        new BukkitRunnable() {
            @Override
            public void run() {
                int northDist = findBlock(player.getWorld(), 0, -1, mat1, mat2);
                int southDist = findBlock(player.getWorld(), 0, 1, mat1, mat2);
                int eastDist = findBlock(player.getWorld(), -1, 0, mat1, mat2);
                int westDist = findBlock(player.getWorld(), 1, 0, mat1, mat2);
                boolean northDists = findEnd(player.getWorld(), 0, -1, mat2);
                boolean southDists = findEnd(player.getWorld(), 0, 1, mat2);
                boolean eastDists = findEnd(player.getWorld(), -1, 0, mat2);
                boolean westDists = findEnd(player.getWorld(), 1, 0, mat2);

                MessageManager.sendMessage(player, "&3Results&7:");
                if (northDist > 0 && northDists) {
                    TrackDirAll(player, 0, -northDist * 25, player2);
                }

                if (eastDist > 0 && eastDists) {
                    TrackDirAll(player, -eastDist * 25, 0, player2);
                }
                if (southDist > 0 && southDists) {

                    TrackDirAll(player, 0, southDist * 25, player2);
                }
                if (westDist > 0 && westDists) {
                    TrackDirAll(player, westDist * 25, 0, player2);
                }
            }
        }.runTask(Legacy.getInstance());
    }

    public void TrackAllSpawn(final Player player, final Player player2) {
        new BukkitRunnable() {
            @Override
            public void run() {
                MessageManager.sendMessage(player, "&3Results&7:");

                TrackDirAll(player, 0, -300, player2);
                TrackDirAll(player, -300, 0, player2);
                TrackDirAll(player, 0, 300, player2);
                TrackDirAll(player, 300, 0, player2);
            }
        }.runTask(Legacy.getInstance());
    }

    public void TrackAll(Player player, Player player2) {
        Block block = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()).getBlock();
        if (block.getType() == Material.DIAMOND_BLOCK && sm.getSpawn().isInSpawnRadius(block.getLocation())) {
            TrackAllSpawn(player, player2);
        } else if (block.getType() == Material.DIAMOND_BLOCK && isPerm(block)) {
            TrackAll(Material.OBSIDIAN, Material.GOLD_BLOCK, player, player2);
        } else if (isTemp(block) && block.getType().equals(Material.OBSIDIAN)) {
            MessageManager.sendMessage(player, "&cYou cannot /track all on a temptracker.");
        } else {
            MessageManager.sendMessage(player, "&cYou must be on a tracker to use this command.");
        }
    }

    public boolean findEnd(final World world, final int x, final int z, Material mat2) {
        boolean yes = false;

        for (int i = 1; i < 1000; i++) {
            Block block = world.getBlockAt(this.mx + x * i, this.my, this.mz + z * i);
            Material bmat = block.getType();

            if (bmat == mat2) {
                yes = true;
                break;
            }
        }
        return yes;
    }

    public String getEnvironmentName(World.Environment env) {
        if (env == World.Environment.NORMAL) {
            return "&aOverworld";
        } else if (env == World.Environment.NETHER) {
            return "&cNether";
        } else {
            return "&eEnd";
        }
    }
}
