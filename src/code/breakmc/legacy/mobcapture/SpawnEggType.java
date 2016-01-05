package code.breakmc.legacy.mobcapture;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public enum SpawnEggType {

    CREEPER(50, EntityType.CREEPER, 25),
    SKELETON(51, EntityType.SKELETON, 25),
    SPIDER(52, EntityType.SPIDER, 25),
    ZOMBIE(54, EntityType.ZOMBIE, 25),
    SLIME(55, EntityType.SLIME, 5),
    GHAST(56, EntityType.GHAST, 25),
    PIG_ZOMBIE(57, EntityType.PIG_ZOMBIE, 25),
    ENDERMAN(58, EntityType.ENDERMAN, 25),
    CAVE_SPIDER(59, EntityType.CAVE_SPIDER, 25),
    SILVERFISH(60, EntityType.SILVERFISH, 25),
    BLAZE(61, EntityType.BLAZE, 25),
    MAGMA_CUBE(62, EntityType.MAGMA_CUBE, 25),
    BAT(65, EntityType.BAT, 1),
    WITCH(66, EntityType.WITCH, 25),
    PIG(90, EntityType.PIG, 1),
    SHEEP(91, EntityType.SHEEP, 1),
    COW(92, EntityType.COW, 1),
    CHICKEN(93, EntityType.CHICKEN, 5),
    SQUID(94, EntityType.SQUID, 1),
    WOLF(95, EntityType.WOLF, 1),
    MUSHROOM_COW(96, EntityType.MUSHROOM_COW, 30),
    SNOWMAN(97, EntityType.SNOWMAN, 1),
    OCELOT(98, EntityType.OCELOT, 1),
    VILLAGER(120, EntityType.VILLAGER, 35),
    HORSE(100, EntityType.HORSE, 1);

    private int id;
    private EntityType entityType;
    private int exp_cost;

    SpawnEggType(int id, EntityType entityType, int exp_cost) {
        this.id = id;
        this.entityType = entityType;
        this.exp_cost = exp_cost;
    }

    public static SpawnEggType getByName(String name) {
        if (name == null) {
            return null;
        }
        for (SpawnEggType spawnEggType : values()) {
            if (spawnEggType.getName().equalsIgnoreCase(name)) {
                return spawnEggType;
            }
        }
        return null;
    }

    public static SpawnEggType getByEntityType(EntityType entityType) {
        if (entityType == null) {
            return null;
        }
        for (SpawnEggType spawnEggType : values()) {
            if (spawnEggType.getEntityType().equals(entityType)) {
                return spawnEggType;
            }
        }
        return null;
    }

    public int getId() {
        return this.id;
    }

    public int getCost() {
        return exp_cost;
    }

    public boolean isInstance(Entity e) {
        return e.getType().equals(getEntityType());
    }

    public String getName() {
        return this.entityType.getName();
    }

    public EntityType getEntityType() {
        return this.entityType;
    }
}