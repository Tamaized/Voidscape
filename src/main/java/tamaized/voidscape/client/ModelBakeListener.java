package tamaized.voidscape.client;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tamaized.voidscape.Voidscape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Voidscape.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelBakeListener {

	@SubscribeEvent
	public static void modelBake(ModelBakeEvent event) {
		for (int i = 0; i <= 1; i++) {
			ModelResourceLocation mrl = new ModelResourceLocation(Objects.requireNonNull(Voidscape.VOIDIC_CRYSTAL_ORE.getId()), i == 0 ? "" : "inventory");
			final IBakedModel model = event.getModelRegistry().get(mrl);
			event.getModelRegistry().put(mrl, new IBakedModel() {
				private Map<Direction, List<BakedQuad>> cachedQuads = Maps.newHashMap();

				@Nonnull
				@Override
				public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand) {
					return cachedQuads.computeIfAbsent(side, (face) -> {
						List<BakedQuad> quads = model.getQuads(state, side, rand);
						for (BakedQuad quad : quads)
							if (quads.indexOf(quad) == 1)
								LightUtil.setLightData(quad, 0xF000F0);
						return quads;
					});
				}

				@Override
				public boolean isAmbientOcclusion() {
					return model.isAmbientOcclusion();
				}

				@Override
				public boolean isGui3d() {
					return model.isGui3d();
				}

				@Override
				public boolean func_230044_c_() {
					return model.func_230044_c_();
				}

				@Override
				public boolean isBuiltInRenderer() {
					return model.isBuiltInRenderer();
				}

				@Nonnull
				@Override
				public TextureAtlasSprite getParticleTexture() {
					return model.getParticleTexture();
				}

				@Nonnull
				@Override
				public ItemOverrideList getOverrides() {
					return model.getOverrides();
				}

				@Nonnull
				@Override
				@SuppressWarnings("deprecation")
				public net.minecraft.client.renderer.model.ItemCameraTransforms getItemCameraTransforms() {
					return model.getItemCameraTransforms();
				}

			});
		}
	}
}