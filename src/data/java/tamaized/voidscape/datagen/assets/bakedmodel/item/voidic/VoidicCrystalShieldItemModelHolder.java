package tamaized.voidscape.datagen.assets.bakedmodel.item.voidic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ItemModelOverridePredicates;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.Optional;

@Component
public class VoidicCrystalShieldItemModelHolder extends VoidicCrystalItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Autowired
	private ItemModelOverridePredicates itemModelOverridePredicates;

	@Nullable
	private ModelFile blockingModel;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_SHIELD;
	}

	public ModelFile build(ItemModelProvider provider) {
		// @formatter:off
		return provider.getBuilder(splitName())
			.override()
				.predicate(itemModelOverridePredicates.BLOCKING, 0)
				.model(provider.getExistingFile(provider.modLoc(splitName("base"))))
			.end()
			.override()
				.predicate(itemModelOverridePredicates.BLOCKING, 1)
				.model(getBlockingModel(provider))
			.end();
		// @formatter:on
	}

	public ModelFile getBlockingModel(ItemModelProvider provider) {
		if (blockingModel == null) {
			blockingModel = provider.withExistingParent(
					splitName("blocking"),
					provider.modLoc(splitName("base"))
				)
				.transforms()
				// @formatter:off
					.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
						.rotation(122, 13, 16)
						.translation(5, -3, 2.75F)
						.scale(2)
					.end()
					.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
						.rotation(122, 13, 16)
						.translation(-2.75F, -3.5F, -1)
						.scale(2)
					.end()
					.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
						.rotation(90, 5, 0)
						.translation(-1.5F, 0, 8)
						.scale(1.5F)
					.end()
					.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
						.rotation(90, 5, 0)
						.translation(-7.25F, 0, 8)
						.scale(1.5F)
					.end()
					.transform(ItemDisplayContext.GROUND)
						.rotation(90, 0, 180)
						.translation(-1.6F, 3, 0)
					.end()
					.transform(ItemDisplayContext.GUI)
						.rotation(90, -10, 205)
						.translation(-1.6F, -1, 0)
						.scale(1.25F)
					.end()
					.transform(ItemDisplayContext.FIXED)
						.rotation(90, 0, 0)
						.translation(1.6F, 0, 2)
					.end()
				// @formatter:on
				.end();
		}
		return blockingModel;
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Voidic Crystal Shield");
	}

}
