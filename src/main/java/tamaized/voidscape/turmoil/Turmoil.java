package tamaized.voidscape.turmoil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.OverlayMessageHandler;
import tamaized.voidscape.network.server.ServerPacketTurmoilAction;
import tamaized.voidscape.network.server.ServerPacketTurmoilTeleport;
import tamaized.voidscape.world.VoidTeleporter;

import javax.annotation.Nullable;
import java.util.Optional;

public class Turmoil implements SubCapability.ISubCap.ISubCapData.All {

	public static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "turmoil");
	private float tick = 0F;
	private float maxTick = 400;
	private boolean dirty = false;
	private boolean started = false;
	private State state = State.CLOSED;
	private Talk.Entry talk;

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public void tick(Entity parent) {
		maxTick = 300;
		if (!(parent instanceof PlayerEntity) || parent.level == null)
			return;
		if (!parent.level.isClientSide() && dirty && parent instanceof ServerPlayerEntity) {
			sendToClient((ServerPlayerEntity) parent);
			dirty = false;
		}
		switch (getState()) {
			default:
			case CLOSED:
				if (!started && !parent.level.isClientSide() && parent.getY() < 5 && parent.tickCount % 100 == 0 && parent.level.getRandom().nextInt(25) == 0)
					setState(State.CONSUME);
				if (tick > 0)
					tick -= Math.min(3, tick);
				break;
			case OPENING:
				if (tick < maxTick)
					tick += Math.min(2.25F, maxTick - tick);
				else
					setState(State.OPEN);
				break;
			case OPEN:
				tick = maxTick;
				break;
			case CONSUME:
				if (!talk().isPresent()) {
					if (parent.getY() >= 5)
						setState(State.CLOSED);
					else {
						if (tick > 0)
							tick -= Math.min(0.01F, tick);
						if (tick < maxTick && parent.tickCount % 400 == 0) {
							tick += Math.min(30, maxTick - tick);
							if (parent.level.isClientSide())
								parent.playSound(SoundEvents.CONDUIT_AMBIENT_SHORT, 1F, 1F);
						}
						if (tick >= maxTick && !parent.level.isClientSide())
							talk(Talk.TEST);
					}
				}
				break;
			case TELEPORTING:
				if (Voidscape.checkForVoidDimension(parent.level))
					setState(State.CLOSED);
				if (tick < maxTick)
					tick += Math.min(2, maxTick - tick);
				else
					setState(State.TELEPORT);
				break;
			case TELEPORT:
				if (!parent.level.isClientSide() && !Voidscape.checkForVoidDimension(parent.level)) {
					parent.changeDimension(Voidscape.getWorld(parent.level, Voidscape.WORLD_KEY_VOID), VoidTeleporter.INSTANCE);
				}
				setState(State.CLOSED);
				break;
		}
	}

	public void clientTeleport() {
		Voidscape.NETWORK.sendToServer(new ServerPacketTurmoilTeleport());
	}

	public void clientAction() {
		final boolean flag = !talk().isPresent();
		if (flag || OverlayMessageHandler.process()) {
			talk(null);
			Voidscape.NETWORK.sendToServer(new ServerPacketTurmoilAction());
			if (flag && started)
				commonAction();
		}
	}

	public void serverAction(Entity parent) {
		if (!parent.isAlive()) {
			boolean cache = started;
			reset();
			started = cache;
			return;
		}
		if (talk != null) {
			talk.finish(parent);
			talk(null);
		} else if (started) {
			commonAction();
		}
	}

	private void commonAction() {
		switch (getState()) {
			case CLOSED:
				setState(State.OPENING);
				break;
			case OPEN:
			case OPENING:
				setState(State.CLOSED);
				break;
			default:
				break;
		}
	}

	private void clientHandleNewState() {
		switch (state) {
			default:
				break;
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt, Direction side) {
		nbt.putBoolean("started", started);
		return nbt;
	}

	@Override
	public void read(CompoundNBT nbt, Direction side) {
		started = nbt.getBoolean("started");
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeVarInt(state.ordinal());
		buffer.writeFloat(tick);
		if (talk != null) {
			buffer.writeBoolean(true);
			buffer.writeResourceLocation(talk.getId());
		} else
			buffer.writeBoolean(false);
	}

	@Override
	public void read(PacketBuffer buffer) {
		State old = state;
		state = State.VALUES[buffer.readVarInt()];
		if (state != old)
			clientHandleNewState();
		tick = buffer.readFloat();
		if (buffer.readBoolean())
			Talk.Entry.findOrExec(buffer.readResourceLocation(), () -> {
				talk = null;
			}).ifPresent(e -> {
				if (talk != e) {
					talk = e;
					OverlayMessageHandler.start(talk);
				}
			});
		else
			talk = null;
	}

	public void start() {
		if (!started && state == State.CONSUME && tick >= maxTick) {
			reset();
			started = true;
			state = State.TELEPORTING;
		}
	}

	public boolean hasStarted() {
		return started;
	}

	public void reset() {
		started = false;
		talk = null;
		state = State.CLOSED;
		dirty = true;
	}

	public void debug() {
		if (started || state != State.CLOSED && state != State.CONSUME)
			reset();
		else if (state == State.CONSUME) {
			if (tick == maxTick)
				reset();
			else
				tick = maxTick;
			dirty = true;
		} else
			setState(State.CONSUME);
	}

	public void forceStart() {
		reset();
		started = true;
	}

	public float getTick() {
		return tick;
	}

	public float getMaxTick() {
		return maxTick;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		dirty = this.state != state;
		this.state = state;
	}

	public Optional<Talk.Entry> talk() {
		return Optional.ofNullable(talk);
	}

	public void talk(@Nullable Talk.Entry entry) {
		dirty = talk != entry;
		talk = entry;
	}

	public enum State {
		CONSUME, CLOSED, OPENING, OPEN, TELEPORTING, TELEPORT;

		static final State[] VALUES = values();
	}
}
