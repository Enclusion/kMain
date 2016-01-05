package code.breakmc.legacy.teams.listeners;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Alex on 6/6/2015.
 */
public class ItemListeners implements Listener {


    private TeamManager tm = Legacy.getInstance().getTeamManager();

    public ItemListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(this, Legacy.getInstance());
    }

    // Comrade's Soup
    @EventHandler
    public void onComradeClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getItemInHand() != null
                && p.getItemInHand().getType() == Material.MUSHROOM_SOUP
                && p.getItemInHand().getItemMeta() != null
                && p.getItemInHand().getItemMeta().getDisplayName() != null
                && p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("Comrade's Soup")) {
            if (p.getExhaustion() == 20) {
                p.setExhaustion(19);
            }

        }
    }

    @EventHandler
    public void onComradeEat(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        if (p.getItemInHand() != null
                && p.getItemInHand().getType() == Material.MUSHROOM_SOUP
                && p.getItemInHand().getItemMeta() != null
                && p.getItemInHand().getItemMeta().getDisplayName() != null
                && p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("Comrade's Soup")) {
            p.setExhaustion(20);
            p.setHealth(20D);
            p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 1, 60));
            if (tm.hasTeam(p.getUniqueId())) {
                Team team = tm.getTeam(p.getUniqueId());
                for (Player teamPlayers : team.getOnlinePlayers()) {
                    if (teamPlayers.getLocation().distance(p.getLocation()) <= 30) {
                        teamPlayers.setHealth(20);
                        teamPlayers.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 1, 60));
                    }
                }
            }

        }
    }
    //Comrade's Soup

    //Hermes Sandals
    public void hermes(final Player p) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (p.getInventory().getBoots() != null
                            && p.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS
                            && p.getInventory().getBoots().getItemMeta() != null
                            && p.getInventory().getBoots().getItemMeta().getDisplayName() != null
                            && p.getInventory().getBoots().getItemMeta().getDisplayName().equalsIgnoreCase("Hermes Sandals")) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 0));
                        if (tm.hasTeam(p.getUniqueId())) {
                            for (Player teamPlayers : tm.getTeam(p.getUniqueId()).getOnlinePlayers()) {
                                if (teamPlayers.getLocation().distance(p.getLocation()) <= 30) {
                                    teamPlayers.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1));
                                    teamPlayers.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 0));
                                }
                            }
                        }
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimerAsynchronously(Legacy.getInstance(), 0L, 20L);

        }

    @EventHandler
    public void onHermesInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getItemInHand() != null
                && p.getItemInHand().getType() == Material.MUSHROOM_SOUP
                && p.getItemInHand().getItemMeta() != null
                && p.getItemInHand().getItemMeta().getDisplayName() != null
                && p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("Hermes Sandals")) {
            if (p.getInventory().getBoots() == null) {
                hermes(p);
            }
        }

    }

    @EventHandler
    public void onHermesInventory(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (p.getInventory().getBoots() != null
                && p.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS
                && p.getInventory().getBoots().getItemMeta() != null
                && p.getInventory().getBoots().getItemMeta().getDisplayName() != null
                && p.getInventory().getBoots().getItemMeta().getDisplayName().equalsIgnoreCase("Hermes Sandals")) {
            hermes(p);
        }
    }
    //Hermes Sandals

    //Gryphon Wing

    HashMap<UUID, Integer> potTime = new HashMap<>();
    @EventHandler
    public void onGryphonClick(PlayerInteractEvent e) {
       final  Player p = e.getPlayer();
        if (p.getInventory().getBoots() != null
                && p.getInventory().getBoots().getType() == Material.QUARTZ
                && p.getInventory().getBoots().getItemMeta() != null
                && p.getInventory().getBoots().getItemMeta().getDisplayName() != null
                && p.getInventory().getBoots().getItemMeta().getDisplayName().equalsIgnoreCase("Gryphon Wing")) {
            if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                PotionEffect effect = null;
                for (PotionEffect effects : p.getActivePotionEffects()) {
                    if (effects.getType() == PotionEffectType.SPEED && effects.getAmplifier() == 2) {
                        effect = effects;
                    }
                }
                if (effect == null) {
                    return;
                }
                if (!(potTime.containsKey(p.getUniqueId()))) {
                    potTime.put(p.getUniqueId(), effect.getDuration());
                }
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 3));
            p.sendMessage(ChatColor.AQUA + "You " + ChatColor.DARK_AQUA + "have used a Gryphon Wing!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (potTime.containsKey(p.getUniqueId())) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, potTime.get(p.getUniqueId()), 1));
                        potTime.remove(p.getUniqueId());
                    }
                }
            }.runTaskLater(Legacy.getInstance(), 200L);
            if (tm.hasTeam(p.getUniqueId())) {
                tm.getTeam(p.getUniqueId()).sendMessage("&b" + p.getName() + " &3has used a Gryphon Wing!");
                for (final Player teamPlayers : tm.getTeam(p.getUniqueId()).getOnlinePlayers()) {
                    if (teamPlayers.hasPotionEffect(PotionEffectType.SPEED)) {
                        PotionEffect effect = null;
                        for (PotionEffect effects : teamPlayers.getActivePotionEffects()) {
                            if (effects.getType() == PotionEffectType.SPEED && effects.getAmplifier() == 2) {
                                effect = effects;
                            }
                        }
                        if (effect == null) {
                            return;
                        }
                        if (!(potTime.containsKey(teamPlayers.getUniqueId()))) {
                            potTime.put(teamPlayers.getUniqueId(), effect.getDuration());
                        }
                    }
                    teamPlayers.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 3));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (potTime.containsKey(teamPlayers.getUniqueId())) {
                                teamPlayers.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, potTime.get(p.getUniqueId()), 1));
                                potTime.remove(p.getUniqueId());
                            }
                        }
                    }.runTaskLater(Legacy.getInstance(), 200L);
                }
            }
        }
    }
    //Gryphon Wing


}
