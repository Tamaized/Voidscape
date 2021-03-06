package tamaized.voidscape.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Dimension;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import tamaized.voidscape.Voidscape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Instance {

	private final RegistryKey<World> location;
	private final Dimension dimension;
	public boolean locked;
	public int tick;
	private ServerWorld level;
	private int unloadTick = 30 * 20;
	private InstanceType type = InstanceType.Unrestricted;
	private int maxPlayers = 1;
	private List<PlayerEntity> players = new ArrayList<>();
	private boolean unloading;

	public Instance(RegistryKey<Dimension> loc, Dimension dimension) {
		if (!(dimension.generator() instanceof InstanceChunkGenerator))
			throw new IllegalArgumentException("Dimension Generator must be of Type: " + InstanceChunkGenerator.class);
		location = RegistryKey.create(Registry.DIMENSION_REGISTRY, loc.location());
		this.dimension = dimension;
	}

	public InstanceChunkGenerator generator() {
		return (InstanceChunkGenerator) dimension.generator();
	}

	public RegistryKey<World> location() {
		return location;
	}

	public Instance setType(InstanceType type) {
		this.type = type;
		return this;
	}

	public InstanceType type() {
		return type;
	}

	public Instance maxPlayers(int a) {
		maxPlayers = a;
		return this;
	}

	public int maxPlayers() {
		return maxPlayers;
	}

	public void init(ServerWorld level) {
		this.level = level;
		Voidscape.LOGGER.info("Initializing Instance: ".concat(this.location.location().toString()));
	}

	public boolean load() {
		Voidscape.LOGGER.info("Loading Instance: ".concat(this.location.location().toString()));
		if (level.getChunkSource().getLoadedChunksCount() > 0) {
			unloadChunks();
			return false;
		}
		InstanceEntitySpawner.spawnEntities(this);
		unloadTick = 0;
		Voidscape.LOGGER.info("Loaded Instance: ".concat(this.location.location().toString()));
		return true;
	}

	public void unload() {
		Voidscape.LOGGER.info("Unloading Instance: ".concat(this.location.location().toString()));
		players.clear();
		level.getEntities().forEach(Entity::remove);
		locked = false;
		tick = 0;
		unloadTick++;
		type = InstanceType.Unrestricted;
		unloading = false;
		Voidscape.LOGGER.info("Unloaded Instance: ".concat(this.location.location().toString()));
	}

	public ServerWorld getLevel() {
		return level;
	}

	public Dimension dimension() {
		return dimension;
	}

	public boolean active() {
		return !players.isEmpty();
	}

	public boolean locked() {
		return locked;
	}

	public boolean unloading() {
		return unloading;
	}

	public List<PlayerEntity> players() {
		return ImmutableList.copyOf(players);
	}

	public void addPlayer(PlayerEntity player) {
		if ((unloadTick < 30 * 20 && !active()) || locked() || players.contains(player) || players.size() >= maxPlayers)
			return;
		if (!active() && !load())
			return;
		final int i = players.size();
		final float p = 0.785F;
		player.moveTo(Math.round(3F * Math.cos(p * i)) + 0.5F, player.getY(), Math.round(3F * Math.sin(p * i)) + 0.5F, -90F, 0F);
		Entity entity = player.changeDimension(Voidscape.getWorld(player.level, location), InstanceTeleporter.INSTANCE);
		if (entity instanceof PlayerEntity)
			players.add((PlayerEntity) entity);
	}

	private void unloadChunks() {
		if (level.getChunkSource().chunkMap instanceof HackyWorldGen.DeepFreezeChunkManager) {
			Voidscape.LOGGER.info("Unloading Instance Chunks: ".concat(this.location.location().toString()));
			unloading = true;
			((HackyWorldGen.DeepFreezeChunkManager) level.getChunkSource().chunkMap).unload();
		}
	}

	public void tick() {
		InstanceSpecialEffects.doEffects(this);
		level.players().forEach(player -> player.abilities.mayBuild = false);
		if (!active()) {
			if (!level.players().isEmpty())
				new ArrayList<>(level.players()).forEach(player -> {
					player.setHealth(player.getMaxHealth() * 0.1F);
					player.changeDimension(Voidscape.getWorld(player.level, World.OVERWORLD), VoidTeleporter.INSTANCE);
				});
			tick = 0;
			locked = false;
		} else if (!locked() && tick % (20 * 30) == 0)
			locked = true;
		if (!level.players().isEmpty())
			level.players().stream().filter(player -> !players.contains(player)).collect(Collectors.toList()).
					forEach(player -> {
						player.setHealth(player.getMaxHealth() * 0.1F);
						player.changeDimension(Voidscape.getWorld(player.level, World.OVERWORLD), VoidTeleporter.INSTANCE);
					});
		if (level.getChunkSource().getLoadedChunksCount() == 0) {
			if (unloadTick == 20 * 10)
				unloadChunks();
			if (unloadTick == 20 * 30)
				unload();
			else
				unloadTick++;
		} else
			unloadTick = 0;
		tick++;
	}

	public enum InstanceType {
		Unrestricted, Normal, Insane;
		private static InstanceType[] VALUES = values();

		@Nullable
		public static InstanceType fromOrdinal(int ordinal) {
			if (ordinal < 0 || ordinal >= VALUES.length)
				return null;
			return VALUES[ordinal];
		}
	}

}
