package tamaized.voidscape.client.entity.render.state;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.armor.ModArmorMaterials;

import java.util.Optional;

@Component(dist = Dist.CLIENT)
public class ChestEquipmentRenderStateExtension {

	@Autowired(dist = Dist.CLIENT)
	private ModArmorMaterials armorMaterials;

	@Autowired(dist = Dist.CLIENT)
	private ModArmorSetComponentDirectory armorSets;

	@Autowired(dist = Dist.CLIENT)
	private ModItemComponents itemComponents;

	public void applyElytra(HumanoidRenderState state) {
		ItemStack stack = state.chestEquipment;
		if (!stack.getOrDefault(itemComponents.ELYTRA, false) || isCorruptChest(stack))
			return;
		Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
		if (equippable == null || equippable.assetId().isEmpty())
			return;
		ResourceKey<EquipmentAsset> elytra = armorMaterials.ELYTRA_EQUIPMENT_ASSETS.get(equippable.assetId().get());
		if (elytra == null)
			return;
		ItemStack swapped = stack.copy();
		swapped.set(DataComponents.EQUIPPABLE, new Equippable(
			equippable.slot(),
			equippable.equipSound(),
			Optional.of(elytra),
			equippable.cameraOverlay(),
			equippable.allowedEntities(),
			equippable.dispensable(),
			equippable.swappable(),
			equippable.damageOnHurt(),
			equippable.equipOnInteract(),
			equippable.canBeSheared(),
			equippable.shearingSound()
		));
		state.chestEquipment = swapped;
	}

	public void applyCape(AvatarRenderState state) {
		if (isCorruptChest(state.chestEquipment))
			state.showCape = false;
	}

	private boolean isCorruptChest(ItemStack stack) {
		return stack.is(armorSets.corruptArmorSet().CORRUPT_CHEST.get());
	}

}
