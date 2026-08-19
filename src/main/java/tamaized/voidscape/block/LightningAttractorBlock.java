package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public class LightningAttractorBlock<T extends LightningBolt> extends Block {

	private final Supplier<EntityType<T>> entityType;
	private final Function<Vec3, Vec3> positionModifier;
	@Nullable
	private final Supplier<BlockState> to;

	private LightningAttractorBlock(Supplier<EntityType<T>> entityType, Function<Vec3, Vec3> positionModifier, @Nullable Supplier<BlockState> to, Properties properties) {
		super(properties);
		this.entityType = entityType;
		this.positionModifier = positionModifier;
		this.to = to;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (random.nextBoolean() || level.players().stream().noneMatch(p -> pos.distSqr(p.blockPosition()) <= 10000))
			return;
		T lit = entityType.get().create(level, EntitySpawnReason.SPAWNER);
		if (lit != null) {
			lit.snapTo(positionModifier.apply(Vec3.atBottomCenterOf(pos)));
			level.addFreshEntity(lit);
			if (to != null)
				level.setBlockAndUpdate(pos, to.get());
		}
	}

	public static class Builder<T extends LightningBolt> {

		private final Supplier<EntityType<T>> entityType;
		private Function<Vec3, Vec3> positionModifier = Function.identity();
		@Nullable
		private Supplier<BlockState> to = null;

		public Builder(Supplier<EntityType<T>> entityType) {
			this.entityType = entityType;
		}

		public Builder<T> positionModifier(Function<Vec3, Vec3> positionModifier) {
			this.positionModifier = positionModifier;
			return this;
		}

		public Builder<T> to(Supplier<BlockState> to) {
			this.to = to;
			return this;
		}

		public LightningAttractorBlock<T> build(BlockBehaviour.Properties properties) {
			return new LightningAttractorBlock<>(entityType, positionModifier, to, properties);
		}
	}

}
