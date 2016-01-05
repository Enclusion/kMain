package code.breakmc.legacy.economy;

import code.breakmc.legacy.Legacy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
}
