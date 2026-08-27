package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.capability.FilteredFluidStacksResourceHandler;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.registry.block.EtherealFruitBlocks;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.registry.fluid.ModFluids;
import tamaized.voidscape.util.LevelUtil;
import tamaized.voidscape.util.SingleResourceCapabilityUtil;
import tamaized.voidscape.util.TransactionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Configurable
public class GerminatorBlockEntity extends TickableBlockEntity {

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private EtherealFruitBlocks etherealFruitBlocks;

	@Autowired
	private FakePlayers fakePlayers;

	@Autowired
	private LevelUtil levelUtil;

	@Autowired
	private ModFluids modFluids;

	@Autowired
	private SingleResourceCapabilityUtil singleResourceCapabilityUtil;

	@Autowired
	private TransactionUtil transactionUtil;

	public static void registerCaps(ModBlockEntities blockEntities, RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.Fluid.BLOCK, blockEntities.GERMINATOR.get(), (object, _) -> object.fluids);
	}

	public final FluidStacksResourceHandler fluids = new FilteredFluidStacksResourceHandler(1, 10000, (_, resource) -> resource.is(modFluids.VOIDIC_SOURCE.get()));

	private int processTick;

	public GerminatorBlockEntity(ModBlockEntities blockEntities, BlockPos pPos, BlockState pBlockState) {
		super(blockEntities.GERMINATOR.get(), pPos, pBlockState);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		processTick = input.getIntOr("processTick", 0);
		fluids.deserialize(input);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("processTick", processTick);
		fluids.serialize(output);
	}

	@Override
	public void tick(Level level, BlockPos blockPos, BlockState blockState) {
		if (level.hasNeighborSignal(blockPos))
			return;
		if (processTick <= 0 && singleResourceCapabilityUtil.amount(fluids) > 0) {
			transactionUtil.execute(transaction -> singleResourceCapabilityUtil.extract(fluids, 1, transaction));
			processTick = 60;
		} else if (processTick > 0) {
			processTick--;
			if (processTick <= 0) {
				AtomicInteger growths = new AtomicInteger(0);
				boolean isVoid = levelUtil.isInVoidDimension(level);
				AABB aabb = new AABB(blockPos).inflate(4D);
				List<BlockPos> list = new ArrayList<>();
				int minX = Mth.floor(aabb.minX);
				int minY = Mth.floor(aabb.minY);
				int minZ = Mth.floor(aabb.minZ);
				int maxX = Mth.floor(aabb.maxX);
				int maxY = Mth.floor(aabb.maxY);
				int maxZ = Mth.floor(aabb.maxZ);
				final int size = (maxX - minX) * (maxY - minY) * (maxZ - minZ);
				for (int i = 0; i < size; i++) {
					if (growths.get() >= 6)
						break;
					BlockPos pos = getRandomBlockPos(list, level.getRandom(), minX, minY, minZ, maxX, maxY, maxZ);
					list.remove(pos);
					if (level.getBlockState(pos.above()).isAir()) {
						if (isVoid && etherealFruitBlocks.VOID.get().defaultBlockState().canSurvive(level, pos.above())) {
							level.setBlockAndUpdate(pos.above(), switch (level.getBiome(pos.above()).unwrapKey().map(ResourceKey::identifier).orElse(Identifier.withDefaultNamespace("")).getPath()) {
								default -> etherealFruitBlocks.VOID.get().defaultBlockState();
								case "null" -> etherealFruitBlocks.NULL.get().defaultBlockState();
								case "overworld" -> etherealFruitBlocks.OVERWORLD.get().defaultBlockState();
								case "nether" -> etherealFruitBlocks.NETHER.get().defaultBlockState();
								case "end" -> etherealFruitBlocks.END.get().defaultBlockState();
							});
							level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, 15);
							growths.incrementAndGet();
							continue;
						}
						if (level instanceof ServerLevel serverLevel) {
							if (BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), level, pos, FakePlayerFactory.get(serverLevel, fakePlayers.GERMINATOR))) {
								serverLevel.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, 15);
								growths.incrementAndGet();
							}
						}
					}
				}
				if (growths.get() <= 0) {
					processTick = 120;
				} else {
					level.getEntities(EntityTypeTest.forClass(ServerPlayer.class), aabb, EntitySelector.NO_SPECTATORS)
							.forEach(advancementTriggers.GERMINATOR_TRIGGER.get()::trigger);
				}
			}
		}
	}

	private BlockPos getRandomBlockPos(List<BlockPos> list, RandomSource random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		while (true) {
			BlockPos pos = new BlockPos(random.nextInt(maxX - minX) + minX, random.nextInt(maxY - minY) + minY, random.nextInt(maxZ - minZ) + minZ);
			if (list.contains(pos))
				continue;
			list.add(pos);
			return pos;
		}
	}

}
