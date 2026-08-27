package tamaized.voidscape.registry.armor;

import com.google.common.base.Suppliers;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModItemTags;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class ModArmorMaterials {

	@Autowired
	private ModItemTags itemTags;

	private final ResourceKey<EquipmentAsset> ASSET_KEY_VOIDIC_CRYSTAL = assetKey("voidic_crystal");
	public final Supplier<ArmorMaterial> VOIDIC_CRYSTAL = Suppliers.memoize(() -> new ArmorMaterial(
		39,
		Util.make(new EnumMap<>(ArmorType.class), map -> {
			map.put(ArmorType.BOOTS, 3);
			map.put(ArmorType.LEGGINGS, 6);
			map.put(ArmorType.CHESTPLATE, 8);
			map.put(ArmorType.HELMET, 3);
			map.put(ArmorType.BODY, 11);
		}),
		17,
		SoundEvents.ARMOR_EQUIP_DIAMOND,
		4F,
		0.10F,
		itemTags.REPAIR_MATERIAL_VOIDIC_CRYSTAL,
		ASSET_KEY_VOIDIC_CRYSTAL
	));

	private final ResourceKey<EquipmentAsset> ASSET_KEY_CORRUPT = assetKey("corrupt");
	public final Supplier<ArmorMaterial> CORRUPT = Suppliers.memoize(() -> new ArmorMaterial(
		41,
		Util.make(new EnumMap<>(ArmorType.class), map -> {
			map.put(ArmorType.BOOTS, 4);
			map.put(ArmorType.LEGGINGS, 7);
			map.put(ArmorType.CHESTPLATE, 9);
			map.put(ArmorType.HELMET, 4);
			map.put(ArmorType.BODY, 12);
		}),
		19,
		SoundEvents.ARMOR_EQUIP_NETHERITE,
		5F,
		0.15F,
		itemTags.REPAIR_MATERIAL_CORRUPT,
		ASSET_KEY_CORRUPT
	));


	private final ResourceKey<EquipmentAsset> ASSET_KEY_TITANITE = assetKey("titanite");
	public final Supplier<ArmorMaterial> TITANITE = Suppliers.memoize(() -> new ArmorMaterial(
		43,
		Util.make(new EnumMap<>(ArmorType.class), map -> {
			map.put(ArmorType.BOOTS, 5);
			map.put(ArmorType.LEGGINGS, 8);
			map.put(ArmorType.CHESTPLATE, 10);
			map.put(ArmorType.HELMET, 5);
			map.put(ArmorType.BODY, 13);
		}),
		17,
		SoundEvents.ARMOR_EQUIP_NETHERITE,
		6F,
		0.20F,
		itemTags.REPAIR_MATERIAL_TITANITE,
		ASSET_KEY_TITANITE
	));

	private final ResourceKey<EquipmentAsset> ASSET_KEY_ICHOR = assetKey("ichor");
	public final Supplier<ArmorMaterial> ICHOR = Suppliers.memoize(() -> new ArmorMaterial(
		45,
		Util.make(new EnumMap<>(ArmorType.class), map -> {
			map.put(ArmorType.BOOTS, 6);
			map.put(ArmorType.LEGGINGS, 9);
			map.put(ArmorType.CHESTPLATE, 11);
			map.put(ArmorType.HELMET, 6);
			map.put(ArmorType.BODY, 14);
		}),
		23,
		SoundEvents.ARMOR_EQUIP_NETHERITE,
		7F,
		0.25F,
		itemTags.REPAIR_MATERIAL_ICHOR,
		ASSET_KEY_ICHOR
	));


	private final ResourceKey<EquipmentAsset> ASSET_KEY_ASTRAL = assetKey("astral");
	public final Supplier<ArmorMaterial> ASTRAL = Suppliers.memoize(() -> new ArmorMaterial(
		47,
		Util.make(new EnumMap<>(ArmorType.class), map -> {
			map.put(ArmorType.BOOTS, 7);
			map.put(ArmorType.LEGGINGS, 10);
			map.put(ArmorType.CHESTPLATE, 12);
			map.put(ArmorType.HELMET, 7);
			map.put(ArmorType.BODY, 15);
		}),
		25,
		SoundEvents.ARMOR_EQUIP_DIAMOND,
		8F,
		0.30F,
		itemTags.REPAIR_MATERIAL_ASTRAL,
		ASSET_KEY_ASTRAL
	));

	public final Map<ResourceKey<EquipmentAsset>, ResourceKey<EquipmentAsset>> ELYTRA_EQUIPMENT_ASSETS = Map.of(
		ASSET_KEY_VOIDIC_CRYSTAL, elytraAssetKey(ASSET_KEY_VOIDIC_CRYSTAL),
		ASSET_KEY_TITANITE, elytraAssetKey(ASSET_KEY_TITANITE),
		ASSET_KEY_ICHOR, elytraAssetKey(ASSET_KEY_ICHOR),
		ASSET_KEY_ASTRAL, elytraAssetKey(ASSET_KEY_ASTRAL)
	);

	private ResourceKey<EquipmentAsset> assetKey(String material) {
		return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Voidscape.MODID, material));
	}

	private ResourceKey<EquipmentAsset> elytraAssetKey(ResourceKey<EquipmentAsset> parent) {
		return ResourceKey.create(EquipmentAssets.ROOT_ID, parent.identifier().withSuffix("_elytra"));
	}

}
