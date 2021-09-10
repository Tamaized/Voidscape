package tamaized.voidscape.turmoil;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import tamaized.voidscape.Voidscape;

import javax.annotation.Nullable;

public class TrackedTurmoilData implements SubCapability.ISubCap.ISubCapData.All {

	private static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "tracked");
	public boolean incapacitated;
	private boolean dirty;

	@Override
	public void clone(SubCapability.ISubCap.ISubCapData old, boolean death) {

	}

	@Override
	public void tick(Entity parent) {
		if (incapacitated && parent.level != null && !(parent.level.dimension().location().getNamespace().equals(Voidscape.MODID) && parent.level.dimension().location().getPath().contains("instance"))) {
			incapacitated = false;
			if (parent instanceof LivingEntity)
				((LivingEntity) parent).deathTime = 0;
		}
		if (incapacitated && parent instanceof LivingEntity) {
			((LivingEntity) parent).setHealth(0.5F);
			((LivingEntity) parent).deathTime++;
		}
		if (dirty != incapacitated) {
			dirty = incapacitated;
			if (!incapacitated && parent instanceof LivingEntity)
				((LivingEntity) parent).deathTime = 0;
			if (parent.level != null && !parent.level.isClientSide())
				sendToClients(parent);
		}
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public CompoundTag write(CompoundTag nbt, @Nullable Direction side) {
		nbt.putBoolean("incapacitated", incapacitated);
		return nbt;
	}

	@Override
	public void read(CompoundTag nbt, @Nullable Direction side) {
		incapacitated = nbt.getBoolean("incapacitated");
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBoolean(incapacitated);
	}

	@Override
	public void read(FriendlyByteBuf buffer) {
		incapacitated = buffer.readBoolean();
	}
}
