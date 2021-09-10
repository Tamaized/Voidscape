package tamaized.voidscape.world;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Either;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.chunk.storage.IOWorker;
import net.minecraft.world.level.chunk.storage.RegionFileStorage;
import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
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

	public static class DeepFreezeChunkManager extends ChunkMap {

		private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
		private static final Method TicketManager_isChunkToRemove = ObfuscationReflectionHelper.findMethod(DistanceManager.class, "m_7009_", long.class);
		private static final Method TicketManager_updateChunkScheduling = ObfuscationReflectionHelper.findMethod(DistanceManager.class, "m_7288_", long.class, int.class, ChunkHolder.class, int.class);
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

		public DeepFreezeChunkManager(ServerLevel serverWorld_, LevelStorageSource.LevelStorageAccess levelSave_, DataFixer dataFixer_, StructureManager templateManager_, Executor executor_, BlockableEventLoop<Runnable> threadTaskExecutor_, LightChunkGetter chunkLightProvider_, ChunkGenerator chunkGenerator_, ChunkProgressListener chunkProgressListener, ChunkStatusUpdateListener chunkStatusListener_, Supplier<DimensionDataStorage> supplier_, int int_, boolean boolean_) {
			super(serverWorld_, levelSave_, dataFixer_, templateManager_, executor_, threadTaskExecutor_, chunkLightProvider_, chunkGenerator_, chunkProgressListener, chunkStatusListener_, supplier_, int_, boolean_);
			this.worker = new DeepFreezeIOWorker(((InstanceChunkGenerator) chunkGenerator_).snapshot(), new File(levelSave_.getDimensionPath(serverWorld_.dimension()), "region"), boolean_, "chunk");
		}

		@Nullable
		@Override
		public CompoundTag read(ChunkPos chunkPos_) throws IOException {
			return worker.load(chunkPos_);
		}

		void unload() {
			getChunks().forEach(chunk -> {
				try {
					if (!(boolean) handle_TicketManager_isChunkToRemove.invokeExact(getDistanceManager(), chunk.getPos().toLong()))
						handle_TicketManager_updateChunkScheduling.invokeExact(getDistanceManager(), chunk.getPos().toLong(), ChunkMap.MAX_CHUNK_DISTANCE + 1, chunk, 0);
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
			});
		}
	}

	static class DeepFreezeIOWorker extends IOWorker {

		private final ResourceLocation location;
		private final RegionFileStorage deepStorage;

		protected DeepFreezeIOWorker(ResourceLocation location, File file_, boolean boolean_, String string_) {
			super(file_, boolean_, string_);
			this.location = location;
			this.deepStorage = new RegionFileStorage(new File(file_, "deepfreeze"), boolean_);
		}

		@Nullable
		@Override
		public CompoundTag load(ChunkPos chunkPos_) throws IOException {
			CompletableFuture<CompoundTag> completablefuture = this.submitTask(() -> {
				try {
					CompoundTag compoundnbt = this.deepStorage.read(chunkPos_);
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
									CompoundTag nbt = NbtIo.read(data);
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
