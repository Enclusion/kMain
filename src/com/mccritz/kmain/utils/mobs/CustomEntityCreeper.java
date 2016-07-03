package com.mccritz.kmain.utils.mobs;

import net.minecraft.server.v1_7_R4.EntityCreeper;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R4.World;
import org.bukkit.craftbukkit.v1_7_R4.util.UnsafeList;

import java.lang.reflect.Field;

public class CustomEntityCreeper extends EntityCreeper {

    public CustomEntityCreeper(World world) {
        super(world);

        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            final Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            bField.set(this.goalSelector, new UnsafeList());
            bField.set(this.targetSelector, new UnsafeList());
            cField.set(this.goalSelector, new UnsafeList());
            cField.set(this.targetSelector, new UnsafeList());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
