package tamaized.voidscape.client;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.item.BreakableHelper;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;
import tamaized.voidscape.registry.armor.set.*;
import tamaized.voidscape.registry.tool.set.*;

import java.util.HashMap;
import java.util.Map;

@Component
public class ModelBakeListener {

	@Autowired
	private ItemModelOverridePredicates itemModelOverridePredicates;

	@Autowired
	private ModToolSetComponentDirectory toolSets;

	@Autowired
	private ModArmorSetComponentDirectory armorSets;

	private final Map<ResourceLocation, ResourceLocation> REMAPPER = new HashMap<>();

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::modelBake);
	}

	private void modelBake(ModelEvent.ModifyBakingResult event) {
		// Broken Variants
		impBroken(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_SWORD.get());
		impBroken(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_AXE.get());
		impBroken(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_BOW.get());
		impBroken(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_XBOW.get());
		impBroken(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_PICKAXE.get());
		impBroken(armorSets.voidicCrystalArmorSet().VOIDIC_CRYSTAL_HELMET.get());
		impBroken(armorSets.voidicCrystalArmorSet().VOIDIC_CRYSTAL_CHEST.get());
		impBroken(armorSets.voidicCrystalArmorSet().VOIDIC_CRYSTAL_LEGS.get());
		impBroken(armorSets.voidicCrystalArmorSet().VOIDIC_CRYSTAL_BOOTS.get());

		impBroken(toolSets.charredToolSet().CHARRED_WARHAMMER.get());

		impBroken(toolSets.corruptToolSet().CORRUPT_SWORD.get());
		impBroken(toolSets.corruptToolSet().CORRUPT_AXE.get());
		impBroken(toolSets.corruptToolSet().CORRUPT_BOW.get());
		impBroken(toolSets.corruptToolSet().CORRUPT_XBOW.get());
		impBroken(armorSets.corruptArmorSet().CORRUPT_HELMET.get());
		impBroken(armorSets.corruptArmorSet().CORRUPT_CHEST.get());
		impBroken(armorSets.corruptArmorSet().CORRUPT_LEGS.get());
		impBroken(armorSets.corruptArmorSet().CORRUPT_BOOTS.get());

		impBroken(toolSets.titaniteToolSet().TITANITE_SWORD.get());
		impBroken(toolSets.titaniteToolSet().TITANITE_AXE.get());
		impBroken(toolSets.titaniteToolSet().TITANITE_PICKAXE.get());
		impBroken(toolSets.titaniteToolSet().TITANITE_HOE.get());
		impBroken(toolSets.titaniteToolSet().TITANITE_BOW.get());
		impBroken(toolSets.titaniteToolSet().TITANITE_XBOW.get());
		impBroken(armorSets.titaniteArmorSet().TITANITE_HELMET.get());
		impBroken(armorSets.titaniteArmorSet().TITANITE_CHEST.get());
		impBroken(armorSets.titaniteArmorSet().TITANITE_LEGS.get());
		impBroken(armorSets.titaniteArmorSet().TITANITE_BOOTS.get());

		impBroken(toolSets.ichorToolSet().ICHOR_SWORD.get());
		impBroken(toolSets.ichorToolSet().ICHOR_AXE.get());
		impBroken(toolSets.ichorToolSet().ICHOR_PICKAXE.get());
		impBroken(toolSets.ichorToolSet().ICHOR_BOW.get());
		impBroken(toolSets.ichorToolSet().ICHOR_XBOW.get());
		impBroken(armorSets.ichorArmorSet().ICHOR_HELMET.get());
		impBroken(armorSets.ichorArmorSet().ICHOR_CHEST.get());
		impBroken(armorSets.ichorArmorSet().ICHOR_LEGS.get());
		impBroken(armorSets.ichorArmorSet().ICHOR_BOOTS.get());

		impBroken(toolSets.astralToolSet().ASTRAL_SWORD.get());
		impBroken(toolSets.astralToolSet().ASTRAL_AXE.get());
		impBroken(toolSets.astralToolSet().ASTRAL_PICKAXE.get());
		impBroken(toolSets.astralToolSet().ASTRAL_SHOVEL.get());
		impBroken(toolSets.astralToolSet().ASTRAL_BOW.get());
		impBroken(toolSets.astralToolSet().ASTRAL_XBOW.get());
		impBroken(armorSets.astralArmorSet().ASTRAL_HELMET.get());
		impBroken(armorSets.astralArmorSet().ASTRAL_CHEST.get());
		impBroken(armorSets.astralArmorSet().ASTRAL_LEGS.get());
		impBroken(armorSets.astralArmorSet().ASTRAL_BOOTS.get());

		// Bows
		impBow(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_BOW.get());
		impBow(toolSets.corruptToolSet().CORRUPT_BOW.get());
		impBow(toolSets.titaniteToolSet().TITANITE_BOW.get());
		impBow(toolSets.ichorToolSet().ICHOR_BOW.get());
		impBow(toolSets.astralToolSet().ASTRAL_BOW.get());

		// XBows
		impXBow(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_XBOW.get());
		impXBow(toolSets.corruptToolSet().CORRUPT_XBOW.get());
		impXBow(toolSets.titaniteToolSet().TITANITE_XBOW.get());
		impXBow(toolSets.ichorToolSet().ICHOR_XBOW.get());
		impXBow(toolSets.astralToolSet().ASTRAL_XBOW.get());

		// Shields
		impShield(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_SHIELD.get());

	}

	private void impBroken(Item item) {
		ItemProperties.register(item, itemModelOverridePredicates.BROKEN, (stack, level, entity, prop) -> BreakableHelper.isBroken(stack) ? 1F : 0F);
	}

	private void impBow(Item item) {
		ItemProperties.register(item, itemModelOverridePredicates.PULL, (stack, level, entity, prop) ->

			entity == null ? 0.0F : entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F);

		ItemProperties.register(item, itemModelOverridePredicates.PULLING, (stack, level, entity, prop) ->

			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
	}

	private void impXBow(Item item) {
		ItemProperties.register(item, itemModelOverridePredicates.PULL, (stack, level, entity, prop) ->

			entity == null ? 0.0F : CrossbowItem.isCharged(stack) ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(stack, entity));

		ItemProperties.register(item, itemModelOverridePredicates.PULLING, (stack, level, entity, prop) ->

			entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

		ItemProperties.register(item, itemModelOverridePredicates.CHARGED, (stack, level, entity, prop) ->

			entity != null && CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

		ItemProperties.register(item, itemModelOverridePredicates.FIREWORK, (stack, level, entity, prop) ->

			entity != null && CrossbowItem.isCharged(stack) && stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY).contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
	}

	private void impShield(Item item) {
		ItemProperties.register(item, itemModelOverridePredicates.BLOCKING, (stack, level, entity, prop) ->

			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
	}

	public void redirectModels(ModelBakery bakery) {
		redirectModelLocation(bakery, "voidic", "voidic_crystal_",
			toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_AXE,
			toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_PICKAXE,
			toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_SWORD,
			toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_SHIELD,
			toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_BOW,
			toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_XBOW,
			armorSets.voidicCrystalArmorSet().VOIDIC_CRYSTAL_HELMET,
			armorSets.voidicCrystalArmorSet().VOIDIC_CRYSTAL_CHEST,
			armorSets.voidicCrystalArmorSet().VOIDIC_CRYSTAL_LEGS,
			armorSets.voidicCrystalArmorSet().VOIDIC_CRYSTAL_BOOTS
		);
		redirectModelLocation(bakery, "charred", "charred_", toolSets.charredToolSet().CHARRED_WARHAMMER);
		redirectModelLocation(bakery, "corrupt", "corrupt_",
			toolSets.corruptToolSet().CORRUPT_AXE,
			toolSets.corruptToolSet().CORRUPT_SWORD,
			toolSets.corruptToolSet().CORRUPT_BOW,
			toolSets.corruptToolSet().CORRUPT_XBOW,
			armorSets.corruptArmorSet().CORRUPT_HELMET,
			armorSets.corruptArmorSet().CORRUPT_CHEST,
			armorSets.corruptArmorSet().CORRUPT_LEGS,
			armorSets.corruptArmorSet().CORRUPT_BOOTS
		);
		redirectModelLocation(bakery, "titanite", "titanite_",
			toolSets.titaniteToolSet().TITANITE_AXE,
			toolSets.titaniteToolSet().TITANITE_PICKAXE,
			toolSets.titaniteToolSet().TITANITE_HOE,
			toolSets.titaniteToolSet().TITANITE_SWORD,
			toolSets.titaniteToolSet().TITANITE_BOW,
			toolSets.titaniteToolSet().TITANITE_XBOW,
			armorSets.titaniteArmorSet().TITANITE_HELMET,
			armorSets.titaniteArmorSet().TITANITE_CHEST,
			armorSets.titaniteArmorSet().TITANITE_LEGS,
			armorSets.titaniteArmorSet().TITANITE_BOOTS
		);
		redirectModelLocation(bakery, "ichor", "ichor_",
			toolSets.ichorToolSet().ICHOR_SWORD,
			toolSets.ichorToolSet().ICHOR_AXE,
			toolSets.ichorToolSet().ICHOR_PICKAXE,
			toolSets.ichorToolSet().ICHOR_BOW,
			toolSets.ichorToolSet().ICHOR_XBOW,
			armorSets.ichorArmorSet().ICHOR_HELMET,
			armorSets.ichorArmorSet().ICHOR_CHEST,
			armorSets.ichorArmorSet().ICHOR_LEGS,
			armorSets.ichorArmorSet().ICHOR_BOOTS
		);
		redirectModelLocation(bakery, "astral", "astral_",
			toolSets.astralToolSet().ASTRAL_SWORD,
			toolSets.astralToolSet().ASTRAL_AXE,
			toolSets.astralToolSet().ASTRAL_PICKAXE,
			toolSets.astralToolSet().ASTRAL_SHOVEL,
			toolSets.astralToolSet().ASTRAL_BOW,
			toolSets.astralToolSet().ASTRAL_XBOW,
			armorSets.astralArmorSet().ASTRAL_HELMET,
			armorSets.astralArmorSet().ASTRAL_CHEST,
			armorSets.astralArmorSet().ASTRAL_LEGS,
			armorSets.astralArmorSet().ASTRAL_BOOTS
		);
	}

	@SafeVarargs
	private void redirectModelLocation(ModelBakery bakery, String subfolder, String remove, DeferredHolder<Item, Item>... items) {
		// TODO: do we even need this anymore?
		/*for (DeferredHolder<Item, Item> item : items) {
			ResourceLocation location = item.getId();
			ModelResourceLocation oldMrl = new ModelResourceLocation(location, "inventory");
			ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(location.getNamespace(), subfolder.concat("/").concat(location.getPath().replaceFirst(remove, "")));
			ModelResourceLocation mrl = new ModelResourceLocation(rl, "inventory");
			REMAPPER.put(location, rl);
			bakery.getBakedTopLevelModels().loadTopLevel(mrl);
			bakery.unbakedCache.put(oldMrl, bakery.unbakedCache.get(mrl));
			Minecraft.getInstance().getItemRenderer().getItemModelShaper().register(item.get(), mrl);
		}*/
	}

	public void clearOldModels(ModelBakery bakery) {
		/*REMAPPER.keySet().forEach(location -> {
			ModelResourceLocation oldMrl = new ModelResourceLocation(location, "inventory");
			bakery.unbakedCache.remove(oldMrl);
			bakery.topLevelModels.remove(oldMrl);
		});*/
	}

}