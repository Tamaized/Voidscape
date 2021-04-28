package tamaized.voidscape.world;

import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.EntityCorruptedPawnBoss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public final class InstanceEntitySpawner {

	private static final Map<ResourceLocation, List<BiConsumer<World, Instance.InstanceType>>> REGISTRY = new HashMap<>();

	static {
		registerSpawner(new ResourceLocation(Voidscape.MODID, "pawn"), (level, type) -> {
			EntityCorruptedPawnBoss boss = new EntityCorruptedPawnBoss(level);
			boss.initInstanceType(type);
			boss.restrictTo(new BlockPos(18, 61, 0), 1024);
			boss.moveTo(boss.getRestrictCenter().getX() + 0.5F, boss.getRestrictCenter().getY(), boss.getRestrictCenter().getZ() + 0.5F);
			boss.lookAt(EntityAnchorArgument.Type.EYES, new Vector3d(0.5F, 63, 0.5F));
			level.addFreshEntity(boss);
		});
	}

	private InstanceEntitySpawner() {

	}

	@SafeVarargs
	public static void registerSpawner(ResourceLocation instance, BiConsumer<World, Instance.InstanceType>... entities) {
		REGISTRY.computeIfAbsent(instance, (e) -> new ArrayList<>());
		for (BiConsumer<World, Instance.InstanceType> entity : entities)
			REGISTRY.get(instance).add(entity);
	}

	public static void spawnEntities(Instance instance) {
		REGISTRY.computeIfAbsent(instance.generator().group(), (e) -> new ArrayList<>());
		REGISTRY.get(instance.generator().group()).forEach(entity -> entity.accept(instance.getLevel(), instance.type()));
	}

}
