package tamaized.voidscape.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModDimensions;

import java.util.Objects;
import java.util.Optional;

@Component
public class LevelUtil {

	@Autowired
	private ModDimensions dimensions;

	public Optional<ServerLevel> asServerLevel(Level level) {
		if (level instanceof ServerLevel serverLevel) {
			return Optional.of(serverLevel);
		}

		return Optional.empty();
	}

	public boolean isInVoidDimension(@Nullable Level level) {
		if (level == null)
			return false;
		return level.dimension() == dimensions.VOID;
	}

	public Optional<ServerLevel> getLevel(Level level, ResourceKey<Level> dest) {
		return Optional.ofNullable(level.getServer())
			.map(server -> server.getLevel(dest));
	}

	public Optional<ServerLevel> getPlayersSpawnLevel(ServerPlayer player) {
		ResourceKey<Level> dest = Level.OVERWORLD;
		if (player.getRespawnConfig() != null) {
			dest = player.getRespawnConfig().respawnData().dimension();
		}
		return getLevel(player.level(), dest);
	}

	public Optional<ServerLevel> getVoidDimension(Level currentLevel) {
		return getLevel(currentLevel, dimensions.VOID);
	}

	public Optional<ServerLevel> getDimensionForTeleport(Level currentLevel) {
		return isInVoidDimension(currentLevel) ? getLevel(currentLevel, Level.OVERWORLD) : getVoidDimension(currentLevel);
	}

	public long getServerSideLevelSeed() {
		return Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer()).getWorldGenSettings().options().seed();
	}

}
