package tamaized.voidscape.registry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.registry.ModItemProperties;

import java.util.function.Supplier;

@Component
public class OreBlocks {

	@Autowired
	private ModItemProperties itemProperties;

	private final DeferredRegister<Block> REGISTRY_BLOCK = RegUtil.create(Registries.BLOCK);
	private final DeferredRegister<Item> REGISTRY_ITEM = RegUtil.create(Registries.ITEM);

	public final DeferredHolder<Block, Block> VOIDIC_CRYSTAL_ORE = REGISTRY_BLOCK.register("voidic_crystal_ore", () -> new Block(
		Block.Properties.of()
			.sound(SoundType.STONE)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(3F, 3600000.0F)
			.requiresCorrectToolForDrops()
	) {
		@Override
		public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
			boolean flag = super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
			world.setBlock(pos, Blocks.BEDROCK.defaultBlockState(), world.isClientSide() ? 11 : 3);
			return flag;
		}
	});
	public final Supplier<Item> VOIDIC_CRYSTAL_ORE_ITEM = REGISTRY_ITEM.register(VOIDIC_CRYSTAL_ORE.getId().getPath(), () -> new BlockItem(
		VOIDIC_CRYSTAL_ORE.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

}
