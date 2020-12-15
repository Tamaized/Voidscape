package tamaized.voidscape.turmoil;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import tamaized.voidscape.Voidscape;

public class TurmoilStats implements SubCapability.ISubCap.ISubCapData.All {

	public static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "turmoilstats");

	private int voidicPower = 0;
	private int nullPower = 0;
	private int insanePower = 0;
	private int spellpower = 0;
	private int rechargeRate = 0;
	private int cooldown = 0;
	private int cost = 0;
	private int crit = 0;
	private boolean voidmancerStance = false;

	@Override
	public void tick(Entity parent) {
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
	public CompoundNBT write(CompoundNBT nbt, Direction side) {
		return nbt;
	}

	@Override
	public void read(CompoundNBT nbt, Direction side) {

	}

	@Override
	public void write(PacketBuffer buffer) {

	}

	@Override
	public void read(PacketBuffer buffer) {

	}

	public int getVoidicPower() {
		return voidicPower;
	}

	public void setVoidicPower(int a) {
		voidicPower = a;
	}

	public int getNullPower() {
		return nullPower;
	}

	public void setNullPower(int a) {
		nullPower = a;
	}

	public int getInsanePower() {
		return insanePower;
	}

	public void setInsanePower(int a) {
		insanePower = a;
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
}
