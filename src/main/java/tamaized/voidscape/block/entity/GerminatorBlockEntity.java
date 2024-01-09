package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
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
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GerminatorBlockEntity extends BlockEntity {

	public final FluidTank fluids = new FluidTank(10000, fluidStack -> fluidStack.getFluid() == ModFluids.VOIDIC_SOURCE.get());

	private int processTick;

	public GerminatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(ModBlockEntities.GERMINATOR.get(), pPos, pBlockState);
	}

	@Override
	public void setLevel(Level pLevel) {
		super.setLevel(pLevel);
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		processTick = pTag.getInt("processTick");
		fluids.readFromNBT(pTag.getCompound("tank"));
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		pTag.putInt("processTick", processTick);
		pTag.put("tank", fluids.writeToNBT(new CompoundTag()));
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity be) {
		if (!(be instanceof GerminatorBlockEntity entity))
			return;
		IFluidHandler fluid = entity.fluids;
		if (entity.processTick <= 0 && fluid.getFluidInTank(0).getAmount() > 0) {
			fluid.drain(1, IFluidHandler.FluidAction.EXECUTE);
			entity.processTick = 60;
		} else if (entity.processTick > 0) {
			entity.processTick--;
			if (entity.processTick <= 0) {
				AtomicInteger growths = new AtomicInteger(0);
				boolean isVoid = Voidscape.checkForVoidDimension(level);
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
						if (isVoid && ModBlocks.ETHEREAL_FRUIT_VOID.get().defaultBlockState().canSurvive(level, pos.above())) {
							level.setBlockAndUpdate(pos.above(), switch (level.getBiome(pos.above()).unwrapKey().map(ResourceKey::location).orElse(new ResourceLocation("")).getPath()) {
								default -> ModBlocks.ETHEREAL_FRUIT_VOID.get().defaultBlockState();
								case "null" -> ModBlocks.ETHEREAL_FRUIT_NULL.get().defaultBlockState();
								case "overworld" -> ModBlocks.ETHEREAL_FRUIT_OVERWORLD.get().defaultBlockState();
								case "nether" -> ModBlocks.ETHEREAL_FRUIT_NETHER.get().defaultBlockState();
								case "end" -> ModBlocks.ETHEREAL_FRUIT_END.get().defaultBlockState();
							});
							level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, 0);
							growths.incrementAndGet();
							continue;
						}
						if (level instanceof ServerLevel serverLevel) {
							if (BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), level, pos, FakePlayerFactory.get(serverLevel, Voidscape.FAKE_PLAYER))) {
								serverLevel.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, 0);
								growths.incrementAndGet();
							}
						}
					}
				}
				if (growths.get() <= 0) {
					entity.processTick = 120;
				} else {
					level.getEntities(EntityTypeTest.forClass(ServerPlayer.class), aabb, EntitySelector.NO_SPECTATORS)
							.forEach(ModAdvancementTriggers.GERMINATOR_TRIGGER.get()::trigger);
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
