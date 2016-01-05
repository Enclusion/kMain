package code.breakmc.legacy.kits;

import code.BreakMC.origin.util.DateUtil;
import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.PlayerUtility;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Getter
@Setter
public class Kit {

    private ProfileManager pm = Legacy.getInstance().getProfileManager();
    private String name;
    private List<ItemStack> contents = new ArrayList<>();
    private int delay;

    public Kit(String name) {
        this.name = name;
    }

    public Kit(String name, List<ItemStack> contents) {
        this.name = name;
        this.contents = contents;
    }

    public Kit(String name, List<ItemStack> contents, int delay) {
        this.name = name;
        this.contents = contents;
        this.delay = delay;
    }

    public void apply(Player p) {
        long nextUse = getNextUse(pm.getProfile(p.getUniqueId()));

        if (nextUse == 0L) {
            if (getContents() == null || getContents().isEmpty()) {
                return;
            }

            if (PlayerUtility.checkSlotsAvailable(p.getInventory()) < getContents().size()) {
                MessageManager.sendMessage(p, "&cYou do not have enough inventory space");
                return;
            }

            for (ItemStack is : getContents()) {
                if (is == null) {
                    continue;
                }

                if (is.getType().toString().contains("DIAMOND") || is.getType().toString().contains("IRON") || is.getType().toString().contains("GOLD")) {
                    ItemMeta im = is.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + "Unsalvagable");
                    im.setLore(lore);
                    is.setItemMeta(im);
                }

                p.getInventory().addItem(is);
            }

            p.updateInventory();

            if (!p.hasPermission("kits.*")) {
                pm.getProfile(p.getUniqueId()).getUsedKits().put(name, new GregorianCalendar().getTimeInMillis());
            }

            MessageManager.sendMessage(p, "&aYou have received kit \"" + name + "\".");
        } else if (nextUse < 0L) {
            MessageManager.sendMessage(p, "&cYou can only use this kit once!");
        } else {
            MessageManager.sendMessage(p, "&7You can use this kit in " + DateUtil.formatDateDiff(nextUse));
        }
    }

    public long getNextUse(Profile prof) {
        long lastUse = prof.getUsedKits().containsKey(name) ? prof.getUsedKits().get(name) : 0;
        Calendar current = new GregorianCalendar();

        Calendar delayUse = new GregorianCalendar();
        delayUse.setTimeInMillis(lastUse);
        delayUse.add(Calendar.SECOND, delay);
        delayUse.add(Calendar.MILLISECOND, (int) ((delay * 1000.0) % 1000.0));

        if (lastUse == 0L || lastUse > current.getTimeInMillis()) {
            return 0L;
        } else if (delay < 0d) {
            return -1;
        } else if (delayUse.before(current)) {
            return 0L;
        } else {
            // If the kit has been used recently, return the next time it can be used.
            return delayUse.getTimeInMillis();
        }
    }
}
