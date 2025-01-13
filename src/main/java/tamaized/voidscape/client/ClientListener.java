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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix4f;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Config;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.particle.ParticleSpellCloud;
import tamaized.voidscape.client.shader.Shaders;
import tamaized.voidscape.client.ui.RenderTurmoil;
import tamaized.voidscape.network.DonatorHandler;
import tamaized.voidscape.network.server.ServerPacketHandlerDonatorSettings;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModParticles;

import javax.annotation.Nullable;
import java.util.Objects;

@Component
public class ClientListener {

	@PostConstruct
	private void init(IEventBus busMod, IEventBus busForge) {
		busMod.addListener(RegisterDimensionSpecialEffectsEvent.class, event -> event
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

		busMod.addListener(EntityRenderersEvent.AddLayers.class, event -> {
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
