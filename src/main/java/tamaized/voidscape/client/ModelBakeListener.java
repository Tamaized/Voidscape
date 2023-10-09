package tamaized.voidscape.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModArmors;
import tamaized.voidscape.registry.ModTools;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Voidscape.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelBakeListener {

	private static final Map<ResourceLocation, ResourceLocation> REMAPPER = new HashMap<>();

	@SubscribeEvent
	public static void modelBake(ModelEvent.ModifyBakingResult event) {
		// Broken Variants
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

		impBroken(ModTools.TITANITE_SWORD.get());
		impBroken(ModTools.TITANITE_AXE.get());
		impBroken(ModTools.TITANITE_PICKAXE.get());
		impBroken(ModTools.TITANITE_HOE.get());
		impBroken(ModTools.TITANITE_BOW.get());
		impBroken(ModTools.TITANITE_XBOW.get());
		impBroken(ModArmors.TITANITE_HELMET.get());
		impBroken(ModArmors.TITANITE_CHEST.get());
		impBroken(ModArmors.TITANITE_LEGS.get());
		impBroken(ModArmors.TITANITE_BOOTS.get());

		impBroken(ModTools.ICHOR_SWORD.get());
		impBroken(ModTools.ICHOR_AXE.get());
		impBroken(ModTools.ICHOR_PICKAXE.get());
		impBroken(ModTools.ICHOR_BOW.get());
		impBroken(ModTools.ICHOR_XBOW.get());

		// Bows
		impBow(ModTools.VOIDIC_CRYSTAL_BOW.get());
		impBow(ModTools.CORRUPT_BOW.get());
		impBow(ModTools.TITANITE_BOW.get());
		impBow(ModTools.ICHOR_BOW.get());

		// XBows
		impXBow(ModTools.VOIDIC_CRYSTAL_XBOW.get());
		impXBow(ModTools.CORRUPT_XBOW.get());
		impXBow(ModTools.TITANITE_XBOW.get());
		impXBow(ModTools.ICHOR_XBOW.get());

		// Shields
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
		redirectModelLocation(bakery, "voidic", "voidic_crystal_",
				ModTools.VOIDIC_CRYSTAL_AXE,
				ModTools.VOIDIC_CRYSTAL_PICKAXE,
				ModTools.VOIDIC_CRYSTAL_SWORD,
				ModTools.VOIDIC_CRYSTAL_SHIELD,
				ModTools.VOIDIC_CRYSTAL_BOW,
				ModTools.VOIDIC_CRYSTAL_XBOW,
				ModArmors.VOIDIC_CRYSTAL_HELMET,
				ModArmors.VOIDIC_CRYSTAL_CHEST,
				ModArmors.VOIDIC_CRYSTAL_LEGS,
				ModArmors.VOIDIC_CRYSTAL_BOOTS
		);
		redirectModelLocation(bakery, "charred", "charred_", ModTools.CHARRED_WARHAMMER);
		redirectModelLocation(bakery, "corrupt", "corrupt_",
				ModTools.CORRUPT_AXE,
				ModTools.CORRUPT_SWORD,
				ModTools.CORRUPT_BOW,
				ModTools.CORRUPT_XBOW,
				ModArmors.CORRUPT_HELMET,
				ModArmors.CORRUPT_CHEST,
				ModArmors.CORRUPT_LEGS,
				ModArmors.CORRUPT_BOOTS
		);
		redirectModelLocation(bakery, "titanite", "titanite_",
				ModTools.TITANITE_AXE,
				ModTools.TITANITE_PICKAXE,
				ModTools.TITANITE_HOE,
				ModTools.TITANITE_SWORD,
				ModTools.TITANITE_BOW,
				ModTools.TITANITE_XBOW,
				ModArmors.TITANITE_HELMET,
				ModArmors.TITANITE_CHEST,
				ModArmors.TITANITE_LEGS,
				ModArmors.TITANITE_BOOTS
		);
		redirectModelLocation(bakery, "ichor", "ichor_",
				ModTools.ICHOR_SWORD,
				ModTools.ICHOR_AXE,
				ModTools.ICHOR_PICKAXE,
				ModTools.ICHOR_BOW,
				ModTools.ICHOR_XBOW
		);
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

}