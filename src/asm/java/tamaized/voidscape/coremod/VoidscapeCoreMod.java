package tamaized.voidscape.coremod;

import net.neoforged.neoforgespi.transformation.ClassProcessorProvider;
import tamaized.voidscape.coremod.transformers.elytra.DisableCapeRenderTransformer;
import tamaized.voidscape.coremod.transformers.elytra.ShouldRenderElytraTransformer;
import tamaized.voidscape.coremod.transformers.entity.render.transparency.ModifyEntityRenderTransparencyTransformer;
import tamaized.voidscape.coremod.transformers.entity.render.transparency.ModifyEntityRenderTypeTransformer;
import tamaized.voidscape.coremod.transformers.biome.BiomeSnowAndFreezeTransformer;
import tamaized.voidscape.coremod.transformers.item.ProjectileWeaponItemUseAmmoTransformer;
import tamaized.voidscape.coremod.transformers.render.ItemInHandRendererIsBowTransformer;
import tamaized.voidscape.coremod.transformers.visibility.LightTextureBrightnessTransformer;
import tamaized.voidscape.coremod.transformers.visibility.LightTextureNightVisionAndGammaTransformer;

public class VoidscapeCoreMod implements ClassProcessorProvider {
	@Override
	public void createProcessors(Context context, Collector collector) {
		// Elytra
		collector.add(new DisableCapeRenderTransformer());
		collector.add(new ShouldRenderElytraTransformer());

		// Entity Render Transparency
		collector.add(new ModifyEntityRenderTransparencyTransformer());
		collector.add(new ModifyEntityRenderTypeTransformer());

		// Dimension Snow/Freeze Control
		collector.add(new BiomeSnowAndFreezeTransformer());

		// Visibility
		collector.add(new LightTextureBrightnessTransformer());
		collector.add(new LightTextureNightVisionAndGammaTransformer());

		// ItemInHandRenderer
		collector.add(new ItemInHandRendererIsBowTransformer());

		// ProjectileWeaponItem
		collector.add(new ProjectileWeaponItemUseAmmoTransformer());
	}
}