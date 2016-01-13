package code.breakmc.legacy.economy;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Calvin on 5/30/2015.
 */
public class EconomyListener implements Listener {

    private Legacy main = Legacy.getInstance();
    private EconomyManager eco = main.getEconomyManager();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if (e.getInventory().equals(eco.getItemInventory1())) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                if (item.getType() == Material.STAINED_GLASS_PANE) {
                    if (item.getDurability() == 5) {
                        p.openInventory(eco.getItemInventory2());
                    }
                }
            }
        } else if (e.getInventory().equals(eco.getItemInventory2())) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                if (item.getType() == Material.STAINED_GLASS_PANE) {
                    if (item.getDurability() == 14) {
                        p.openInventory(eco.getItemInventory1());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getItem() != null) {
                if (e.getItem().getType() == Material.PAPER && e.getItem().hasItemMeta()) {
                    if (e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().hasDisplayName()) {
                        main.getProfileManager().getProfile(p.getUniqueId()).setBalance(main.getProfileManager().getProfile(p.getUniqueId()).getBalance() + 300);
                        MessageManager.sendMessage(p, "&aYou have received $300, sorry for the rollback!");

                        if (e.getPlayer().getItemInHand().getAmount() > 1) {
                            e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
                        } else {
                            e.getPlayer().setItemInHand(null);
                            e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                        }
                    }
                }
            }
        }
    }
}
