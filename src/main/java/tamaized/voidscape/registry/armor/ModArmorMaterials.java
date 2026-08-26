package tamaized.voidscape.registry.armor;

import com.google.common.base.Suppliers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

import java.util.EnumMap;
import java.util.function.Supplier;

@Component
public class ModArmorMaterials {

	public final TagKey<Item> TAG_REPAIR_MATERIAL_VOIDIC_CRYSTAL = TagKey.create(
		Registries.ITEM,
		Identifier.fromNamespaceAndPath(Voidscape.MODID, "voidic_crystal")
	);
	public final ArmorMaterial VOIDIC_CRYSTAL = new ArmorMaterial(
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
		TAG_REPAIR_MATERIAL_VOIDIC_CRYSTAL,
		ResourceKey.create(EquipmentAssets.ROOT_ID, TAG_REPAIR_MATERIAL_VOIDIC_CRYSTAL.location())
	);

	public final TagKey<Item> TAG_REPAIR_MATERIAL_CORRUPT = TagKey.create(
		Registries.ITEM,
		Identifier.fromNamespaceAndPath(Voidscape.MODID, "corrupt")
	);
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
		TAG_REPAIR_MATERIAL_CORRUPT,
		ResourceKey.create(EquipmentAssets.ROOT_ID, TAG_REPAIR_MATERIAL_CORRUPT.location())
	));

	public final TagKey<Item> TAG_REPAIR_MATERIAL_TITANITE = TagKey.create(
		Registries.ITEM,
		Identifier.fromNamespaceAndPath(Voidscape.MODID, "titanite")
	);
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
		TAG_REPAIR_MATERIAL_TITANITE,
		ResourceKey.create(EquipmentAssets.ROOT_ID, TAG_REPAIR_MATERIAL_TITANITE.location())
	));

	public final TagKey<Item> TAG_REPAIR_MATERIAL_ICHOR = TagKey.create(
		Registries.ITEM,
		Identifier.fromNamespaceAndPath(Voidscape.MODID, "ichor")
	);
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
		TAG_REPAIR_MATERIAL_ICHOR,
		ResourceKey.create(EquipmentAssets.ROOT_ID, TAG_REPAIR_MATERIAL_ICHOR.location())
	));

	public final TagKey<Item> TAG_REPAIR_MATERIAL_ASTRAL = TagKey.create(
		Registries.ITEM,
		Identifier.fromNamespaceAndPath(Voidscape.MODID, "astral")
	);
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
		TAG_REPAIR_MATERIAL_ASTRAL,
		ResourceKey.create(EquipmentAssets.ROOT_ID, TAG_REPAIR_MATERIAL_ASTRAL.location())
	));

}
