package tamaized.voidscape.world.featureconfig;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class ClusterConfig implements FeatureConfiguration {
	public static final Codec<ClusterConfig> CODEC = RecordCodecBuilder.create((config) -> config.
			group(
					BlockStateProvider.CODEC.fieldOf("to_place").forGetter((conf) -> conf.provider),
					BlockPredicate.CODEC.fieldOf("predicate").forGetter((conf) -> conf.predicate),
					PrimitiveCodec.FLOAT.fieldOf("chance").forGetter((conf) -> conf.chance)
			).apply(config, ClusterConfig::new));

	public final BlockStateProvider provider;
	public final BlockPredicate predicate;
	public final float chance;

	public ClusterConfig(BlockStateProvider provider, BlockPredicate predicate, float chance) {
		this.provider = provider;
		this.predicate = predicate;
		this.chance = chance;
	}
}
