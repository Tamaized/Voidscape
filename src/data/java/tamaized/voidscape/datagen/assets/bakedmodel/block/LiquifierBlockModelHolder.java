package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.block.BlockModelHolder;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.Optional;

@Component
public class LiquifierBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Nullable
	@Override
	protected DeferredHolder<Block, ? extends Block> blockForName() {
		return blocks.machineBlocks().MACHINE_LIQUIFIER;
	}

	@Override
	public boolean hasStandardBlockItem() {
		return true;
	}

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/liquifier");
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
	}

	@Override
	public boolean hasBlockState() {
		return true;
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Voidic Liquifier");
	}
}
