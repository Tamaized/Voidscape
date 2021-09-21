package tamaized.voidscape;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.turmoil.Insanity;
import tamaized.voidscape.turmoil.Turmoil;
import tamaized.voidscape.world.VoidBiome;
import tamaized.voidscape.world.VoidChunkGenerator;
import tamaized.voidscape.world.VoidDimension;
import tamaized.voidscape.world.VoidTeleporter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Mod(Voidscape.MODID)
public class Voidscape {

	public static final String MODID = "voidscape";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder.
			named(new ResourceLocation(MODID, MODID)).
			clientAcceptedVersions(s -> true).
			serverAcceptedVersions(s -> true).
			networkProtocolVersion(() -> "1").
			simpleChannel();

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
	private static DimensionType DIMENSION_TYPE;

	public Voidscape() {
		IEventBus busMod = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus busForge = MinecraftForge.EVENT_BUS;
		for (DeferredRegister register : REGISTERS)
			register.register(busMod);
		busMod.addListener((Consumer<FMLCommonSetupEvent>) event -> {
			getDimensionType();
			NetworkMessages.register(NETWORK);
		});
		busForge.addListener((Consumer<LivingDeathEvent>) event -> {
			if (event.getEntity() instanceof PlayerEntity && event.getEntity().world.dimension.getType().getId() == getDimensionTypeID()) {
				event.setCanceled(true);
				((PlayerEntity) event.getEntity()).setHealth(((PlayerEntity) event.getEntity()).getMaxHealth());
				event.getEntity().changeDimension(DimensionType.OVERWORLD, VoidTeleporter.INSTANCE);
			}
		});
		CapabilityManager.INSTANCE.register(Turmoil.ITurmoilData.class, new Turmoil.ITurmoilData.Storage() {
		}, Turmoil.AttachedTurmoilData::new);
		Turmoil.AttachedTurmoilData.register(Insanity::new);

	}

	private static <R extends IForgeRegistryEntry<R>> DeferredRegister<R> create(DeferredRegister<R> register) {
		REGISTERS.add(register);
		return register;
	}

	public static int getDimensionTypeID() {
		return DIMENSION_TYPE.getId();
	}

	public static DimensionType getDimensionType() {
		return DIMENSION_TYPE = DimensionManager.registerOrGetDimension(new ResourceLocation(MODID, "void"), DIMENSION.get(), new PacketBuffer(Unpooled.buffer()), false);
	}

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	public static <T> T getNull() {
		return null;
	}

}
