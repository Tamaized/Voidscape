package tamaized.voidscape.registry.fluid;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class ModFluidTypes {

	private final List<ClientData> clientData = new ArrayList<>();

	public final DeferredHolder<FluidType, FluidType> VOIDIC = addClientData(RegUtil.register(NeoForgeRegistries.Keys.FLUID_TYPES, "voidic", () -> new FluidType(
			FluidType.Properties.create()
				.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
				.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
				.lightLevel(1)
				.density(4000)
				.viscosity(4000))),
		Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/fluid/voidic/still"),
		Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/fluid/voidic/flowing"),
		null,
		new Vector3f(0.5F, 0F, 1F),
		null
	);

	@PostConstruct
	private void init(IEventBus bus) {
		// FIXME
		/*bus.addListener(RegisterClientExtensionsEvent.class, event -> clientData.forEach(data -> event.registerFluidType(new IClientFluidTypeExtensions() {
			@Override
			public Identifier getStillTexture() {
				return data.stillpath;
			}

			@Override
			public Identifier getFlowingTexture() {
				return data.flowingpath;
			}

			@Override
			public @Nullable Identifier getOverlayTexture() {
				return data.overlay;
			}

			@Override
			public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
				return data.color != null ? data.color.get().apply(pos) | 0xFF000000 : this.getTintColor();
			}

			@Override
			public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
				return data.fog;
			}
		}, data.fluid)));*/
	}

	private DeferredHolder<FluidType, FluidType> addClientData(DeferredHolder<FluidType, FluidType> fluid, Identifier stillpath, Identifier flowingpath, @Nullable Identifier overlay, Vector3f fog, @Nullable Supplier<Function<BlockPos, Integer>> color) {
		clientData.add(new ClientData(fluid, stillpath, flowingpath, overlay, fog, color));
		return fluid;
	}

	private record ClientData(DeferredHolder<FluidType, FluidType> fluid, Identifier stillpath, Identifier flowingpath, @Nullable Identifier overlay, Vector3f fog, @Nullable Supplier<Function<BlockPos, Integer>> color) {

	}

}
