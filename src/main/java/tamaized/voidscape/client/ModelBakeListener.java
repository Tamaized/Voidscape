package tamaized.voidscape.client;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModArmors;
import tamaized.voidscape.registry.ModBlocks;
import tamaized.voidscape.registry.ModItems;
import tamaized.voidscape.registry.ModTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Voidscape.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelBakeListener {

	private static final Map<ResourceLocation, ResourceLocation> REMAPPER = new HashMap<>();

	private static void addBlock(List<ModelResourceLocation> list, RegistryObject<Block> object, String... extra) {
		add(list, object, "", extra);
		add(list, object, "inventory", extra);
	}

	private static void add(List<ModelResourceLocation> list, RegistryObject<Item> object, String... extra) {
		add(list, object, "inventory", extra);
	}

	private static void add(List<ModelResourceLocation> list, RegistryObject<?> object, String loc, String... extra) {
		List<String> extras = new ArrayList<>();
		extras.add("");
		extras.addAll(Arrays.asList(extra));
		String location = REMAPPER.getOrDefault(object.getId(), object.getId()).toString();
		extras.forEach(e -> list.add(new ModelResourceLocation(location.concat(e), loc)));
	}

	@SubscribeEvent
	public static void applyColors(RegisterColorHandlersEvent.Block event) {
		event.register((blockState, iBlockDisplayReader, blockPos, i) -> 0x331166, ModBlocks.ANTIROCK.get());
	}

	@SubscribeEvent
	public static void modelBake(ModelEvent.BakingCompleted event) {
		List<ModelResourceLocation> fullbrightList = new ArrayList<>();
		List<ModelResourceLocation> overlayList = new ArrayList<>();
		List<ModelResourceLocation> itemOverlayList = new ArrayList<>();

		add(fullbrightList, ModItems.VOIDIC_CRYSTAL);
		add(fullbrightList, ModItems.ETHEREAL_ESSENCE);
		add(fullbrightList, ModItems.FRUIT);
		add(itemOverlayList, ModItems.CHARRED_BONE);
		add(fullbrightList, ModItems.CHARRED_WARHAMMER_HEAD);

		add(fullbrightList, ModTools.VOIDIC_CRYSTAL_SWORD);
		add(fullbrightList, ModTools.VOIDIC_CRYSTAL_BOW);
		add(fullbrightList, ModTools.VOIDIC_CRYSTAL_XBOW);
		add(fullbrightList, ModTools.VOIDIC_CRYSTAL_AXE);
		add(fullbrightList, ModTools.VOIDIC_CRYSTAL_PICKAXE);
		add(fullbrightList, ModTools.VOIDIC_CRYSTAL_SHIELD);
		add(fullbrightList, ModArmors.VOIDIC_CRYSTAL_HELMET);
		add(fullbrightList, ModArmors.VOIDIC_CRYSTAL_CHEST);
		add(fullbrightList, ModArmors.VOIDIC_CRYSTAL_LEGS);
		add(fullbrightList, ModArmors.VOIDIC_CRYSTAL_BOOTS);

		add(fullbrightList, ModTools.CHARRED_WARHAMMER);

		add(fullbrightList, ModTools.CORRUPT_SWORD);
		add(fullbrightList, ModTools.CORRUPT_BOW);
		add(fullbrightList, ModTools.CORRUPT_XBOW);
		add(fullbrightList, ModTools.CORRUPT_AXE);
		add(fullbrightList, ModArmors.CORRUPT_HELMET);
		add(fullbrightList, ModArmors.CORRUPT_CHEST);
		add(fullbrightList, ModArmors.CORRUPT_LEGS);
		add(fullbrightList, ModArmors.CORRUPT_BOOTS);

		addBlock(overlayList, ModBlocks.VOIDIC_CRYSTAL_ORE);
		addBlock(fullbrightList, ModBlocks.VOIDIC_CRYSTAL_BLOCK);
		addBlock(fullbrightList, ModBlocks.ANTIROCK);
		addBlock(fullbrightList, ModBlocks.NULL_BLACK);
		addBlock(fullbrightList, ModBlocks.NULL_WHITE);
		add(fullbrightList, ModBlocks.PLANT, "inventory");
		add(fullbrightList, ModBlocks.PLANT, "state=void");
		add(fullbrightList, ModBlocks.PLANT, "state=overworld");
		add(fullbrightList, ModBlocks.PLANT, "state=nether");
		add(fullbrightList, ModBlocks.PLANT, "state=end");

		fullbrightList.forEach(mrl -> {
			final BakedModel model = event.getModels().get(mrl);
			if (model != null)
				event.getModels().put(mrl, new FullBrightModel(model));
			else
				Voidscape.LOGGER.error("Null Model! " + mrl);
		});
		overlayList.forEach(mrl -> {
			final BakedModel model = event.getModels().get(mrl);
			if (model != null)
				event.getModels().put(mrl, new FullBrightModel(model) {
					@NotNull
					@Override
					public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
						List<BakedQuad> quads = cachedQuads.get(side);
						if (quads == null) {
							quads = model.getQuads(state, side, rand);
							for (BakedQuad quad : quads) {
								if (quads.indexOf(quad) == 1)
									QuadTransformers.applyingLightmap(0xF000F0).process(quad);
							}
							cachedQuads.put(side, quads);
						}
						return quads; // computeIfAbsent has issues, don't use it
					}
				});
			else
				Voidscape.LOGGER.error("Null Model! " + mrl);
		});
		itemOverlayList.forEach(mrl -> {
			final BakedModel model = event.getModels().get(mrl);
			if (model != null)
				event.getModels().put(mrl, new FullBrightModel(model) {
					@NotNull
					@Override
					public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
						List<BakedQuad> quads = cachedQuads.get(side);
						if (quads == null) {
							quads = model.getQuads(state, side, rand);
							for (BakedQuad quad : quads) {
								if (quad.getSprite().getName().getPath().contains("_overlay"))
									QuadTransformers.applyingLightmap(0xF000F0).process(quad);
							}
							cachedQuads.put(side, quads);
						}
						return quads; // computeIfAbsent has issues, don't use it
					}
				});
			else
				Voidscape.LOGGER.error("Null Model! " + mrl);
		});

		impBroken(ModTools.VOIDIC_CRYSTAL_SWORD.get());
		impBroken(ModTools.VOIDIC_CRYSTAL_AXE.get());
		impBroken(ModTools.VOIDIC_CRYSTAL_BOW.get());
		impBroken(ModTools.VOIDIC_CRYSTAL_XBOW.get());
		impBroken(ModTools.VOIDIC_CRYSTAL_PICKAXE.get());
		impBroken(ModArmors.VOIDIC_CRYSTAL_HELMET.get());
		impBroken(ModArmors.VOIDIC_CRYSTAL_CHEST.get());
		impBroken(ModArmors.VOIDIC_CRYSTAL_LEGS.get());
		impBroken(ModArmors.VOIDIC_CRYSTAL_BOOTS.get());

		impBroken(ModTools.CHARRED_WARHAMMER.get());

		impBroken(ModTools.CORRUPT_SWORD.get());
		impBroken(ModTools.CORRUPT_AXE.get());
		impBroken(ModTools.CORRUPT_BOW.get());
		impBroken(ModTools.CORRUPT_XBOW.get());
		impBroken(ModArmors.CORRUPT_HELMET.get());
		impBroken(ModArmors.CORRUPT_CHEST.get());
		impBroken(ModArmors.CORRUPT_LEGS.get());
		impBroken(ModArmors.CORRUPT_BOOTS.get());

		impBow(ModTools.VOIDIC_CRYSTAL_BOW.get());
		impBow(ModTools.CORRUPT_BOW.get());

		impXBow(ModTools.VOIDIC_CRYSTAL_XBOW.get());
		impXBow(ModTools.CORRUPT_XBOW.get());

		impShield(ModTools.VOIDIC_CRYSTAL_SHIELD.get());


	}

	private static void impBroken(Item item) {
		ItemProperties.register(item, new ResourceLocation("broken"), (stack, level, entity, prop) -> RegUtil.ToolAndArmorHelper.isBroken(stack) ? 1F : 0F);
	}

	private static void impBow(Item item) {
		ItemProperties.register(item, new ResourceLocation("pull"), (stack, level, entity, prop) ->

				entity == null ? 0.0F : entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F);

		ItemProperties.register(item, new ResourceLocation("pulling"), (stack, level, entity, prop) ->

				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
	}

	private static void impXBow(Item item) {
		ItemProperties.register(item, new ResourceLocation("pull"), (stack, level, entity, prop) ->

				entity == null ? 0.0F : CrossbowItem.isCharged(stack) ? 0.0F : (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(stack));

		ItemProperties.register(item, new ResourceLocation("pulling"), (stack, level, entity, prop) ->

				entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

		ItemProperties.register(item, new ResourceLocation("charged"), (stack, level, entity, prop) ->

				entity != null && CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

		ItemProperties.register(item, new ResourceLocation("firework"), (stack, level, entity, prop) ->

				entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
	}

	private static void impShield(Item item) {
		ItemProperties.register(item, new ResourceLocation("blocking"), (stack, level, entity, prop) ->

				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
	}

	public static void redirectModels(ModelBakery bakery) {
		redirectModelLocation(bakery, "voidic", "voidic_crystal_", ModTools.
				VOIDIC_CRYSTAL_AXE, ModTools.
				VOIDIC_CRYSTAL_PICKAXE, ModTools.
				VOIDIC_CRYSTAL_SWORD, ModTools.
				VOIDIC_CRYSTAL_SHIELD, ModTools.
				VOIDIC_CRYSTAL_BOW, ModTools.
				VOIDIC_CRYSTAL_XBOW, ModArmors.
				VOIDIC_CRYSTAL_HELMET, ModArmors.
				VOIDIC_CRYSTAL_CHEST, ModArmors.
				VOIDIC_CRYSTAL_LEGS, ModArmors.
				VOIDIC_CRYSTAL_BOOTS);
		redirectModelLocation(bakery, "charred", "charred_", ModTools.CHARRED_WARHAMMER);
		redirectModelLocation(bakery, "corrupt", "corrupt_", ModTools.
				CORRUPT_AXE, ModTools.
				CORRUPT_SWORD, ModTools.
				CORRUPT_BOW, ModTools.
				CORRUPT_XBOW, ModArmors.
				CORRUPT_HELMET, ModArmors.
				CORRUPT_CHEST, ModArmors.
				CORRUPT_LEGS, ModArmors.
				CORRUPT_BOOTS);
	}

	@SafeVarargs
	private static void redirectModelLocation(ModelBakery bakery, String subfolder, String remove, RegistryObject<Item>... items) {
		for (RegistryObject<Item> item : items) {
			ResourceLocation location = item.getId();
			if (location == null)
				continue;
			ModelResourceLocation oldMrl = new ModelResourceLocation(location, "inventory");
			ResourceLocation rl = new ResourceLocation(location.getNamespace(), subfolder.concat("/").concat(location.getPath().replaceFirst(remove, "")));
			ModelResourceLocation mrl = new ModelResourceLocation(rl, "inventory");
			REMAPPER.put(location, rl);
			bakery.loadTopLevel(mrl);
			bakery.unbakedCache.put(oldMrl, bakery.unbakedCache.get(mrl));
			Minecraft.getInstance().getItemRenderer().getItemModelShaper().
					register(item.get(), mrl);
		}
	}

	public static void clearOldModels(ModelBakery bakery) {
		REMAPPER.keySet().forEach(location -> {
			ModelResourceLocation oldMrl = new ModelResourceLocation(location, "inventory");
			bakery.unbakedCache.remove(oldMrl);
			bakery.topLevelModels.remove(oldMrl);
		});
	}

	private static class FullBrightModel extends BakedModelWrapper<BakedModel> {

		private final BakedModel model;
		private final ItemOverrides overrides;
		Map<Direction, List<BakedQuad>> cachedQuads = Maps.newHashMap();

		private FullBrightModel(BakedModel delegate) {
			super(delegate);
			model = delegate;
			overrides = new FullbrightItemOverrideList(delegate.getOverrides());
		}

		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
			List<BakedQuad> quads = cachedQuads.get(side);
			if (quads == null) {
				quads = model.getQuads(state, side, rand);
				for (BakedQuad quad : quads) {
					QuadTransformers.applyingLightmap(0xF000F0).process(quad);
					quad.shade = false;
				}
				cachedQuads.put(side, quads);
			}
			return quads; // computeIfAbsent has issues, don't use it
		}

		@NotNull
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
			return getQuads(state, side, rand);
		}


		@Override
		public boolean useAmbientOcclusion() {
			return false;
		}

		@Override
		public boolean useAmbientOcclusion(BlockState state) {
			return false;
		}

		@Override
		public boolean useAmbientOcclusion(BlockState state, RenderType renderType) {
			return false;
		}

		@NotNull
		@Override
		public ItemOverrides getOverrides() {
			return overrides;
		}

		@NotNull
		@Override
		@SuppressWarnings("deprecation")
		public net.minecraft.client.renderer.block.model.ItemTransforms getTransforms() {
			return model.getTransforms();
		}

		private static class FullbrightItemOverrideList extends ItemOverrides {

			public FullbrightItemOverrideList(ItemOverrides delegate) {
				properties = delegate.properties;
				List<BakedOverride> overridesList = new ArrayList<>();
				for (BakedOverride override : delegate.overrides) {
					if (override.model != null)
						overridesList.add(new BakedOverride(override.matchers, new FullBrightModel(override.model)));
				}
				overrides = overridesList.toArray(new BakedOverride[0]);
			}

		}


	}
}