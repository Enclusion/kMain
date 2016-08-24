package com.mccritz.kmain.kits;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.utils.DateUtil;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.PlayerUtility;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Getter
@Setter
public class Kit {

    private ProfileManager pm = kMain.getInstance().getProfileManager();
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
                MessageManager.message(p, "&7You do not have enough inventory space.");
                return;
            }

            for (ItemStack is : getContents()) {
                if (is == null) {
                    continue;
                }

                p.getInventory().addItem(is);
            }

            p.updateInventory();

            if (!p.hasPermission("kits.*")) {
                pm.getProfile(p.getUniqueId()).getUsedKits().put(name, new GregorianCalendar().getTimeInMillis());
            }

            MessageManager.message(p, "&7You have received kit &c" + name + "&7.");
        } else if (nextUse < 1L) {
            MessageManager.message(p, "&cYou have already used this kit &conce&7.");
        } else {
            MessageManager.message(p, "&7You can use this kit in &c" + DateUtil.formatDateDiff(nextUse) + "&7.");
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
