package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.block.BlockModelHolder;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.util.ModTextureSlots;

@Component
public class PortalNSBlockModelHolder extends BlockModelHolder {

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended(m -> m
				.element(e -> e
					.from(0, 0, 6).to(16, 16, 10)
					.face(Direction.NORTH, f -> f.uvs(0, 0, 16, 16).texture(ModTextureSlots.PORTAL))
					.face(Direction.SOUTH, f -> f.uvs(0, 0, 16, 16).texture(ModTextureSlots.PORTAL))))
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/portal_ns"), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
		mapping
			.putRef(TextureSlot.PARTICLE, ModTextureSlots.PORTAL)
			.putForced(ModTextureSlots.PORTAL, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/portal"), true));
	}
}
