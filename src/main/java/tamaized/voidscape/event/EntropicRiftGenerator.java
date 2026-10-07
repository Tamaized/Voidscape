package tamaized.voidscape.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.entity.EntropicRiftEntity;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.util.LevelUtil;

@Component
public class EntropicRiftGenerator {

	@Autowired
	private LevelUtil levelUtil;

	@Autowired
	private ModDataAttachments dataAttachments;

	@Autowired
	private ModEntities entities;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(PlayerTickEvent.Post.class, event -> {
			Player player = event.getEntity();
			Level level = player.level();
			if (!player.isSpectator() && levelUtil.isInVoidDimension(level)) {
				if (!level.isClientSide() &&
					player.getData(dataAttachments.INSANITY).getParanoia() / 600F > 0.75F &&
					player.tickCount % 30 == 0 &&
					player.getRandom().nextFloat() <= 0.60F
				) {
					final int dist = 16;
					final int rad = dist / 2;
					BlockPos dest = player.blockPosition().offset(randomOffset(player, dist, rad), randomOffset(player, dist, rad), randomOffset(player, dist, rad));
					if (
						!level.getBlockState(dest).isAir() &&
							level.getBlockState(dest.above()).isAir() &&
							level.getBlockState(dest.above(2)).isAir() &&
							level.getBlockState(dest.above(3)).isAir() &&
							level.getEntitiesOfClass(
								EntropicRiftEntity.class,
								new AABB(dest.getBottomCenter(), dest.above().getCenter()).inflate(8.0D)
							).isEmpty()
					) {
						EntropicRiftEntity rift = new EntropicRiftEntity(entities.ENTROPIC_RIFT.get(), level, player).markForDissipation();
						rift.snapTo(dest.above().getBottomCenter());
						level.addFreshEntity(rift);
						if (player instanceof ServerPlayer serverPlayer)
							serverPlayer.connection.send(new ClientboundSoundPacket(
								BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EVOKER_PREPARE_SUMMON),
								SoundSource.PLAYERS,
								rift.getX(),
								rift.getY(),
								rift.getZ(),
								4F,
								0.25F,
								level.getRandom().nextLong()
							));

					}
				}
			}
		});
	}

	private int randomOffset(Player player, int distance, int radius) {
		return player.getRandom().nextInt(distance) - radius;
	}

}
