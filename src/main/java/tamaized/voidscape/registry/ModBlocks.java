package tamaized.voidscape.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.voidscape.entity.EntityAntiBolt;

import java.util.Random;

public class ModBlocks {

	private static final DeferredRegister<Block> REGISTRY = RegUtil.create(ForgeRegistries.BLOCKS);

	public static final RegistryObject<Block> VOIDIC_CRYSTAL_ORE = REGISTRY.register("voidic_crystal_ore", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).
			strength(3F, 3F).
			requiresCorrectToolForDrops()) {
		@Override
		public boolean removedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
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
		public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
			if (random.nextBoolean() || level.players().stream().noneMatch(p -> pos.distSqr(p.blockPosition()) <= 10000))
				return;
			LightningBolt lit = EntityType.LIGHTNING_BOLT.create(level);
			if (lit != null) {
				lit.moveTo(Vec3.atBottomCenterOf(pos));
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

	public static final RegistryObject<Block> ANTIROCK = REGISTRY.register("antirock", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).
			strength(-1.0F, 3600000.0F).
			noDrops().
			isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)) {
		@Override
		public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
			if (random.nextBoolean() || level.players().stream().noneMatch(p -> pos.distSqr(p.blockPosition()) <= 10000))
				return;
			EntityAntiBolt lit = ModEntities.ANTI_BOLT.get().create(level);
			if (lit != null) {
				lit.moveTo(Vec3.atBottomCenterOf(pos).subtract(0, 0.01F, 0));
				level.addFreshEntity(lit);
			}
		}

		@Override
		public boolean isRandomlyTicking(BlockState state) {
			return true;
		}
	});
	public static final RegistryObject<Item> ANTIROCK_ITEM = ModItems.REGISTRY.
			register(ANTIROCK.getId().getPath(), () -> new BlockItem(ANTIROCK.get(), RegUtil.ItemProps.DEFAULT.get()));

	public static final RegistryObject<Block> NULL_BLACK = REGISTRY.register("null_black", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).
			strength(-1.0F, 3600000.0F).
			noDrops().
			isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)));
	public static final RegistryObject<Item> NULL_BLACK_ITEM = ModItems.REGISTRY.
			register(NULL_BLACK.getId().getPath(), () -> new BlockItem(NULL_BLACK.get(), RegUtil.ItemProps.DEFAULT.get()));
	public static final RegistryObject<Block> NULL_WHITE = REGISTRY.register("null_white", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).
			strength(-1.0F, 3600000.0F).
			noDrops().
			isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)));
	public static final RegistryObject<Item> NULL_WHITE_ITEM = ModItems.REGISTRY.
			register(NULL_WHITE.getId().getPath(), () -> new BlockItem(NULL_WHITE.get(), RegUtil.ItemProps.DEFAULT.get()));

	static void classload() {

	}
}
