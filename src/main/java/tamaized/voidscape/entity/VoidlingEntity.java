package tamaized.voidscape.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModDataAttachments;

public class VoidlingEntity extends Spider implements IEthereal {

	public VoidlingEntity(EntityType<? extends VoidlingEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Spider.createAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.4F)
				.add(ModAttributes.VOIDIC_DMG.get(), 3.0D)
				.add(ModAttributes.VOIDIC_RES.get(), 2.0D);
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		if (super.doHurtTarget(entity)) {
			if (entity instanceof LivingEntity living)
				entity.getData(ModDataAttachments.INSANITY.get()).addInfusion(25, living);
			return true;
		}
		return false;
	}
}
