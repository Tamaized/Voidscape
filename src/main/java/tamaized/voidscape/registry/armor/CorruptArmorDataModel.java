package tamaized.voidscape.registry.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import tamaized.beanification.Autowired;
import tamaized.regutil.ArmorDataModel;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.ModModelLayerLocations;
import tamaized.voidscape.client.entity.model.ModelArmorCorrupt;

import java.util.Optional;

public class CorruptArmorDataModel extends ArmorDataModel {

	@Autowired(dist = Dist.CLIENT)
	private static ModModelLayerLocations modelLayerLocations;

	private final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/models/armor/corrupt.png");
	private final Identifier TEXTURE_OVERLAY = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/models/armor/corrupt_overlay.png");

	public CorruptArmorDataModel() {
		super(false, true, true);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	@SuppressWarnings("unchecked")
	public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
		ModelArmorCorrupt<LivingEntity> model = new ModelArmorCorrupt<>(Minecraft.getInstance().getEntityModels().bakeLayer(
			armorSlot == EquipmentSlot.LEGS ?
				modelLayerLocations.MODEL_ARMOR_CORRUPT_INNER :
				modelLayerLocations.MODEL_ARMOR_CORRUPT_OUTER
		));
		model.rightfoot.visible = false;
		model.leftfoot.visible = false;
		model.bodyToLeg.visible = false;
		model.rightleg.visible = false;
		model.leftleg.visible = false;
		model.body.visible = false;
		model.rightarm.visible = false;
		model.leftarm.visible = false;
		model.head.visible = false;
		model.headoverlay.visible = false;
		switch (armorSlot) {
			case FEET -> {
				model.rightfoot.visible = true;
				model.leftfoot.visible = true;
			}
			case LEGS -> {
				model.rightleg.visible = true;
				model.leftleg.visible = true;
			}
			case CHEST -> {
				model.bodyToLeg.visible = true;
				model.body.visible = true;
				model.rightarm.visible = true;
				model.leftarm.visible = true;
				float tick = entityLiving.tickCount + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
				float scale = 0.05F;
				float amp = 0.15F;
				float offset = 0.25F;
				model.topLeftTentacle.xRot = Mth.cos(tick * scale) * amp + offset;
				model.topLeftTentacle.yRot = Mth.sin(tick * scale + 0.2F) * amp + offset;
				model.topRightTentacle.xRot = Mth.sin(tick * scale + 0.4F) * amp + offset;
				model.topRightTentacle.yRot = Mth.cos(tick * scale + 0.6F) * amp - offset;
				model.bottomLeftTentacle.xRot = Mth.sin(tick * scale + 0.7F) * amp - offset;
				model.bottomLeftTentacle.yRot = Mth.cos(tick * scale + 0.5F) * amp + offset;
				model.bottomRightTentacle.xRot = Mth.cos(tick * scale + 0.3F) * amp - offset;
				model.bottomRightTentacle.yRot = Mth.sin(tick * scale + 0.1F) * amp - offset;
			}
			case HEAD -> {
				model.head.visible = true;
				model.headoverlay.visible = true;
			}
			default -> {
			}
		}
		return (A) model;
	}

	@Override
	public Optional<Identifier> getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, boolean inner) {
		return Optional.of(inner ? TEXTURE_OVERLAY : TEXTURE);
	}

}
