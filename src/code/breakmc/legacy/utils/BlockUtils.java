package code.breakmc.legacy.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class BlockUtils {

    private final Plugin plugin;
    private static final Map<Location, BlockState> toRegen = Maps.newHashMap();
    private static final List<BlockRegen> tasks = Lists.newArrayList();

    public BlockUtils(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean hasTasks() {
        return !tasks.isEmpty();
    }

    public void forceTasks() {
        for (BlockRegen task : tasks) {
            task.cancel();
        }
    }

    public boolean alreadyScheduled(Location location) {
        return toRegen.containsKey(location);
    }

    public boolean request(Block block, long ticksLater) {
        return request(block.getState(), ticksLater);
    }

    public boolean request(BlockState state, long ticksLater) {
        if (alreadyScheduled(state.getLocation())) {
            return false;
        }

        toRegen.put(state.getLocation(), state);

        BlockRegen regenTask = new BlockRegen(state.getLocation(), state);
        regenTask.runTaskLater(plugin, ticksLater);

        return tasks.add(regenTask);
    }

    public void onBlockBreak(BlockBreakEvent event, long ticksLater) {
        request(event.getBlock(), ticksLater);
    }

    public void onBlockPlace(BlockPlaceEvent event, long ticksLater) {
        request(event.getBlockReplacedState(), ticksLater);
    }

    public void regenAllBlocks(boolean isShutdown) {
        for (BlockState states : toRegen.values()) {
            if (!isShutdown) {
                BlockRegen regenTask = new BlockRegen(states.getLocation(), states);
                regenTask.runTaskLater(plugin, 1L);

                tasks.add(regenTask);
            } else {
                states.getLocation().getBlock().setType(states.getType());
                states.getLocation().getBlock().setData(states.getBlock().getData());
            }
        }
    }

    public class BlockRegen extends BukkitRunnable {

        private final BlockState state;
        private final Location location;
        private boolean hasRun = false;

        protected BlockRegen(Location location, BlockState state) {
            this.state = state;
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }

        public BlockState getState() {
            return state;
        }

        @Override
        public void cancel() {
            if (!hasRun) {
                run();
            }

            super.cancel();
        }

        @Override
        public void run() {
            getLocation().getBlock().setType(getState().getType());
            getLocation().getBlock().setData(getState().getBlock().getData());

            finish();
        }

        public void finish() {
            this.hasRun = true;
            if (toRegen.containsKey(getLocation())) {
                toRegen.remove(getLocation());
            }

            if (tasks.contains(this)) {
                tasks.remove(this);
            }
        }
    }

    public static void regenerateBlock(final Block block, final Material type, final byte data) {
        final Location loc = block.getLocation();
        loc.getWorld().playEffect(loc, Effect.STEP_SOUND, (type == Material.AIR) ? block.getType().getId() : type.getId());
        block.setType(type);
        block.setData(data);
        block.getLocation().getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, (block.getState().getBlock().getType() == Material.AIR) ? block.getState().getBlock().getType().getId() : block.getState().getBlock().getType().getId());
    }

    public static List<Location> circle(final Location loc, final int radius, final int height, final boolean hollow, final boolean sphere, final int plusY) {
        final List<Location> circleblocks = new ArrayList<>();
        final int cx = loc.getBlockX();
        final int cy = loc.getBlockY();
        final int cz = loc.getBlockZ();
        for (int x = cx - radius; x <= cx + radius; ++x) {
            for (int z = cz - radius; z <= cz + radius; ++z) {
                for (int y = sphere ? (cy - radius) : cy; y < (sphere ? (cy + radius) : (cy + height)); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < radius * radius && (!hollow || dist >= (radius - 1) * (radius - 1))) {
                        final Location l = new Location(loc.getWorld(), (double) x, (double) (y + plusY), (double) z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    public static List<Block> getRelativeBlocks(final Location center, final int radius, final Collection<Block> startBlocks, final Set<Material> types) {
        final List<Block> relative = new ArrayList<>();
        for (int x = -radius; x < radius; ++x) {
            for (int y = -radius; y < radius; ++y) {
                for (int z = -radius; z < radius; ++z) {
                    final Block newBlock = center.getBlock().getRelative(x, y, z);
                    if (types.contains(newBlock.getType()) && newBlock.getLocation().distance(center) <= radius) {
                        relative.add(newBlock);
                    }
                }
            }
        }
        return relative;
    }

    public static List<Block> getNearbyBlocks(final Location center, final int radius) {
        final List<Location> locs = circle(center, radius, radius, true, true, 0);
        final List<Block> blocks = new ArrayList<>();
        for (final Location loc : locs) {
            blocks.add(loc.getBlock());
        }
        return blocks;
    }

    public static Map<Location, BlockState> getToRegen() {
        return toRegen;
    }
}