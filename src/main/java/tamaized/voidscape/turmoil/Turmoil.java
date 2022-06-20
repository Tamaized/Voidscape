package tamaized.voidscape.turmoil;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ui.OverlayMessageHandler;
import tamaized.voidscape.client.ui.screen.LeaveInstanceScreen;
import tamaized.voidscape.network.server.ServerPacketTurmoilAction;
import tamaized.voidscape.network.server.ServerPacketTurmoilTeleport;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;
import tamaized.voidscape.world.InstanceChunkGenerator;
import tamaized.voidscape.world.VoidTeleporter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Turmoil implements SubCapability.ISubCap.ISubCapData.All {

	public static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "turmoil");
	private final List<TurmoilSkill> skills = new ArrayList<>();
	private float tick = 0F;
	private float maxTick = 400;
	private boolean dirty = false;
	private State state = State.CLOSED;
	private Talk.Entry talk;
	private Progression progression = Progression.None;
	private int level;
	private boolean instanced;
	private int resetCooldown;

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public void tick(Entity parent) {
		if (resetCooldown > 0)
			if (--resetCooldown <= 0)
				dirty = true;
		maxTick = 300;
		if (!(parent instanceof Player) || parent.level == null)
			return;
		if (!parent.level.isClientSide() && parent instanceof ServerPlayer) {
			final boolean cachedInstanced = instanced;
			if (parent.level instanceof ServerLevel && ((ServerLevel) parent.level).getChunkSource().getGenerator() instanceof InstanceChunkGenerator)
				instanced = true;
			else
				instanced = false;
			if (dirty || cachedInstanced != instanced) {
				sendToClient((ServerPlayer) parent);
				dirty = false;
			}
		}
		if (instanced && getState() != State.CLOSED) {
			if (!parent.level.isClientSide() && getState() == State.TELEPORT && parent instanceof ServerPlayer player)
				parent.changeDimension(Voidscape.getPlayersSpawnLevel(player), VoidTeleporter.INSTANCE);
			setState(State.CLOSED);
		}
		if (!hasStarted() && isTalking() && getState() == State.CLOSED)
			talk(null);
		switch (getState()) {
			default:
			case CLOSED:
				if (!hasStarted() && !parent.level.isClientSide() && parent.getY() < (parent.level.getMinBuildHeight() + 12) && parent.tickCount % 100 == 0 && parent.level.getRandom().nextInt(25) == 0)
					setState(State.CONSUME);
				if (tick > 0)
					tick -= Math.min(3, tick);
				if (!parent.level.isClientSide() && progression == Progression.Started && Voidscape.checkForVoidDimension(parent.level) && !isTalking())
					talk(Talk.TUTORIAL);
				if (!parent.level.isClientSide() && progression == Progression.CorruptPhantom && !Voidscape.checkForVoidDimension(parent.level) && !isTalking())
					talk(Talk.CORRUPT_PHANTOM);
				break;
			case OPENING:
				if (tick < maxTick)
					tick += Math.min(2.25F, maxTick - tick);
				else
					setState(State.OPEN);
				break;
			case OPEN:
				tick = maxTick;
				if (!parent.level.isClientSide() && getProgression() == Progression.EnteredVoid && talk == null)
					talk(Talk.TUTORIAL_GUI);
				break;
			case CONSUME:
				if (!isTalking()) {
					if (parent.getY() >= (parent.level.getMinBuildHeight() + 12))
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
							talk(Talk.INTRO);
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
				tick = maxTick;
				if (!parent.level.isClientSide() && parent instanceof ServerPlayer player) {
					if (Voidscape.checkForVoidDimension(parent.level))
						parent.changeDimension(Voidscape.getPlayersSpawnLevel(player), VoidTeleporter.INSTANCE);
					else
						parent.changeDimension(Voidscape.getLevel(parent.level, Voidscape.WORLD_KEY_VOID), VoidTeleporter.INSTANCE);
				}
				setState(State.CLOSED);
				break;
		}
	}

	public void clientTeleport() {
		Voidscape.NETWORK.sendToServer(new ServerPacketTurmoilTeleport());
	}

	@OnlyIn(Dist.CLIENT)
	public void clientAction() {
		final boolean flag = !isTalking();
		if (flag || OverlayMessageHandler.process()) {
			talk(null);
			if (Voidscape.checkForDutyInstance(Minecraft.getInstance().level)) {
				Minecraft.getInstance().setScreen(new LeaveInstanceScreen());
			}
			Voidscape.NETWORK.sendToServer(new ServerPacketTurmoilAction());
			if (flag && hasStarted())
				commonAction();
		}
	}

	public void serverAction(Entity parent) {
		if (!parent.isAlive()) {
			setProgression(Progression.None);
			talk = null;
			state = State.CLOSED;
			dirty = true;
			return;
		}
		if (talk != null) {
			talk.finish(parent);
			talk(null);
		} else if (hasStarted()) {
			commonAction();
		}
	}

	private void commonAction() {
		switch (getState()) {
			case CLOSED -> setState(progression.ordinal() >= Progression.EnteredVoid.ordinal() ? State.OPENING : State.TELEPORTING);
			case OPEN, OPENING -> setState(State.CLOSED);
			default -> {
			}
		}
	}

	public void resetSkills(TurmoilStats stats) {
		resetCooldown = 20 * 60 * 60;
		skills.clear();
		stats.reset();
		dirty = true;
	}

	public int getResetCooldown() {
		return resetCooldown;
	}

	@Override
	public CompoundTag write(CompoundTag nbt, @Nullable Direction side) {
		nbt.putInt("progression", progression.ordinal());
		nbt.putInt("level", level);
		nbt.putIntArray("skills", skills.stream().mapToInt(TurmoilSkill::getID).toArray());
		nbt.putInt("reset", resetCooldown);
		return nbt;
	}

	@Override
	public void read(CompoundTag nbt, @Nullable Direction side) {
		setProgression(Progression.get(nbt.getInt("progression")));
		level = nbt.getInt("level");
		skills.clear();
		for (int id : nbt.getIntArray("skills")) {
			TurmoilSkill skill = TurmoilSkill.getFromID(id);
			if (skill != null)
				skills.add(skill);
		}
		resetCooldown = nbt.getInt("reset");
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeVarInt(level);
		buffer.writeInt(resetCooldown);
		buffer.writeVarInt(progression.ordinal());
		buffer.writeVarInt(state.ordinal());
		buffer.writeFloat(tick);
		buffer.writeBoolean(instanced);
		buffer.writeVarInt(skills.size());
		skills.forEach(skill -> buffer.writeVarInt(skill.getID()));
		boolean flag = talk != null;
		buffer.writeBoolean(flag);
		if (flag)
			buffer.writeResourceLocation(talk.getId());
	}

	@Override
	public void read(FriendlyByteBuf buffer) {
		level = buffer.readVarInt();
		resetCooldown = buffer.readInt();
		progression = Progression.get(buffer.readVarInt());
		state = State.get(buffer.readVarInt());
		tick = buffer.readFloat();
		instanced = buffer.readBoolean();
		int len = buffer.readVarInt();
		skills.clear();
		for (int index = 0; index < len; index++) {
			TurmoilSkill skill = TurmoilSkill.getFromID(buffer.readVarInt());
			if (skill != null)
				skills.add(skill);
		}
		if (buffer.readBoolean())
			Talk.Entry.findOrExec(buffer.readResourceLocation(), () -> talk = null).ifPresent(e -> {
				if (talk != e) {
					talk = e;
					OverlayMessageHandler.start(talk);
				}
			});
		else
			talk = null;
	}

	public void start() {
		if (!hasStarted() && state == State.CONSUME && tick >= maxTick) {
			reset();
			setProgression(Progression.Started);
			state = State.TELEPORTING;
		}
	}

	public Progression getProgression() {
		return progression;
	}

	public void setProgression(Progression progression) {
		this.progression = progression;
		dirty = true;
	}

	public void progressTo(Progression progression) {
		if (getProgression().ordinal() + 1 == progression.ordinal())
			setProgression(progression);
	}

	public boolean hasStarted() {
		return progression.ordinal() > Progression.None.ordinal();
	}

	public void reset() {
		setProgression(Progression.None);
		talk = null;
		state = State.CLOSED;
		level = 0;
		skills.clear();
		resetCooldown = 0;
		dirty = true;
	}

	public void resetResetCooldown() {
		resetCooldown = 0;
		dirty = true;
	}

	public void debug() {
		if (hasStarted() || state != State.CLOSED && state != State.CONSUME)
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
		setProgression(Progression.Started);
	}

	public float getTick() {
		return tick;
	}

	public float getMaxTick() {
		return maxTick;
	}

	public boolean inInstance() {
		return instanced;
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

	public boolean isTalking() {
		return talk != null;
	}

	public void talk(@Nullable Talk.Entry entry) {
		dirty = talk != entry;
		talk = entry;
	}

	public void levelUp() {
		level++;
		dirty = true;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		if (this.level > level)
			skills.clear();
		this.level = level;
		dirty = true;
	}

	public int getPoints() {
		return level - skills.stream().mapToInt(TurmoilSkill::getCost).sum();
	}

	public void claimSkill(@Nullable TurmoilSkill skill) {
		if (canClaim(skill)) {
			skills.add(skill);
			dirty = true;
		}
	}

	public List<TurmoilSkill> getSkills() {
		return ImmutableList.copyOf(skills);
	}

	public boolean hasSkill(TurmoilSkill skill) {
		return skills.contains(skill);
	}

	public boolean hasCoreSkill() {
		return skills.size() > 0;
	}

	public TurmoilSkill.CoreType classType() {
		for (TurmoilSkill skill : skills) {
			if (skill.coreType() != TurmoilSkill.CoreType.Null)
				return skill.coreType();
		}
		return TurmoilSkill.CoreType.Null;
	}

	public boolean canClaim(@Nullable TurmoilSkill skill) {
		if (skill == null || skill.disabled() || hasSkill(skill))
			return false;
		int spent = skills.stream().mapToInt(TurmoilSkill::getCost).sum();
		int points = level - spent;
		return (skill.isCore() && spent == 0 && points > 0) || (!skill.isCore() && points >= skill.getCost() && skill.hasRequired(skills));
	}

	@Override
	public void clone(SubCapability.ISubCap.ISubCapData old, boolean death) {
		if (old instanceof Turmoil o)
			read(o.write(new CompoundTag(), null), null);
	}

	public enum State {
		CLOSED, CONSUME, OPENING, OPEN, TELEPORTING, TELEPORT;

		static final State[] VALUES = values();

		public static State get(int ordinal) {
			if (ordinal < 0 | ordinal >= VALUES.length)
				return CLOSED;
			else
				return VALUES[ordinal];
		}
	}
}
