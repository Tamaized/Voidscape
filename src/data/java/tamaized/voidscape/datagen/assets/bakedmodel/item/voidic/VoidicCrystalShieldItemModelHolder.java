package tamaized.voidscape.datagen.assets.bakedmodel.item.voidic;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.item.ItemModelHolder;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.Objects;
import java.util.Optional;

@Component
public class VoidicCrystalShieldItemModelHolder extends ItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_SHIELD;
	}

	@Override
	public Identifier finalize(ItemModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		Identifier base = Identifier.fromNamespaceAndPath(Voidscape.MODID, name("base"));
		Identifier blocking = ExtendedModelTemplateBuilder.builder()
			.parent(base)
			.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, t -> t
				.rotation(122, 13, 16)
				.translation(5, -3, 2.75F)
				.scale(2))
			.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, t -> t
				.rotation(122, 13, 16)
				.translation(-2.75F, -3.5F, -1)
				.scale(2))
			.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, t -> t
				.rotation(90, 5, 0)
				.translation(-1.5F, 0, 8)
				.scale(1.5F))
			.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, t -> t
				.rotation(90, 5, 0)
				.translation(-7.25F, 0, 8)
				.scale(1.5F))
			.transform(ItemDisplayContext.GROUND, t -> t
				.rotation(90, 0, 180)
				.translation(-1.6F, 3, 0))
			.transform(ItemDisplayContext.GUI, t -> t
				.rotation(90, -10, 205)
				.translation(-1.6F, -1, 0)
				.scale(1.25F))
			.transform(ItemDisplayContext.FIXED, t -> t
				.rotation(90, 0, 0)
				.translation(1.6F, 0, 2))
			.build()
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, name("blocking")), new TextureMapping(), provider.modelOutput);
		provider.itemModelOutput.accept(
			Objects.requireNonNull(itemForName()).value(),
			ItemModelUtils.conditional(ItemModelUtils.isUsingItem(), ItemModelUtils.plainModel(blocking), ItemModelUtils.plainModel(base))
		);
		return base;
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Voidic Crystal Shield");
	}
}
