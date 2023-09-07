package tamaized.voidscape.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.block.BlockEtherealPlant;
import tamaized.voidscape.entity.EntityAntiBolt;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;

import java.util.Random;

public class ModBlocks implements RegistryClass {

	private static final DeferredRegister<Block> REGISTRY = RegUtil.create(ForgeRegistries.BLOCKS);

	public static final RegistryObject<Block> VOIDIC_CRYSTAL_ORE = REGISTRY.register("voidic_crystal_ore", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
			.strength(3F, 3F)
			.requiresCorrectToolForDrops()) {
		@Override
		public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
			boolean flag = super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
			world.setBlock(pos, Blocks.BEDROCK.defaultBlockState(), world.isClientSide() ? 11 : 3);
			return flag;
		}
	});
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_ORE_ITEM = ModItems.REGISTRY.
			register(VOIDIC_CRYSTAL_ORE.getId().getPath(), () -> new BlockItem(VOIDIC_CRYSTAL_ORE.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> VOIDIC_CRYSTAL_BLOCK = REGISTRY.register("voidic_crystal_block", () -> new Block(Block.Properties.of(Material.AMETHYST, MaterialColor.COLOR_PURPLE)
			.strength(3F, 3F)
			.requiresCorrectToolForDrops()
			.sound(SoundType.AMETHYST)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_BLOCK_ITEM = ModItems.REGISTRY.
			register(VOIDIC_CRYSTAL_BLOCK.getId().getPath(), () -> new BlockItem(VOIDIC_CRYSTAL_BLOCK.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> THUNDERROCK = REGISTRY.register("thunderrock", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.lightLevel(state -> 15)
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)) {
		@Override
		public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
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
			register(THUNDERROCK.getId().getPath(), () -> new BlockItem(THUNDERROCK.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final RegistryObject<Block> ANTIROCK = REGISTRY.register("antirock", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)) {
		@Override
		public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
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
			register(ANTIROCK.getId().getPath(), () -> new BlockItem(ANTIROCK.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final RegistryObject<Block> NULL_BLACK = REGISTRY.register("null_black", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> true)
			.sound(SoundType.AMETHYST)));
	public static final RegistryObject<Item> NULL_BLACK_ITEM = ModItems.REGISTRY.
			register(NULL_BLACK.getId().getPath(), () -> new BlockItem(NULL_BLACK.get(), ModItems.ItemProps.DEFAULT.properties().get()));
	public static final RegistryObject<Block> NULL_WHITE = REGISTRY.register("null_white", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> true)
			.sound(SoundType.AMETHYST)));
	public static final RegistryObject<Item> NULL_WHITE_ITEM = ModItems.REGISTRY.
			register(NULL_WHITE.getId().getPath(), () -> new BlockItem(NULL_WHITE.get(), ModItems.ItemProps.DEFAULT.properties().get()));
	public static final RegistryObject<Block> NULL_BROWN = REGISTRY.register("null_brown", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> true)
			.sound(SoundType.AMETHYST)));
	public static final RegistryObject<Item> NULL_BROWN_ITEM = ModItems.REGISTRY.
			register(NULL_BROWN.getId().getPath(), () -> new BlockItem(NULL_BROWN.get(), ModItems.ItemProps.DEFAULT.properties().get()));
	public static final RegistryObject<Block> NULL_GRAY = REGISTRY.register("null_gray", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> true)
			.sound(SoundType.AMETHYST)));
	public static final RegistryObject<Item> NULL_GRAY_ITEM = ModItems.REGISTRY.
			register(NULL_GRAY.getId().getPath(), () -> new BlockItem(NULL_GRAY.get(), ModItems.ItemProps.DEFAULT.properties().get()));
	public static final RegistryObject<Block> NULL_GREEN = REGISTRY.register("null_green", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> true)
			.sound(SoundType.AMETHYST)));
	public static final RegistryObject<Item> NULL_GREEN_ITEM = ModItems.REGISTRY.
			register(NULL_GREEN.getId().getPath(), () -> new BlockItem(NULL_GREEN.get(), ModItems.ItemProps.DEFAULT.properties().get()));
	public static final RegistryObject<Block> NULL_BLUE = REGISTRY.register("null_blue", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> true)
			.sound(SoundType.AMETHYST)));
	public static final RegistryObject<Item> NULL_BLUE_ITEM = ModItems.REGISTRY.
			register(NULL_BLUE.getId().getPath(), () -> new BlockItem(NULL_BLUE.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final RegistryObject<Block> EXIT_PORTAL = REGISTRY.register("exit_portal", () -> new HalfTransparentBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noOcclusion()
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> true)
			.sound(SoundType.AMETHYST)) {
		@Override
		@SuppressWarnings("deprecation")
		public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player player, InteractionHand p_60507_, BlockHitResult p_60508_) {
			if (Voidscape.checkForDutyInstance(player.level)) {
				if (!player.level.isClientSide())
					player.getCapability(SubCapability.CAPABILITY).ifPresent(c -> c.get(Voidscape.subCapTurmoilData).
							ifPresent(data -> data.setState(Turmoil.State.TELEPORT)));
				return InteractionResult.SUCCESS;
			}
			return super.use(p_60503_, p_60504_, p_60505_, player, p_60507_, p_60508_);
		}
	});
	public static final RegistryObject<Item> EXIT_PORTAL_ITEM = ModItems.REGISTRY.
			register(EXIT_PORTAL.getId().getPath(), () -> new BlockItem(EXIT_PORTAL.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final RegistryObject<Block> PLANT = REGISTRY.register("plant", () -> new BlockEtherealPlant(Block.Properties.of(Material.PLANT, MaterialColor.COLOR_PURPLE)
			.noCollission()
			.instabreak()
			.sound(SoundType.CROP)
			.offsetType(BlockBehaviour.OffsetType.XYZ)));
	public static final RegistryObject<Item> PLANT_ITEM = ModItems.REGISTRY.
			register(PLANT.getId().getPath(), () -> new BlockItem(PLANT.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	@Override
	public void init(IEventBus bus) {

	}
}