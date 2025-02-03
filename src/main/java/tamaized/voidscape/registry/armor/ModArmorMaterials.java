package tamaized.voidscape.registry.armor;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.ArmorData;
import tamaized.regutil.ArmorDataModel;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.ModModelLayerLocations;
import tamaized.voidscape.registry.item.MaterialItems;

import java.util.EnumMap;
import java.util.List;

@Component
public class ModArmorMaterials {

	@Autowired
	private MaterialItems materialItems;

	@Autowired(dist = Dist.CLIENT)
	private ModModelLayerLocations modelLayerLocations;

	private final DeferredRegister<ArmorMaterial> REGISTRY = RegUtil.create(Registries.ARMOR_MATERIAL);

	public final ArmorData VOIDIC_CRYSTAL = new ArmorData(REGISTRY.register("voidic_crystal", () -> new ArmorMaterial(
		Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
			map.put(ArmorItem.Type.BOOTS, 3);
			map.put(ArmorItem.Type.LEGGINGS, 6);
			map.put(ArmorItem.Type.CHESTPLATE, 8);
			map.put(ArmorItem.Type.HELMET, 3);
			map.put(ArmorItem.Type.BODY, 11);
		}),
		17,
		SoundEvents.ARMOR_EQUIP_DIAMOND,
		() -> Ingredient.of(materialItems.VOIDIC_CRYSTAL.get()),
		List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "voidic_crystal"))),
		4F,
		0.10F
	)), 39, new ArmorDataModel(true, false, false));

	public final ArmorData CORRUPT = new ArmorData(REGISTRY.register("corrupt", () -> new ArmorMaterial(
		Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
			map.put(ArmorItem.Type.BOOTS, 4);
			map.put(ArmorItem.Type.LEGGINGS, 7);
			map.put(ArmorItem.Type.CHESTPLATE, 9);
			map.put(ArmorItem.Type.HELMET, 4);
			map.put(ArmorItem.Type.BODY, 12);
		}),
		19,
		SoundEvents.ARMOR_EQUIP_NETHERITE,
		() -> Ingredient.of(materialItems.TENDRIL.get()),
		List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "corrupt"))),
		5F,
		0.15F
	)), 41, new CorruptArmorDataModel());

	public final ArmorData TITANITE = new ArmorData(REGISTRY.register("titanite", () -> new ArmorMaterial(
		Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
			map.put(ArmorItem.Type.BOOTS, 5);
			map.put(ArmorItem.Type.LEGGINGS, 8);
			map.put(ArmorItem.Type.CHESTPLATE, 10);
			map.put(ArmorItem.Type.HELMET, 5);
			map.put(ArmorItem.Type.BODY, 13);
		}),
		17,
		SoundEvents.ARMOR_EQUIP_NETHERITE,
		() -> Ingredient.of(materialItems.TITANITE_SHARD.get()),
		List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "titanite"))),
		6F,
		0.20F
	)), 43, new CrystallineArmorDataModel("titanite", () -> modelLayerLocations.MODEL_ARMOR_TITANITE, true));

	public final ArmorData ICHOR = new ArmorData(REGISTRY.register("ichor", () -> new ArmorMaterial(
		Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
			map.put(ArmorItem.Type.BOOTS, 6);
			map.put(ArmorItem.Type.LEGGINGS, 9);
			map.put(ArmorItem.Type.CHESTPLATE, 11);
			map.put(ArmorItem.Type.HELMET, 6);
			map.put(ArmorItem.Type.BODY, 14);
		}),
		23,
		SoundEvents.ARMOR_EQUIP_NETHERITE,
		() -> Ingredient.of(materialItems.ICHOR_CRYSTAL.get()),
		List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "ichor"))),
		7F,
		0.25F
	)), 45, new CrystallineArmorDataModel("ichor", () -> modelLayerLocations.MODEL_ARMOR_ICHOR, true));

	public final ArmorData ASTRAL = new ArmorData(REGISTRY.register("astral", () -> new ArmorMaterial(
		Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
			map.put(ArmorItem.Type.BOOTS, 7);
			map.put(ArmorItem.Type.LEGGINGS, 10);
			map.put(ArmorItem.Type.CHESTPLATE, 12);
			map.put(ArmorItem.Type.HELMET, 7);
			map.put(ArmorItem.Type.BODY, 15);
		}),
		25,
		SoundEvents.ARMOR_EQUIP_DIAMOND,
		() -> Ingredient.of(materialItems.ASTRAL_CRYSTAL.get()),
		List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "astral"))),
		8F,
		0.30F
	)), 47, new CrystallineArmorDataModel("astral", () -> modelLayerLocations.MODEL_ARMOR_ICHOR, false));

}
