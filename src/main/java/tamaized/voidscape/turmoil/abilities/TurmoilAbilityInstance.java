package tamaized.voidscape.turmoil.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.server.ServerPacketTurmoilActivateAbility;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.TurmoilStats;

public final class TurmoilAbilityInstance {

	private final TurmoilAbility ability;
	private long lastCast;
	private TurmoilStats casterStats;

	public TurmoilAbilityInstance(TurmoilAbility ability) {
		this.ability = ability;
	}

	public static TurmoilAbilityInstance decode(PacketBuffer packet) {
		TurmoilAbility ability = TurmoilAbility.getFromID(packet.readInt());
		long data = packet.readLong();
		if (ability == null)
			return null;
		TurmoilAbilityInstance instance = new TurmoilAbilityInstance(ability);
		instance.lastCast = data;
		return instance;
	}

	public TurmoilAbility ability() {
		return ability;
	}

	public int getCalcCost(TurmoilStats stats) {
		return (int) (ability.cost() * (1F - (float) stats.stats().cost / 100F));
	}

	public int getCalcCooldown(TurmoilStats stats) {
		return (int) (ability.cooldown() * (1F - (float) stats.stats().cooldown / 100F));
	}

	public boolean canAfford(LivingEntity caster) {
		return caster.canUpdate() && caster.getCapability(SubCapability.CAPABILITY).map(resolve -> resolve.get(Voidscape.subCapTurmoilStats).map(stats -> TurmoilAbility.getPower(stats, ability.costType()) >= getCalcCost(stats)).get()).orElse(false);
	}

	public boolean canExecute(LivingEntity caster) {
		return caster.canUpdate() && cooldownRemaining(caster.level) <= 0 && canAfford(caster);
	}

	public void executeClientSide(TurmoilStats stats, LivingEntity caster, int slot) {
		if (!canExecute(caster))
			return;
		Voidscape.NETWORK.sendToServer(new ServerPacketTurmoilActivateAbility(slot));
		TurmoilAbility.drainPower(stats, ability.costType(), getCalcCost(stats));
		putOnCooldown(caster);
	}

	public void execute(LivingEntity caster) {
		if (!canExecute(caster))
			return;
		caster.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> {
			casterStats = stats;
			if (ability.execute(caster)) {
				TurmoilAbility.drainPower(stats, ability.costType(), getCalcCost(stats));
				putOnCooldown(caster);
			} else {
				setCooldown(caster, 40);
				stats.markDirty();
			}
		}));
	}

	private void putOnCooldown(LivingEntity caster) {
		lastCast = caster.level.getGameTime();
		if (ability.toggle() != TurmoilAbility.Toggle.None) {
			caster.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilStats).ifPresent(data -> {
				for (int i = 0; i < 9; i++) {
					TurmoilAbilityInstance slot = data.getAbility(i);
					if (slot != null && slot.ability().toggle() == ability.toggle())
						slot.setExactCooldown(lastCast);
				}
			}));
		}
	}

	private void setExactCooldown(long ticks) {
		lastCast = ticks;
	}

	private void setCooldown(LivingEntity caster, long ticks) {
		lastCast = ticks - filterCooldown() + caster.level.getGameTime();
	}

	public void resetCooldown() {
		lastCast = 0;
	}

	public long cooldownRemaining(World level) {
		return Math.max((filterCooldown() - level.getGameTime() + lastCast), 0);
	}

	public float cooldownPercent(World level) {
		return MathHelper.clamp((float) cooldownRemaining(level) / (float) filterCooldown(), 0F, 1F);
	}

	private int filterCooldown() {
		return casterStats == null ? ability.cooldown() : getCalcCooldown(casterStats);
	}

	public void encode(PacketBuffer packet) {
		packet.writeInt(ability.id());
		packet.writeLong(lastCast);
	}

}
