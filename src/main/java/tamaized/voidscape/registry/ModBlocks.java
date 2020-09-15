package tamaized.voidscape.registry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

	private static final DeferredRegister<Block> REGISTRY = RegUtil.create(ForgeRegistries.BLOCKS);
	public static final RegistryObject<Block> VOIDIC_CRYSTAL_ORE = REGISTRY.register("voidic_crystal_ore", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).
			harvestTool(ToolType.PICKAXE).
			hardnessAndResistance(3F, 3F).
			setRequiresTool().
			harvestLevel(ItemTier.DIAMOND.getHarvestLevel())) {
		@Override
		public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
			boolean flag = super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
			world.setBlockState(pos, Blocks.BEDROCK.getDefaultState(), world.isRemote ? 11 : 3);
			return flag;
		}
	});
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_ORE_ITEM = ModItems.REGISTRY.register(VOIDIC_CRYSTAL_ORE.getId().getPath(), () -> new BlockItem(VOIDIC_CRYSTAL_ORE.get(), RegUtil.ItemProps.VOIDIC_CRYSTAL.get()));

	static void classload() {

	}
}
