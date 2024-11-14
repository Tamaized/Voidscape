package tamaized.voidscape.item;

import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.advancement.GenericAdvancementTrigger;

import java.util.function.Supplier;

public class BlockTransformer extends Item {

	private final Supplier<Block> from;
	private final Supplier<Block> to;
	private final @Nullable Supplier<GenericAdvancementTrigger> advancement;

	public BlockTransformer(Supplier<Block> from, Supplier<Block> to, Properties properties) {
		this(from, to, null, properties);
	}

	public BlockTransformer(Supplier<Block> from, Supplier<Block> to, @Nullable Supplier<GenericAdvancementTrigger> advancement, Properties properties) {
		super(properties);
		this.from = from;
		this.to = to;
		this.advancement = advancement;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (Voidscape.checkForVoidDimension(context.getLevel()) && context.getLevel().getBlockState(context.getClickedPos()).is(from.get())) {
			context.getLevel().setBlockAndUpdate(context.getClickedPos(), to.get().defaultBlockState());
			if (context.getPlayer() == null || !context.getPlayer().isCreative())
				context.getItemInHand().shrink(1);
			context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 1F, 0.5F + context.getLevel().getRandom().nextFloat() * 0.5F);
			if (context.getLevel() instanceof ServerLevel)
				for (int i = 0; i < 50; i++)
					((ServerLevel) context.getLevel()).sendParticles(ParticleTypes.WITCH, context.
						getClickedPos().getX() + context.getLevel().getRandom().nextFloat(), context.
						getClickedPos().getY() + context.getLevel().getRandom().nextFloat(), context.
						getClickedPos().getZ() + context.getLevel().getRandom().nextFloat(), 0, 0, 0, 0, 1F);
			if (advancement != null && context.getPlayer() instanceof ServerPlayer serverPlayer)
				advancement.get().trigger(serverPlayer);
			return InteractionResult.SUCCESS;
		}
		return super.useOn(context);
	}

}
