package tamaized.voidscape.client.ui;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector3f;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ClientListener;
import tamaized.voidscape.client.ClientUtil;
import tamaized.voidscape.client.Shaders;
import tamaized.voidscape.client.StencilBufferUtil;
import tamaized.voidscape.client.ui.screen.IncapacitatedScreen;
import tamaized.voidscape.client.ui.screen.MainScreen;
import tamaized.voidscape.client.ui.screen.TurmoilScreen;
import tamaized.voidscape.turmoil.Insanity;
import tamaized.voidscape.turmoil.Progression;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;
import tamaized.voidscape.turmoil.TurmoilStats;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;
import tamaized.voidscape.turmoil.abilities.TurmoilAbilityInstance;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Voidscape.MODID)
public class RenderTurmoil {

	public static final int STENCIL_INDEX = 10;
	public static final ResourceLocation TEXTURE_MASK = new ResourceLocation(Voidscape.MODID, "textures/ui/mask.png");
	static final ResourceLocation TEXTURE_VOIDICINFUSION = new ResourceLocation(Voidscape.MODID, "textures/ui/voidicinfusion.png");
	static final ResourceLocation TEXTURE_WATCHINGYOU = new ResourceLocation(Voidscape.MODID, "textures/ui/watchingyou.png");
	private static float deltaTick;
	private static Boolean deltaPos = true;
	private static Turmoil.State lastState = Turmoil.State.CLOSED;
	private static float fadeTick = 0;
	private static ResourceLocation TEXTURE_GUI_ICONS_LOCATION_GRAYSCALE;

	public static void resetFade() {
		fadeTick = ClientUtil.tick + 10 * 30;
	}

