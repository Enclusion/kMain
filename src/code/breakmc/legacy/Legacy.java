package code.breakmc.legacy;

import code.breakmc.legacy.commands.*;
import code.breakmc.legacy.economy.EconomyListener;
import code.breakmc.legacy.economy.EconomyManager;
import code.breakmc.legacy.enderchest.EnderchestListener;
import code.breakmc.legacy.enderchest.EnderchestManager;
import code.breakmc.legacy.kitviewer.KitListener;
import code.breakmc.legacy.listeners.*;
import code.breakmc.legacy.mobcapture.MobCaptureListener;
import code.breakmc.legacy.profiles.ProfileListeners;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.spawn.SpawnListeners;
import code.breakmc.legacy.spawn.SpawnManager;
import code.breakmc.legacy.teams.BaseTeamCommand;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.teams.TeamTagManager;
import code.breakmc.legacy.teams.listeners.ChatListener;
import code.breakmc.legacy.teams.listeners.FriendlyFireListener;
import code.breakmc.legacy.teams.listeners.JoinListener;
import code.breakmc.legacy.utils.BlockUtils;
import code.breakmc.legacy.utils.LagTask;
import code.breakmc.legacy.utils.NMSUtils;
import code.breakmc.legacy.utils.command.Register;
import code.breakmc.legacy.utils.mobs.*;
import code.breakmc.legacy.warps.OverrideListener;
import code.breakmc.legacy.warps.WarpManager;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.MongoClient;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.UnknownHostException;
import java.util.logging.Level;

/**
 * Created by Calvin on 4/22/2015.
 * Project: Legacy
 */
public class Legacy extends JavaPlugin {

    private static Legacy instance;
    private DB database;
    private DB registerDatabase;
    private ProfileManager profileManager;
    private EconomyManager economyManager;
    private SpawnManager spawnManager;
    private TeamManager teamManager;
    private TeamTagManager teamtagManager;
    private WarpManager warpManager;
    private EnderchestManager enderchestManager;
    private BlockUtils blockUtils;

    public void onEnable() {
        instance = this;

        setupDatabase();
        clearVillagers();

        profileManager = new ProfileManager();
        economyManager = new EconomyManager();
        spawnManager = new SpawnManager();
        teamManager = new TeamManager();
        teamtagManager = new TeamTagManager();
        warpManager = new WarpManager();
        enderchestManager = new EnderchestManager();
        blockUtils = new BlockUtils(this);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new LagTask(), 100L, 1L);

