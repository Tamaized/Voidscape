package tamaized.voidscape.client;


import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;
import tamaized.voidscape.Config;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ui.RenderTurmoil;
import tamaized.voidscape.network.DonatorHandler;
import tamaized.voidscape.network.server.ServerPacketHandlerDonatorSettings;
import tamaized.voidscape.registry.ModBlocks;
import tamaized.voidscape.registry.RegUtil;
import tamaized.voidscape.turmoil.BindData;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public class ClientListener {

	private static final String CATEGORY = Voidscape.MODID.substring(0, 1).toUpperCase(Locale.US).concat(Voidscape.MODID.substring(1));

	public static final KeyMapping KEY_TURMOIL = new KeyMapping(

			"Summon forth your inner turmoil", // FIXME: localization

			KeyConflictContext.IN_GAME,

			InputConstants.Type.KEYSYM,

			GLFW.GLFW_KEY_Z,

			CATEGORY

	);

	private static final List<KeyMapping> ABILITY_KEYS = new ArrayList<>();
	public static final KeyMapping KEY_SPELL_1 = register(new KeyMapping(unloc("spell.1"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_1, CATEGORY));
	public static final KeyMapping KEY_SPELL_2 = register(new KeyMapping(unloc("spell.2"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_2, CATEGORY));
	public static final KeyMapping KEY_SPELL_3 = register(new KeyMapping(unloc("spell.3"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_3, CATEGORY));
	public static final KeyMapping KEY_SPELL_4 = register(new KeyMapping(unloc("spell.4"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_4, CATEGORY));
	public static final KeyMapping KEY_SPELL_5 = register(new KeyMapping(unloc("spell.5"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_5, CATEGORY));
	public static final KeyMapping KEY_SPELL_6 = register(new KeyMapping(unloc("spell.6"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_6, CATEGORY));
	public static final KeyMapping KEY_SPELL_7 = register(new KeyMapping(unloc("spell.7"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_7, CATEGORY));
	public static final KeyMapping KEY_SPELL_8 = register(new KeyMapping(unloc("spell.8"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_8, CATEGORY));
	public static final KeyMapping KEY_SPELL_9 = register(new KeyMapping(unloc("spell.9"), KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_9, CATEGORY));
	private static boolean turmoilDown = false;

	private static float pitch, yaw, roll;

	private static boolean hackyRenderSkip;
	private static float capturedPartialTicks;

	public static void init() {
		IEventBus busMod = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus busForge = MinecraftForge.EVENT_BUS;
		Shaders.init(busMod);
		DonatorLayer.setup();
		TintHandler.setup(busMod);
		busForge.addListener(EventPriority.HIGHEST, (Consumer<RenderLivingEvent.Pre<LivingEntity, ?>>) event -> {
			if (!hackyRenderSkip && !event.getEntity().canUpdate() && event.getEntity().
					getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapBind).map(BindData::isBound).orElse(false)).orElse(false)) {
				event.setCanceled(true);
				hackyRenderSkip = true;
				capturedPartialTicks = event.getPartialTick();
				event.getRenderer().render(event.getEntity(),

						Mth.lerp(1F, event.getEntity().yRotO, event.getEntity().getYRot()),

						1F,

						event.getPoseStack(),

						event.getMultiBufferSource(),

						event.getPackedLight());
				hackyRenderSkip = false;
			}
		});
		busForge.addListener((Consumer<TickEvent.ClientTickEvent>) event -> {
			if (event.phase == TickEvent.Phase.START) {
				if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null)
					Config.CLIENT_CONFIG.DONATOR.dirty = true;
				else if (Config.CLIENT_CONFIG.DONATOR.dirty) {
					Config.CLIENT_CONFIG.DONATOR.dirty = false;
					if (DonatorHandler.donators.contains(Minecraft.getInstance().player.getUUID()))
						Voidscape.NETWORK.sendToServer(new ServerPacketHandlerDonatorSettings(new DonatorHandler.DonatorSettings(Config.CLIENT_CONFIG.DONATOR.enabled.get(), Config.CLIENT_CONFIG.DONATOR.color.get())));
				}
				return;
			}
			if (!Minecraft.getInstance().isPaused() && Minecraft.getInstance().player != null)
				Minecraft.getInstance().player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapBind).ifPresent(bind -> bind.tick(Minecraft.getInstance().player)));
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
			if (Minecraft.getInstance().level != null)
				Minecraft.getInstance().level.entitiesForRendering().forEach(e -> e.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapBind).ifPresent(data -> {
					if (!e.canUpdate())
						data.tick(e);
				})));
		});
		busForge.addListener((Consumer<EntityViewRenderEvent.FogColors>) event -> {
			if (Minecraft.getInstance().level != null && Voidscape.checkForVoidDimension(Minecraft.getInstance().level)) {
				event.setRed(0.04F);
				event.setGreen(0.03F);
				event.setBlue(0.05F);
				if (Minecraft.getInstance().player != null)
					Minecraft.getInstance().player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity).ifPresent(data -> {
						event.setRed(Mth.clamp(data.getParanoia() / 1200F, 0.04F, 1F));
					}));
			}
		});
		busForge.addListener((Consumer<AttackEntityEvent>) event -> {
			if (event.getPlayer().level.isClientSide() && event.getPlayer() instanceof LocalPlayer && event.getPlayer() == Minecraft.getInstance().player)
				RenderTurmoil.resetFade();
		});
		busForge.addListener((Consumer<ArrowNockEvent>) event -> {
			if (event.getPlayer().level.isClientSide() && event.getPlayer() instanceof LocalPlayer && event.getPlayer() == Minecraft.getInstance().player)
				RenderTurmoil.resetFade();
		});
		busForge.addListener((Consumer<EntityViewRenderEvent.FieldOfView>) event -> {
			if (event.getCamera().getEntity() instanceof LivingEntity living) {
				ItemStack itemstack = living.getUseItem();
				if (living.isUsingItem()) {
					if (RegUtil.isMyBow(itemstack, Items.BOW)) {
						int i = living.getTicksUsingItem();
						float f1 = (float) i / 20.0F;
						if (f1 > 1.0F) {
							f1 = 1.0F;
						} else {
							f1 = f1 * f1;
						}

						event.setFOV(event.getFOV() * (1.0F - f1 * 0.15F));
					} else if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && living instanceof Player player && player.isScoping()) {
						event.setFOV(0.1F);
					}
				}
			}
		});
		busMod.addListener((Consumer<FMLClientSetupEvent>) event -> {
			ClientRegistry.registerKeyBinding(KEY_TURMOIL);
			ABILITY_KEYS.forEach(ClientRegistry::registerKeyBinding);

			ItemBlockRenderTypes.setRenderLayer(ModBlocks.VOIDIC_CRYSTAL_ORE.get(), RenderType.cutoutMipped());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.PLANT.get(), RenderType.cutoutMipped());

			DimensionSpecialEffects info = new DimensionSpecialEffects(Float.NaN, false, DimensionSpecialEffects.SkyType.NONE, false, false) {
				@Override
				public Vec3 getBrightnessDependentFogColor(Vec3 p_230494_1_, float p_230494_2_) {
					return Vec3.ZERO;
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
			};
			info.setSkyRenderHandler(new VoidSkyRenderer());
			DimensionSpecialEffects.EFFECTS.put(Voidscape.WORLD_KEY_VOID.location(), info);
		});
		busMod.addListener((Consumer<EntityRenderersEvent.AddLayers>) event -> {
			event.getSkins().forEach(renderer -> {
				LivingEntityRenderer<Player, EntityModel<Player>> skin = event.getSkin(renderer);
				attachRenderLayers(Objects.requireNonNull(skin));
			});
		});
	}

	private static KeyMapping register(KeyMapping key) {
		ABILITY_KEYS.add(key);
		return key;
	}

	public static List<KeyMapping> getAbilityKeys() {
		return ImmutableList.copyOf(ABILITY_KEYS);
	}

	private static String unloc(String k) {
		return Voidscape.MODID.concat(".KeyMapping.".concat(k));
	}

	private static <T extends LivingEntity, M extends EntityModel<T>> void attachRenderLayers(LivingEntityRenderer<T, M> renderer) {
		renderer.addLayer(new DonatorLayer<>(renderer));
	}

}
