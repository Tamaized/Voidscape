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
import tamaized.voidscape.registry.armor.set.*;
import tamaized.voidscape.registry.tool.set.*;

import java.util.HashMap;
import java.util.Map;

@Component
public class ModelBakeListener {

	@Autowired
	private VoidicCrystalToolSet voidicCrystalToolSet;

	@Autowired
	private VoidicCrystalArmorSet voidicCrystalArmorSet;

	@Autowired
	private CharredToolSet charredToolSet;

	@Autowired
	private CorruptToolSet corruptToolSet;

	@Autowired
	private CorruptArmorSet corruptArmorSet;

	@Autowired
	private TitaniteToolSet titaniteToolSet;

	@Autowired
	private TitaniteArmorSet titaniteArmorSet;

	@Autowired
	private IchorToolSet ichorToolSet;

	@Autowired
	private IchorArmorSet ichorArmorSet;

	@Autowired
	private AstralToolSet astralToolSet;

	@Autowired
	private AstralArmorSet astralArmorSet;

	private final Map<ResourceLocation, ResourceLocation> REMAPPER = new HashMap<>();

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::modelBake);
	}

	private void modelBake(ModelEvent.ModifyBakingResult event) {
		// Broken Variants
		impBroken(voidicCrystalToolSet.VOIDIC_CRYSTAL_SWORD.get());
		impBroken(voidicCrystalToolSet.VOIDIC_CRYSTAL_AXE.get());
		impBroken(voidicCrystalToolSet.VOIDIC_CRYSTAL_BOW.get());
		impBroken(voidicCrystalToolSet.VOIDIC_CRYSTAL_XBOW.get());
		impBroken(voidicCrystalToolSet.VOIDIC_CRYSTAL_PICKAXE.get());
		impBroken(voidicCrystalArmorSet.VOIDIC_CRYSTAL_HELMET.get());
		impBroken(voidicCrystalArmorSet.VOIDIC_CRYSTAL_CHEST.get());
		impBroken(voidicCrystalArmorSet.VOIDIC_CRYSTAL_LEGS.get());
		impBroken(voidicCrystalArmorSet.VOIDIC_CRYSTAL_BOOTS.get());

		impBroken(charredToolSet.CHARRED_WARHAMMER.get());

		impBroken(corruptToolSet.CORRUPT_SWORD.get());
		impBroken(corruptToolSet.CORRUPT_AXE.get());
		impBroken(corruptToolSet.CORRUPT_BOW.get());
		impBroken(corruptToolSet.CORRUPT_XBOW.get());
		impBroken(corruptArmorSet.CORRUPT_HELMET.get());
		impBroken(corruptArmorSet.CORRUPT_CHEST.get());
		impBroken(corruptArmorSet.CORRUPT_LEGS.get());
		impBroken(corruptArmorSet.CORRUPT_BOOTS.get());

		impBroken(titaniteToolSet.TITANITE_SWORD.get());
		impBroken(titaniteToolSet.TITANITE_AXE.get());
		impBroken(titaniteToolSet.TITANITE_PICKAXE.get());
		impBroken(titaniteToolSet.TITANITE_HOE.get());
		impBroken(titaniteToolSet.TITANITE_BOW.get());
		impBroken(titaniteToolSet.TITANITE_XBOW.get());
		impBroken(titaniteArmorSet.TITANITE_HELMET.get());
		impBroken(titaniteArmorSet.TITANITE_CHEST.get());
		impBroken(titaniteArmorSet.TITANITE_LEGS.get());
		impBroken(titaniteArmorSet.TITANITE_BOOTS.get());

		impBroken(ichorToolSet.ICHOR_SWORD.get());
		impBroken(ichorToolSet.ICHOR_AXE.get());
		impBroken(ichorToolSet.ICHOR_PICKAXE.get());
		impBroken(ichorToolSet.ICHOR_BOW.get());
		impBroken(ichorToolSet.ICHOR_XBOW.get());
		impBroken(ichorArmorSet.ICHOR_HELMET.get());
		impBroken(ichorArmorSet.ICHOR_CHEST.get());
		impBroken(ichorArmorSet.ICHOR_LEGS.get());
		impBroken(ichorArmorSet.ICHOR_BOOTS.get());

		impBroken(astralToolSet.ASTRAL_SWORD.get());
		impBroken(astralToolSet.ASTRAL_AXE.get());
		impBroken(astralToolSet.ASTRAL_PICKAXE.get());
		impBroken(astralToolSet.ASTRAL_SHOVEL.get());
		impBroken(astralToolSet.ASTRAL_BOW.get());
		impBroken(astralToolSet.ASTRAL_XBOW.get());
		impBroken(astralArmorSet.ASTRAL_HELMET.get());
		impBroken(astralArmorSet.ASTRAL_CHEST.get());
		impBroken(astralArmorSet.ASTRAL_LEGS.get());
		impBroken(astralArmorSet.ASTRAL_BOOTS.get());

		// Bows
		impBow(voidicCrystalToolSet.VOIDIC_CRYSTAL_BOW.get());
		impBow(corruptToolSet.CORRUPT_BOW.get());
		impBow(titaniteToolSet.TITANITE_BOW.get());
		impBow(ichorToolSet.ICHOR_BOW.get());
		impBow(astralToolSet.ASTRAL_BOW.get());

		// XBows
		impXBow(voidicCrystalToolSet.VOIDIC_CRYSTAL_XBOW.get());
		impXBow(corruptToolSet.CORRUPT_XBOW.get());
		impXBow(titaniteToolSet.TITANITE_XBOW.get());
		impXBow(ichorToolSet.ICHOR_XBOW.get());
		impXBow(astralToolSet.ASTRAL_XBOW.get());

		// Shields
		impShield(voidicCrystalToolSet.VOIDIC_CRYSTAL_SHIELD.get());

	}

	private void impBroken(Item item) {
		ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "broken"), (stack, level, entity, prop) -> BreakableHelper.isBroken(stack) ? 1F : 0F);
	}

	private void impBow(Item item) {
		ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "pull"), (stack, level, entity, prop) ->

			entity == null ? 0.0F : entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F);

		ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "pulling"), (stack, level, entity, prop) ->

			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
	}

	private void impXBow(Item item) {
		ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "pull"), (stack, level, entity, prop) ->

			entity == null ? 0.0F : CrossbowItem.isCharged(stack) ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(stack, entity));

		ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "pulling"), (stack, level, entity, prop) ->

			entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

		ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "charged"), (stack, level, entity, prop) ->

			entity != null && CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

		ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "firework"), (stack, level, entity, prop) ->

			entity != null && CrossbowItem.isCharged(stack) && stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY).contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
	}

	private void impShield(Item item) {
		ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "blocking"), (stack, level, entity, prop) ->

			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
	}

	public void redirectModels(ModelBakery bakery) {
		redirectModelLocation(bakery, "voidic", "voidic_crystal_",
			voidicCrystalToolSet.VOIDIC_CRYSTAL_AXE,
			voidicCrystalToolSet.VOIDIC_CRYSTAL_PICKAXE,
			voidicCrystalToolSet.VOIDIC_CRYSTAL_SWORD,
			voidicCrystalToolSet.VOIDIC_CRYSTAL_SHIELD,
			voidicCrystalToolSet.VOIDIC_CRYSTAL_BOW,
			voidicCrystalToolSet.VOIDIC_CRYSTAL_XBOW,
			voidicCrystalArmorSet.VOIDIC_CRYSTAL_HELMET,
			voidicCrystalArmorSet.VOIDIC_CRYSTAL_CHEST,
			voidicCrystalArmorSet.VOIDIC_CRYSTAL_LEGS,
			voidicCrystalArmorSet.VOIDIC_CRYSTAL_BOOTS
		);
		redirectModelLocation(bakery, "charred", "charred_", charredToolSet.CHARRED_WARHAMMER);
		redirectModelLocation(bakery, "corrupt", "corrupt_",
			corruptToolSet.CORRUPT_AXE,
			corruptToolSet.CORRUPT_SWORD,
			corruptToolSet.CORRUPT_BOW,
			corruptToolSet.CORRUPT_XBOW,
			corruptArmorSet.CORRUPT_HELMET,
			corruptArmorSet.CORRUPT_CHEST,
			corruptArmorSet.CORRUPT_LEGS,
			corruptArmorSet.CORRUPT_BOOTS
		);
		redirectModelLocation(bakery, "titanite", "titanite_",
			titaniteToolSet.TITANITE_AXE,
			titaniteToolSet.TITANITE_PICKAXE,
			titaniteToolSet.TITANITE_HOE,
			titaniteToolSet.TITANITE_SWORD,
			titaniteToolSet.TITANITE_BOW,
			titaniteToolSet.TITANITE_XBOW,
			titaniteArmorSet.TITANITE_HELMET,
			titaniteArmorSet.TITANITE_CHEST,
			titaniteArmorSet.TITANITE_LEGS,
			titaniteArmorSet.TITANITE_BOOTS
		);
		redirectModelLocation(bakery, "ichor", "ichor_",
			ichorToolSet.ICHOR_SWORD,
			ichorToolSet.ICHOR_AXE,
			ichorToolSet.ICHOR_PICKAXE,
			ichorToolSet.ICHOR_BOW,
			ichorToolSet.ICHOR_XBOW,
			ichorArmorSet.ICHOR_HELMET,
			ichorArmorSet.ICHOR_CHEST,
			ichorArmorSet.ICHOR_LEGS,
			ichorArmorSet.ICHOR_BOOTS
		);
		redirectModelLocation(bakery, "astral", "astral_",
			astralToolSet.ASTRAL_SWORD,
			astralToolSet.ASTRAL_AXE,
			astralToolSet.ASTRAL_PICKAXE,
			astralToolSet.ASTRAL_SHOVEL,
			astralToolSet.ASTRAL_BOW,
			astralToolSet.ASTRAL_XBOW,
			astralArmorSet.ASTRAL_HELMET,
			astralArmorSet.ASTRAL_CHEST,
			astralArmorSet.ASTRAL_LEGS,
			astralArmorSet.ASTRAL_BOOTS
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