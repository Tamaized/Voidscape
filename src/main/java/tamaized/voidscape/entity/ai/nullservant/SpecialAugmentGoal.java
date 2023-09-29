package tamaized.voidscape.entity.ai.nullservant;

import net.minecraft.world.entity.ai.goal.Goal;
import tamaized.voidscape.entity.NullServantAugmentBlockEntity;
import tamaized.voidscape.entity.NullServantEntity;

import java.util.EnumSet;

public class SpecialAugmentGoal extends Goal {

	private final NullServantEntity parent;
	private int cooldown;
	private int tick;

	private NullServantAugmentBlockEntity block1;
	private NullServantAugmentBlockEntity block2;
	private NullServantAugmentBlockEntity block3;

	public SpecialAugmentGoal(NullServantEntity parent) {
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		this.parent = parent;
	}

	@Override
	public boolean canUse() {
		return !parent.getAugmentAttack() && parent.getAugment() > 0 && parent.getHealth() < parent.getMaxHealth() / 2F && cooldown-- <= 0;
	}

	@Override
	public boolean canContinueToUse() {
		return parent.getAugmentAttack() && (block1 != null && block1.isAlive()) || (block2 != null && block2.isAlive()) || (block3 != null && block3.isAlive());
	}

	@Override
	public void start() {
		tick = 0;
		parent.setAugmentAttack(true);
		block1 = new NullServantAugmentBlockEntity(parent);
		block2 = new NullServantAugmentBlockEntity(parent);
		block3 = new NullServantAugmentBlockEntity(parent);
		parent.level().addFreshEntity(block1);
		parent.level().addFreshEntity(block2);
		parent.level().addFreshEntity(block3);
		block1.randomPosOrDiscard();
		block2.randomPosOrDiscard();
		block3.randomPosOrDiscard();
	}

	@Override
	public void stop() {
		parent.setAugmentAttack(false);
		block1 = null;
		block2 = null;
		block3 = null;
		cooldown = 600;
	}

	@Override
	public void tick() {
		switch (parent.getAugment()) {
			case NullServantEntity.AUGMENT_TITANITE -> {
				if (tick++ % 60 == 0)
					parent.heal(1F);
			}
		}
	}
}
