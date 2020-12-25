package tamaized.voidscape.entity;

import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.turmoil.SubCapability;

import javax.annotation.Nullable;

public class EntityCorruptedPawnPhantom extends EntityCorruptedPawn implements IEntityAdditionalSpawnData {

	private Entity target;
	private boolean debug;

	public EntityCorruptedPawnPhantom(World level) {
		this(ModEntities.CORRUPTED_PAWN_PHANTOM.get(), level);
	}

	public EntityCorruptedPawnPhantom(EntityType<? extends EntityCorruptedPawn> p_i48577_1_, World p_i48577_2_) {
		super(p_i48577_1_, p_i48577_2_);
		setNoGravity(true);
		noPhysics = true;
		forcedLoading = true;
	}

	public EntityCorruptedPawnPhantom target(PlayerEntity player) {
		target = player;
		return this;
	}

	public EntityCorruptedPawnPhantom debug() {
		debug = true;
		return this;
	}

	@Override
	public boolean shouldRender(@Nullable PlayerEntity player) {
		return debug || (player != null && player.equals(target));
	}

	@Override
	public void tick() {
		if (debug) {
			setDeltaMovement(getLookAngle().scale(0.05F));
			super.tick();
			return;
		}
		if (!level.isClientSide()) {
			if (target == null || !target.isAlive())
				remove();
			else {
				target.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity).ifPresent(data -> {
					if (!equals(data.getHunter()))
						remove();
					else {
						lookAt(EntityAnchorArgument.Type.EYES, target.position());
						setDeltaMovement(position().subtract(target.position()).normalize().scale(-0.5F));
					}
				}));
			}
		} else if (target != null) {
			target.lookAt(EntityAnchorArgument.Type.EYES, position());
			if (target instanceof PlayerEntity && tickCount % 5 == 0)
				level.playSound((PlayerEntity) target, blockPosition(), SoundEvents.GUARDIAN_ATTACK, SoundCategory.MASTER, 4F, 0.5F);
		}
		super.tick();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return debug && super.hurt(source, amount);
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeBoolean(debug);
		buffer.writeVarInt(target == null ? -1 : target.getId());
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		debug = additionalData.readBoolean();
		target = level.getEntity(additionalData.readVarInt());
	}
}
