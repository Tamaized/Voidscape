package tamaized.voidscape.world;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKeyCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.chunk.IChunkLightProvider;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import net.minecraft.world.chunk.storage.IOWorker;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import tamaized.voidscape.Voidscape;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class HackyWorldGen {

	public static long seed;

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

		public FixedSingleBiomeProvider(Supplier<Biome> biome) {
			super(biome);
			if (!Objects.requireNonNull(biome.get().getRegistryName()).getNamespace().equals(Voidscape.MODID)) {
				Voidscape.LOGGER.error("SOMETHING IS VERY WRONG HERE!\nTHIS IS NOT A VOIDSCAPE BIOME!!\n" + biome.get().getRegistryName());
			}
		}

		@Override
		protected Codec<? extends BiomeProvider> codec() {
			return CODEC;
		}

	}

	public static class DeepFreezeChunkManager extends ChunkManager {

		private final IOWorker worker;

		public DeepFreezeChunkManager(ServerWorld serverWorld_, SaveFormat.LevelSave levelSave_, DataFixer dataFixer_, TemplateManager templateManager_, Executor executor_, ThreadTaskExecutor<Runnable> threadTaskExecutor_, IChunkLightProvider chunkLightProvider_, ChunkGenerator chunkGenerator_, IChunkStatusListener chunkStatusListener_, Supplier<DimensionSavedDataManager> supplier_, int int_, boolean boolean_) {
			super(serverWorld_, levelSave_, dataFixer_, templateManager_, executor_, threadTaskExecutor_, chunkLightProvider_, chunkGenerator_, chunkStatusListener_, supplier_, int_, boolean_);
			ObfuscationReflectionHelper.setPrivateValue(ChunkManager.class, this, new AccessibleProxyTicketManager(executor_, threadTaskExecutor_), "field_219267_u");
			this.worker = new DeepFreezeIOWorker(((InstanceChunkGenerator) chunkGenerator_).snapshot(), new File(levelSave_.getDimensionPath(serverWorld_.dimension()), "region"), boolean_, "chunk");
		}

		@Nullable
		@Override
		public CompoundNBT read(ChunkPos chunkPos_) throws IOException {
			return worker.load(chunkPos_);
		}

		void unload() {
			getChunks().forEach(chunk -> {
				if (!((AccessibleProxyTicketManager) getDistanceManager()).isChunkToRemove(chunk.getPos().toLong()))
					((AccessibleProxyTicketManager) getDistanceManager()).updateChunkScheduling(chunk.getPos().toLong(), ChunkManager.MAX_CHUNK_DISTANCE + 1, chunk, 0);
			});
		}

		class AccessibleProxyTicketManager extends ChunkManager.ProxyTicketManager {

			AccessibleProxyTicketManager(Executor executor_, Executor executor1_) {
				super(executor_, executor1_);
			}

			@Override
			public boolean isChunkToRemove(long long_) {
				return super.isChunkToRemove(long_);
			}

			@Nullable
			@Override
			public ChunkHolder updateChunkScheduling(long chunkPosIn, int newLevel, @Nullable ChunkHolder holder, int oldLevel) {
				return super.updateChunkScheduling(chunkPosIn, newLevel, holder, oldLevel);
			}
		}
	}

	static class DeepFreezeIOWorker extends IOWorker {

		private final ResourceLocation location;
		private final RegionFileCache deepStorage;

		protected DeepFreezeIOWorker(ResourceLocation location, File file_, boolean boolean_, String string_) {
			super(file_, boolean_, string_);
			this.location = location;
			this.deepStorage = new RegionFileCache(new File(file_, "deepfreeze"), boolean_);
		}

		@Nullable
		@Override
		public CompoundNBT load(ChunkPos chunkPos_) throws IOException {
			CompletableFuture<CompoundNBT> completablefuture = this.submitTask(() -> {
				try {
					CompoundNBT compoundnbt = this.deepStorage.read(chunkPos_);
					if (compoundnbt == null) {
						try (InputStream stream = getClass().getResourceAsStream("/data/".
								concat(location.getNamespace()).
								concat("/worldgen/").
								concat(Voidscape.MODID).
								concat("/instance/").
								concat(location.getPath()).
								concat("/r.").
								concat(String.valueOf(chunkPos_.getRegionX())).
								concat(".").
								concat(String.valueOf(chunkPos_.getRegionZ())).
								concat(".mca"))) {
							if (stream != null) {
								DataInputStream data = new RegionFileInputStream(stream).getChunkDataInputStream(chunkPos_);
								if (data != null) {
									CompoundNBT nbt = CompressedStreamTools.read(data);
									deepStorage.write(chunkPos_, nbt);
									compoundnbt = nbt;
								}
							}
						}
					}
					return Either.left(compoundnbt);
				} catch (Exception exception) {
					Voidscape.LOGGER.warn("Failed to read chunk {}", chunkPos_, exception);
					return Either.right(exception);
				}

			});

			try {
				return completablefuture.join();
			} catch (CompletionException completionexception) {
				if (completionexception.getCause() instanceof IOException) {
					throw (IOException) completionexception.getCause();
				} else {
					throw completionexception;
				}
			}
		}

	}

}
