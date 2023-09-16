package tamaized.voidscape.entity.ai.wrath;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import tamaized.voidscape.entity.VoidsWrathEntity;

import java.util.EnumSet;

public class ChargedExplosionGoal extends Goal {

    private final VoidsWrathEntity parent;
    private int tick;

    public ChargedExplosionGoal(VoidsWrathEntity parent) {
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.parent = parent;
    }

    @Override
    public boolean canUse() {
        return parent.getHealth() < parent.getMaxHealth() / 2F && parent.tickCount % 60 == 0 && parent.getRandom().nextInt(3) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return parent.isPowered();
    }

    @Override
    public void start() {
        parent.markGlowing(true);
        tick = 0;
    }

    @Override
    public void stop() {
        parent.markGlowing(false);
    }

    @Override
    public void tick() {
        if (tick > 100) {
            parent.level().explode(parent, parent.getX(), parent.getY(), parent.getZ(), 6F, Level.ExplosionInteraction.MOB);
            parent.markGlowing(false);
        } else {
            tick++;
        }
    }

}
