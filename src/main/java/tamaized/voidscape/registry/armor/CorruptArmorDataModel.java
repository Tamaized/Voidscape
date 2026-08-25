package tamaized.voidscape.registry.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jspecify.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.regutil.ArmorDataModel;
import tamaized.regutil.GearItemHandler;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.ModModelLayerLocations;
import tamaized.voidscape.client.entity.model.ModelArmorCorrupt;

import java.util.Optional;

@Configurable
public class CorruptArmorDataModel extends ArmorDataModel {

	@Autowired(dist = Dist.CLIENT)
	private ModModelLayerLocations modelLayerLocations;

	@Autowired
	private GearItemHandler gearItemHandler;

	private final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/models/armor/corrupt.png");
	private final Identifier TEXTURE_OVERLAY = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/models/armor/corrupt_overlay.png");

	public CorruptArmorDataModel() {
		super(false, true, true);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public @Nullable Model<?> getArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model<?> original) {
		Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
		if (equippable == null)
			return null;
		EquipmentSlot armorSlot = equippable.slot();
		ModelArmorCorrupt<HumanoidRenderState> model = new ModelArmorCorrupt<>(Minecraft.getInstance().getEntityModels().bakeLayer(
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
			}
			case HEAD -> {
				model.head.visible = true;
				model.headoverlay.visible = true;
			}
			default -> {
			}
		}
		return model;
	}

	@Override
	public Optional<Identifier> getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer) {
		return Optional.of(gearItemHandler.renderingArmorOverlay ? TEXTURE_OVERLAY : TEXTURE);
	}

}
