package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import tamaized.beanification.Autowired;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.registry.block.EtherealFruitBlocks;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.registry.fluid.ModFluids;
import tamaized.voidscape.util.LevelUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GerminatorBlockEntity extends BlockEntity {

	@Autowired
	private static ModAdvancementTriggers advancementTriggers;

	@Autowired
	private static ModBlockEntities blockEntities;

	@Autowired
	private static EtherealFruitBlocks etherealFruitBlocks;

	@Autowired
	private static FakePlayers fakePlayers;

	@Autowired
	private static ModFluids modFluids;

	@Autowired
	private static LevelUtil levelUtil;

	public static void registerCaps(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntities.GERMINATOR.get(), (object, context) -> object.fluids);
	}

	public final FluidTank fluids = new FluidTank(10000, fluidStack -> fluidStack.getFluid() == modFluids.VOIDIC_SOURCE.get());

	private int processTick;

	public GerminatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(blockEntities.GERMINATOR.get(), pPos, pBlockState);
	}

	@Override
	public void setLevel(Level pLevel) {
		super.setLevel(pLevel);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		processTick = tag.getInt("processTick");
		fluids.readFromNBT(registries, tag.getCompound("tank"));
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putInt("processTick", processTick);
		tag.put("tank", fluids.writeToNBT(registries, new CompoundTag()));
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity be) {
		if (!(be instanceof GerminatorBlockEntity entity) || level.hasNeighborSignal(blockPos))
			return;
		IFluidHandler fluid = entity.fluids;
		if (entity.processTick <= 0 && fluid.getFluidInTank(0).getAmount() > 0) {
			fluid.drain(1, IFluidHandler.FluidAction.EXECUTE);
			entity.processTick = 60;
		} else if (entity.processTick > 0) {
			entity.processTick--;
			if (entity.processTick <= 0) {
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
							level.setBlockAndUpdate(pos.above(), switch (level.getBiome(pos.above()).unwrapKey().map(ResourceKey::location).orElse(ResourceLocation.withDefaultNamespace("")).getPath()) {
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
					entity.processTick = 120;
				} else {
					level.getEntities(EntityTypeTest.forClass(ServerPlayer.class), aabb, EntitySelector.NO_SPECTATORS)
							.forEach(advancementTriggers.GERMINATOR_TRIGGER.get()::trigger);
				}
			}
		}
	}

	private static BlockPos getRandomBlockPos(List<BlockPos> list, RandomSource random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		while (true) {
			BlockPos pos = new BlockPos(random.nextInt(maxX - minX) + minX, random.nextInt(maxY - minY) + minY, random.nextInt(maxZ - minZ) + minZ);
			if (list.contains(pos))
				continue;
			list.add(pos);
			return pos;
		}
	}

}
