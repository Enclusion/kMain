package com.mccritz.kmain;

import com.mccritz.kmain.commands.*;
import com.mccritz.kmain.economy.EconomyListener;
import com.mccritz.kmain.economy.EconomyManager;
import com.mccritz.kmain.kits.KitManager;
import com.mccritz.kmain.listeners.*;
import com.mccritz.kmain.mobcapture.MobCaptureListener;
import com.mccritz.kmain.profiles.ProfileListeners;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.spawn.SpawnListeners;
import com.mccritz.kmain.spawn.SpawnManager;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.teams.TeamTagManager;
import com.mccritz.kmain.teams.commands.BaseTeamCommand;
import com.mccritz.kmain.teams.listeners.TeamListeners;
import com.mccritz.kmain.utils.BlockUtils;
import com.mccritz.kmain.utils.PlayerUtility;
import com.mccritz.kmain.utils.command.Register;
import com.mccritz.kmain.utils.glaedr.Glaedr;
import com.mccritz.kmain.utils.mobs.*;
import com.mccritz.kmain.warps.OverrideListener;
import com.mccritz.kmain.warps.WarpManager;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.mongodb.morphia.Morphia;

/**
 * Created by Calvin on 4/22/2015.
 * Project: Legacy
 */
public class Legacy extends JavaPlugin {

    private static Legacy instance;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private Morphia morphia;
    private TeleportationHandler teleportationHandler;
    private ProfileManager profileManager;
    private KitManager kitManager;
    private EconomyManager economyManager;
    private SpawnManager spawnManager;
    private TeamManager teamManager;
    private TeamTagManager teamTagManager;
    private WarpManager warpManager;
    private BlockUtils blockUtils;
    private Glaedr glaedr;

    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        setupDatabase();
        clearVillagers();

        teleportationHandler = new TeleportationHandler();
        profileManager = new ProfileManager();
        kitManager = new KitManager();
        economyManager = new EconomyManager();
        spawnManager = new SpawnManager();
        teamManager = new TeamManager();
        teamTagManager = new TeamTagManager();
        warpManager = new WarpManager();
        blockUtils = new BlockUtils(this);
        glaedr = new Glaedr(this, "&c&lMcCritZ");

        registerCommands();
        registerListeners();
        registerDummyEntities();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : PlayerUtility.getOnlinePlayers()) {
                    if (all.hasMetadata("spawnprotected")) {
                        all.removeMetadata("spawnprotected", instance);

                        spawnManager.getSpawnProtected().add(all.getUniqueId());

                        Command_hud.getDisplayList().add(all.getUniqueId());

//                        teamTagManager.initPlayer(all);
//                        teamTagManager.sendTeamsToPlayer(all);
//                        teamTagManager.reloadPlayer(all);
                    }
                }
            }
        }.runTaskLaterAsynchronously(this, 20L);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : PlayerUtility.getOnlinePlayers()) {
                    PlayerUtility.updateScoreboard(all);
                }
            }
        }.runTaskTimerAsynchronously(this, 10L, 10L);
    }

    public void onDisable() {
        kitManager.saveAllKits();
        profileManager.saveProfiles();
        teamManager.saveTeams();
        teamTagManager.getAllTeams().forEach(teamTagManager::sendPacketsRemoveTeam);
        warpManager.saveWarps();
        spawnManager.saveSpawn();

        clearVillagers();

        for (Player all : PlayerUtility.getOnlinePlayers()) {
            all.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

            if (spawnManager.getSpawnProtected().contains(all.getUniqueId())) {
                all.setMetadata("spawnprotected", new FixedMetadataValue(this, true));
            }
        }

        blockUtils.regenAllBlocks(true);

        mongoClient.close();
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
            register.registerCommand("setspawn", new Command_setspawn());
            register.registerCommand("spawn", new Command_spawn());
            register.registerCommand("testmessage", new Command_testmessage());
            register.registerCommand("track", new Command_track());
            register.registerCommand("toggledm", new Command_toggledm());
            register.registerCommand("clearspawn", new Command_clearspawn());
            register.registerCommand("home", new Command_home());
            register.registerCommand("sethome", new Command_sethome());
            register.registerCommand("homeas", new Command_homeas());
            register.registerCommand("kit", new Command_kit());
            register.registerCommand("setkit", new Command_setkit());
            register.registerCommand("delkit", new Command_delkit());
            register.registerCommand("hud", new Command_hud());
            register.registerCommand("tpa", new Command_tpa());
            register.registerCommand("tpaccept", new Command_tpaccept());
            register.registerCommand("tpahere", new Command_tpahere());
            register.registerCommand("tpdeny", new Command_tpdeny());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new MobCaptureListener(), this);
        getServer().getPluginManager().registerEvents(new ProfileListeners(), this);
        getServer().getPluginManager().registerEvents(new OverrideListener(), this);
        getServer().getPluginManager().registerEvents(new IronBoatListener(), this);
        getServer().getPluginManager().registerEvents(new SalvagingListeners(), this);
        getServer().getPluginManager().registerEvents(new SpawnListeners(), this);
        getServer().getPluginManager().registerEvents(new OptimzationsListener(), this);
        getServer().getPluginManager().registerEvents(new SoupListener(), this);
        getServer().getPluginManager().registerEvents(new TeamListeners(), this);
        getServer().getPluginManager().registerEvents(new CombatLogListener(), this);
        getServer().getPluginManager().registerEvents(new EconomyListener(), this);
        getServer().getPluginManager().registerEvents(new Command_toggledm(), this);
    }

    public void registerDummyEntities() {
        //Passive
        new CustomEntityController().registerEntity("Sheep", 91, EntitySheep.class, CustomEntitySheep.class);
        new CustomEntityController().registerEntity("Pig", 90, EntityPig.class, CustomEntityPig.class);
        new CustomEntityController().registerEntity("Cow", 92, EntityCow.class, CustomEntityCow.class);
        new CustomEntityController().registerEntity("Chicken", 93, EntityChicken.class, CustomEntityChicken.class);
        new CustomEntityController().registerEntity("Bat", 65, EntityBat.class, CustomEntityBat.class);
        new CustomEntityController().registerEntity("Ocelot", 98, EntityOcelot.class, CustomEntityOcelot.class);

        //Aggressive
        new CustomEntityController().registerEntity("Blaze", 61, EntityBlaze.class, CustomEntityBlaze.class);
        new CustomEntityController().registerEntity("Creeper", 50, EntityCreeper.class, CustomEntityCreeper.class);
        new CustomEntityController().registerEntity("SilverFish", 60, EntitySilverfish.class, CustomEntitySilverfish.class);
        new CustomEntityController().registerEntity("Skeleton", 51, EntitySkeleton.class, CustomEntitySkeleton.class);
        new CustomEntityController().registerEntity("Zombie", 54, EntityZombie.class, CustomEntityZombie.class);
        new CustomEntityController().registerEntity("PigZombie", 57, EntityPigZombie.class, CustomEntityPigZombie.class);
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
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(1000000).build();
        mongoClient = new MongoClient(getConfig().getString("database.host"), options);
        mongoDatabase = mongoClient.getDatabase(getConfig().getString("database.database-name"));

        morphia = new Morphia();
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public Morphia getMorphia() {
        return morphia;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public KitManager getKitManager() {
        return kitManager;
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
        return teamTagManager;
    }

    public TeleportationHandler getTeleportationHandler() {
        return teleportationHandler;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public BlockUtils getBlockUtils() {
        return blockUtils;
    }

    public Glaedr getGlaedr() {
        return glaedr;
    }

    public static Legacy getInstance() {
        return instance;
    }
}