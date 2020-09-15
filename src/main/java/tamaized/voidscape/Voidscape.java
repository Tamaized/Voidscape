package tamaized.voidscape;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.registry.ModBlocks;
import tamaized.voidscape.registry.RegUtil;
import tamaized.voidscape.turmoil.Insanity;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;
import tamaized.voidscape.world.VoidChunkGenerator;
import tamaized.voidscape.world.VoidTeleporter;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
	public static final RegistryKey<World> WORLD_KEY_VOID = RegistryKey.func_240903_a_(Registry.WORLD_KEY, new ResourceLocation(MODID, "void"));
	public static final SubCapability.ISubCap.SubCapKey<Turmoil> subCapTurmoilData = SubCapability.AttachedSubCap.register(Turmoil.class, Turmoil::new);
	public static final SubCapability.ISubCap.SubCapKey<Insanity> subCapInsanity = SubCapability.AttachedSubCap.register(Insanity.class, Insanity::new);

	public Voidscape() {
		IEventBus busMod = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus busForge = MinecraftForge.EVENT_BUS;
		RegUtil.register(busMod);
		busMod.addListener((Consumer<FMLCommonSetupEvent>) event -> {
			NetworkMessages.register(NETWORK);
			Registry.register(Registry.field_239690_aB_, new ResourceLocation(MODID, "void"), VoidChunkGenerator.codec);
			CapabilityManager.INSTANCE.register(SubCapability.ISubCap.class, new SubCapability.ISubCap.Storage() {
			}, SubCapability.AttachedSubCap::new);
		});
		busForge.addListener((Consumer<FMLServerStartingEvent>) event ->

				event.getServer().getCommandManager().getDispatcher().register(LiteralArgumentBuilder.<CommandSource>literal("voidscape").
						then(VoidCommands.Debug.register()))

		);
		busForge.addListener((Consumer<LivingDeathEvent>) event -> {
			if (event.getEntity() instanceof PlayerEntity && checkForVoidDimension(event.getEntity().world)) {
				event.setCanceled(true);
				((PlayerEntity) event.getEntity()).setHealth(((PlayerEntity) event.getEntity()).getMaxHealth() * 0.1F);
				event.getEntity().changeDimension(getWorld(event.getEntity().world, World.field_234918_g_), VoidTeleporter.INSTANCE);
			}
		});
		busForge.addListener((Consumer<TickEvent.PlayerTickEvent>) event -> {
			if (event.player.world != null && checkForVoidDimension(event.player.world) && event.player.ticksExisted % 30 == 0 && event.player.getRNG().nextFloat() <= 0.10F) {
				final int dist = 64;
				final int rad = dist / 2;
				final Supplier<Integer> exec = () -> event.player.getRNG().nextInt(dist) - rad;
				BlockPos dest = event.player.func_233580_cy_().add(exec.get(), exec.get(), exec.get());
				if (event.player.world.getBlockState(dest).equals(Blocks.BEDROCK.getDefaultState()))
					event.player.world.setBlockState(dest, ModBlocks.VOIDIC_CRYSTAL_ORE.get().getDefaultState());
			}
		});
	}

	public static boolean checkForVoidDimension(World world) {
		return world.func_234923_W_().func_240901_a_().equals(WORLD_KEY_VOID.func_240901_a_());
	}

	public static ServerWorld getWorld(World world, RegistryKey<World> dest) {
		return Objects.requireNonNull(Objects.requireNonNull(world.getServer()).getWorld(dest));
	}

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	public static <T> T getNull() {
		return null;
	}

}
