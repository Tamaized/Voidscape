package tamaized.voidscape.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.EffectCures;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.dimension.SpawnPointTeleporter;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModItemComponentDirectory;
import tamaized.voidscape.util.LevelUtil;

@Component
public class VoidDimensionDeathHandler {

	@Autowired
	private LevelUtil levelUtil;

	@Autowired
	private SpawnPointTeleporter spawnPointTeleporter;

	@Autowired
	private ModDataAttachments dataAttachments;

	@Autowired
	private ModItemComponentDirectory items;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(EventPriority.HIGHEST, LivingDeathEvent.class, this::handlePlayerDeath);
		bus.addListener(EventPriority.LOWEST, LivingDeathEvent.class, this::handleMobDeath);
	}

	private void handlePlayerDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			if (levelUtil.isInVoidDimension(player.level())) {
				player.setHealth(player.getMaxHealth() * 0.1F);
				player.removeEffectsCuredBy(EffectCures.MILK);
				player.resetFallDistance();
				if (!player.level().isClientSide())
					levelUtil.getPlayersSpawnLevel(player)
						.or(() -> levelUtil.getLevel(player.level(), Level.OVERWORLD))
						.ifPresent(level -> {
							player.changeDimension(spawnPointTeleporter.make(player, level));
							event.setCanceled(true);
						});
			}
		}
	}

	private void handleMobDeath(LivingDeathEvent event) {
		if (!(event.getEntity() instanceof Player) && !(event.getEntity() instanceof IEthereal)) {
			if ((event.getSource().getDirectEntity() instanceof Player || event.getSource().getEntity() instanceof Player) &&
				levelUtil.isInVoidDimension(event.getEntity().level()) &&
				event.getEntity().getData(dataAttachments.INSANITY).getInfusion() >= 200 &&
				event.getEntity().getRandom().nextInt(3) > 0) {
				Containers.dropItemStack(
					event.getEntity().level(),
					event.getEntity().getX(),
					event.getEntity().getY(),
					event.getEntity().getZ(),
					new ItemStack(items.miscItems().ETHEREAL_ESSENCE.get())
				);
			}
		}
	}

}
