package com.mccritz.kmain;

import com.mccritz.kmain.commands.*;
import com.mccritz.kmain.listeners.*;
import com.mccritz.kmain.profiles.ProfileListeners;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.spawn.SpawnListeners;
import com.mccritz.kmain.spawn.SpawnManager;
import com.mccritz.kmain.teams.TeamManager;
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
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class kMain extends JavaPlugin {

    private static kMain instance;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private TeleportationHandler teleportationHandler;
    private ProfileManager profileManager;
    private SpawnManager spawnManager;
    private TeamManager teamManager;
    private WarpManager warpManager;
    private BlockUtils blockUtils;
    private Glaedr glaedr;

    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        setupDatabase();

        teleportationHandler = new TeleportationHandler();
        profileManager = new ProfileManager();
        spawnManager = new SpawnManager();
        teamManager = new TeamManager();
        warpManager = new WarpManager();
        blockUtils = new BlockUtils(this);
        glaedr = new Glaedr("&c&lTeams");

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

                        HudCommand.getDisplayList().add(all.getUniqueId());
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
        profileManager.saveProfiles();
        teamManager.saveTeams();
        warpManager.saveWarps();
        spawnManager.saveSpawn();

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
        try {
            Register register = new Register();

            register.registerCommand("team", new BaseTeamCommand());
            register.registerCommand("balance", new BalanceCommand());
            register.registerCommand("balancetop", new BalanceTopCommand());
            register.registerCommand("economy", new EconomyCommand());
            register.registerCommand("go", new GoCommand());
            register.registerCommand("goas", new GoAsCommand());
            register.registerCommand("setspawn", new SetSpawnCommand());
            register.registerCommand("spawn", new SpawnCommand());
            register.registerCommand("testmessage", new TestMessageCommand());
            register.registerCommand("track", new TrackCommand());
            register.registerCommand("toggledm", new ToggleDMCommand());
            register.registerCommand("clearspawn", new ClearSpawnCommand());
            register.registerCommand("home", new HomeCommand());
            register.registerCommand("sethome", new SetHomeCommand());
            register.registerCommand("homeas", new HomeAsCommand());
            register.registerCommand("hud", new HudCommand());
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
        getServer().getPluginManager().registerEvents(new ExpBottleListeners(), this);
        getServer().getPluginManager().registerEvents(new ToggleDMCommand(), this);
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

    public void setupDatabase() {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(1000000).build();
        mongoClient = new MongoClient(getConfig().getString("database.host"), options);
        mongoDatabase = mongoClient.getDatabase(getConfig().getString("database.database-name"));
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
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

    public static kMain getInstance() {
        return instance;
    }
}