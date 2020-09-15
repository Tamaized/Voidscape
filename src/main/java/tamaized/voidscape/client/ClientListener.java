package tamaized.voidscape.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModBlocks;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Voidscape.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientListener {

	public static final KeyBinding KEY = new KeyBinding(

			"Summon forth your inner turmoil",

			KeyConflictContext.IN_GAME,

			InputMappings.Type.KEYSYM,

			GLFW.GLFW_KEY_Z,

			Voidscape.MODID.substring(0, 1).toUpperCase(Locale.US).concat(Voidscape.MODID.substring(1))

	);

	static {
		MinecraftForge.EVENT_BUS.addListener((Consumer<TickEvent.ClientTickEvent>) event -> {
			if (event.phase == TickEvent.Phase.START)
				return;
			if (KEY.isPressed() && Minecraft.getInstance().player != null && Minecraft.getInstance().world != null)
				Minecraft.getInstance().player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(Turmoil::action));

		});
		MinecraftForge.EVENT_BUS.addListener((Consumer<EntityViewRenderEvent.FogColors>) event -> {
			if (Minecraft.getInstance().world != null && Voidscape.checkForVoidDimension(Minecraft.getInstance().world)) {
				event.setRed(0.04F);
				event.setGreen(0.03F);
				event.setBlue(0.05F);
			}
		});
	}

	@SubscribeEvent
	public static void setup(FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(KEY);
		RenderTypeLookup.setRenderLayer(ModBlocks.VOIDIC_CRYSTAL_ORE.get(), RenderType.getCutoutMipped());
		DimensionRenderInfo.field_239208_a_.put(Voidscape.getDimensionType(), new DimensionRenderInfo(Float.NaN, false, DimensionRenderInfo.FogType.NONE, false, false) {
			@Override
			public Vector3d func_230494_a_(Vector3d p_230494_1_, float p_230494_2_) {
				return Vector3d.ZERO;
			}

			@Override
			public boolean func_230493_a_(int p_230493_1_, int p_230493_2_) {
				return true;
			}

			@Override
			@Nullable
			public float[] func_230492_a_(float p_230492_1_, float p_230492_2_) {
				return null;
			}
		});
	}

}
