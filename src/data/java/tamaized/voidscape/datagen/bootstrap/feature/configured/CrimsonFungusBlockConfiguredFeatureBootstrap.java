package tamaized.voidscape.datagen.bootstrap.feature.configured;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import tamaized.beanification.Component;

@Component
public class CrimsonFungusBlockConfiguredFeatureBootstrap extends ConfiguredFeatureBootstrapHolder {

	@Override
	public String name() {
		return "crimson_fungus_block";
	}

	@Override
	public ConfiguredFeature<?, ?> make(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		return new ConfiguredFeature<>(
			Feature.SIMPLE_BLOCK,
			new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.CRIMSON_FUNGUS))
		);
	}
}
