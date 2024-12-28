package tamaized.voidscape.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.block.OreBlocks;
import tamaized.voidscape.util.LevelUtil;

@Component
public class VoidicCrystalOreGenerator {

	@Autowired
	private LevelUtil levelUtil;

	@Autowired
	private ModDataAttachments dataAttachments;

	@Autowired
	private OreBlocks oreBlocks;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(PlayerTickEvent.class, event -> {
			Player player = event.getEntity();
			Level level = player.level();
			if (!player.isSpectator() && levelUtil.isInVoidDimension(level)) {
				if ((!level.isClientSide() || player.getData(dataAttachments.INSANITY).getParanoia() / 600F > 0.25F) &&
					player.tickCount % 30 == 0 &&
					player.getRandom().nextFloat() <= 0.20F
				) {
					final int dist = 64;
					final int rad = dist / 2;
					BlockPos dest = player.blockPosition().offset(randomOffset(player, dist, rad), randomOffset(player, dist, rad), randomOffset(player, dist, rad));
					if (level.getBlockState(dest).equals(Blocks.BEDROCK.defaultBlockState()))
						level.setBlockAndUpdate(dest, oreBlocks.VOIDIC_CRYSTAL_ORE.get().defaultBlockState());
				}
			}
		});
	}

	private int randomOffset(Player player, int distance, int radius) {
		return player.getRandom().nextInt(distance) - radius;
	}

}
