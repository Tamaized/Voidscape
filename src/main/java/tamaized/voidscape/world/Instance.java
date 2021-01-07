package tamaized.voidscape.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Dimension;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import tamaized.voidscape.Voidscape;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Instance {

	private final RegistryKey<World> location;
	private final Dimension dimension;
	public boolean locked;
	public int tick;
	private int unloadTick;
	private InstanceType type = InstanceType.Solo;
	private ServerWorld level;
	private int maxPlayers = 1;
	private List<PlayerEntity> players = new ArrayList<>();

	public Instance(RegistryKey<Dimension> loc, Dimension dimension) {
		location = RegistryKey.create(Registry.DIMENSION_REGISTRY, loc.location());
		this.dimension = dimension;
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
		if (level.getChunkSource().getLoadedChunksCount() > 0) {
			unloadChunks();
			return false;
		}
		unloadTick = 0;
		Voidscape.LOGGER.info("Loading Instance: ".concat(this.location.location().toString()));
		return true;
	}

	public void unload() {
		players.clear();
		locked = false;
		unloadTick = 0;
		Voidscape.LOGGER.info("Unloading Instance: ".concat(this.location.location().toString()));
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

	public void addPlayer(PlayerEntity player) {
		if (locked() || players.contains(player) || players.size() >= maxPlayers)
			return;
		if (!active() && !load())
			return;
		Entity entity = player.changeDimension(Voidscape.getWorld(player.level, location), InstanceTeleporter.INSTANCE);
		if (entity instanceof PlayerEntity) {
			players.add((PlayerEntity) entity);
		}
	}

	private void unloadChunks() {
		if (level.getChunkSource().chunkMap instanceof HackyWorldGen.DeepFreezeChunkManager)
			((HackyWorldGen.DeepFreezeChunkManager) level.getChunkSource().chunkMap).unload();
	}

	public void tick() {
		if (!active()) {
			if (!level.players().isEmpty())
				new ArrayList<>(level.players()).forEach(player -> {
					player.setHealth(player.getMaxHealth() * 0.1F);
					player.changeDimension(Voidscape.getWorld(player.level, World.OVERWORLD), VoidTeleporter.INSTANCE);
				});
			tick = 0;
			locked = false;
			return;
		}
		if (!locked() && tick % (20 * 30) == 0)
			locked = true;
		if (!level.players().isEmpty())
			level.players().stream().filter(player -> !players.contains(player)).collect(Collectors.toList()).
					forEach(player -> {
						player.setHealth(player.getMaxHealth() * 0.1F);
						player.changeDimension(Voidscape.getWorld(player.level, World.OVERWORLD), VoidTeleporter.INSTANCE);
					});
		if (unloadTick == 0 && locked() && level.players().isEmpty())
			unloadChunks();
		if (level.getChunkSource().getLoadedChunksCount() == 0) {
			if (unloadTick % 30 == 0)
				unload();
			else
				unloadTick++;
		}
		tick++;
	}

	public enum InstanceType {
		Solo, Normal, Insane
	}

}
