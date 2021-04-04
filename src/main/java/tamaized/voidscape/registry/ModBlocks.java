package tamaized.voidscape.registry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class ModBlocks {

	private static final DeferredRegister<Block> REGISTRY = RegUtil.create(ForgeRegistries.BLOCKS);
	public static final RegistryObject<Block> VOIDIC_CRYSTAL_ORE = REGISTRY.register("voidic_crystal_ore", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).
			harvestTool(ToolType.PICKAXE).
			strength(3F, 3F).
			requiresCorrectToolForDrops().
			harvestLevel(ItemTier.DIAMOND.getLevel())) {
		@Override
		public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
			boolean flag = super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
			world.setBlock(pos, Blocks.BEDROCK.defaultBlockState(), world.isClientSide() ? 11 : 3);
			return flag;
		}
	});
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_ORE_ITEM = ModItems.REGISTRY.
			register(VOIDIC_CRYSTAL_ORE.getId().getPath(), () -> new BlockItem(VOIDIC_CRYSTAL_ORE.get(), RegUtil.ItemProps.VOIDIC_CRYSTAL.get()));
	public static final RegistryObject<Block> THUNDERROCK = REGISTRY.register("thunderrock", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).
			strength(-1.0F, 3600000.0F).
			noDrops().
			lightLevel(state -> 15).
			isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)) {
		@Override
		public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random random) {
			if (random.nextBoolean() || level.players().stream().noneMatch(p -> pos.distSqr(p.blockPosition()) <= 10000))
				return;
			LightningBoltEntity lit = EntityType.LIGHTNING_BOLT.create(level);
			if (lit != null) {
				lit.moveTo(Vector3d.atBottomCenterOf(pos));
				level.addFreshEntity(lit);
			}
		}

		@Override
		public boolean isRandomlyTicking(BlockState state) {
			return true;
		}
	});
	public static final RegistryObject<Item> THUNDERROCK_ITEM = ModItems.REGISTRY.
			register(THUNDERROCK.getId().getPath(), () -> new BlockItem(THUNDERROCK.get(), RegUtil.ItemProps.DEFAULT.get()));

	static void classload() {

	}
}
