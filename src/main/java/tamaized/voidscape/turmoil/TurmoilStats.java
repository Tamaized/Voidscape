package tamaized.voidscape.turmoil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;
import tamaized.voidscape.turmoil.abilities.TurmoilAbilityInstance;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TurmoilStats implements SubCapability.ISubCap.ISubCapData.All {

	public static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "turmoilstats");

	private TurmoilAbilityInstance[] slots = new TurmoilAbilityInstance[9];

	private int voidicPower = 0;
	private int nullPower = 0;
	private int insanePower = 0;

	private int spellpower = 0;
	private int rechargeRate = 0;
	private int cooldown = 0;
	private int cost = 0;
	private int crit = 0;
	private boolean voidmancerStance = false;

	private boolean dirty = false;

	public void reset() {
		voidicPower = 0;
		nullPower = 0;
		insanePower = 0;
		spellpower = 0;
		rechargeRate = 0;
		cooldown = 0;
		cost = 0;
		crit = 0;
		voidmancerStance = false;
		slots = new TurmoilAbilityInstance[9];
		dirty = true;
	}

	@Override
	public void tick(Entity parent) {
		if (parent.tickCount % 20 * 10 == 0)
			dirty = true;
		if (!parent.level.isClientSide() && dirty && parent instanceof ServerPlayerEntity) {
			sendToClient((ServerPlayerEntity) parent);
			dirty = false;
		}
		if (voidicPower < 1000)
			voidicPower += 1 + rechargeRate;
		if (nullPower > 0 && parent.tickCount % ((voidmancerStance ? 2 : 1) * (1 + rechargeRate)) == 0)
			nullPower--;
		if (insanePower > 0 && parent.tickCount % 10 == 0)
			insanePower--;
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt, @Nullable Direction side) {
		nbt.putIntArray("slots", Arrays.stream(slots).mapToInt(slot -> slot == null ? -1 : slot.ability().id()).toArray());
		return nbt;
	}

	@Override
	public void read(CompoundNBT nbt, @Nullable Direction side) {
		int[] s = nbt.getIntArray("slots");
		Map<TurmoilAbility, TurmoilAbilityInstance> cache = new HashMap<>();
		for (int i = 0; i < s.length; i++) {
			TurmoilAbility ability = TurmoilAbility.getFromID(s[i]);
			if (ability == null)
				slots[i] = null;
			else {
				if (!cache.containsKey(ability))
					cache.put(ability, new TurmoilAbilityInstance(ability));
				slots[i] = cache.get(ability);
			}
		}
		dirty = true;
	}

	@Override
	public void write(PacketBuffer buffer) {
		for (TurmoilAbilityInstance slot : slots) {
			if (slot == null) {
				buffer.writeVarInt(-1);
				buffer.writeLong(0L);
			} else
				slot.encode(buffer);
		}
	}

	@Override
	public void read(PacketBuffer buffer) {
		Map<TurmoilAbility, TurmoilAbilityInstance> cache = new HashMap<>();
		for (int i = 0; i < 9; i++) {
			TurmoilAbilityInstance instance = TurmoilAbilityInstance.decode(buffer);
			if (instance != null) {
				if (cache.containsKey(instance.ability()))
					instance = cache.get(instance.ability());
				else
					cache.put(instance.ability(), instance);
			}
			slots[i] = instance;
		}
	}

	public int getVoidicPower() {
		return voidicPower;
	}

	public void setVoidicPower(int a) {
		voidicPower = a;
		dirty = true;
	}

	public int getNullPower() {
		return nullPower;
	}

	public void setNullPower(int a) {
		nullPower = a;
		dirty = true;
	}

	public int getInsanePower() {
		return insanePower;
	}

	public void setInsanePower(int a) {
		insanePower = a;
		dirty = true;
	}

	public int getSpellpower() {
		return spellpower;
	}

	public int getRechargeRate() {
		return rechargeRate;
	}

	public int getCooldownReduction() {
		return cooldown;
	}

	public int getCostReduction() {
		return cost;
	}

	public int getSpellCrit() {
		return crit;
	}

	public void setSlot(@Nullable TurmoilAbilityInstance ability, int slot) {
		if (slot < 0 || slot >= 9)
			return;
		slots[slot] = ability;
		dirty = true;
	}

	@Nullable
	public TurmoilAbilityInstance getAbility(int slot) {
		if (slot < 0 || slot >= 9)
			return null;
		return slots[slot];
	}

	public void executeAbility(LivingEntity caster, int slot) {
		TurmoilAbilityInstance a = getAbility(slot);
		if (a != null) {
			if (caster.level.isClientSide()) {
				a.executeClientSide(this, caster, slot);
				return;
			}
			a.execute(caster);
		}
	}

	@Override
	public void clone(SubCapability.ISubCap.ISubCapData old, boolean death) {
		if (old instanceof TurmoilStats) {
			TurmoilStats o = (TurmoilStats) old;
			System.arraycopy(o.slots, 0, slots, 0, slots.length);
			if (!death) {
				voidicPower = o.voidicPower;
				nullPower = o.nullPower;
				insanePower = o.insanePower;
			}
		}
	}
}
