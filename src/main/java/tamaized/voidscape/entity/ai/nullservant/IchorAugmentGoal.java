package tamaized.voidscape.entity.ai.nullservant;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import org.joml.Vector3f;
import tamaized.voidscape.entity.NullServantEntity;
import tamaized.voidscape.entity.NullServantIchorBoltEntity;
import tamaized.voidscape.entity.PhantomNullServantEntity;

import java.util.EnumSet;

public class IchorAugmentGoal extends Goal {

	private final NullServantEntity parent;
	private int cooldown;
	private int tick;
	private int nextActionTick;

	private int hitCounter = 0;

	private PhantomNullServantEntity phantom1;
	private PhantomNullServantEntity phantom2;
	private PhantomNullServantEntity phantom3;
	private PhantomNullServantEntity phantom4;

	private PhantomNullServantEntity primed;

	public IchorAugmentGoal(NullServantEntity parent) {
		this.setFlags(EnumSet.of(Flag.MOVE));
		this.parent = parent;
	}

	@Override
	public boolean canUse() {
		return !parent.getAugmentAttack() && parent.getAugment() == NullServantEntity.AUGMENT_ICHOR && (parent.getHealth() / parent.getMaxHealth()) <= 0.75F && cooldown-- <= 0;
	}

	@Override
	public boolean canContinueToUse() {
		return parent.getAugmentAttack() && hitCounter < 3;
	}

	@Override
	public void start() {
		tick = 0;
		nextActionTick = 70;
		hitCounter = 0;
		primed = null;
		parent.setAugmentAttack(true);
		phantom1 = new PhantomNullServantEntity(parent);
		phantom2 = new PhantomNullServantEntity(parent);
		phantom3 = new PhantomNullServantEntity(parent);
		phantom4 = new PhantomNullServantEntity(parent);
		phantom1.setAugment(NullServantEntity.AUGMENT_ICHOR);
		phantom2.setAugment(NullServantEntity.AUGMENT_ICHOR);
		phantom3.setAugment(NullServantEntity.AUGMENT_ICHOR);
		phantom4.setAugment(NullServantEntity.AUGMENT_ICHOR);
		parent.level().addFreshEntity(phantom1);
		parent.level().addFreshEntity(phantom2);
		parent.level().addFreshEntity(phantom3);
		parent.level().addFreshEntity(phantom4);
		phantom1.randomPosOrDiscard();
		phantom2.randomPosOrDiscard();
		phantom3.randomPosOrDiscard();
		phantom4.randomPosOrDiscard();
	}

	@Override
	public void stop() {
		primed = null;
		parent.setAugmentAttack(false);
		parent.setAugmentAttackAoes(new Vector3f(), new Vector3f());
		phantom1 = null;
		phantom2 = null;
		phantom3 = null;
		phantom4 = null;
		nextActionTick = 0;
		hitCounter = 0;
		cooldown = 150;
	}

	public void applyHit() {
		hitCounter++;
	}

	@Override
	public void tick() {
		if (tick++ % 30 == 0)
			parent.heal(1F);
		if (tick > nextActionTick) {
			if (primed == null) {
				PhantomNullServantEntity[] phantoms = {phantom1, phantom2, phantom3, phantom4};
				primed = phantoms[parent.getRandom().nextInt(4)];
				primed.setAugmentAttack(true);
				nextActionTick = tick + 120;
			} else {
				parent.playSound(SoundEvents.BLAZE_SHOOT, 4F, (1.0F + (parent.getRandom().nextFloat() - parent.getRandom().nextFloat()) * 0.2F) * 0.7F);
				parent.level().addFreshEntity(new NullServantIchorBoltEntity(primed));
				primed.setAugmentAttack(false);
				primed = null;
				nextActionTick = tick + 50;
			}
		}
	}
}
