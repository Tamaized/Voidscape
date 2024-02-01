package tamaized.voidscape.registry;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.fluid.VoidicFluid;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModFluids implements RegistryClass {

	@Override
	public void init(IEventBus bus) {

	}

	private static final DeferredRegister<FluidType> REGISTERY_FLUID_TYPE = RegUtil.create(NeoForgeRegistries.Keys.FLUID_TYPES);
	private static final DeferredRegister<Fluid> REGISTERY_FLUID = RegUtil.create(Registries.FLUID);

	public static final Supplier<FluidType> VOIDIC_TYPE = REGISTERY_FLUID_TYPE.register("voidic", makeFluidType(
			FluidType.Properties.create()
					.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
					.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
					.lightLevel(1)
					.density(4000)
					.viscosity(4000),
			new ResourceLocation(Voidscape.MODID, "block/fluid/voidic/still"),
			new ResourceLocation(Voidscape.MODID, "block/fluid/voidic/flowing"),
			null,
			new Vector3f(0.5F, 0F, 1F),
			null
	));

	public static class CircularReferenceHandler {
		public static final Supplier<Item> VOIDIC_BUCKET = ModItems.REGISTRY
				.register("voidic_bucket", () -> new BucketItem(VOIDIC_SOURCE, ModItems.ItemProps.DEFAULT.properties().get().stacksTo(1).craftRemainder(Items.BUCKET)));
		private static final BaseFlowingFluid.Properties VOIDIC_PROPERTIES = new BaseFlowingFluid.Properties(VOIDIC_TYPE, VOIDIC_SOURCE, VOIDIC_FLOWING)
				.bucket(VOIDIC_BUCKET);
	}

	public static final Supplier<FlowingFluid> VOIDIC_SOURCE = REGISTERY_FLUID.register("voidic_source", () -> new VoidicFluid.Source(CircularReferenceHandler.VOIDIC_PROPERTIES));
	public static final Supplier<FlowingFluid> VOIDIC_FLOWING = REGISTERY_FLUID.register("voidic_flowing", () -> new VoidicFluid.Flowing(CircularReferenceHandler.VOIDIC_PROPERTIES));

	private static Supplier<FluidType> makeFluidType(FluidType.Properties props, ResourceLocation stillpath, ResourceLocation flowingpath, @Nullable ResourceLocation overlay, Vector3f fog, @Nullable Supplier<Function<BlockPos, Integer>> color) {
		return () -> new FluidType(props) {
			@Override
			public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
				consumer.accept(new IClientFluidTypeExtensions() {
					@Override
					public ResourceLocation getStillTexture() {
						return stillpath;
					}

					@Override
					public ResourceLocation getFlowingTexture() {
						return flowingpath;
					}

					@Override
					public @Nullable ResourceLocation getOverlayTexture() {
						return overlay;
					}

					@Override
					public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
						return color != null ? color.get().apply(pos) | 0xFF000000 : this.getTintColor();
					}

					@Override
					public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
						return fog;
					}
				});
			}
		};
	}

}
