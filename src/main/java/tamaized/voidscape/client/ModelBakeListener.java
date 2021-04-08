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
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Voidscape.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelBakeListener {

	private static void addBlock(List<ModelResourceLocation> list, RegistryObject<Block> object, String... extra) {
		add(list, object, "", extra);
		add(list, object, "inventory", extra);
	}

	private static void add(List<ModelResourceLocation> list, RegistryObject<Item> object, String... extra) {
		add(list, object, "inventory", extra);
	}

	private static void add(List<ModelResourceLocation> list, RegistryObject object, String loc, String... extra) {
		List<String> extras = new ArrayList<>();
		extras.add("");
		extras.addAll(Arrays.asList(extra));
		extras.forEach(e -> list.add(new ModelResourceLocation(object.getId().toString().concat(e), loc)));
	}

	@SubscribeEvent
	public static void applyColors(ColorHandlerEvent.Block event) {
		event.getBlockColors().register((blockState, iBlockDisplayReader, blockPos, i) -> 0x331166, ModBlocks.ANTIROCK.get());
	}

	@SubscribeEvent
	public static void modelBake(ModelBakeEvent event) {
		List<ModelResourceLocation> fullbrightList = new ArrayList<>();
		List<ModelResourceLocation> overlayList = new ArrayList<>();
		add(fullbrightList, ModItems.VOIDIC_CRYSTAL);
		add(fullbrightList, ModItems.ETHEREAL_ESSENCE);
		add(fullbrightList, ModTools.VOIDIC_CRYSTAL_SWORD);
		add(fullbrightList, ModTools.VOIDIC_CRYSTAL_BOW);
		add(fullbrightList, ModTools.VOIDIC_CRYSTAL_SHIELD);
		add(fullbrightList, ModTools.VOIDIC_CRYSTAL_AXE);
		add(fullbrightList, ModTools.VOIDIC_CRYSTAL_PICKAXE);
		add(fullbrightList, ModArmors.VOIDIC_CRYSTAL_HELMET);
		add(fullbrightList, ModArmors.VOIDIC_CRYSTAL_CHEST);
		add(fullbrightList, ModArmors.VOIDIC_CRYSTAL_LEGS);
		add(fullbrightList, ModArmors.VOIDIC_CRYSTAL_BOOTS);
		addBlock(overlayList, ModBlocks.VOIDIC_CRYSTAL_ORE);
		addBlock(fullbrightList, ModBlocks.ANTIROCK);
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
		ItemModelsProperties.register(ModTools.VOIDIC_CRYSTAL_BOW.get(), new ResourceLocation("pull"), (stack, level, entity) ->

				entity == null ? 0.0F : entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F);
		ItemModelsProperties.register(ModTools.VOIDIC_CRYSTAL_BOW.get(), new ResourceLocation("pulling"), (stack, level, entity) ->

				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
		ItemModelsProperties.register(ModTools.VOIDIC_CRYSTAL_SHIELD.get(), new ResourceLocation("blocking"), (stack, level, entity) ->

				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
	}

	private static class FullBrightModel implements IBakedModel {

		private final IBakedModel model;
		private final ItemOverrideList overrides;
		Map<Direction, List<BakedQuad>> cachedQuads = Maps.newHashMap();
		private FullBrightModel(IBakedModel delegate) {
			model = delegate;
			overrides = new FullbrightItemOverrideList(delegate.getOverrides());
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
			return overrides;
		}

		@Nonnull
		@Override
		@SuppressWarnings("deprecation")
		public net.minecraft.client.renderer.model.ItemCameraTransforms getTransforms() {
			return model.getTransforms();
		}

		private static class FullbrightItemOverrideList extends ItemOverrideList {

			public FullbrightItemOverrideList(ItemOverrideList delegate) {
				overrides.addAll(delegate.overrides);
				overrideModels = new ArrayList<>();
				delegate.overrideModels.forEach(model -> overrideModels.add(new FullBrightModel(model)));
			}

		}


	}
}