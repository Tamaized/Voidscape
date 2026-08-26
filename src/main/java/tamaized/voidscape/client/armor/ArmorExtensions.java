package tamaized.voidscape.client.armor;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.function.BiFunction;

public class ArmorExtensions implements IClientItemExtensions {

	@Nullable
	private final Identifier texture;

	private final ArmorModelCache<EquipmentSlot, Model<?>> models;

	public ArmorExtensions(@Nullable Identifier texture, BiFunction<EntityModelSet, EquipmentSlot, Model<?>> modelFactory) {
		this.texture = texture;
		models = new ArmorModelCache<>(new EnumMap<>(EquipmentSlot.class), modelFactory);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Model getHumanoidArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model original) {
		Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
		if (equippable == null || equippable.slot().getType() != EquipmentSlot.Type.HUMANOID_ARMOR)
			return original;
		return models.get(equippable.slot());
	}

	@Override
	public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer, Identifier _default) {
		return texture != null ? texture : _default;
	}

}