	@SubscribeEvent
	public static void tick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START || Minecraft.getInstance().isPaused() || Minecraft.getInstance().level == null)
			return;
		ClientUtil.tick++;
		if (fadeTick <= 0)
			resetFade();
		if (Minecraft.getInstance().player != null)
			Minecraft.getInstance().player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> {
				cap.get(Voidscape.subCapTurmoilTracked).ifPresent(data -> {
					if (data.incapacitated) {
						if (Minecraft.getInstance().screen == null || (!Minecraft.getInstance().screen.isPauseScreen() && !(Minecraft.getInstance().screen instanceof IncapacitatedScreen)))
							Minecraft.getInstance().setScreen(new IncapacitatedScreen());
					}
				});
				cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
					if (data.getState() != Turmoil.State.CLOSED)
						lastState = data.getState();
					if (data.getState() == Turmoil.State.OPEN && Minecraft.getInstance().screen == null && data.getTick() >= data.getMaxTick())
						Minecraft.getInstance().setScreen(new MainScreen());
					if (data.getTick() <= 0)
						deltaTick = 0;
					else if (data.getTick() > deltaTick)
						deltaPos = true;
					else if (data.getTick() < deltaTick)
						deltaPos = false;
					if (deltaPos != null && data.getState() == Turmoil.State.CONSUME) {
						if (deltaPos)
							deltaTick++;
						else {
							deltaPos = null;
							deltaTick = data.getTick();
						}
					} else
						deltaTick = data.getTick();
				});
			});
	}

	@SubscribeEvent
	public static void render(RenderGameOverlayEvent.Post event) {
		Level world = Minecraft.getInstance().level;
		if (world != null && Minecraft.getInstance().player != null) {
			Minecraft.getInstance().player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				if (data.hasCoreSkill()) {
					cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> {
						if (event.getType() == RenderGameOverlayEvent.ElementType.LAYER && event instanceof RenderGameOverlayEvent.PostLayer layer) {
							if (layer.getOverlay() == ForgeIngameGui.HOTBAR_ELEMENT) {
								if (fadeTick <= 0 || fadeTick > ClientUtil.tick) {
									renderSpellBar(event.getMatrixStack(), 0, event.getPartialTicks());
									renderAbilityInstances(event.getMatrixStack(), stats, event.getPartialTicks());
									renderAbilityActivates(event.getMatrixStack(), event.getPartialTicks());
									renderAbilityToggle(event.getMatrixStack(), stats, event.getPartialTicks());
									renderAbilityCooldowns(event.getMatrixStack(), stats, event.getPartialTicks());
								}
							} else if (layer.getOverlay() == ForgeIngameGui.EXPERIENCE_BAR_ELEMENT) {
								int xOffset = 124;
								if (stats.getVoidicPower() < 1000) {
									ClientUtil.bindTexture(AbstractWidget.GUI_ICONS_LOCATION);
									Window window = event.getWindow();
									int x = window.getGuiScaledWidth() / 2 + xOffset + 2;
									int k = (int) (stats.getVoidicPower() / 1000F * 26F);
									int l = window.getGuiScaledHeight() - 25;
									RenderSystem.setShaderColor(0.5F, 0F, 1F, 1F);
									event.getMatrixStack().pushPose();
									event.getMatrixStack().translate(x, l, 0);
									event.getMatrixStack().mulPose(Vector3f.ZP.rotationDegrees(90));
									event.getMatrixStack().translate(-x, -l, 0);
									Minecraft.getInstance().gui.blit(event.getMatrixStack(), x, l, 0, 64, 25, 5);
									if (k > 0) {
										Minecraft.getInstance().gui.blit(event.getMatrixStack(), x + 26 - k, l, 26 - k, 69, k, 5);
									}
									event.getMatrixStack().popPose();
									RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

									String s = stats.getVoidicPower() + "";
									int i1 = (window.getGuiScaledWidth() - Minecraft.getInstance().gui.getFont().width(s)) / 2 + xOffset;
									int j1 = l - 8;
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) (i1 + 1), (float) j1, 0);
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) (i1 - 1), (float) j1, 0);
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) i1, (float) (j1 + 1), 0);
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) i1, (float) (j1 - 1), 0);
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) i1, (float) j1, 0x7700FF);
									xOffset += 26;
								}
								if (stats.getNullPower() > 0) {
									if (TEXTURE_GUI_ICONS_LOCATION_GRAYSCALE == null) {
										TEXTURE_GUI_ICONS_LOCATION_GRAYSCALE = new ResourceLocation(Voidscape.MODID, "texture_gui_icons_location_grayscale");
										try (Resource iresource = Minecraft.getInstance().getResourceManager().getResource(AbstractWidget.GUI_ICONS_LOCATION)) {
											NativeImage image = NativeImage.read(iresource.getInputStream());
											for (int x = 0; x < image.getWidth(); x++) {
												for (int y = 0; y < image.getHeight(); y++) {
													int pixel = image.getPixelRGBA(x, y);
													int L = (int) (0.299D * ((pixel) & 0xFF) + 0.587D * ((pixel >> 8) & 0xFF) + 0.114D * ((pixel >> 16) & 0xFF));
													image.setPixelRGBA(x, y, NativeImage.combine((pixel >> 24) & 0xFF, L, L, L));
												}
											}
											Minecraft.getInstance().getTextureManager().register(TEXTURE_GUI_ICONS_LOCATION_GRAYSCALE, new AbstractTexture() {
												@Override
												public void load(@Nonnull ResourceManager manager) {
													TextureUtil.prepareImage(this.getId(), 0, image.getWidth(), image.getHeight());
													image.upload(0, 0, 0, 0, 0, image.getWidth(), image.getHeight(), false, false, false, true);
												}
											});
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
									ClientUtil.bindTexture(TEXTURE_GUI_ICONS_LOCATION_GRAYSCALE);
									Window window = event.getWindow();
									int x = window.getGuiScaledWidth() / 2 + xOffset + 2;
									int k = (int) (stats.getNullPower() / 1000F * 26F);
									int l = window.getGuiScaledHeight() - 25;
									RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
									event.getMatrixStack().pushPose();
									event.getMatrixStack().translate(x, l, 0);
									event.getMatrixStack().mulPose(Vector3f.ZP.rotationDegrees(90));
									event.getMatrixStack().translate(-x, -l, 0);
									Minecraft.getInstance().gui.blit(event.getMatrixStack(), x, l, 0, 64, 25, 5);
									if (k > 0) {
										Minecraft.getInstance().gui.blit(event.getMatrixStack(), x + 26 - k, l, 26 - k, 69, k, 5);
									}
									event.getMatrixStack().popPose();
									RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

									String s = stats.getNullPower() + "";
									int i1 = (window.getGuiScaledWidth() - Minecraft.getInstance().gui.getFont().width(s)) / 2 + xOffset;
									int j1 = l - 8;
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) (i1 + 1), (float) j1, 0);
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) (i1 - 1), (float) j1, 0);
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) i1, (float) (j1 + 1), 0);
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) i1, (float) (j1 - 1), 0);
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) i1, (float) j1, 0xFFFFFF);
									xOffset += 26;
								}
								if (stats.getInsanePower() > 0) {
									ClientUtil.bindTexture(AbstractWidget.GUI_ICONS_LOCATION);
									Window window = event.getWindow();
									int x = window.getGuiScaledWidth() / 2 + xOffset + 2;
									int k = (int) (stats.getInsanePower() / 1000F * 26F);
									int l = window.getGuiScaledHeight() - 25;
									RenderSystem.setShaderColor(1F, 0F, 0F, 1F);
									event.getMatrixStack().pushPose();
									event.getMatrixStack().translate(x, l, 0);
									event.getMatrixStack().mulPose(Vector3f.ZP.rotationDegrees(90));
									event.getMatrixStack().translate(-x, -l, 0);
									Minecraft.getInstance().gui.blit(event.getMatrixStack(), x, l, 0, 64, 25, 5);
									if (k > 0) {
										Minecraft.getInstance().gui.blit(event.getMatrixStack(), x + 26 - k, l, 26 - k, 69, k, 5);
									}
									event.getMatrixStack().popPose();
									RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

									String s = stats.getInsanePower() + "";
									int i1 = (window.getGuiScaledWidth() - Minecraft.getInstance().gui.getFont().width(s)) / 2 + xOffset;
									int j1 = l - 8;
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) (i1 + 1), (float) j1, 0);
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) (i1 - 1), (float) j1, 0);
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) i1, (float) (j1 + 1), 0);
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) i1, (float) (j1 - 1), 0);
									Minecraft.getInstance().gui.getFont().draw(event.getMatrixStack(), s, (float) i1, (float) j1, 0xFF0000);
								}
							}
						}
					});
				}
				if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
					return;
				cap.get(Voidscape.subCapInsanity).ifPresent(insanity -> renderInsanity(data, insanity, event.getMatrixStack(), event.getPartialTicks()));
				float perc = Mth.clamp(

						(deltaTick + (deltaPos == null ?

								data.getState() == Turmoil.State.CONSUME ? -0.01F : 0 :

								deltaPos ? 1F - event.getPartialTicks() : event.getPartialTicks())


						) / data.getMaxTick(),

						0F, 1F);
				if (perc > 0) {
					RenderSystem.enableBlend();
					{

						Window window = Minecraft.getInstance().getWindow();

						float x = 0F;
						float y = 0F;
						float w = window.getGuiScaledWidth();
						float h = window.getGuiScaledHeight();
						float z = 401F; // Catch All

						ClientUtil.bindTexture(TEXTURE_MASK);
						Color24.INSTANCE.set(1F, 1F, 1F, 1F).apply(true, x, y, z, w, h);
						StencilBufferUtil.setup(STENCIL_INDEX, () -> Shaders.OPTIMAL_ALPHA_LESSTHAN_POS_TEX_COLOR.invokeThenEndTesselator(perc));

						Color24.INSTANCE.set(0F, 0F, 0F, 1F).apply(false, x, y, z, w, h);
						StencilBufferUtil.renderAndFlush(STENCIL_INDEX, () -> Shaders.WRAPPED_POS_COLOR.invokeThenEndTesselator());
					}
					RenderSystem.disableBlend();
				}
			}));
		}
		if (!(Minecraft.getInstance().screen instanceof TurmoilScreen))
			OverlayMessageHandler.render(event.getMatrixStack(), event.getPartialTicks());
	}

	private static void renderInsanity(Turmoil data, Insanity insanity, PoseStack matrixStack, float partialTicks) {
		renderInfusion(insanity, matrixStack, partialTicks);
		renderParanoia(data, insanity, matrixStack, partialTicks);
	}

	private static void renderParanoia(Turmoil data, Insanity insanity, PoseStack matrixStack, float partialTicks) {
		if (insanity.getParanoia() < 500F || data.getProgression().ordinal() >= Progression.CorruptPawnPost.ordinal())
			return;
		float perc = (insanity.getParanoia() - 500F) / 90F;
		perc = Mth.clamp(perc, 0, 1);
		perc *= 0.25F;
		float endPerc = (insanity.getParanoia() - 590F) / 10F;
		endPerc = Mth.clamp(endPerc, 0, 1);
		endPerc *= 0.25F;
		perc += endPerc;
		BufferBuilder buffer = Tesselator.getInstance().getBuilder();
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		ClientUtil.bindTexture(TEXTURE_WATCHINGYOU);
		final float w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		final float h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		buffer.vertex(0, h, 0).uv(0, 1).color(1F, 1F, 1F, perc).endVertex();
		buffer.vertex(w, h, 0).uv(1, 1).color(1F, 1F, 1F, perc).endVertex();
		buffer.vertex(w, 0, 0).uv(1, 0).color(1F, 1F, 1F, perc).endVertex();
		buffer.vertex(0, 0, 0).uv(0, 0).color(1F, 1F, 1F, perc).endVertex();
		RenderSystem.enableBlend();
		Shaders.OPTIMAL_ALPHA_GREATERTHAN_POS_TEX_COLOR.invokeThenEndTesselator(0F);
		RenderSystem.disableBlend();
	}

	private static void renderInfusion(Insanity insanity, PoseStack matrixStack, float partialTicks) {
		if (insanity.getInfusion() <= 0)
			return;
		float perc = insanity.getInfusion() / 600F;
		perc = Mth.clamp(perc, 0, 1);
		BufferBuilder buffer = Tesselator.getInstance().getBuilder();
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		ClientUtil.bindTexture(TEXTURE_VOIDICINFUSION);
		final float w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		final float h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		buffer.vertex(0, h, 0).uv(0, 1).color(0.4F, 0F, 1F, perc).endVertex();
		buffer.vertex(w, h, 0).uv(1, 1).color(0.4F, 0F, 1F, perc).endVertex();
		buffer.vertex(w, 0, 0).uv(1, 0).color(0.4F, 0F, 1F, perc).endVertex();
		buffer.vertex(0, 0, 0).uv(0, 0).color(0.4F, 0F, 1F, perc).endVertex();
		RenderSystem.enableBlend();
		Shaders.OPTIMAL_ALPHA_GREATERTHAN_POS_TEX_COLOR.invokeThenEndTesselator(0F);
		RenderSystem.disableBlend();
	}

	private static float fade(float base, float partialTicks) {
		return fadeTick <= 0 || fadeTick - ClientUtil.tick > 20 * 5 ? base : Math.max(0, base * ((fadeTick - (ClientUtil.tick + partialTicks)) / (20F * 5F)));
	}

	public static void renderSpellBar(PoseStack matrixStack, int z, float partialTicks) {
		ClientUtil.bindTexture(AbstractWidget.WIDGETS_LOCATION);
		int w = 61;
		int h = 22;
		int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() - w - 2;
		int y = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - ((h - 2) / 2 * 3);
		Minecraft.getInstance().gui.setBlitOffset(-90 + z);
		float alpha = z != 0 ? 0.25F : fade(0.25F, partialTicks);
		RenderSystem.setShaderColor(1F, 1F, 1F, alpha);
		RenderSystem.enableBlend();
		Shaders.OPTIMAL_ALPHA_GREATERTHAN_POS_TEX.invokeThenClear(0F, () -> {
			for (int i = 0; i < 3; i++)
				BlitWithoutShader.blit(matrixStack, x, y + (h - 2) * i, 0, 1, w, h);
		});
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
	}

	private static void renderAbilityInstances(PoseStack stack, TurmoilStats stats, float partialTicks) {
		int s = 16;
		int ox = Minecraft.getInstance().getWindow().getGuiScaledWidth() - (20) * 3;
		int oy = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - (28);
		for (int i = 0; i < 9; i++) {
			TurmoilAbilityInstance instance = stats.getAbility(i);
			if (instance == null)
				continue;
			int x = ox + (20) * (i % 3);
			int y = oy + (20) * (i / 3);
			ClientUtil.bindTexture(instance.ability().getTexture());
			BufferBuilder buffer = Tesselator.getInstance().getBuilder();
			buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
			buffer.vertex(x, y, 0F).uv(0, 0).endVertex();
			buffer.vertex(x, y + s, 0F).uv(0, 1).endVertex();
			buffer.vertex(x + s, y + s, 0F).uv(1, 1).endVertex();
			buffer.vertex(x + s, y, 0F).uv(1, 0).endVertex();
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1F, 1F, 1F, fade(1F, partialTicks));
			Shaders.OPTIMAL_ALPHA_GREATERTHAN_POS_TEX_COLOR.invokeThenEndTesselator(0F);
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			RenderSystem.disableBlend();
		}
	}

	private static void renderAbilityActivates(PoseStack stack, float partialTicks) {
		int s = 16;
		int ox = Minecraft.getInstance().getWindow().getGuiScaledWidth() - (20) * 3;
		int oy = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - (28);
		List<KeyMapping> list = ClientListener.getAbilityKeys();
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).isDown())
				continue;
			int x = ox + (20) * (i % 3);
			int y = oy + (20) * (i / 3);
			BufferBuilder buffer = Tesselator.getInstance().getBuilder();
			buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
			buffer.vertex(x, y, 0F).color(0F, 1F, 1F, 0F).endVertex();
			buffer.vertex(x, y + s, 0F).color(0F, 1F, 1F, 1F).endVertex();
			buffer.vertex(x + s, y + s, 0F).color(0F, 1F, 1F, 1F).endVertex();
			buffer.vertex(x + s, y, 0F).color(0F, 1F, 1F, 0F).endVertex();
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1F, 1F, 1F, fade(1F, partialTicks));
			Shaders.OPTIMAL_ALPHA_GREATERTHAN_POS_COLOR.invokeThenEndTesselator(0F);
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			RenderSystem.disableBlend();
		}
	}

	private static void renderAbilityCooldowns(PoseStack stack, TurmoilStats stats, float partialTicks) {
		if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null)
			return;
		int s = 16;
		int ox = Minecraft.getInstance().getWindow().getGuiScaledWidth() - (20) * 3;
		int oy = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - (28);
		for (int i = 0; i < 9; i++) {
			TurmoilAbilityInstance instance = stats.getAbility(i);
			if (instance == null)
				continue;
			float perc = instance.canAfford(Minecraft.getInstance().player) ? instance.cooldownPercent(Minecraft.getInstance().level) : 1F;
			boolean toggle = false;
			if (perc == 0 && instance.ability().toggle() != TurmoilAbility.Toggle.None && (toggle = stats.isActive(instance.ability().toggle())) && !stats.isActive(instance.ability()))
				perc = 1F;
			if (perc <= 0)
				continue;
			int x = ox + (20) * (i % 3);
			int y = oy + (20) * (i / 3);
			float offset = y + s * (1F - perc);
			BufferBuilder buffer = Tesselator.getInstance().getBuilder();
			buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
			final float alpha = 0.75F * fade(1F, partialTicks);
			buffer.vertex(x, offset, 0F).color(1F, 0.1F, 0F, alpha).endVertex();
			buffer.vertex(x, y + s, 0F).color(1F, 0.1F, 0F, alpha).endVertex();
			buffer.vertex(x + s, y + s, 0F).color(1F, 0.1F, 0F, alpha).endVertex();
			buffer.vertex(x + s, offset, 0F).color(1F, 0.1F, 0F, alpha).endVertex();
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1F, 1F, 1F, fade(1F, partialTicks));
			Shaders.OPTIMAL_ALPHA_GREATERTHAN_POS_COLOR.invokeThenEndTesselator(0F);
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			RenderSystem.disableBlend();
			RenderSystem.enableBlend();
			//			RenderSystem.alphaFunc(GL11.GL_GREATER, 0F); // FIXME: alphaFunc on text doesnt look possible, is this even needed here?
			RenderSystem.setShaderColor(1F, 1F, 1F, fade(1F, partialTicks));
			if (!toggle) {
				boolean flag;
				String text = (flag = instance.cooldownRemaining(Minecraft.getInstance().level) > 0) ? String.valueOf(instance.cooldownRemaining(Minecraft.getInstance().level) / 20) : String.valueOf(instance.getCalcCost(stats));
				Minecraft.getInstance().font.drawShadow(stack, text, x + s / 2F - Minecraft.getInstance().font.width(text) / 2F, y + s / 2F - Minecraft.getInstance().font.lineHeight / 2F, (Math.max(0x04, (int) (fade(1F, partialTicks) * 0xFF)) << 24) | (flag ? 0xFFFF00 : instance.ability().
						costType() == TurmoilAbility.Type.Insane ? 0xFF0000 : instance.ability().costType() == TurmoilAbility.Type.Null ? 0xFFFFFF : 0x7700FF));
			}
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			//			RenderSystem.defaultAlphaFunc();
			RenderSystem.disableBlend();
		}
	}

	private static void renderAbilityToggle(PoseStack stack, TurmoilStats stats, float partialTicks) {
		int s = 16;
		int ox = Minecraft.getInstance().getWindow().getGuiScaledWidth() - (20) * 3;
		int oy = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - (28);
		for (int i = 0; i < 9; i++) {
			TurmoilAbilityInstance instance = stats.getAbility(i);
			if (instance == null || !stats.isActive(instance.ability()))
				continue;
			int x = ox + (20) * (i % 3);
			int y = oy + (20) * (i / 3);
			BufferBuilder buffer = Tesselator.getInstance().getBuilder();
			buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
			buffer.vertex(x, y, 0F).color(1F, 1F, 0F, 1F).endVertex();
			buffer.vertex(x, y + s, 0F).color(1F, 1F, 0F, 0F).endVertex();
			buffer.vertex(x + s, y + s, 0F).color(1F, 1F, 0F, 0F).endVertex();
			buffer.vertex(x + s, y, 0F).color(1F, 1F, 0F, 1F).endVertex();
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1F, 1F, 1F, fade(1F, partialTicks));
			Shaders.OPTIMAL_ALPHA_GREATERTHAN_POS_COLOR.invokeThenEndTesselator(0F);
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			RenderSystem.disableBlend();
		}
	}

	public static class Color24 {

		public static final Color24 INSTANCE = new Color24();

		public int bit24;
		public int bit16;
		public int bit8;
		public int bit0;

		private Color24() {}

		public static float asFloat(int value) {
			return value / 255F;
		}

		public static int asInt(float value) {
			return (int) (value * 255);
		}

		public int packed() {
			return (bit24 << 24) | (bit16 << 16) | (bit8 << 8) | bit0;
		}

		public Color24 unpack(int packed) {
			return set(

					(packed >> 24) & 0xFF,

					(packed >> 16) & 0xFF,

					(packed >> 8) & 0xFF,

					packed & 0xFF

			);
		}

		public Color24 apply(boolean tex, float x, float y, float z, float w, float h) {
			BufferBuilder buffer = Tesselator.getInstance().getBuilder();
			buffer.begin(VertexFormat.Mode.QUADS, tex ? DefaultVertexFormat.POSITION_TEX_COLOR : DefaultVertexFormat.POSITION_COLOR);
			final float r = asFloat(bit24);
			final float g = asFloat(bit16);
			final float b = asFloat(bit8);
			final float a = asFloat(bit0);
			buffer.vertex(x, y + h, z);
			if (tex)
				buffer.uv(0F, 1F);
			buffer.color(r, g, b, a).endVertex();
			buffer.vertex(x + w, y + h, z);
			if (tex)
				buffer.uv(1F, 1F);
			buffer.color(r, g, b, a).endVertex();
			buffer.vertex(x + w, y, z);
			if (tex)
				buffer.uv(1F, 0F);
			buffer.color(r, g, b, a).endVertex();
			buffer.vertex(x, y, z);
			if (tex)
				buffer.uv(0F, 0F);
			buffer.color(r, g, b, a).endVertex();
			return this;
		}

		public void endTesselator() {
			Tesselator.getInstance().end();
		}

		public Color24 set(int b24, int b16, int b8, int b0) {
			bit0 = b0;
			bit8 = b8;
			bit16 = b16;
			bit24 = b24;
			return this;
		}

		public Color24 set(float b24, float b16, float b8, float b0) {
			set(asInt(b24), asInt(b16), asInt(b8), asInt(b0));
			return this;
		}

	}

}
