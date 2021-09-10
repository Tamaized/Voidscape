package tamaized.voidscape.client.ui.screen;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ClientUtil;
import tamaized.voidscape.network.server.ServerPacketCommenceDuty;
import tamaized.voidscape.network.server.ServerPacketDisbandParty;
import tamaized.voidscape.network.server.ServerPacketRemovePartyMember;
import tamaized.voidscape.network.server.ServerPacketRequestPartyInfo;
import tamaized.voidscape.network.server.ServerPacketSetPartyPassword;
import tamaized.voidscape.party.ClientPartyInfo;
import tamaized.voidscape.turmoil.Duties;
import tamaized.voidscape.world.Instance;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FormPartyScreen extends TurmoilScreen {

	private static final int buttonWidth = 180;
	private static final int buttonHeight = 20;
	private static final int spacingHeight = (int) (buttonHeight * 1.5F);
	private final Duties.Duty duty;
	private final Instance.InstanceType type;
	private String oldPassword = "";
	private String password = "";
	private Button commence;
	private EditBox passwordWidget;
	private List<Button> kickMemberButtons = new ArrayList<>();

	public FormPartyScreen(Duties.Duty duty, Instance.InstanceType type) {
		super(new TranslatableComponent(Voidscape.MODID.concat(".screen.form")));
		this.duty = duty;
		this.type = type;
	}

	private static void quad(BufferBuilder buffer, float x, float y, float z, float w, float h, float r, float g, float b, float a) {
		buffer.vertex(x, y + h, z).color(r, g, b, a).uv(0F, 1F).endVertex();
		buffer.vertex(x + w, y + h, z).color(r, g, b, a).uv(1F, 1F).endVertex();
		buffer.vertex(x + w, y, z).color(r, g, b, a).uv(1F, 0F).endVertex();
		buffer.vertex(x, y, z).color(r, g, b, a).uv(0F, 0F).endVertex();
	}

	private static void drawPartyBox(BufferBuilder buffer, int x, int y, boolean host) {
		final float w = buttonWidth;
		final float h = 24F;
		quad(buffer, x + 2F, y, 0F, w - 4F, 2F, host ? 1F : 0.5F, host ? 1F : 0F, host ? 0F : 1F, host ? 1F : 0.5F);
		quad(buffer, x + w - 2F, y, 0F, 2F, h, host ? 1F : 0.5F, host ? 1F : 0F, host ? 0F : 1F, host ? 1F : 0.5F);
		quad(buffer, x + 2F, y + h - 2F, 0F, w - 4F, 2F, host ? 1F : 0.5F, host ? 1F : 0F, host ? 0F : 1F, host ? 1F : 0.5F);
		quad(buffer, x, y, 0F, 2F, h, host ? 1F : 0.5F, host ? 1F : 0F, host ? 0F : 1F, host ? 1F : 0.5F);
	}

	public static void renderPlayerHead(@Nullable PlayerInfo info, PoseStack matrixStack_, int x, int y) {
		if (info == null || Minecraft.getInstance().level == null)
			return;
		ClientUtil.bindTexture(info.getSkinLocation());
		RenderSystem.enableTexture();
		GameProfile gameprofile = info.getProfile();
		Player playerentity = Minecraft.getInstance().level.getPlayerByUUID(gameprofile.getId());
		boolean flag1 = playerentity != null && playerentity.isModelPartShown(PlayerModelPart.CAPE) && ("Dinnerbone".equals(gameprofile.getName()) || "Grumm".equals(gameprofile.getName()));
		int i3 = 8 + (flag1 ? 8 : 0);
		int j3 = 8 * (flag1 ? -1 : 1);
		GuiComponent.blit(matrixStack_, x + 4, y + 4, 16, 16, 8.0F, (float) i3, 8, j3, 64, 64);
		if (playerentity != null && playerentity.isModelPartShown(PlayerModelPart.HAT)) {
			int k3 = 8 + (flag1 ? 8 : 0);
			int l3 = 8 * (flag1 ? -1 : 1);
			GuiComponent.blit(matrixStack_, x + 4, y + 4, 16, 16, 40.0F, (float) k3, 8, l3, 64, 64);
		}
		RenderSystem.disableTexture();
	}

	@Override
	protected void init() {
		super.init();
		if (minecraft == null)
			return;
		Window window = minecraft.getWindow();
		for (int i = 0; i < 7; i++) {
			final int index = i;
			Button b;
			kickMemberButtons.add(addRenderableWidget(b = new Button(0, 0, 20, 20, new TextComponent("X"), button -> {
				if (minecraft.player != null && ClientPartyInfo.host.getId().equals(minecraft.player.getUUID()) && ClientPartyInfo.members.size() > index) {
					ClientPartyInfo.members.remove(index);
					Voidscape.NETWORK.sendToServer(new ServerPacketRemovePartyMember(index));
				}
			})));
			b.visible = false;
		}
		addRenderableWidget(commence = new Button(

				10,

				22,

				buttonWidth,

				buttonHeight,

				new TranslatableComponent("Commence"), // FIXME: localize

				button -> {
					ClientPartyInfo.reserving = true;
					Voidscape.NETWORK.sendToServer(new ServerPacketCommenceDuty());
				}

		));
		commence.active = false;
		addRenderableWidget(new Button(

				window.getGuiScaledWidth() - buttonWidth - 10,

				22,

				buttonWidth,

				buttonHeight,

				new TranslatableComponent("Disband"),

				button -> Voidscape.NETWORK.sendToServer(new ServerPacketDisbandParty())

		));
		addRenderableWidget(passwordWidget = new EditBox(

				font,

				(int) (window.getGuiScaledWidth() / 2F - buttonWidth / 2F),

				window.getGuiScaledHeight() - buttonHeight * 2 - 10,

				buttonWidth,

				buttonHeight,

				new TranslatableComponent("Password")

		));
		/*
			TODO: override renderButton and show only * characters ?
			Potential impl:
				{
					@Override
					void renderButton(...) {
						final String cache = value
						value = value.replaceAll(".", "*")
						super.renderButton(...)
						value = cache
					}
				}
		 */
		passwordWidget.visible = false;
		passwordWidget.setValue(ClientPartyInfo.password);
		passwordWidget.setResponder(pass -> this.password = pass);
		addRenderableWidget(new Button(

				(int) (window.getGuiScaledWidth() / 2F - buttonWidth / 2F),

				window.getGuiScaledHeight() - buttonHeight - 5,

				buttonWidth,

				buttonHeight,

				new TranslatableComponent("Back"),

				button -> minecraft.setScreen(new PartySearchScreen(duty, type))

		));
	}

	@Override
	public void tick() {
		if (minecraft == null || minecraft.player == null || ClientPartyInfo.host == null)
			return;
		if (minecraft.player.tickCount % (20 * 3) == 0)
			Voidscape.NETWORK.sendToServer(new ServerPacketRequestPartyInfo());
		if (ClientPartyInfo.host.getId().equals(minecraft.player.getUUID()))
			commence.active = passwordWidget.active = true;
		else
			commence.active = passwordWidget.active = false;
		passwordWidget.visible = ClientPartyInfo.host.getId().equals(minecraft.player.getUUID());
		passwordWidget.active = !ClientPartyInfo.reserving;
		passwordWidget.tick();
		if (ClientPartyInfo.reserving || (ClientPartyInfo.members.size() != ClientPartyInfo.max - 1 && type != Instance.InstanceType.Unrestricted))
			commence.active = false;
		if (!password.equals(oldPassword))
			Voidscape.NETWORK.sendToServer(new ServerPacketSetPartyPassword(password));
		oldPassword = password;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return super.keyPressed(keyCode, scanCode, modifiers) || this.passwordWidget.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		return super.charTyped(codePoint, modifiers) || this.passwordWidget.charTyped(codePoint, modifiers);
	}

	@Override
	public void render(PoseStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (minecraft == null || minecraft.level == null || minecraft.player == null || ClientPartyInfo.host == null) {
			onClose();
			return;
		}
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		passwordWidget.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		Window window = minecraft.getWindow();
		String text = "Password:";
		if (ClientPartyInfo.host.getId().equals(minecraft.player.getUUID()))
			font.draw(p_230430_1_, text, window.getGuiScaledWidth() / 2F - font.width(text) / 2F, window.getGuiScaledHeight() - buttonHeight * 3 - 5, 0xFFFFFFFF);
		text = duty.display().getString();
		font.draw(p_230430_1_, text, window.getGuiScaledWidth() / 2F - font.width(text) / 2F, 10, 0xFFFFFFFF);
		text = type.name();
		font.draw(p_230430_1_, text, window.getGuiScaledWidth() / 2F - font.width(text) / 2F, font.lineHeight + 12, 0xFFFFFFFF);
		BufferBuilder buffer = Tesselator.getInstance().getBuilder();
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		int x1 = (int) (window.getGuiScaledWidth() / 2F - 10 - buttonWidth);
		int x2 = (int) (window.getGuiScaledWidth() / 2F + 10);
		int y = (int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F);
		final List<Runnable> renderPlayers = new ArrayList<>();
		final Consumer<Integer> renderPlayerFactory = (index) -> renderPlayers.add(() -> {
			PlayerInfo info = minecraft.player.connection.getPlayerInfo(ClientPartyInfo.members.get(index).getId());
			int y0 = y + 29 * ((index + 1) % 4);
			renderPlayerHead(info, p_230430_1_, index < 3 ? x1 : x2, y0);
			if (info != null)
				font.draw(p_230430_1_, info.getProfile().getName(), (index < 3 ? x1 : x2) + 22, y0 + 12 - font.lineHeight / 2F, 0xFFFFFFFF);
		});
		drawPartyBox(buffer, x1, y, true);
		renderPlayers.add(() -> {
			PlayerInfo info = minecraft.player.connection.getPlayerInfo(ClientPartyInfo.host.getId());
			renderPlayerHead(info, p_230430_1_, x1, y);
			if (info != null)
				font.draw(p_230430_1_, info.getProfile().getName(), x1 + 22, y + 12 - font.lineHeight / 2F, 0xFFFFFFFF);
		});
		kickMemberButtons.forEach(button -> button.visible = false);
		for (int i = 0; i < ClientPartyInfo.members.size(); i++) {
			int x = i < 3 ? x1 : x2;
			int y0 = y + 29 * ((i + 1) % 4);
			if (ClientPartyInfo.host.getId().equals(minecraft.player.getUUID())) {
				Button button = kickMemberButtons.get(i);
				button.visible = true;
				button.x = x + buttonWidth - 22;
				button.y = y0 + 2;
			}
			drawPartyBox(buffer, x, y0, false);
			renderPlayerFactory.accept(i);
		}
		RenderSystem.enableBlend();
		Tesselator.getInstance().end();
		RenderSystem.disableBlend();
		renderPlayers.forEach(Runnable::run);
	}

}
