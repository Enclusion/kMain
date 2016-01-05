package code.breakmc.legacy.kits;

import code.breakmc.legacy.Legacy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class KitManager {

    private Legacy main = Legacy.getInstance();
    private File file;
    private FileConfiguration config;
    private List<Kit> kits = new ArrayList<>();

    public KitManager() {
        file = new File(main.getDataFolder(), "kits.yml");
        config = YamlConfiguration.loadConfiguration(file);
        loadKits();
    }

    public void loadKits() {
        if (!config.contains("kits")) {
            return;
        }

        for (String kitName : config.getConfigurationSection("kits").getKeys(false)) {
            Kit k = new Kit(kitName);
            int delay = config.contains("kits." + kitName + ".delay") ? config.getInt("kits." + kitName + ".delay") : 0;
            List<ItemStack> kitContents = (List<ItemStack>) config.getList("kits." + kitName + ".inventory");

            k.setContents(kitContents);
            k.setDelay(delay);

            kits.add(k);
        }

        List<String> kitNames = new ArrayList<>();
        for (Kit k : kits) {
            kitNames.add(k.getName());
        }

        if (kits.size() != 0) {
            main.getLogger().log(Level.INFO, "Kits Loaded: " + kitNames.toString().replace("[", "").replace("]", ""));
        }
    }

    public void saveAllKits() {
        config.set("kits", null);

        for (Kit k : kits) {
            config.set("kits." + k.getName() + ".delay", k.getDelay());
            config.set("kits." + k.getName() + ".inventory", k.getContents());
        }

        try {
            config.save(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        List<String> kitNames = kits.stream().map(Kit::getName).collect(Collectors.toList());

        if (kits.size() != 0) {
            main.getLogger().log(Level.INFO, "Kits Saved: " + kitNames.toString().replace("[", "").replace("]", ""));
        }
    }

    public Kit getKit(String kitName) {
        for (Kit k : kits) {
            if (k.getName().equalsIgnoreCase(kitName)) {
                return k;
            }
        }

        return null;
    }

    public List<Kit> getKits() {
        return kits;
    }
}
