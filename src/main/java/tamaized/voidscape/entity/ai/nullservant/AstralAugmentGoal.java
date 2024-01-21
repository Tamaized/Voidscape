package tamaized.voidscape.entity.ai.nullservant;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import tamaized.voidscape.entity.NullServantEntity;
import tamaized.voidscape.entity.NullServantIchorBoltEntity;
import tamaized.voidscape.entity.PhantomNullServantEntity;
import tamaized.voidscape.entity.StrangePearlEntity;

import java.util.EnumSet;

public class AstralAugmentGoal extends Goal {

	private final NullServantEntity parent;
	private int cooldown;
	private int tick;
	private int nextActionTick1;
	private int nextActionTick2;
	private int nextActionTick3;
	private int nextActionTick4;

	private PhantomNullServantEntity phantom1;
	private PhantomNullServantEntity phantom2;
	private PhantomNullServantEntity phantom3;
	private PhantomNullServantEntity phantom4;

	private boolean primed1;
	private boolean primed2;
	private boolean primed3;
	private boolean primed4;

	public AstralAugmentGoal(NullServantEntity parent) {
		this.setFlags(EnumSet.of(Flag.MOVE));
		this.parent = parent;
	}

	@Override
	public boolean canUse() {
		return !parent.getAugmentAttack() && parent.getAugment() == NullServantEntity.AUGMENT_ASTRAL && (parent.getHealth() / parent.getMaxHealth()) <= 0.9F && cooldown-- <= 0;
	}

	@Override
	public boolean canContinueToUse() {
		return parent.getAugmentAttack() && (phantom1 != null || phantom2 != null || phantom3 != null || phantom4 != null);
	}

	@Override
	public void start() {
		tick = 0;
		nextActionTick1 = 70;
		nextActionTick2 = 70 + parent.getRandom().nextInt(33);
		nextActionTick3 = 70 + parent.getRandom().nextInt(66);
		nextActionTick4 = 70 + parent.getRandom().nextInt(99);
		primed1 = false;
		primed2 = false;
		primed3 = false;
		primed4 = false;
		parent.setAugmentAttack(true);
		phantom1 = new PhantomNullServantEntity(parent);
		phantom2 = new PhantomNullServantEntity(parent);
		phantom3 = new PhantomNullServantEntity(parent);
		phantom4 = new PhantomNullServantEntity(parent);
		phantom1.setAugment(NullServantEntity.AUGMENT_ASTRAL);
		phantom2.setAugment(NullServantEntity.AUGMENT_ASTRAL);
		phantom3.setAugment(NullServantEntity.AUGMENT_ASTRAL);
		phantom4.setAugment(NullServantEntity.AUGMENT_ASTRAL);
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
		primed1 = false;
		primed2 = false;
		primed3 = false;
		primed4 = false;
		parent.setAugmentAttack(false);
		parent.setAugmentAttackAoes(new Vector3f(), new Vector3f());
		phantom1 = null;
		phantom2 = null;
		phantom3 = null;
		phantom4 = null;
		nextActionTick1 = 0;
		nextActionTick2 = 0;
		nextActionTick3 = 0;
		nextActionTick4 = 0;
		cooldown = 150;
	}

	@Override
	public void tick() {
		if (phantom1 != null && !phantom1.isAlive())
			phantom1 = null;
		if (phantom2 != null && !phantom2.isAlive())
			phantom2 = null;
		if (phantom3 != null && !phantom3.isAlive())
			phantom3 = null;
		if (phantom4 != null && !phantom4.isAlive())
			phantom4 = null;
		if (tick++ % 30 == 0)
			parent.heal(1F);
		if (tick > nextActionTick1 && phantom1 != null) {
			if (!primed1) {
				primed1 = true;
				phantom1.setAugmentAttack(true);
				nextActionTick1 = tick + 120;
			} else {
				shoot(phantom1);
				primed1 = false;
				phantom1.setAugmentAttack(false);
				nextActionTick1 = tick + 50 + parent.getRandom().nextInt(20);
			}
		}
		if (tick > nextActionTick2 && phantom2 != null) {
			if (!primed2) {
				primed2 = true;
				phantom2.setAugmentAttack(true);
				nextActionTick2 = tick + 120;
			} else {
				shoot(phantom2);
				primed2 = false;
				phantom2.setAugmentAttack(false);
				nextActionTick2 = tick + 50 + parent.getRandom().nextInt(20);
			}
		}
		if (tick > nextActionTick3 && phantom3 != null) {
			if (!primed3) {
				primed3 = true;
				phantom3.setAugmentAttack(true);
				nextActionTick3 = tick + 120;
			} else {
				shoot(phantom3);
				primed3 = false;
				phantom3.setAugmentAttack(false);
				nextActionTick3 = tick + 50 + parent.getRandom().nextInt(20);
			}
		}
		if (tick > nextActionTick4 && phantom4 != null) {
			if (!primed4) {
				primed4 = true;
				phantom4.setAugmentAttack(true);
				nextActionTick4 = tick + 120;
			} else {
				shoot(phantom4);
				primed4 = false;
				phantom4.setAugmentAttack(false);
				nextActionTick4 = tick + 50 + parent.getRandom().nextInt(20);
			}
		}
	}

	private void shoot(PhantomNullServantEntity phantom) {
		parent.playSound(SoundEvents.BLAZE_SHOOT, 4F, (1.0F + (parent.getRandom().nextFloat() - parent.getRandom().nextFloat()) * 0.2F) * 0.7F);
		StrangePearlEntity pearl = new StrangePearlEntity(parent.level(), phantom);
		pearl.setNoGravity(true);
		Vec3 pos = phantom.position();
		Player player = parent.level().getNearestPlayer(pos.x(), pos.y(), pos.z(), 256, false);
		if (player != null) {
			Vec3 dir = player.getBoundingBox().getCenter().subtract(pos).subtract(0, player.getEyeHeight(), 0).normalize().scale(0.25F);
			pearl.setDeltaMovement(dir);
			parent.level().addFreshEntity(pearl.setDamage(8F));
		}
	}
}
