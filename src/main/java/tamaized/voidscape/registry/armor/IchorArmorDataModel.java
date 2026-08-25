package tamaized.voidscape.registry.armor;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import tamaized.beanification.Autowired;
import tamaized.voidscape.client.entity.ModModelLayerLocations;

public class IchorArmorDataModel extends CrystallineArmorDataModel {

	@Autowired(dist = Dist.CLIENT)
	private ModModelLayerLocations modelLayerLocations;

	public IchorArmorDataModel() {
		super("ichor", true);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected ModelLayerLocation modelLayerLocation() {
		return modelLayerLocations.MODEL_ARMOR_ICHOR;
	}

}
