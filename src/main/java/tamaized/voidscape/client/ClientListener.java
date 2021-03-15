package tamaized.voidscape.client;


import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ui.RenderTurmoil;
import tamaized.voidscape.registry.ModBlocks;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Voidscape.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientListener {

	private static final String CATEGORY = Voidscape.MODID.substring(0, 1).toUpperCase(Locale.US).concat(Voidscape.MODID.substring(1));

	public static final KeyBinding KEY_TURMOIL = new KeyBinding(

			"Summon forth your inner turmoil",

			KeyConflictContext.IN_GAME,

			InputMappings.Type.KEYSYM,

			GLFW.GLFW_KEY_Z,

			CATEGORY

	);

	private static final List<KeyBinding> ABILITY_KEYS = new ArrayList<>();

	private static KeyBinding register(KeyBinding key) {
		ABILITY_KEYS.add(key);
		return key;
	}

	public static List<KeyBinding> getAbilityKeys() {
		return ImmutableList.copyOf(ABILITY_KEYS);
	}

	public static final KeyBinding KEY_SPELL_1 = register(new KeyBinding(unloc("spell.1"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_1, CATEGORY));
	public static final KeyBinding KEY_SPELL_2 = register(new KeyBinding(unloc("spell.2"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_2, CATEGORY));
	public static final KeyBinding KEY_SPELL_3 = register(new KeyBinding(unloc("spell.3"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_3, CATEGORY));
	public static final KeyBinding KEY_SPELL_4 = register(new KeyBinding(unloc("spell.4"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_4, CATEGORY));
	public static final KeyBinding KEY_SPELL_5 = register(new KeyBinding(unloc("spell.5"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_5, CATEGORY));
	public static final KeyBinding KEY_SPELL_6 = register(new KeyBinding(unloc("spell.6"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_6, CATEGORY));
	public static final KeyBinding KEY_SPELL_7 = register(new KeyBinding(unloc("spell.7"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_7, CATEGORY));
	public static final KeyBinding KEY_SPELL_8 = register(new KeyBinding(unloc("spell.8"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_8, CATEGORY));
	public static final KeyBinding KEY_SPELL_9 = register(new KeyBinding(unloc("spell.9"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_9, CATEGORY));

	private static boolean turmoilDown = false;

	static {
		MinecraftForge.EVENT_BUS.addListener((Consumer<TickEvent.ClientTickEvent>) event -> {
			if (event.phase == TickEvent.Phase.START)
				return;
			if (!turmoilDown && KEY_TURMOIL.isDown() && Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
				Minecraft.getInstance().player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(Turmoil::clientAction));
				turmoilDown = true;
			}
			if (!KEY_TURMOIL.isDown())
				turmoilDown = false;
			if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null && Minecraft.getInstance().screen == null) {
				for (int i = 0; i < ABILITY_KEYS.size(); i++) {
					final int slot = i;
					if (ABILITY_KEYS.get(i).isDown()) {
						Minecraft.getInstance().player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> stats.executeAbility(Minecraft.getInstance().player, slot)));
						RenderTurmoil.resetFade();
					}
				}
			}
			if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hurtTime > 0)
				RenderTurmoil.resetFade();
		});
		MinecraftForge.EVENT_BUS.addListener((Consumer<EntityViewRenderEvent.FogColors>) event -> {
			if (Minecraft.getInstance().level != null && Voidscape.checkForVoidDimension(Minecraft.getInstance().level)) {
				event.setRed(0.04F);
				event.setGreen(0.03F);
				event.setBlue(0.05F);
				if (Minecraft.getInstance().player != null)
					Minecraft.getInstance().player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity).ifPresent(data -> {
						event.setRed(MathHelper.clamp(data.getParanoia() / 1200F, 0.04F, 1F));
					}));
			}
		});
		MinecraftForge.EVENT_BUS.addListener((Consumer<AttackEntityEvent>) event -> {
			if (event.getPlayer().level.isClientSide() && event.getPlayer() instanceof ClientPlayerEntity && event.getPlayer() == Minecraft.getInstance().player)
				RenderTurmoil.resetFade();
		});
		MinecraftForge.EVENT_BUS.addListener((Consumer<ArrowNockEvent>) event -> {
			if (event.getPlayer().level.isClientSide() && event.getPlayer() instanceof ClientPlayerEntity && event.getPlayer() == Minecraft.getInstance().player)
				RenderTurmoil.resetFade();
		});
	}

	private static String unloc(String k) {
		return Voidscape.MODID.concat(".keybinding.".concat(k));
	}

	@SubscribeEvent
	public static void setup(FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(KEY_TURMOIL);
		ABILITY_KEYS.forEach(ClientRegistry::registerKeyBinding);
		RenderTypeLookup.setRenderLayer(ModBlocks.VOIDIC_CRYSTAL_ORE.get(), RenderType.cutoutMipped());
		DimensionRenderInfo.EFFECTS.put(Voidscape.WORLD_KEY_VOID.location(), new DimensionRenderInfo(Float.NaN, false, DimensionRenderInfo.FogType.NONE, false, false) {
			@Override
			public Vector3d getBrightnessDependentFogColor(Vector3d p_230494_1_, float p_230494_2_) {
				return Vector3d.ZERO;
			}

			@Override
			public boolean isFoggyAt(int p_230493_1_, int p_230493_2_) {
				return true;
			}

			@Override
			@Nullable
			public float[] getSunriseColor(float p_230492_1_, float p_230492_2_) {
				return null;
			}
		});
	}

}
