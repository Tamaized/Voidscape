package tamaized.voidscape.coremod;

import cpw.mods.modlauncher.api.ITransformer;
import net.neoforged.neoforgespi.coremod.ICoreMod;
import tamaized.voidscape.coremod.transformers.elytra.DisableCapeRenderTransformer;
import tamaized.voidscape.coremod.transformers.elytra.ShouldRenderElytraTransformer;
import tamaized.voidscape.coremod.transformers.entity.render.transparency.ModifyEntityRenderTransparencyTransformer;
import tamaized.voidscape.coremod.transformers.entity.render.transparency.ModifyEntityRenderTypeTransformer;
import tamaized.voidscape.coremod.transformers.biome.BiomeSnowAndFreezeTransformer;
import tamaized.voidscape.coremod.transformers.render.ItemInHandRendererIsBowTransformer;
import tamaized.voidscape.coremod.transformers.visibility.LightTextureBrightnessTransformer;
import tamaized.voidscape.coremod.transformers.visibility.LightTextureNightVisionAndGammaTransformer;

import java.util.List;

public class VoidscapeCoreMod implements ICoreMod {
	@Override
	public Iterable<? extends ITransformer<?>> getTransformers() {
		return List.of(
			// Elytra
			new DisableCapeRenderTransformer(),
			new ShouldRenderElytraTransformer(),

			// Entity Render Transparency
			new ModifyEntityRenderTransparencyTransformer(),
			new ModifyEntityRenderTypeTransformer(),

			// Dimension Snow/Freeze Control
			new BiomeSnowAndFreezeTransformer(),

			// Visibility
			new LightTextureBrightnessTransformer(),
			new LightTextureNightVisionAndGammaTransformer(),

			// ItemInHandRenderer
			new ItemInHandRendererIsBowTransformer()
		);
	}
}