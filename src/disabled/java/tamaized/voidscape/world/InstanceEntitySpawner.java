package tamaized.voidscape.world;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.EntityCorruptedPawnBoss;
import tamaized.voidscape.entity.EntityVoidsWrathBoss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class InstanceEntitySpawner {

	private static final Map<ResourceLocation, List<Consumer<Level>>> REGISTRY = new HashMap<>();

	static {
		registerSpawner(new ResourceLocation(Voidscape.MODID, "psychosis"), (level) -> {
			EntityVoidsWrathBoss boss = new EntityVoidsWrathBoss(level);
			boss.initInstance();
			boss.restrictTo(new BlockPos(42, 75, 4), 32);
			boss.moveTo(boss.getRestrictCenter().getX() + 0.5F, boss.getRestrictCenter().getY(), boss.getRestrictCenter().getZ() + 0.5F);
			boss.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(34.5F, 77, 4.5F));
			level.addFreshEntity(boss);
		});
		registerSpawner(new ResourceLocation(Voidscape.MODID, "pawn"), (level) -> {
			EntityCorruptedPawnBoss boss = new EntityCorruptedPawnBoss(level);
			boss.initInstance();
			boss.restrictTo(new BlockPos(18, 61, 0), 1024);
			boss.moveTo(boss.getRestrictCenter().getX() + 0.5F, boss.getRestrictCenter().getY(), boss.getRestrictCenter().getZ() + 0.5F);
			boss.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(0.5F, 63, 0.5F));
			level.addFreshEntity(boss);
		});
	}

	private InstanceEntitySpawner() {

	}

	@SafeVarargs
	public static void registerSpawner(ResourceLocation instance, Consumer<Level>... entities) {
		REGISTRY.computeIfAbsent(instance, (e) -> new ArrayList<>());
		for (Consumer<Level> entity : entities)
			REGISTRY.get(instance).add(entity);
	}

	public static void spawnEntities(Instance instance) {
		REGISTRY.computeIfAbsent(instance.generator().group(), (e) -> new ArrayList<>());
		REGISTRY.get(instance.generator().group()).forEach(entity -> entity.accept(instance.getLevel()));
	}

}
