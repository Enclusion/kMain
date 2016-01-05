package code.breakmc.legacy.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.Random;

/**
 * Created by Calvin on 5/4/2015.
 */
public class HungerListener implements Listener {

    @EventHandler
    public void onHungerLoss(FoodLevelChangeEvent e) {
        if ((e.getFoodLevel() < ((Player) e.getEntity()).getFoodLevel()) && (new Random().nextInt(100) > 4)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getType() == InventoryType.BREWING) {
            if (e.getSlot() == 0 || e.getSlot() == 1 || e.getSlot() == 2) {
                if (e.getClick() == ClickType.SHIFT_LEFT && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || e.getClick() == ClickType.SHIFT_RIGHT && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
