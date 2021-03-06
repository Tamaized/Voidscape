package tamaized.voidscape.client.ui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.server.ServerPacketRequestJoinParty;
import tamaized.voidscape.network.server.ServerPacketRequestPartyList;
import tamaized.voidscape.party.ClientPartyInfo;
import tamaized.voidscape.turmoil.Duties;
import tamaized.voidscape.world.Instance;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.stream.Collectors;

public class PartyListScreen extends TurmoilScreen {

	private final Duties.Duty duty;
	private final Instance.InstanceType type;
	public boolean joining;
	private boolean initedOnce;
	private Button join;
	private TextFieldWidget password;
	private PartyList list;

	public PartyListScreen(Duties.Duty duty, Instance.InstanceType type) {
		super(new TranslationTextComponent(Voidscape.MODID.concat(".screen.form")));
		this.duty = duty;
		this.type = type;
		ClientPartyInfo.error = null;
	}

	@Override
	protected void init() {
		super.init();
		if (minecraft == null)
			return;
		Voidscape.NETWORK.sendToServer(new ServerPacketRequestPartyList());
		MainWindow window = minecraft.getWindow();
		final int buttonWidth = 180;
		final int buttonHeight = 20;
		final int spacingHeight = (int) (buttonHeight * 1.5F);
		int listY = (int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F);
		if (this.initedOnce) {
			list.updateSize((int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F) + buttonWidth + 5,

					window.getGuiScaledHeight() - buttonHeight - 10 - listY,

					listY,

					window.getGuiScaledHeight() - buttonHeight - 10);
			list.setLeftPos((int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F) + buttonWidth + 5);
		} else {
			list = new PartyList(minecraft,

					(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F) + buttonWidth + 5,

					window.getGuiScaledHeight() - buttonHeight - 10 - listY,

					listY,

					window.getGuiScaledHeight() - buttonHeight - 10,

					24

			);
			list.setLeftPos((int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F) + buttonWidth + 5);
			list.children().clear();
			ClientPartyInfo.PARTIES.forEach(party -> list.children().add(list.new Entry(party.host.getId())));
			children.add(list);
			initedOnce = true;
		}
		addButton(join = new Button(

				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),

				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F),

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Join Party"),

				button -> {
					if (list.getSelected() == null)
						return;
					joining = true;
					join.active = false;
					password.active = false;
					Voidscape.NETWORK.sendToServer(new ServerPacketRequestJoinParty(list.getSelected().host, password.getValue()));
				}

		));
		join.active = false;
		password = new TextFieldWidget(

				font,

				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),

				(int) ((window.getGuiScaledHeight() / 4F - buttonHeight / 2F) + spacingHeight + font.lineHeight + 2),

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Password")

		);
		children.add(password);
		addButton(new Button(

				(int) (window.getGuiScaledWidth() / 2F - buttonWidth / 2F),

				window.getGuiScaledHeight() - buttonHeight - 5,

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Back"),

				button -> minecraft.setScreen(new ConfirmDutyScreen(duty))

		));
	}

	@Override
	public void tick() {
		if (minecraft != null && minecraft.player != null && minecraft.player.tickCount % (20 * 5) == 0)
			Voidscape.NETWORK.sendToServer(new ServerPacketRequestPartyList());
		password.tick();
		list.children().stream().filter(entry -> ClientPartyInfo.PARTIES.stream().noneMatch(party -> party.host.getId().equals(entry.host))).collect(Collectors.toList()).forEach(list::removeEntry);
		ClientPartyInfo.PARTIES.stream().filter(party -> list.children().stream().noneMatch(entry -> entry.host.equals(party.host.getId()))).forEach(party -> list.children().add(list.new Entry(party.host.getId())));
		join.active = !joining && list.getSelected() != null;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return super.keyPressed(keyCode, scanCode, modifiers) || this.password.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		return super.charTyped(codePoint, modifiers) || this.password.charTyped(codePoint, modifiers);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		if (minecraft == null || minecraft.level == null || minecraft.player == null) {
			onClose();
			return;
		}
		if (ClientPartyInfo.host != null) {
			minecraft.setScreen(new FormPartyScreen(ClientPartyInfo.duty, ClientPartyInfo.type));
			return;
		}
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		list.render(matrixStack, mouseX, mouseY, partialTicks);
		password.render(matrixStack, mouseX, mouseY, partialTicks);
		String text = new TranslationTextComponent("Password:").getString();
		font.draw(matrixStack, text, (int) (minecraft.getWindow().getGuiScaledWidth() / 4 - font.width(text) / 2F), (int) (minecraft.getWindow().getGuiScaledHeight() / 4F - 20F / 2F) + 30F, 0xFFFFFFFF);
		text = duty.display().getString();
		font.draw(matrixStack, text, Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2F - font.width(text) / 2F, 10, 0xFFFFFFFF);
		text = type.name();
		font.draw(matrixStack, text, Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2F - font.width(text) / 2F, font.lineHeight + 12, 0xFFFFFFFF);
		if (ClientPartyInfo.error != null) {
			final String error = ClientPartyInfo.error.getString();
			minecraft.font.draw(matrixStack, error, Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2F - font.width(error) / 2F, font.lineHeight * 2F + 16, 0xFFFF0000);
		}
	}

	static class PartyList extends ExtendedList<PartyList.Entry> {

		public PartyList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
			super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
			setRenderBackground(false);
			setRenderTopAndBottom(false);
			headerHeight = 4;
		}

		@Override
		protected int getScrollbarPosition() {
			return x1 - 4;
		}

		@Override
		public int getRowWidth() {
			return x1 - x0 - 10;
		}

		private static void quad(BufferBuilder buffer, float x, float y, float z, float w, float h, float r, float g, float b, float a) {
			buffer.vertex(x, y + h, z).color(r, g, b, a).uv(0F, 1F).endVertex();
			buffer.vertex(x + w, y + h, z).color(r, g, b, a).uv(1F, 1F).endVertex();
			buffer.vertex(x + w, y, z).color(r, g, b, a).uv(1F, 0F).endVertex();
			buffer.vertex(x, y, z).color(r, g, b, a).uv(0F, 0F).endVertex();
		}

		@Override
		public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			super.render(matrixStack, mouseX, mouseY, partialTicks);
			if (children().isEmpty()) {
				final String text = "No Parties Found";
				minecraft.font.draw(matrixStack, text, x0 + width / 2F - minecraft.font.width(text) / 2F, y0 + 15, 0xFFFF00FF);
			}
			BufferBuilder buffer = Tessellator.getInstance().getBuilder();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			quad(buffer, x0 + 3, y0, 0, width - 6, 3, 0.6F, 0F, 1F, 0.25F);
			quad(buffer, x0 + width - 3, y0, 0, 3, height, 0.6F, 0F, 1F, 0.25F);
			quad(buffer, x0 + 3, y0 + height - 3, 0, width - 6, 3, 0.6F, 0F, 1F, 0.25F);
			quad(buffer, x0, y0, 0, 3, height, 0.6F, 0F, 1F, 0.25F);
			RenderSystem.enableBlend();
			Tessellator.getInstance().end();
			RenderSystem.disableBlend();
		}

		@Override
		public boolean removeEntry(@Nonnull Entry p_230956_1_) { // Accessibility
			return super.removeEntry(p_230956_1_);
		}

		class Entry extends ExtendedList.AbstractListEntry<PartyList.Entry> {

			private final UUID host;

			Entry(UUID host) {
				this.host = host;
			}

			@Override
			public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
				setSelected(this);
				return false;
			}

			@Override
			public void render(@Nonnull MatrixStack mStack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
				RenderSystem.color4f(1F, 1F, 1F, 1F);
				final float realWidth = entryWidth - 5;
				BufferBuilder buffer = Tessellator.getInstance().getBuilder();
				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
				quad(buffer, left + 1, top, 0, realWidth - 2, 1, 1, 1, 1, 1);
				quad(buffer, left, top, 0, 1, entryHeight, 1, 1, 1, 1);
				quad(buffer, left + 1, top + entryHeight - 1, 0, realWidth - 2, 1, 1, 1, 1, 1);
				quad(buffer, left + realWidth - 1, top, 0, 1, entryHeight, 1, 1, 1, 1);
				RenderSystem.disableTexture();
				Tessellator.getInstance().end();
				RenderSystem.enableTexture();
				ClientPartyInfo.PARTIES.stream().filter(p -> p.host.getId().equals(host)).findAny().ifPresent(party -> {
					if (minecraft.player != null) {
						ClientPlayNetHandler network = minecraft.player.connection;
						FormPartyScreen.renderPlayerHead(network.getPlayerInfo(party.host.getId()), mStack, left - 2, top - 2);
					}
					Minecraft.getInstance().font.draw(mStack, party.host.getName(), left + 2 + 16 + 2, top + 2 + (entryHeight - 2) / 2F - Minecraft.getInstance().font.lineHeight / 2F, 0xFFFFFFFF);
					final String text = party.members + "/" + party.max;
					Minecraft.getInstance().font.draw(mStack, text, left + realWidth - 2 - Minecraft.getInstance().font.width(text), top + 2 + (entryHeight - 2) / 2F - Minecraft.getInstance().font.lineHeight / 2F, 0xFFFFFFFF);
				});

			}
		}

	}

}
