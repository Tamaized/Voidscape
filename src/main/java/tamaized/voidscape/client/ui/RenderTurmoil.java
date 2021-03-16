package tamaized.voidscape.client.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ClientListener;
import tamaized.voidscape.client.ClientUtil;
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

import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Voidscape.MODID)
public class RenderTurmoil {

	public static final int STENCIL_INDEX = 10;
	public static final ResourceLocation TEXTURE_MASK = new ResourceLocation(Voidscape.MODID, "textures/ui/mask.png");
	public static final Color24 colorHolder = new Color24();
	static final ResourceLocation TEXTURE_VOIDICINFUSION = new ResourceLocation(Voidscape.MODID, "textures/ui/voidicinfusion.png");
	static final ResourceLocation TEXTURE_WATCHINGYOU = new ResourceLocation(Voidscape.MODID, "textures/ui/watchingyou.png");
	private static float deltaTick;
	private static Boolean deltaPos;
	private static Turmoil.State lastState = Turmoil.State.CLOSED;
	private static float fadeTick = 0;

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
		World world = Minecraft.getInstance().level;
		if (world != null && Minecraft.getInstance().player != null) {
			Minecraft.getInstance().player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				if (data.hasCoreSkill()) {
					cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> {
						switch (event.getType()) {
							case HOTBAR: {
								if (fadeTick <= 0 || fadeTick > ClientUtil.tick) {
									renderSpellBar(event.getMatrixStack(), 0, event.getPartialTicks());
									renderAbilityInstances(event.getMatrixStack(), stats, event.getPartialTicks());
									renderAbilityActivates(event.getMatrixStack(), event.getPartialTicks());
									renderAbilityToggle(event.getMatrixStack(), stats, event.getPartialTicks());
									renderAbilityCooldowns(event.getMatrixStack(), stats, event.getPartialTicks());
								}
							}
							break;
							case EXPERIENCE: {
								int xOffset = 124;
								if (stats.getVoidicPower() < 1000) {
									Minecraft.getInstance().getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
									MainWindow window = event.getWindow();
									int x = window.getGuiScaledWidth() / 2 + xOffset + 2;
									int k = (int) (stats.getVoidicPower() / 1000F * 26F);
									int l = window.getGuiScaledHeight() - 25;
									RenderSystem.color4f(0.5F, 0F, 1F, 1F);
									event.getMatrixStack().pushPose();
									event.getMatrixStack().translate(x, l, 0);
									event.getMatrixStack().mulPose(Vector3f.ZP.rotationDegrees(90));
									event.getMatrixStack().translate(-x, -l, 0);
									Minecraft.getInstance().gui.blit(event.getMatrixStack(), x, l, 0, 64, 25, 5);
									if (k > 0) {
										Minecraft.getInstance().gui.blit(event.getMatrixStack(), x + 26 - k, l, 26 - k, 69, k, 5);
									}
									event.getMatrixStack().popPose();
									RenderSystem.color4f(1F, 1F, 1F, 1F);

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
									Minecraft.getInstance().getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
									MainWindow window = event.getWindow();
									int x = window.getGuiScaledWidth() / 2 + xOffset + 2;
									int k = (int) (stats.getNullPower() / 1000F * 26F);
									int l = window.getGuiScaledHeight() - 25;
									RenderSystem.color4f(1F, 1F, 1F, 1F);
									event.getMatrixStack().pushPose();
									event.getMatrixStack().translate(x, l, 0);
									event.getMatrixStack().mulPose(Vector3f.ZP.rotationDegrees(90));
									event.getMatrixStack().translate(-x, -l, 0);
									Minecraft.getInstance().gui.blit(event.getMatrixStack(), x, l, 0, 64, 25, 5);
									if (k > 0) {
										Minecraft.getInstance().gui.blit(event.getMatrixStack(), x + 26 - k, l, 26 - k, 69, k, 5);
									}
									event.getMatrixStack().popPose();
									RenderSystem.color4f(1F, 1F, 1F, 1F);

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
									Minecraft.getInstance().getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
									MainWindow window = event.getWindow();
									int x = window.getGuiScaledWidth() / 2 + xOffset + 2;
									int k = (int) (stats.getInsanePower() / 1000F * 26F);
									int l = window.getGuiScaledHeight() - 25;
									RenderSystem.color4f(1F, 0F, 0F, 1F);
									event.getMatrixStack().pushPose();
									event.getMatrixStack().translate(x, l, 0);
									event.getMatrixStack().mulPose(Vector3f.ZP.rotationDegrees(90));
									event.getMatrixStack().translate(-x, -l, 0);
									Minecraft.getInstance().gui.blit(event.getMatrixStack(), x, l, 0, 64, 25, 5);
									if (k > 0) {
										Minecraft.getInstance().gui.blit(event.getMatrixStack(), x + 26 - k, l, 26 - k, 69, k, 5);
									}
									event.getMatrixStack().popPose();
									RenderSystem.color4f(1F, 1F, 1F, 1F);

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
							break;
							default:
								break;
						}
					});
				}
				if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
					return;
				cap.get(Voidscape.subCapInsanity).ifPresent(insanity -> renderInsanity(data, insanity, event.getMatrixStack(), event.getPartialTicks()));
				float perc = MathHelper.clamp(

						(deltaTick + (deltaPos == null ?

								data.getState() == Turmoil.State.CONSUME ? -0.01F : 0 :

								deltaPos ? 1F - event.getPartialTicks() : event.getPartialTicks())


						) / data.getMaxTick(),

						0F, 1F);
				if (perc > 0) {
					RenderSystem.enableBlend();
					RenderSystem.enableAlphaTest();
					{

						BufferBuilder buffer = Tessellator.getInstance().getBuilder();
						buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);

						MainWindow window = Minecraft.getInstance().getWindow();

						float x = 0F;
						float y = 0F;
						float w = window.getGuiScaledWidth();
						float h = window.getGuiScaledHeight();
						float z = 401F; // Catch All

						Consumer<Color24> verticies = color -> {
							final float r = Color24.asFloat(color.bit24);
							final float g = Color24.asFloat(color.bit16);
							final float b = Color24.asFloat(color.bit8);
							final float a = Color24.asFloat(color.bit0);
							buffer.vertex(x, y + h, z).color(r, g, b, a).uv(0F, 1F).endVertex();
							buffer.vertex(x + w, y + h, z).color(r, g, b, a).uv(1F, 1F).endVertex();
							buffer.vertex(x + w, y, z).color(r, g, b, a).uv(1F, 0F).endVertex();
							buffer.vertex(x, y, z).color(r, g, b, a).uv(0F, 0F).endVertex();
						};
						verticies.accept(colorHolder.set(1F, 1F, 1F, 1F));

						Minecraft.getInstance().getTextureManager().bind(TEXTURE_MASK);

						StencilBufferUtil.setup(STENCIL_INDEX, () -> {
							RenderSystem.alphaFunc(GL11.GL_LESS, perc);
							Tessellator.getInstance().end();
							RenderSystem.defaultAlphaFunc();
						});

						buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
						verticies.accept(colorHolder.set(0F, 0F, 0F, 1F));

						StencilBufferUtil.render(STENCIL_INDEX, () -> {
							RenderSystem.disableTexture();
							Tessellator.getInstance().end();
							RenderSystem.enableTexture();
						}, true);
					}
					RenderSystem.disableAlphaTest();
					RenderSystem.disableBlend();
				}
			}));
		}
		if (!(Minecraft.getInstance().screen instanceof TurmoilScreen))
			OverlayMessageHandler.render(event.getMatrixStack(), event.getPartialTicks());
	}

	private static void renderInsanity(Turmoil data, Insanity insanity, MatrixStack matrixStack, float partialTicks) {
		renderInfusion(insanity, matrixStack, partialTicks);
		renderParanoia(data, insanity, matrixStack, partialTicks);
	}

	private static void renderParanoia(Turmoil data, Insanity insanity, MatrixStack matrixStack, float partialTicks) {
		if (insanity.getParanoia() < 500F || data.getProgression().ordinal() >= Progression.CorruptPawnPost.ordinal())
			return;
		float perc = (insanity.getParanoia() - 500F) / 90F;
		perc = MathHelper.clamp(perc, 0, 1);
		perc *= 0.25F;
		float endPerc = (insanity.getParanoia() - 590F) / 10F;
		endPerc = MathHelper.clamp(endPerc, 0, 1);
		endPerc *= 0.25F;
		perc += endPerc;
		BufferBuilder buffer = Tessellator.getInstance().getBuilder();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
		Minecraft.getInstance().getTextureManager().bind(TEXTURE_WATCHINGYOU);
		final float w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		final float h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		buffer.vertex(0, h, 0).color(1F, 1F, 1F, perc).uv(0, 1).endVertex();
		buffer.vertex(w, h, 0).color(1F, 1F, 1F, perc).uv(1, 1).endVertex();
		buffer.vertex(w, 0, 0).color(1F, 1F, 1F, perc).uv(1, 0).endVertex();
		buffer.vertex(0, 0, 0).color(1F, 1F, 1F, perc).uv(0, 0).endVertex();
		RenderSystem.enableBlend();
		RenderSystem.alphaFunc(GL11.GL_GREATER, 0F);
		Tessellator.getInstance().end();
		RenderSystem.defaultAlphaFunc();
		RenderSystem.disableBlend();
	}

	private static void renderInfusion(Insanity insanity, MatrixStack matrixStack, float partialTicks) {
		if (insanity.getInfusion() <= 0)
			return;
		float perc = insanity.getInfusion() / 600F;
		perc = MathHelper.clamp(perc, 0, 1);
		BufferBuilder buffer = Tessellator.getInstance().getBuilder();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
		Minecraft.getInstance().getTextureManager().bind(TEXTURE_VOIDICINFUSION);
		final float w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		final float h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		buffer.vertex(0, h, 0).color(0.4F, 0F, 1F, perc).uv(0, 1).endVertex();
		buffer.vertex(w, h, 0).color(0.4F, 0F, 1F, perc).uv(1, 1).endVertex();
		buffer.vertex(w, 0, 0).color(0.4F, 0F, 1F, perc).uv(1, 0).endVertex();
		buffer.vertex(0, 0, 0).color(0.4F, 0F, 1F, perc).uv(0, 0).endVertex();
		RenderSystem.enableBlend();
		RenderSystem.alphaFunc(GL11.GL_GREATER, 0F);
		Tessellator.getInstance().end();
		RenderSystem.defaultAlphaFunc();
		RenderSystem.disableBlend();
	}

	private static float fade(float base, float partialTicks) {
		return fadeTick <= 0 || fadeTick - ClientUtil.tick > 20 * 5 ? base : Math.max(0, base * ((fadeTick - (ClientUtil.tick + partialTicks)) / (20F * 5F)));
	}

	public static void renderSpellBar(MatrixStack matrixStack, int z, float partialTicks) {
		Minecraft.getInstance().getTextureManager().bind(HackyInGameGUIAccessor.WIDGETS_LOCATION());
		int w = 61;
		int h = 22;
		int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() - w - 2;
		int y = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - ((h - 2) / 2 * 3);
		Minecraft.getInstance().gui.setBlitOffset(-90 + z);
		float alpha = z != 0 ? 0.25F : fade(0.25F, partialTicks);
		RenderSystem.color4f(1F, 1F, 1F, alpha);
		RenderSystem.enableBlend();
		RenderSystem.alphaFunc(GL11.GL_GREATER, 0F);
		for (int i = 0; i < 3; i++)
			Minecraft.getInstance().gui.blit(matrixStack, x, y + (h - 2) * i, 0, 1, w, h);
		RenderSystem.defaultAlphaFunc();
		RenderSystem.disableBlend();
		RenderSystem.color4f(1F, 1F, 1F, 1F);
	}

	private static void renderAbilityInstances(MatrixStack stack, TurmoilStats stats, float partialTicks) {
		int s = 16;
		int ox = Minecraft.getInstance().getWindow().getGuiScaledWidth() - (20) * 3;
		int oy = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - (28);
		for (int i = 0; i < 9; i++) {
			TurmoilAbilityInstance instance = stats.getAbility(i);
			if (instance == null)
				continue;
			int x = ox + (20) * (i % 3);
			int y = oy + (20) * (i / 3);
			Minecraft.getInstance().getTextureManager().bind(instance.ability().getTexture());
			BufferBuilder buffer = Tessellator.getInstance().getBuilder();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			buffer.vertex(x, y, 0F).uv(0, 0).endVertex();
			buffer.vertex(x, y + s, 0F).uv(0, 1).endVertex();
			buffer.vertex(x + s, y + s, 0F).uv(1, 1).endVertex();
			buffer.vertex(x + s, y, 0F).uv(1, 0).endVertex();
			RenderSystem.enableBlend();
			RenderSystem.alphaFunc(GL11.GL_GREATER, 0F);
			RenderSystem.color4f(1F, 1F, 1F, fade(1F, partialTicks));
			Tessellator.getInstance().end();
			RenderSystem.color4f(1F, 1F, 1F, 1F);
			RenderSystem.defaultAlphaFunc();
			RenderSystem.disableBlend();
		}
	}

	private static void renderAbilityActivates(MatrixStack stack, float partialTicks) {
		int s = 16;
		int ox = Minecraft.getInstance().getWindow().getGuiScaledWidth() - (20) * 3;
		int oy = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - (28);
		List<KeyBinding> list = ClientListener.getAbilityKeys();
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).isDown())
				continue;
			int x = ox + (20) * (i % 3);
			int y = oy + (20) * (i / 3);
			BufferBuilder buffer = Tessellator.getInstance().getBuilder();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			buffer.vertex(x, y, 0F).color(0F, 1F, 1F, 0F).endVertex();
			buffer.vertex(x, y + s, 0F).color(0F, 1F, 1F, 1F).endVertex();
			buffer.vertex(x + s, y + s, 0F).color(0F, 1F, 1F, 1F).endVertex();
			buffer.vertex(x + s, y, 0F).color(0F, 1F, 1F, 0F).endVertex();
			RenderSystem.enableBlend();
			RenderSystem.disableTexture();
			RenderSystem.shadeModel(GL11.GL_SMOOTH);
			RenderSystem.alphaFunc(GL11.GL_GREATER, 0F);
			RenderSystem.color4f(1F, 1F, 1F, fade(1F, partialTicks));
			Tessellator.getInstance().end();
			RenderSystem.color4f(1F, 1F, 1F, 1F);
			RenderSystem.defaultAlphaFunc();
			RenderSystem.shadeModel(GL11.GL_FLAT);
			RenderSystem.enableTexture();
			RenderSystem.disableBlend();
		}
	}

	private static void renderAbilityCooldowns(MatrixStack stack, TurmoilStats stats, float partialTicks) {
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
			Minecraft.getInstance().getTextureManager().bind(instance.ability().getTexture());
			BufferBuilder buffer = Tessellator.getInstance().getBuilder();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			buffer.vertex(x, offset, 0F).color(1F, 0.1F, 0F, 0.75F).endVertex();
			buffer.vertex(x, y + s, 0F).color(1F, 0.1F, 0F, 0.75F).endVertex();
			buffer.vertex(x + s, y + s, 0F).color(1F, 0.1F, 0F, 0.75F).endVertex();
			buffer.vertex(x + s, offset, 0F).color(1F, 0.1F, 0F, 0.75F).endVertex();
			RenderSystem.enableBlend();
			RenderSystem.disableTexture();
			Tessellator.getInstance().end();
			RenderSystem.enableTexture();
			RenderSystem.disableBlend();
			RenderSystem.enableBlend();
			RenderSystem.alphaFunc(GL11.GL_GREATER, 0F);
			RenderSystem.color4f(1F, 1F, 1F, fade(1F, partialTicks));
			if (!toggle) {
				boolean flag;
				String text = (flag = instance.cooldownRemaining(Minecraft.getInstance().level) > 0) ? String.valueOf(instance.cooldownRemaining(Minecraft.getInstance().level) / 20) : String.valueOf(instance.getCalcCost(stats));
				Minecraft.getInstance().font.drawShadow(stack, text, x + s / 2F - Minecraft.getInstance().font.width(text) / 2F, y + s / 2F - Minecraft.getInstance().font.lineHeight / 2F, flag ? 0xFFFFFF00 : 0xFF7700FF);
			}
			RenderSystem.color4f(1F, 1F, 1F, 1F);
			RenderSystem.defaultAlphaFunc();
			RenderSystem.disableBlend();
		}
	}

	private static void renderAbilityToggle(MatrixStack stack, TurmoilStats stats, float partialTicks) {
		int s = 16;
		int ox = Minecraft.getInstance().getWindow().getGuiScaledWidth() - (20) * 3;
		int oy = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - (28);
		for (int i = 0; i < 9; i++) {
			TurmoilAbilityInstance instance = stats.getAbility(i);
			if (instance == null || !stats.isActive(instance.ability()))
				continue;
			int x = ox + (20) * (i % 3);
			int y = oy + (20) * (i / 3);
			BufferBuilder buffer = Tessellator.getInstance().getBuilder();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			buffer.vertex(x, y, 0F).color(1F, 1F, 0F, 1F).endVertex();
			buffer.vertex(x, y + s, 0F).color(1F, 1F, 0F, 0F).endVertex();
			buffer.vertex(x + s, y + s, 0F).color(1F, 1F, 0F, 0F).endVertex();
			buffer.vertex(x + s, y, 0F).color(1F, 1F, 0F, 1F).endVertex();
			RenderSystem.enableBlend();
			RenderSystem.disableTexture();
			RenderSystem.shadeModel(GL11.GL_SMOOTH);
			RenderSystem.alphaFunc(GL11.GL_GREATER, 0F);
			RenderSystem.color4f(1F, 1F, 1F, fade(1F, partialTicks));
			Tessellator.getInstance().end();
			RenderSystem.color4f(1F, 1F, 1F, 1F);
			RenderSystem.defaultAlphaFunc();
			RenderSystem.shadeModel(GL11.GL_FLAT);
			RenderSystem.enableTexture();
			RenderSystem.disableBlend();
		}
	}

	public static class Color24 {

		public int bit24;
		public int bit16;
		public int bit8;
		public int bit0;

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
