package tamaized.voidscape.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Optional;

public class ClusterConfig implements FeatureConfiguration {
	public static final Codec<ClusterConfig> CODEC = RecordCodecBuilder.create((config) -> config.
			group(
					BlockStateProvider.CODEC.fieldOf("to_place").forGetter(conf -> conf.provider),
					BlockPredicate.CODEC.fieldOf("predicate").forGetter(conf -> conf.predicate),
					PrimitiveCodec.FLOAT.fieldOf("chance").forGetter(conf -> conf.chance),
					PrimitiveCodec.INT.fieldOf("max").forGetter(conf -> conf.max)
			).apply(config, ClusterConfig::new));

	public final BlockStateProvider provider;
	public final BlockPredicate predicate;
	public final float chance;
	public final int max;

	public ClusterConfig(BlockStateProvider provider, BlockPredicate predicate, float chance, int max) {
		this.provider = provider;
		this.predicate = predicate;
		this.chance = chance;
		this.max = max;
	}
}
