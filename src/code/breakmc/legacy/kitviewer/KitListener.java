package code.breakmc.legacy.kitviewer;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Calvin on 5/8/2015.
 */
public class KitListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains("Hover") ||
                e.getInventory().getName().contains("Premium") ||
                e.getInventory().getName().contains("Vip") ||
                e.getInventory().getName().contains("Pro") ||
                e.getInventory().getName().contains("Legend") ||
                e.getInventory().getName().contains("Master") ||
                e.getInventory().getName().contains("Infamous") ||
                e.getInventory().getName().contains("Advanced") ||
                e.getInventory().getName().contains("Extreme") ||
                e.getInventory().getName().contains("High") ||
                e.getInventory().getName().contains("Higher") ||
                e.getInventory().getName().contains("Highest") ||
                e.getInventory().equals(KitViewerManager.workbench1)) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getType() == Material.PAPER) {
                    if (e.getCurrentItem().hasItemMeta()) {
                        if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Premium")) {
                                KitViewerManager.openPremiumKit(p);
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Vip")) {
                                KitViewerManager.openVipKit(p);
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Pro")) {
                                KitViewerManager.openProKit(p);
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Legend")) {
                                KitViewerManager.openLegendKit(p);
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Master")) {
                                KitViewerManager.openMasterKit(p);
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Infamous")) {
                                KitViewerManager.openInfamousKit(p);
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Advanced")) {
                                KitViewerManager.openAdvancedKit(p);
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Extreme")) {
                                KitViewerManager.openExtremeKit(p);
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("High")) {
                                KitViewerManager.openHighKit(p);
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Higher")) {
                                KitViewerManager.openHigherKit(p);
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Highest")) {
                                KitViewerManager.openHighestKit(p);
                            }
                        }
                    }
                } else if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
                    if (e.getCurrentItem().hasItemMeta()) {
                        if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Premium")) {
                                p.closeInventory();
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                                MessageManager.sendMessage(p, " ");
                                MessageManager.sendMessage(p, "&aWebstore link: &bwww.breakmc.com/store");
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Vip")) {
                                p.closeInventory();
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                                MessageManager.sendMessage(p, " ");
                                MessageManager.sendMessage(p, "&aWebstore link: &bwww.breakmc.com/store");
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Pro")) {
                                p.closeInventory();
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                                MessageManager.sendMessage(p, " ");
                                MessageManager.sendMessage(p, "&aWebstore link: &bwww.breakmc.com/store");
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Legend")) {
                                p.closeInventory();
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                                MessageManager.sendMessage(p, " ");
                                MessageManager.sendMessage(p, "&aWebstore link: &bwww.breakmc.com/store");
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Master")) {
                                p.closeInventory();
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                                MessageManager.sendMessage(p, " ");
                                MessageManager.sendMessage(p, "&aWebstore link: &bwww.breakmc.com/store");
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Infamous")) {
                                p.closeInventory();
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                                MessageManager.sendMessage(p, " ");
                                MessageManager.sendMessage(p, "&aWebstore link: &bwww.breakmc.com/store");
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Advanced")) {
                                p.closeInventory();
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                                MessageManager.sendMessage(p, " ");
                                MessageManager.sendMessage(p, "&aWebstore link: &bwww.breakmc.com/store");
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Extreme")) {
                                p.closeInventory();
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                                MessageManager.sendMessage(p, " ");
                                MessageManager.sendMessage(p, "&aWebstore link: &bwww.breakmc.com/store");
                                MessageManager.sendMessage(p, "&2&l&n&m----------------------------");
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.hasBlock()) {
            if (e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
                BlockState st = e.getClickedBlock().getState();
                Sign sign = (Sign) st;

                if (Legacy.getInstance().getSpawnManager().getSpawnProtected().contains(p.getUniqueId())) {
                    if (sign.getLine(1).contains("Premium")) {
                        KitViewerManager.openPremiumView(p);
                    } else if (sign.getLine(1).contains("Vip")) {
                        KitViewerManager.openVIPView(p);
                    } else if (sign.getLine(1).contains("Pro")) {
                        KitViewerManager.openProView(p);
                    } else if (sign.getLine(1).contains("Legend")) {
                        KitViewerManager.openLegendView(p);
                    } else if (sign.getLine(1).contains("Master")) {
                        KitViewerManager.openMasterView(p);
                    } else if (sign.getLine(1).contains("Infamous")) {
                        KitViewerManager.openInfamousView(p);
                    } else if (sign.getLine(1).contains("Advanced")) {
                        KitViewerManager.openAdvancedView(p);
                    } else if (sign.getLine(1).contains("Extreme")) {
                        KitViewerManager.openExtremeView(p);
                    } else if (sign.getLine(1).contains("Iron Boat")) {
                        KitViewerManager.openIronBoatRecipe(p);
                    } else if (sign.getLine(1).contains("Economy")) {
                        p.openInventory(Legacy.getInstance().getEconomyManager().getItemInventory1());
                    }
                }
            }
        }
    }
}
