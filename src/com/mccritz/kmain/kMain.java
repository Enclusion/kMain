package com.mccritz.kmain;

import com.mccritz.kmain.commands.*;
import com.mccritz.kmain.economy.EconomyManager;
import com.mccritz.kmain.economy.commands.*;
import com.mccritz.kmain.events.EventManager;
import com.mccritz.kmain.events.end.commands.EndEventCommand;
import com.mccritz.kmain.events.end.listeners.EndEventListeners;
import com.mccritz.kmain.kits.KitManager;
import com.mccritz.kmain.listeners.*;
import com.mccritz.kmain.profiles.ProfileListeners;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.spawn.SpawnListeners;
import com.mccritz.kmain.spawn.SpawnManager;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.teams.commands.BaseTeamCommand;
import com.mccritz.kmain.teams.listeners.TeamListeners;
import com.mccritz.kmain.utils.BlockUtils;
import com.mccritz.kmain.utils.ItemDb;
import com.mccritz.kmain.utils.PlayerUtility;
import com.mccritz.kmain.utils.command.Register;
import com.mccritz.kmain.utils.glaedr.Glaedr;
import com.mccritz.kmain.utils.glaedr.scoreboards.PlayerScoreboard;
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
    private KitManager kitManager;
    private EventManager eventManager;
    private EconomyManager economyManager;
    private SpawnManager spawnManager;
    private TeamManager teamManager;
    private WarpManager warpManager;
    private BlockUtils blockUtils;
    private Glaedr glaedr;
    private ItemDb itemDb;

    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        setupDatabase();

        teleportationHandler = new TeleportationHandler();
        profileManager = new ProfileManager();
        eventManager = new EventManager();
        economyManager = new EconomyManager();
        spawnManager = new SpawnManager();
        teamManager = new TeamManager();
        warpManager = new WarpManager();
        blockUtils = new BlockUtils(this);
        glaedr = new Glaedr(this, "&c&lMcCritZ");
        itemDb = new ItemDb();

        if (getConfig().getBoolean("kits.enabled"))
            kitManager = new KitManager();

        registerCommands();
        registerListeners();
        registerDummyEntities();

        new BukkitRunnable() {
            @Override
            public void run() {
                glaedr.checkPlayers();

                for (Player all : PlayerUtility.getOnlinePlayers()) {
                    if (all.hasMetadata("spawnprotected")) {
                        all.removeMetadata("spawnprotected", instance);

                        spawnManager.getSpawnProtected().add(all.getUniqueId());

                        HudCommand.getDisplayList().add(all.getUniqueId());
                        new PlayerScoreboard(all);
                    }
                }

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "toggleworld world_the_end");
            }
        }.runTaskLater(this, 20L);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : PlayerUtility.getOnlinePlayers()) {
                    PlayerUtility.updateScoreboard(all);
                }
            }
        }.runTaskTimerAsynchronously(this, 10L, 5L);
    }

    public void onDisable() {
        profileManager.saveProfiles(false);
        teamManager.saveTeams(false);
        warpManager.saveWarps(false);
        spawnManager.saveSpawn();
        economyManager.saveSales(false);

        if (getConfig().getBoolean("kits.enabled"))
            kitManager.saveAllKits();

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
            register.registerCommand("teamas", new TeamAsCommand());
            register.registerCommand("hud", new HudCommand());
            register.registerCommand("debug", new DebugCommand());
            register.registerCommand("forcesave", new ForceSaveCommand());
            register.registerCommand("editspawn", new EditSpawnCommand());
            register.registerCommand("endevent", new EndEventCommand());
            register.registerCommand("blackhole", new BlackholeCommand());

            if (getConfig().getBoolean("kits.enabled")) {
                register.registerCommand("kit", new KitCommand());
                register.registerCommand("setkit", new SetKitCommand());
                register.registerCommand("delkit", new DelKitCommand());
            }

            /*
             Economy Commands
            */
            register.registerCommand("balance", new BalanceCommand());
            register.registerCommand("buy", new BuyCommand());
            register.registerCommand("sell", new SellCommand());
            register.registerCommand("price", new PriceCommand());
            register.registerCommand("deposit", new DepositCommand());
            register.registerCommand("withdraw", new WithdrawCommand());
            register.registerCommand("halteconomy", new HaltEconomyCommand());
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
        Bukkit.getPluginManager().registerEvents(new EndEventListeners(), this);
    }

    public void registerDummyEntities() {
        //Passive
        new CustomEntityController().registerEntity("Bat", 65, EntityBat.class, CustomEntityBat.class);

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

    public KitManager getKitManager() {
        return kitManager;
    }

    public EventManager getEventManager() {
        return eventManager;
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

    public ItemDb getItemDb() {
        return itemDb;
    }

    public static kMain getInstance() {
        return instance;
    }
}