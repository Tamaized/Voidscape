package tamaized.voidscape.turmoil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import tamaized.voidscape.Voidscape;

public class BindData implements SubCapability.ISubCap.ISubCapData.INetworkHandler {

	private static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "bind");

	private boolean bound;
	private int ticks;

	public void bind(boolean flag) {
		bound = flag;
		ticks = 20;
	}

	public boolean isBound() {
		return bound;
	}

	@Override
	public void clone(SubCapability.ISubCap.ISubCapData old, boolean death) {
		if (!death && old instanceof BindData)
			bind(((BindData) old).bound);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeBoolean(bound);
	}

	@Override
	public void read(PacketBuffer buffer) {
		bound = buffer.readBoolean();
	}

	public void tick(Entity parent) {
		parent.canUpdate(parent.isSpectator() || !bound);
		if (!parent.canUpdate()) {
			parent.setDeltaMovement(Vector3d.ZERO);
			if (parent instanceof LivingEntity) {
				LivingEntity living = (LivingEntity) parent;
				living.animationSpeedOld = living.animationSpeed;
			}
		}
		if (parent instanceof ServerPlayerEntity && parent.tickCount % 20 == 0) {
			sendToClients(parent);
			if (ticks <= 0)
				bound = false;
		}
		ticks--;
	}
}
