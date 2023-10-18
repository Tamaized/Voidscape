package tamaized.voidscape.client;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.joml.Matrix4f;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Config;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.capability.SubCapability;
import tamaized.voidscape.client.particle.ParticleSpellCloud;
import tamaized.voidscape.client.ui.RenderTurmoil;
import tamaized.voidscape.network.DonatorHandler;
import tamaized.voidscape.network.server.ServerPacketHandlerDonatorSettings;
import tamaized.voidscape.registry.ModParticles;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public class ClientListener {

	public static void init() {
		IEventBus busMod = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus busForge = MinecraftForge.EVENT_BUS;
		Shaders.init(busMod);
		DonatorLayer.setup();
		TintHandler.setup(busMod);
		busMod.addListener(RenderTurmoil::render);
		busMod.addListener((Consumer<RegisterParticleProvidersEvent>) event -> {
			event.registerSpriteSet(ModParticles.SPELL_CLOUD.get(), ParticleSpellCloud.Factory::new);
		});
		busForge.addListener((Consumer<TickEvent.ClientTickEvent>) event -> {
			RenderTurmoil.tick(event);
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
		});
		busForge.addListener((Consumer<ViewportEvent.ComputeFogColor>) event -> {
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
		busForge.addListener((Consumer<ComputeFovModifierEvent>) event -> {
			ItemStack itemstack = event.getPlayer().getUseItem();
			if (event.getPlayer().isUsingItem()) {
				if (RegUtil.isMyBow(itemstack, Items.BOW)) {
					float f1 = (float) event.getPlayer().getTicksUsingItem() / 20.0F;
					f1 = f1 > 1.0F ? 1.0F : f1 * f1;
					event.setNewFovModifier((float) Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0D, event.getFovModifier() * (1.0F - f1 * 0.15F)));
				}
			}
		});
		busMod.addListener((Consumer<RegisterDimensionSpecialEffectsEvent>) event -> event
				.register(Voidscape.WORLD_KEY_VOID.location(), new DimensionSpecialEffects(Float.NaN, false, DimensionSpecialEffects.SkyType.NONE, false, false) {
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

					@Override
					public boolean renderSky(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
						VoidSkyRenderer.render(ticks, partialTick, poseStack, level, Minecraft.getInstance());
						return true;
					}
				}));
		busMod.addListener((Consumer<EntityRenderersEvent.AddLayers>) event -> {
			event.getSkins().forEach(renderer -> {
				LivingEntityRenderer<Player, EntityModel<Player>> skin = event.getSkin(renderer);
				attachRenderLayers(Objects.requireNonNull(skin));
			});
		});
	}

	private static <T extends LivingEntity, M extends EntityModel<T>> void attachRenderLayers(LivingEntityRenderer<T, M> renderer) {
		renderer.addLayer(new DonatorLayer<>(renderer));
	}

}
