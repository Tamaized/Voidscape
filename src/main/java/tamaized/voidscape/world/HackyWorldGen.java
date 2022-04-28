package tamaized.voidscape.world;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Either;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
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
import tamaized.voidscape.registry.ModBiomes;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HackyWorldGen {

	public static long seed;

	private static final long VERSION = 5;

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
			this.worker = new DeepFreezeIOWorker(((InstanceChunkGenerator) chunkGenerator_).snapshot(), levelSave_.getDimensionPath(serverWorld_.dimension()).resolve("region"), boolean_, "chunk");
		}

		@Override
		public void write(ChunkPos p_63503_, CompoundTag p_63504_) {
			worker.store(p_63503_, p_63504_);
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

		protected DeepFreezeIOWorker(ResourceLocation location, Path file_, boolean boolean_, String string_) {
			super(file_, boolean_, string_);
			this.location = location;
			this.deepStorage = new RegionFileStorage(file_.resolve("deepfreeze"), boolean_);
		}

		@Nullable
		private InputStream getRegionFolder() {
			return getClass().getResourceAsStream("/data/".
					concat(location.getNamespace()).
					concat("/worldgen/").
					concat(Voidscape.MODID).
					concat("/instance/").
					concat(location.getPath()));
		}

		@Override
		public CompletableFuture<Void> store(ChunkPos chunkPos_, @Nullable CompoundTag nbt) {
			return this.submitTask(() -> {
				/*try {
					deepStorage.write(chunkPos_, nbt);
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				return Either.left(new CompletableFuture<Void>());
			}).thenCompose(Function.identity());
		}

		@Nullable
		@Override
		public CompoundTag load(ChunkPos chunkPos_) throws IOException {
			CompletableFuture<CompoundTag> completablefuture = this.submitTask(() -> {
				boolean write = false;
				try {
					CompoundTag compoundnbt = deepStorage.read(chunkPos_);
					if (compoundnbt == null || !compoundnbt.
							contains(Voidscape.MODID, Tag.TAG_COMPOUND) || compoundnbt.
							getCompound(Voidscape.MODID).getLong("check") != VERSION) {
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
									CompoundTag check = new CompoundTag();
									check.putLong("check", VERSION);
									nbt.put(Voidscape.MODID, check);
									write = true;
									compoundnbt = nbt;
								}
							}
						}
					}
					if (compoundnbt != null)
						sanitize(location, compoundnbt, null);
					if (write)
						deepStorage.write(chunkPos_, compoundnbt);
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

	public static void main(String[] args) throws IOException {
		DeepFreezeIOWorker worker = new DeepFreezeIOWorker(new ResourceLocation(Voidscape.MODID, "psychosis"), Path.of("./sanitized/region/"), false, "chunk");
		List<String> regions;
		try (InputStream stream = worker.getRegionFolder(); InputStreamReader ir = new InputStreamReader(Objects.requireNonNull(stream)); BufferedReader reader = new BufferedReader(ir)) {
			regions = reader.lines().collect(Collectors.toList());
		}
		regions.forEach(r -> {
			System.out.println("Sanitizing: " + r);
			Matcher matcher = Pattern.compile("r\\.(.*?)\\.(.*?)\\.mca").matcher(r);
			if (matcher.find()) {
				int x = Integer.parseInt(matcher.group(1));
				int z = Integer.parseInt(matcher.group(2));
				System.out.println("rX: " + x + "; rZ: " + z);
				int cx = x << 5;
				int cz = z << 5;
				System.out.println("cX: " + cx + "; cZ: " + cz);
				for (int px = cx; px < (x + 1) << 5; px++) {
					for (int pz = cz; pz < (z + 1) << 5; pz++) {
						System.out.print("Reading " + px + " : " + pz + "; ");
						try {
							final ChunkPos pos = new ChunkPos(px, pz);
							final CompoundTag nbt = worker.load(pos);
							if (nbt != null) {
								sanitize(worker.location, nbt, () -> System.out.print("Sanitizing; "));
								worker.deepStorage.write(pos, nbt);
								System.out.print("Sanitized; ");
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				System.out.println("\nDone\n");
			}
		});
		System.out.println("Finished");
	}

	private static void sanitize(ResourceLocation location, CompoundTag nbt, @Nullable Runnable exec) {
		nbt.getList("sections", Tag.TAG_COMPOUND).listIterator().forEachRemaining(tag -> {
			CompoundTag ct = ((CompoundTag) tag);
			ct.remove("biomes");
			CompoundTag biomes = new CompoundTag();
			ListTag list = new ListTag();
			list.add(StringTag.valueOf(location.toString()));
			biomes.put("palette", list);
			ct.put("biomes", biomes);
			if (exec != null)
				exec.run();
		});
	}

}
