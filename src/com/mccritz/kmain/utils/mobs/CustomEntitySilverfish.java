package com.mccritz.kmain.utils.mobs;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_7_R4.util.UnsafeList;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntitySilverfish;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R4.World;

public class CustomEntitySilverfish extends EntitySilverfish {

    public CustomEntitySilverfish(World world) {
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

    @Override
    public void a(Entity ent, float f) {
    }
}
