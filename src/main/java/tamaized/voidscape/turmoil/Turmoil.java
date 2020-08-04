package tamaized.voidscape.turmoil;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.world.VoidTeleporter;

public class Turmoil implements SubCapability.ISubCap.ISubCapData.All {

	public static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "turmoil");
	private long teleportTick = 0L;
	private long maxTeleportTick = 20 * 7 + 10;
	private boolean dirty = false;
	private State state = State.CLOSED;

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public void tick(Entity parent) {
		if (parent == null || parent.world == null)
			return;
		if (!parent.world.isRemote() && dirty) {
			sendToClients(parent);
			dirty = false;
		}
		if ((state == State.TELEPORTING || state == State.TELEPORT) && Voidscape.checkForVoidDimension(parent.world))
			state = Turmoil.State.CLOSED;
		if (state != Turmoil.State.TELEPORTING && state != State.TELEPORT) {
			if (teleportTick > 0)
				teleportTick--;
		} else {
			if (teleportTick < maxTeleportTick - 1)
				teleportTick++;
			else if (state != State.TELEPORT) {
				state = State.TELEPORT;
				if (parent.world.isRemote || Voidscape.checkForVoidDimension(parent.world))
					return;
				parent.changeDimension(Voidscape.getWorld(parent.world, Voidscape.WORLD_KEY), VoidTeleporter.INSTANCE);
			}
		}
	}

	public void doAction(Entity parent) {
		if (state == State.CLOSED && !Voidscape.checkForVoidDimension(parent.world))
			setState(State.TELEPORTING);
		else if (state == State.TELEPORTING)
			setState(State.CLOSED);
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt, Direction side) {
		return nbt;
	}

	@Override
	public void read(CompoundNBT nbt, Direction side) {

	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeVarInt(state.ordinal());
	}

	@Override
	public void read(PacketBuffer buffer) {
		state = State.VALUES[buffer.readVarInt()];
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
		dirty = true;
	}

	public enum State {
		CLOSED, OPEN, TALKING, TELEPORTING, TELEPORT;

		static final State[] VALUES = values();
	}
}
