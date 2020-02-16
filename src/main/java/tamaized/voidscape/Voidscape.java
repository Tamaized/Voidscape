package tamaized.voidscape;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;
import tamaized.voidscape.world.VoidBiome;
import tamaized.voidscape.world.VoidChunkGenerator;
import tamaized.voidscape.world.VoidDimension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Mod(Voidscape.MODID)
public class Voidscape {

	public static final String MODID = "voidscape";

	private static final List<DeferredRegister> REGISTERS = new ArrayList<>();

	private static final DeferredRegister<ModDimension> REGISTRY_MOD_DIMENSION = create(new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, MODID));
	public static final RegistryObject<ModDimension> DIMENSION = REGISTRY_MOD_DIMENSION.register("void", () -> new ModDimension() {
		@Override
		public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
			return VoidDimension::new;
		}
	});
	private static final DeferredRegister<Biome> REGISTRY_BIOME = create(new DeferredRegister<>(ForgeRegistries.BIOMES, MODID));
	public static final RegistryObject<Biome> BIOME = REGISTRY_BIOME.register("void", VoidBiome::new);
	private static final DeferredRegister<ChunkGeneratorType<?, ?>> REGISTRY_CHUNK_GENERATOR_TYPE = create(new DeferredRegister<>(ForgeRegistries.CHUNK_GENERATOR_TYPES, MODID));
	public static final RegistryObject<ChunkGeneratorType<GenerationSettings, VoidChunkGenerator>> CHUNK_GENERATOR_TYPE = REGISTRY_CHUNK_GENERATOR_TYPE.
			register("void", () -> new ChunkGeneratorType<>(VoidChunkGenerator::new, false, GenerationSettings::new));
	private static DimensionType DIMENSION_TYPE = null;

	public Voidscape() {
		for (DeferredRegister register : REGISTERS)
			register.register(FMLJavaModLoadingContext.get().getModEventBus());
		FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<FMLCommonSetupEvent>) event ->

				DIMENSION_TYPE = DimensionManager.registerDimension(new ResourceLocation(MODID, "void"), DIMENSION.get(), new PacketBuffer(Unpooled.buffer()), false)

		);
	}

	private static <R extends IForgeRegistryEntry<R>> DeferredRegister<R> create(DeferredRegister<R> register) {
		REGISTERS.add(register);
		return register;
	}

	public static DimensionType getDimensionType() {
		return DIMENSION_TYPE;
	}
}
