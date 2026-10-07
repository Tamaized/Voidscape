package tamaized.voidscape.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;
import java.util.List;

public class AbstractRiftEntity extends Entity {

	public AbstractRiftEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}

	public boolean shouldRender(@Nullable Player player) {
		return true;
	}

	protected boolean tryCloseRift(ServerLevel serverLevel, Item input, Item output, @Nullable Runnable onClose) {
		List<ItemEntity> crystals = serverLevel.getEntitiesOfClass(
			ItemEntity.class,
			getBoundingBox(),
			e -> e.isAlive() && e.getItem().is(input)
		);
		if (crystals.isEmpty())
			return false;
		ItemEntity crystal = crystals.getFirst();
		ItemStack stack = crystal.getItem().copy();
		stack.shrink(1);
		if (stack.isEmpty())
			crystal.discard();
		else
			crystal.setItem(stack);
		double y = getY(0.5D);
		serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, getX(), y, getZ(), 160, getBbWidth() / 2D, getBbHeight() / 3D, getBbWidth() / 2D, 0.2D);
		serverLevel.sendParticles(ParticleTypes.EXPLOSION, getX(), y, getZ(), 4, 0D, 0D, 0D, 0D);
		serverLevel.playSound(null, getX(), y, getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.NEUTRAL, 2F, 0.3F + getRandom().nextFloat() * 0.4F);
		serverLevel.playSound(null, getX(), y, getZ(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.NEUTRAL, 1F, 0.6F + getRandom().nextFloat() * 0.3F);
		ItemEntity thread = new ItemEntity(serverLevel, getX(), y, getZ(), new ItemStack(output));
		thread.setDefaultPickUpDelay();
		serverLevel.addFreshEntity(thread);
		discard();
		if (onClose != null)
			onClose.run();
		return true;
	}

	@Override
	public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(ValueInput valueInput) {

	}

	@Override
	protected void addAdditionalSaveData(ValueOutput valueOutput) {

	}
}
