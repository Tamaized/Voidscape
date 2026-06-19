package tamaized.voidscape.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.level.Level;
import tamaized.beanification.Autowired;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModDataAttachments;

public class VoidlingEntity extends Spider implements IEthereal {

	@Autowired
	private static ModAttributes attributes;

	@Autowired
	private static ModDataAttachments dataAttachments;

	public VoidlingEntity(EntityType<? extends VoidlingEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Spider.createAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.4F)
				.add(Attributes.ARMOR, 8.0D)
				.add(attributes.VOIDIC_DMG, 3.0D)
				.add(attributes.VOIDIC_RES, 2.0D);
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		if (super.doHurtTarget(entity)) {
			if (entity instanceof LivingEntity living)
				entity.getData(dataAttachments.INSANITY.get()).addInfusion(25, living);
			return true;
		}
		return false;
	}
}
