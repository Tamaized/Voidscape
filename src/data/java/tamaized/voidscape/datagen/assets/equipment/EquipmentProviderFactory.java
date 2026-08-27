package tamaized.voidscape.datagen.assets.equipment;

import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.armor.ModArmorMaterials;

import java.util.Optional;
import java.util.function.BiConsumer;

@Component
public class EquipmentProviderFactory {

	@Autowired
	private ModArmorMaterials armorMaterials;

	public EquipmentAssetProvider make(GatherDataEvent event) {
		return new EquipmentAssetProvider(event.getGenerator().getPackOutput()) {

			@Override
			protected void registerModels(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
				humanoid(output, "voidic_crystal");
				humanoid(output, "corrupt");
				humanoid(output, "titanite");
				humanoid(output, "ichor");
				humanoid(output, "astral");
				armorMaterials.ELYTRA_EQUIPMENT_ASSETS.forEach((base, elytra) -> humanoidWithElytra(output, base, elytra));
			}

		};
	}

	private void humanoid(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output, String name) {
		Identifier id = Identifier.fromNamespaceAndPath(Voidscape.MODID, name);
		output.accept(
			ResourceKey.create(EquipmentAssets.ROOT_ID, id),
			EquipmentClientInfo.builder().addHumanoidLayers(id).build()
		);
	}

	private void humanoidWithElytra(
		BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output,
		ResourceKey<EquipmentAsset> base,
		ResourceKey<EquipmentAsset> elytra
	) {
		output.accept(
			elytra,
			EquipmentClientInfo.builder()
				.addHumanoidLayers(base.identifier())
				.addLayers(
					EquipmentClientInfo.LayerType.WINGS,
					new EquipmentClientInfo.Layer(Identifier.withDefaultNamespace("elytra"), Optional.empty(), true)
				)
				.build()
		);
	}

}
