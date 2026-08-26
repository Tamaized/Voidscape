package tamaized.voidscape.client.armor;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jspecify.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ModModelLayerLocations;
import tamaized.voidscape.client.armor.model.ModelArmorFullbright;
import tamaized.voidscape.client.armor.model.ModelArmorCorrupt;
import tamaized.voidscape.client.armor.model.ModelArmorCrystalline;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;
import tamaized.voidscape.registry.armor.set.AstralArmorSet;
import tamaized.voidscape.registry.armor.set.CorruptArmorSet;
import tamaized.voidscape.registry.armor.set.IchorArmorSet;
import tamaized.voidscape.registry.armor.set.TitaniteArmorSet;
import tamaized.voidscape.registry.armor.set.VoidicCrystalArmorSet;

import java.util.function.Function;

@Component(dist = Dist.CLIENT)
public class ArmorClientExtensions {

	@Autowired(dist = Dist.CLIENT)
	private ModArmorSetComponentDirectory armorSets;

	@Autowired(dist = Dist.CLIENT)
	private ModModelLayerLocations modelLayerLocations;

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::registerClientExtensions);
	}

	private void registerClientExtensions(RegisterClientExtensionsEvent event) {
		VoidicCrystalArmorSet voidicCrystal = armorSets.voidicCrystalArmorSet();
		register(
			event,
			new ArmorExtensions(null, (entityModels, armorSlot) -> new ModelArmorFullbright<>(entityModels.bakeLayer(ModelLayers.PLAYER_ARMOR.get(armorSlot)))),
			voidicCrystal.VOIDIC_CRYSTAL_HELMET,
			voidicCrystal.VOIDIC_CRYSTAL_CHEST,
			voidicCrystal.VOIDIC_CRYSTAL_LEGS,
			voidicCrystal.VOIDIC_CRYSTAL_BOOTS
		);

		CorruptArmorSet corrupt = armorSets.corruptArmorSet();
		register(
			event,
			new ArmorExtensions(armorTexture("corrupt"), (entityModels, armorSlot) -> {
				ModelArmorCorrupt<HumanoidRenderState> model = new ModelArmorCorrupt<>(
					entityModels.bakeLayer(armorSlot == EquipmentSlot.LEGS ?
						modelLayerLocations.MODEL_ARMOR_CORRUPT_INNER :
						modelLayerLocations.MODEL_ARMOR_CORRUPT_OUTER),
					armorTexture("corrupt_overlay")
				);
				model.setVisibleFor(armorSlot);
				return model;
			}),
			corrupt.CORRUPT_HELMET,
			corrupt.CORRUPT_CHEST,
			corrupt.CORRUPT_LEGS,
			corrupt.CORRUPT_BOOTS
		);

		TitaniteArmorSet titanite = armorSets.titaniteArmorSet();
		register(
			event,
			crystallineWithOverlayFullbright("titanite", locations -> locations.MODEL_ARMOR_TITANITE),
			titanite.TITANITE_HELMET,
			titanite.TITANITE_CHEST,
			titanite.TITANITE_LEGS,
			titanite.TITANITE_BOOTS
		);

		IchorArmorSet ichor = armorSets.ichorArmorSet();
		register(
			event,
			crystallineWithOverlayFullbright("ichor", locations -> locations.MODEL_ARMOR_ICHOR),
			ichor.ICHOR_HELMET,
			ichor.ICHOR_CHEST,
			ichor.ICHOR_LEGS,
			ichor.ICHOR_BOOTS
		);

		AstralArmorSet astral = armorSets.astralArmorSet();
		register(
			event,
			crystalline("astral", locations -> locations.MODEL_ARMOR_ASTRAL, null, true),
			astral.ASTRAL_HELMET,
			astral.ASTRAL_CHEST,
			astral.ASTRAL_LEGS,
			astral.ASTRAL_BOOTS
		);
	}

	private ArmorExtensions crystallineWithOverlayFullbright(String textureName, Function<ModModelLayerLocations, ModelLayerLocation> layerLocation) {
		return crystalline(textureName, layerLocation, armorTexture(textureName + "_overlay"), false);
	}

	private ArmorExtensions crystalline(
		String textureName,
		Function<ModModelLayerLocations, ModelLayerLocation> layerLocation,
		@Nullable Identifier overlayTexture,
		boolean fullbright
	) {
		return new ArmorExtensions(armorTexture(textureName), (entityModels, armorSlot) -> {
			ModelArmorCrystalline<HumanoidRenderState> model = new ModelArmorCrystalline<>(
				entityModels.bakeLayer(layerLocation.apply(modelLayerLocations)),
				overlayTexture,
				fullbright
			);
			model.setVisibleFor(armorSlot);
			return model;
		});
	}

	private Identifier armorTexture(String name) {
		return Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/models/armor/" + name + ".png");
	}

	@SafeVarargs
	private void register(RegisterClientExtensionsEvent event, IClientItemExtensions extensions, DeferredHolder<Item, Item>... items) {
		for (DeferredHolder<Item, Item> item : items)
			event.registerItem(extensions, item.get());
	}

}