        registerCommands();
        registerListeners();
        registerDummyEntities();
    }

    public void onDisable() {
        profileManager.saveProfiles();
        teamManager.saveTeams();
        warpManager.saveWarps();
        spawnManager.saveSpawn();
        enderchestManager.saveEnderchest();

        teamtagManager.getAllTeams().forEach(teamtagManager::sendPacketsRemoveTeam);

        clearVillagers();

        blockUtils.regenAllBlocks(true);

        database.getMongo().close();
    }

    public void registerCommands() {
        getCommand("buy").setExecutor(economyManager);
        getCommand("sell").setExecutor(economyManager);
        getCommand("price").setExecutor(economyManager);
        getCommand("value").setExecutor(economyManager);
        getCommand("shop").setExecutor(economyManager);
        getCommand("pay").setExecutor(economyManager);

        try {
            Register register = new Register();

            register.registerCommand("team", new BaseTeamCommand());
            register.registerCommand("balance", new Command_balance());
            register.registerCommand("balancetop", new Command_baltop());
            register.registerCommand("economy", new Command_eco());
            register.registerCommand("go", new Command_go());
            register.registerCommand("goas", new Command_goas());
            register.registerCommand("lag", new Command_lag());
            register.registerCommand("logout", new Command_logout());
            register.registerCommand("setspawn", new Command_setspawn());
            register.registerCommand("spawn", new Command_spawn());
            register.registerCommand("testmessage", new Command_testmessage());
            register.registerCommand("track", new Command_track());
            register.registerCommand("toggledm", new Command_toggledm());
            register.registerCommand("clearspawn", new Command_clearspawn());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new MobCaptureListener(), this);
        getServer().getPluginManager().registerEvents(new ProfileListeners(), this);
        getServer().getPluginManager().registerEvents(new OverrideListener(), this);
        getServer().getPluginManager().registerEvents(new IronBoatListener(), this);
        getServer().getPluginManager().registerEvents(new KitListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new FriendlyFireListener(), this);
        getServer().getPluginManager().registerEvents(new SalvagingListeners(), this);
        getServer().getPluginManager().registerEvents(new EnderchestListener(), this);
        getServer().getPluginManager().registerEvents(new CooldownListeners(), this);
        getServer().getPluginManager().registerEvents(new SpawnListeners(), this);
        getServer().getPluginManager().registerEvents(new HungerListener(), this);
        getServer().getPluginManager().registerEvents(new SoupListener(), this);
        getServer().getPluginManager().registerEvents(new StrengthListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new CombatLogListener(), this);
        getServer().getPluginManager().registerEvents(new EconomyListener(), this);
        getServer().getPluginManager().registerEvents(new Command_toggledm(), this);
    }

    public void registerDummyEntities() {
        //Passive
        new NMSUtils().registerEntity("Sheep", 91, EntitySheep.class, Dummy_sheep.class);
        new NMSUtils().registerEntity("Pig", 90, EntityPig.class, Dummy_pig.class);
        new NMSUtils().registerEntity("Cow", 92, EntityCow.class, Dummy_cow.class);
        new NMSUtils().registerEntity("Chicken", 93, EntityChicken.class, Dummy_chicken.class);
        new NMSUtils().registerEntity("Bat", 65, EntityBat.class, Dummy_bat.class);
        new NMSUtils().registerEntity("MushroomCow", 96, EntityMushroomCow.class, Dummy_mooshroom.class);
        new NMSUtils().registerEntity("Ocelot", 98, EntityOcelot.class, Dummy_ocelot.class);

        //Aggressive
        new NMSUtils().registerEntity("Blaze", 61, EntityBlaze.class, Dummy_blaze.class);
        new NMSUtils().registerEntity("CaveSpider", 59, EntityCaveSpider.class, Dummy_cave_spider.class);
        new NMSUtils().registerEntity("Creeper", 50, EntityCreeper.class, Dummy_creeper.class);
        new NMSUtils().registerEntity("SilverFish", 60, EntitySilverfish.class, Dummy_silverfish.class);
        new NMSUtils().registerEntity("Skeleton", 51, EntitySkeleton.class, Dummy_skeleton.class);
        new NMSUtils().registerEntity("Zombie", 54, EntityZombie.class, Dummy_zombie.class);
        new NMSUtils().registerEntity("PigZombie", 57, EntityPigZombie.class, Dummy_zombiepigmen.class);
    }

    public void clearVillagers() {
        for (World w : Bukkit.getWorlds()) {
            w.getEntities().stream().filter(ent -> ent instanceof Villager).forEach(ent -> {
                Villager villager = (Villager) ent;

                if (villager.getMetadata("logger") != null && villager.hasMetadata("logger")) {
                    villager.remove();
                }
            });
        }
    }

    public void setupDatabase() {
        try {
            database = MongoClient.connect(new DBAddress(getConfig().getString("database.host"), getConfig().getString("database.database-name")));
            registerDatabase = MongoClient.connect(new DBAddress(getConfig().getString("database.host"), "breakmc"));
            this.getLogger().log(Level.INFO, "Sucessfully connected to MongoDB.");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            this.getLogger().log(Level.INFO, "Failed to connect to MongoDB.");
        }
    }

    public DB getDb() {
        return database;
    }

    public DB getRegisterDatabase() {
        return registerDatabase;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public TeamTagManager getTeamTagManager() {
        return teamtagManager;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public EnderchestManager getEnderchestManager() {
        return enderchestManager;
    }

    public BlockUtils getBlockUtils() {
        return blockUtils;
    }

    public static Legacy getInstance() {
        return instance;
    }
}