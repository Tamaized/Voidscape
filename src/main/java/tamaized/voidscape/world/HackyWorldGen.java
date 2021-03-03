package tamaized.voidscape.world;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Either;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.IChunkLightProvider;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import net.minecraft.world.chunk.storage.IOWorker;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketManager;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import tamaized.voidscape.Voidscape;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class HackyWorldGen {

	public static long seed;

	public static class DeepFreezeChunkManager extends ChunkManager {

		private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
		private static final Method TicketManager_isChunkToRemove = ObfuscationReflectionHelper.findMethod(TicketManager.class, "func_219371_a", long.class);
		private static final Method TicketManager_updateChunkScheduling = ObfuscationReflectionHelper.findMethod(TicketManager.class, "func_219372_a", long.class, int.class, ChunkHolder.class, int.class);
		private static final MethodHandle handle_TicketManager_isChunkToRemove;
		private static final MethodHandle handle_TicketManager_updateChunkScheduling;

		static {
			MethodHandle tmp_handle_TicketManager_isChunkToRemove = null;
			MethodHandle tmp_handle_TicketManager_updateChunkScheduling = null;
			try {
				tmp_handle_TicketManager_isChunkToRemove = LOOKUP.unreflect(TicketManager_isChunkToRemove);
				tmp_handle_TicketManager_isChunkToRemove = LOOKUP.unreflect(TicketManager_updateChunkScheduling);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			handle_TicketManager_isChunkToRemove = tmp_handle_TicketManager_isChunkToRemove;
			handle_TicketManager_updateChunkScheduling = tmp_handle_TicketManager_updateChunkScheduling;
		}

		private final IOWorker worker;

		public DeepFreezeChunkManager(ServerWorld serverWorld_, SaveFormat.LevelSave levelSave_, DataFixer dataFixer_, TemplateManager templateManager_, Executor executor_, ThreadTaskExecutor<Runnable> threadTaskExecutor_, IChunkLightProvider chunkLightProvider_, ChunkGenerator chunkGenerator_, IChunkStatusListener chunkStatusListener_, Supplier<DimensionSavedDataManager> supplier_, int int_, boolean boolean_) {
			super(serverWorld_, levelSave_, dataFixer_, templateManager_, executor_, threadTaskExecutor_, chunkLightProvider_, chunkGenerator_, chunkStatusListener_, supplier_, int_, boolean_);
			this.worker = new DeepFreezeIOWorker(((InstanceChunkGenerator) chunkGenerator_).snapshot(), new File(levelSave_.getDimensionPath(serverWorld_.dimension()), "region"), boolean_, "chunk");
		}

		@Nullable
		@Override
		public CompoundNBT read(ChunkPos chunkPos_) throws IOException {
			return worker.load(chunkPos_);
		}

		void unload() {
			getChunks().forEach(chunk -> {
				try {
					if (!(boolean) handle_TicketManager_isChunkToRemove.invokeExact(getDistanceManager(), chunk.getPos().toLong()))
						handle_TicketManager_updateChunkScheduling.invokeExact(getDistanceManager(), chunk.getPos().toLong(), ChunkManager.MAX_CHUNK_DISTANCE + 1, chunk, 0);
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
			});
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
