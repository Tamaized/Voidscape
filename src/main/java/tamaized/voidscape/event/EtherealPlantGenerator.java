package tamaized.voidscape.event;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.block.EtherealFruitBlocks;
import tamaized.voidscape.util.LevelUtil;

@Component
public class EtherealPlantGenerator {

	@Autowired
	private LevelUtil levelUtil;

	@Autowired
	private ModDataAttachments dataAttachments;

	@Autowired
	private EtherealFruitBlocks etherealFruitBlocks;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(PlayerTickEvent.class, event -> {
			Player player = event.getEntity();
			Level level = player.level();
			if (!player.isSpectator() && levelUtil.isInVoidDimension(level)) {
				if (!level.isClientSide() && player.tickCount % 15 == 0 && player.getRandom().nextFloat() <= 0.15F) {
					final int dist = 64;
					final int rad = dist / 2;
					BlockPos dest = player.blockPosition().offset(randomOffset(player, dist, rad), randomOffset(player, dist, rad), randomOffset(player, dist, rad));
					if (level.getBlockState(dest).isAir() && etherealFruitBlocks.ETHEREAL_FRUIT_VOID.get().defaultBlockState().canSurvive(level, dest))
						level.setBlockAndUpdate(dest, switch (level.getBiome(dest).unwrapKey().map(ResourceKey::location).orElse(ResourceLocation.withDefaultNamespace("")).getPath()) {
							// TODO: Make this datapack friendly (maybe something like data/voidscape/ethereal_fruit_growths/ethereal_fruit_id.json {"replace": true/false, biomes: ["id"]})
							default -> etherealFruitBlocks.ETHEREAL_FRUIT_VOID.get().defaultBlockState();
							case "null" -> etherealFruitBlocks.ETHEREAL_FRUIT_NULL.get().defaultBlockState();
							case "overworld" -> etherealFruitBlocks.ETHEREAL_FRUIT_OVERWORLD.get().defaultBlockState();
							case "nether" -> etherealFruitBlocks.ETHEREAL_FRUIT_NETHER.get().defaultBlockState();
							case "end" -> etherealFruitBlocks.ETHEREAL_FRUIT_END.get().defaultBlockState();
						});
				}
			}
		});
	}

	private int randomOffset(Player player, int distance, int radius) {
		return player.getRandom().nextInt(distance) - radius;
	}

}
