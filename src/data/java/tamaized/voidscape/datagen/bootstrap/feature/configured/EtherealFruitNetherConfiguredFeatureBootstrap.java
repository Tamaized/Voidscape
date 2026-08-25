package tamaized.voidscape.datagen.bootstrap.feature.configured;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class EtherealFruitNetherConfiguredFeatureBootstrap extends ConfiguredFeatureBootstrapHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public String name() {
		return "ethereal_fruit_nether";
	}

	@Override
	public ConfiguredFeature<?, ?> make(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		return new ConfiguredFeature<>(
			Feature.SIMPLE_BLOCK,
			new SimpleBlockConfiguration(BlockStateProvider.simple(blocks.etherealFruitBlocks().NETHER.get()))
		);
	}
}
