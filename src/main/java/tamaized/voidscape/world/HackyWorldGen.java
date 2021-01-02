package tamaized.voidscape.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKeyCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import tamaized.voidscape.Voidscape;

import java.util.Optional;
import java.util.function.Supplier;

public class HackyWorldGen {

	private static final Codec<Biome> DIRECT_CODEC = RecordCodecBuilder.create((builder1) -> builder1.
			group(Biome.Climate.CODEC.forGetter((biomeIn11) -> biomeIn11.climateSettings), Biome.Category.CODEC.
					fieldOf("category").forGetter((biomeIn10) -> biomeIn10.biomeCategory), Codec.FLOAT.
					fieldOf("depth").forGetter((biomeIn9) -> biomeIn9.depth), Codec.FLOAT.
					fieldOf("scale").forGetter((biomeIn8) -> biomeIn8.scale), BiomeAmbience.CODEC.
					fieldOf("effects").forGetter((biomeIn7) -> biomeIn7.specialEffects), BiomeGenerationSettings.
					CODEC.forGetter((biomeIn6) -> biomeIn6.generationSettings), MobSpawnInfo.
					CODEC.forGetter((biomeIn5) -> biomeIn5.mobSettings), ResourceLocation.CODEC.
					optionalFieldOf("forge:registry_name").forGetter(b -> Optional.ofNullable(b.getRegistryName()))).
			apply(builder1, (climate, category, depth, scale, effects, gen, spawns, name) -> {
				Biome b = new Biome(climate, category, depth, scale, effects, gen, spawns);
				name.ifPresent(b::setRegistryName);
				return b;
			}));
	public static final Codec<Supplier<Biome>> FIXED_BIOME_CODEC = RegistryKeyCodec.create(Registry.BIOME_REGISTRY, DIRECT_CODEC);

	public static void init() {
		Registry.register(Registry.BIOME_SOURCE, Voidscape.MODID + ":fixed", FixedSingleBiomeProvider.CODEC);
	}

	public static class FixedSingleBiomeProvider extends SingleBiomeProvider {

		public static Codec<FixedSingleBiomeProvider> CODEC = FIXED_BIOME_CODEC.
				fieldOf("biome").xmap(FixedSingleBiomeProvider::new, s -> s.biome).stable().codec();

		public FixedSingleBiomeProvider(Supplier<Biome> p_i46709_1_) {
			super(p_i46709_1_);
		}

		@Override
		protected Codec<? extends BiomeProvider> codec() {
			return CODEC;
		}

	}

}
