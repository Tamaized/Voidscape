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
import tamaized.voidscape.world.VoidTeleporter;

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
		if (!(parent instanceof PlayerEntity) || parent.world == null)
			return;
		if (!parent.world.isRemote() && dirty && parent instanceof ServerPlayerEntity) {
			sendToClient((ServerPlayerEntity) parent);
			dirty = false;
		}
		if (!started && !parent.world.isRemote()) {
			if (state == State.CLOSED && parent.getPosY() < 5 && parent.ticksExisted % 100 == 0 && parent.world.getRandom().nextInt(25) == 0)
				setState(State.CONSUME);
			else if (state == State.CONSUME && parent.getPosY() >= 5 && talk == null)
				setState(State.CLOSED);
		}
		if ((state == State.TELEPORTING || state == State.TELEPORT) && Voidscape.checkForVoidDimension(parent.world))
			state = Turmoil.State.CLOSED;
		if (state == State.CONSUME) {
			if (talk == null && tick > 0)
				tick -= 0.01F;
			if (tick < maxTick && parent.ticksExisted % 400 == 0) {
				tick += 30;
				if (parent.world.isRemote())
					parent.playSound(SoundEvents.BLOCK_CONDUIT_AMBIENT_SHORT, 1F, 1F);
			}
			if (talk == null && tick >= maxTick && !parent.world.isRemote())
				talk(Talk.TEST);
		} else if (state != Turmoil.State.TELEPORTING && state != State.TELEPORT) {
			if (tick > 0)
				tick -= 2;
		} else {
			if (tick < maxTick)
				tick += 2;
			else if (started && state != State.TELEPORT) {
				state = State.TELEPORT;
				if (parent.world.isRemote || Voidscape.checkForVoidDimension(parent.world))
					return;
				parent.changeDimension(Voidscape.getWorld(parent.world, Voidscape.WORLD_KEY_VOID), VoidTeleporter.INSTANCE);
			}
		}
	}

	public void action() {
		if (talk == null || OverlayMessageHandler.process()) {
			if (talk != null)
				talk = null;
			Voidscape.NETWORK.sendToServer(new ServerPacketTurmoilAction());
		}
	}

	public void doAction(Entity parent) {
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
			if (state == State.CLOSED && !Voidscape.checkForVoidDimension(parent.world))
				setState(State.TELEPORTING);
			else if (state == State.TELEPORTING)
				setState(State.CLOSED);
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
		if (talk != null) {
			buffer.writeBoolean(true);
			buffer.writeResourceLocation(talk.getId());
		} else
			buffer.writeBoolean(false);
	}

	@Override
	public void read(PacketBuffer buffer) {
		state = State.VALUES[buffer.readVarInt()];
		if (buffer.readBoolean())
			Talk.Entry.find(buffer.readResourceLocation()).ifPresent(e -> {
				if (talk != e) {
					talk = e;
					OverlayMessageHandler.start(talk);
				}
			});
	}

	public void start() {
		reset();
		started = true;
	}

	public void reset() {
		started = false;
		talk = null;
		state = State.CLOSED;
		dirty = true;
	}

	public void debug() {
		if (started || state != State.CLOSED)
			reset();
		else
			setState(State.CONSUME);
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
		this.state = state;
		dirty = true;
	}

	public Optional<Talk.Entry> talk() {
		return Optional.ofNullable(talk);
	}

	public void talk(Talk.Entry entry) {
		talk = entry;
		dirty = true;
	}

	public enum State {
		CONSUME, CLOSED, OPEN, TELEPORTING, TELEPORT;

		static final State[] VALUES = values();
	}
}
