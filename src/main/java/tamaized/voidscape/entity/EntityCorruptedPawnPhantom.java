package tamaized.voidscape.entity;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fmllegacy.common.registry.IEntityAdditionalSpawnData;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.turmoil.SubCapability;

import javax.annotation.Nullable;

public class EntityCorruptedPawnPhantom extends EntityCorruptedPawn implements IEntityAdditionalSpawnData {

	private Entity target;
	private boolean debug;

	public EntityCorruptedPawnPhantom(Level level) {
		this(ModEntities.CORRUPTED_PAWN_PHANTOM.get(), level);
	}

	public EntityCorruptedPawnPhantom(EntityType<? extends EntityCorruptedPawn> p_i48577_1_, Level p_i48577_2_) {
		super(p_i48577_1_, p_i48577_2_);
		setNoGravity(true);
		noPhysics = true;
	}

	public EntityCorruptedPawnPhantom target(Player player) {
		target = player;
		return this;
	}

	public EntityCorruptedPawnPhantom debug(Vec3 pos) {
		debug = true;
		moveTo(pos);
		return this;
	}

	@Override
	public boolean shouldRender(@Nullable Player player) {
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
				remove(RemovalReason.DISCARDED);
			else {
				target.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity).ifPresent(data -> {
					if (!equals(data.getHunter()))
						remove(RemovalReason.DISCARDED);
					else {
						lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
						setDeltaMovement(position().subtract(target.position()).normalize().scale(-0.5F));
					}
				}));
			}
		} else if (target != null) {
			target.lookAt(EntityAnchorArgument.Anchor.EYES, position());
			if (target instanceof Player target && tickCount % 5 == 0)
				level.playSound(target, blockPosition(), SoundEvents.GUARDIAN_ATTACK, SoundSource.MASTER, 4F, 0.5F);
		}
		super.tick();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return debug && super.hurt(source, amount);
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeBoolean(debug);
		buffer.writeVarInt(target == null ? -1 : target.getId());
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		debug = additionalData.readBoolean();
		target = level.getEntity(additionalData.readVarInt());
	}
}
