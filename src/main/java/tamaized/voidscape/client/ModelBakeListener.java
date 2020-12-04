package tamaized.voidscape.client;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModArmors;
import tamaized.voidscape.registry.ModBlocks;
import tamaized.voidscape.registry.ModItems;
import tamaized.voidscape.registry.ModTools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Voidscape.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelBakeListener {

	private static void addBlock(List<ModelResourceLocation> list, RegistryObject<Block> object) {
		add(list, object, "");
		add(list, object, "inventory");
	}

	private static void add(List<ModelResourceLocation> list, RegistryObject<Item> object) {
		add(list, object, "inventory");
	}

	private static void add(List<ModelResourceLocation> list, RegistryObject object, String loc) {
		list.add(new ModelResourceLocation(object.getId(), loc));
	}

	@SubscribeEvent
	public static void modelBake(ModelBakeEvent event) {
		List<ModelResourceLocation> fullbrightList = new ArrayList<>();
		List<ModelResourceLocation> overlayList = new ArrayList<>();
		add(fullbrightList, ModItems.VOIDIC_CRYSTAL);
		add(fullbrightList, ModTools.VOIDIC_CRYSTAL_SWORD);
		add(fullbrightList, ModTools.VOIDIC_CRYSTAL_AXE);
		add(fullbrightList, ModTools.VOIDIC_CRYSTAL_PICKAXE);
		add(fullbrightList, ModArmors.VOIDIC_CRYSTAL_HELMET);
		add(fullbrightList, ModArmors.VOIDIC_CRYSTAL_CHEST);
		add(fullbrightList, ModArmors.VOIDIC_CRYSTAL_LEGS);
		add(fullbrightList, ModArmors.VOIDIC_CRYSTAL_BOOTS);
		addBlock(overlayList, ModBlocks.VOIDIC_CRYSTAL_ORE);
		fullbrightList.forEach(mrl -> {
			final IBakedModel model = event.getModelRegistry().get(mrl);
			event.getModelRegistry().put(mrl, new FullBrightModel(model));
		});
		overlayList.forEach(mrl -> {
			final IBakedModel model = event.getModelRegistry().get(mrl);
			event.getModelRegistry().put(mrl, new FullBrightModel(model) {
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
			});
		});
	}

	private static class FullBrightModel implements IBakedModel {

		private final IBakedModel model;
		Map<Direction, List<BakedQuad>> cachedQuads = Maps.newHashMap();

		private FullBrightModel(IBakedModel delegate) {
			model = delegate;
		}

		@Nonnull
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand) {
			return cachedQuads.computeIfAbsent(side, (face) -> {
				List<BakedQuad> quads = model.getQuads(state, side, rand);
				for (BakedQuad quad : quads)
					LightUtil.setLightData(quad, 0xF000F0);
				return quads;
			});
		}

		@Override
		public boolean useAmbientOcclusion() {
			return model.useAmbientOcclusion();
		}

		@Override
		public boolean isGui3d() {
			return model.isGui3d();
		}

		@Override
		public boolean usesBlockLight() {
			return model.usesBlockLight();
		}

		@Override
		public boolean isCustomRenderer() {
			return model.isCustomRenderer();
		}

		@Nonnull
		@Override
		public TextureAtlasSprite getParticleIcon() {
			return model.getParticleIcon();
		}

		@Nonnull
		@Override
		public ItemOverrideList getOverrides() {
			return model.getOverrides();
		}

		@Nonnull
		@Override
		@SuppressWarnings("deprecation")
		public net.minecraft.client.renderer.model.ItemCameraTransforms getTransforms() {
			return model.getTransforms();
		}


	}
}