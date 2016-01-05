package code.breakmc.legacy.mobcapture;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Calvin on 5/5/2015.
 */
public class MobCaptureListener implements Listener {

    @EventHandler
    public void onCap(EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            if (e.getDamager() instanceof Egg) {
                Egg egg = (Egg) e.getDamager();
                Player p = (Player) egg.getShooter();
                SpawnEggType type = SpawnEggType.getByEntityType(e.getEntity().getType());

                if (type == null) {
                    MessageManager.sendMessage(p, "&cYou cannot capture this type of mob!");
                    return;
                }

                if (e.getEntity() instanceof Ageable) {
                    Ageable ageable = (Ageable) e.getEntity();
                    if (!ageable.isAdult() && ageable.getType() != EntityType.SLIME) {
                        MessageManager.sendMessage(p, "&cYou cannot capture babies!");
                        return;
                    }
                }

                if (p.getLevel() < type.getCost()) {
                    MessageManager.sendMessage(p, "&cYou need " + type.getCost() + " levels to capture this mob!");
                    return;
                }

                if (e.getEntity() instanceof Animals && e.getEntity().getType() != EntityType.VILLAGER && e.getEntity().getType() != EntityType.MUSHROOM_COW && e.getEntity().getType() != EntityType.SLIME) {
                    Location location = e.getEntity().getLocation();
                    e.getEntity().remove();
                    World world = location.getWorld();
                    ItemStack item = new ItemStack(Material.MONSTER_EGG, 1, (short) type.getId());
                    world.dropItem(location, item);
                    MessageManager.sendMessage(p, "&cYou have successfully captured this '" + type.getName() + "'!");
                    return;
                }

                if (p.getLevel() >= 25) {
                    if (e.getEntity() instanceof Monster) {
                        Location location = e.getEntity().getLocation();
                        e.getEntity().remove();
                        World world = location.getWorld();
                        ItemStack item = new ItemStack(Material.MONSTER_EGG, 1, (short) type.getId());
                        world.dropItem(location, item);
                        MessageManager.sendMessage(p, "&cYou have successfully captured this '" + type.getName() + "'!");
                        return;
                    }
                }

                if (e.getEntity().getType().equals(EntityType.MUSHROOM_COW)) {
                    Location location = e.getEntity().getLocation();
                    e.getEntity().remove();
                    World world = location.getWorld();
                    ItemStack item = new ItemStack(Material.MONSTER_EGG, 1, (short) type.getId());
                    world.dropItem(location, item);
                    p.setLevel(0);
                    p.setExp(0);
                    MessageManager.sendMessage(p, "&cCapturing this '" + type.getName() + "' has drained all of your xp levels from you!");
                    return;
                }

                if (e.getEntity().getType().equals(EntityType.VILLAGER)) {
                    Location location = e.getEntity().getLocation();
                    e.getEntity().remove();
                    World world = location.getWorld();
                    ItemStack item = new ItemStack(Material.MONSTER_EGG, 1, (short) type.getId());
                    world.dropItem(location, item);
                    p.setLevel(0);
                    p.setExp(0);
                    MessageManager.sendMessage(p, "&cCapturing this '" + type.getName() + "' has drained all of your xp levels from you!");
                    return;
                }

                if (e.getEntity().getType().equals(EntityType.SLIME)) {
                    Location location = e.getEntity().getLocation();
                    e.getEntity().remove();
                    World world = location.getWorld();
                    ItemStack item = new ItemStack(Material.MONSTER_EGG, 1, (short) type.getId());
                    world.dropItem(location, item);
                    p.setLevel(0);
                    p.setExp(0);
                    MessageManager.sendMessage(p, "&cCapturing this '" + type.getName() + "' has drained all of your xp levels from you!");
                }
            }
        }
    }
}
