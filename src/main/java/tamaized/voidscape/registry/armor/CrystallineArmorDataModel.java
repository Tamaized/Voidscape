package tamaized.voidscape.registry.armor;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.regutil.ArmorDataModel;
import tamaized.regutil.GearItemHandler;
import tamaized.voidscape.Voidscape;

import java.util.Optional;

@Configurable
public abstract class CrystallineArmorDataModel extends ArmorDataModel {

	@Autowired
	private GearItemHandler gearItemHandler;

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

	// FIXME
	/*@Override
	@OnlyIn(Dist.CLIENT)
	public @Nullable Model<?> getArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model<?> original) {
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
	}*/

	@Override
	public Optional<Identifier> getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer) {
		return Optional.of(gearItemHandler.renderingArmorOverlay ? TEXTURE_OVERLAY : TEXTURE);
	}

}
