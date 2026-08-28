package tamaized.voidscape.coremod;

import net.neoforged.neoforgespi.transformation.ClassProcessorProvider;
import tamaized.voidscape.coremod.transformers.entity.render.transparency.ModifyEntityRenderTransparencyTransformer;
import tamaized.voidscape.coremod.transformers.entity.render.transparency.ModifyEntityRenderTypeTransformer;
import tamaized.voidscape.coremod.transformers.biome.BiomeSnowAndFreezeTransformer;
import tamaized.voidscape.coremod.transformers.item.ProjectileWeaponItemUseAmmoTransformer;

public class VoidscapeCoreMod implements ClassProcessorProvider {
	@Override
	public void createProcessors(Context context, Collector collector) {
		// Entity Render Transparency
		collector.add(new ModifyEntityRenderTransparencyTransformer());
		collector.add(new ModifyEntityRenderTypeTransformer());

		// Dimension Snow/Freeze Control
		collector.add(new BiomeSnowAndFreezeTransformer());

		// ProjectileWeaponItem
		collector.add(new ProjectileWeaponItemUseAmmoTransformer());
	}
}