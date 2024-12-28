package tamaized.voidscape.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.util.LevelUtil;

@Component
public class VoidDimensionValidSpawnHandler {

	@Autowired
	private LevelUtil levelUtil;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(MobSpawnEvent.SpawnPlacementCheck.class, event -> {
			if (event.getSpawnType() == MobSpawnType.NATURAL && levelUtil.isInVoidDimension(event.getLevel().getLevel()) && event.getLevel().getLightEmission(event.getPos()) <= 7) {
				event.setResult(MobSpawnEvent.SpawnPlacementCheck.Result.SUCCEED);
			}
		});

		bus.addListener(MobSpawnEvent.PositionCheck.class, event -> {
			if (event.getSpawnType() == MobSpawnType.NATURAL && levelUtil.isInVoidDimension(event.getLevel().getLevel())) {
				Player player = event.getLevel().getNearestPlayer(event.getX(), event.getY(), event.getZ(), -1.0D, false);
				if (player != null &&
					isValidPositionForMob(
						event.getLevel().getLevel(),
						event.getEntity(),
						player.distanceToSqr(event.getX(), event.getY(), event.getZ()),
						BlockPos.containing(event.getX(), event.getY(), event.getZ())))
					event.setResult(MobSpawnEvent.PositionCheck.Result.SUCCEED);
				else
					event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
			}
		});
	}

	private boolean isValidPositionForMob(ServerLevel serverWorld_, Mob mobEntity_, double double_, BlockPos pos) {
		if (double_ > (double) (mobEntity_.getType().getCategory().getDespawnDistance() * mobEntity_.getType().getCategory().getDespawnDistance()) && mobEntity_.removeWhenFarAway(double_)) {
			return false;
		} else {
			return mobEntity_.checkSpawnObstruction(serverWorld_) &&
				   (!(mobEntity_ instanceof Zoglin || mobEntity_ instanceof IEthereal) || SpawnPlacementTypes.ON_GROUND.isSpawnPositionOk(serverWorld_, pos, mobEntity_.getType()));
		}
	}

}
