package tamaized.voidscape.client.fluid;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.joml.Vector4f;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.fluid.ModFluidTypes;
import tamaized.voidscape.registry.fluid.ModFluids;

@Component(dist = Dist.CLIENT)
public class FluidClientExtensions {

	@Autowired(dist = Dist.CLIENT)
	private ModFluids fluids;

	@Autowired(dist = Dist.CLIENT)
	private ModFluidTypes fluidTypes;

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(RegisterFluidModelsEvent.class, event -> event.register(
			new FluidModel.Unbaked(
				new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/fluid/voidic/still")),
				new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/fluid/voidic/flowing")),
				null,
				null
			),
			fluids.VOIDIC_SOURCE,
			fluids.VOIDIC_FLOWING
		));

		bus.addListener(RegisterClientExtensionsEvent.class, event -> event.registerFluidType(new IClientFluidTypeExtensions() {
			@Override
			public void modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f fluidFogColor) {
				fluidFogColor.set(0.5F, 0F, 1F, fluidFogColor.w);
			}
		}, fluidTypes.VOIDIC));
	}

}
