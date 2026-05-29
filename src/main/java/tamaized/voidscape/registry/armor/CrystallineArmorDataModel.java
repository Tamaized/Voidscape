package tamaized.voidscape.registry.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import tamaized.regutil.ArmorDataModel;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.model.ModelArmorCrystalline;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class CrystallineArmorDataModel extends ArmorDataModel {

	private final Identifier TEXTURE;
	private final Identifier TEXTURE_OVERLAY;

	private final boolean overlay;

	public CrystallineArmorDataModel(String textureName, boolean overlay) {
		super(!overlay, overlay, overlay);
		TEXTURE = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/models/armor/"+textureName+".png");
		TEXTURE_OVERLAY = overlay ? Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/models/armor/"+textureName+"_overlay.png") : TEXTURE;
		this.overlay = overlay;
	}


	@OnlyIn(Dist.CLIENT)
	protected abstract ModelLayerLocation modelLayerLocation();

	@Override
	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
		ModelArmorCrystalline<LivingEntity> model = new ModelArmorCrystalline<>(Minecraft.getInstance().getEntityModels().bakeLayer(modelLayerLocation()), !overlay);
		model.head.visible = false;
		model.headoverlay.visible = false;
		model.body.visible = false;
		model.leftarm.visible = false;
		model.rightarm.visible = false;
		model.leftleg.visible = false;
		model.rightleg.visible = false;
		model.leftfoot.visible = false;
		model.rightfoot.visible = false;
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
				model.body.visible = true;
				model.rightarm.visible = true;
				model.leftarm.visible = true;
			}
			case HEAD -> {
				model.head.visible = true;
				model.headoverlay.visible = true;
			}
		}
		return (A) model;
	}

	@Override
	public Optional<Identifier> getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, boolean inner) {
		return Optional.of(RegUtil.renderingArmorOverlay ? TEXTURE_OVERLAY : TEXTURE);
	}

}
