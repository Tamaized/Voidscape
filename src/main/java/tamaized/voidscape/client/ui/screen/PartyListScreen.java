package tamaized.voidscape.client.ui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.server.ServerPacketRequestPartyList;
import tamaized.voidscape.party.ClientPartyInfo;
import tamaized.voidscape.turmoil.Duties;
import tamaized.voidscape.world.Instance;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class PartyListScreen extends TurmoilScreen {

	private final Duties.Duty duty;
	private final Instance.InstanceType type;
	private boolean initedOnce;
	public boolean joining;
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

					buttonHeight

			);
			list.setLeftPos((int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F) + buttonWidth + 5);
			list.children().clear();
			ClientPartyInfo.PARTIES.forEach(party -> list.children().add(new PartyList.Entry(party)));
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
					joining = true;
					join.active = false;
					password.active = false;
				}

		));
		join.active = false;
		addWidget(password = new TextFieldWidget(

				font,

				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),

				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F) + spacingHeight,

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Password")

		));
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
		list.children().stream().filter(entry -> !ClientPartyInfo.PARTIES.contains(entry.party)).collect(Collectors.toList()).forEach(list::removeEntry);
		ClientPartyInfo.PARTIES.stream().filter(party -> list.children().stream().noneMatch(entry -> entry.party == party)).forEach(party -> list.children().add(new PartyList.Entry(party)));
		join.active = !joining && list.getSelected() != null;
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
	}

	static class PartyList extends ExtendedList<PartyList.Entry> {

		private static void quad(BufferBuilder buffer, float x, float y, float z, float w, float h, float r, float g, float b, float a) {
			buffer.vertex(x, y + h, z).color(r, g, b, a).uv(0F, 1F).endVertex();
			buffer.vertex(x + w, y + h, z).color(r, g, b, a).uv(1F, 1F).endVertex();
			buffer.vertex(x + w, y, z).color(r, g, b, a).uv(1F, 0F).endVertex();
			buffer.vertex(x, y, z).color(r, g, b, a).uv(0F, 0F).endVertex();
		}

		public PartyList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
			super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
			setRenderBackground(false);
			setRenderTopAndBottom(false);
		}

		@Override
		public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			super.render(matrixStack, mouseX, mouseY, partialTicks);
			if (children().isEmpty()) {
				final String text = "No Parties Found";
				minecraft.font.draw(matrixStack, text, x0 + width / 2F - minecraft.font.width(text) / 2F, y0 + 15, 0xFFFF00FF);
			}
			if (ClientPartyInfo.error != null) {
				final String error = ClientPartyInfo.error.getString();
				minecraft.font.draw(matrixStack, error, x0 + width / 2F - minecraft.font.width(error) / 2F, y0 + 15 + minecraft.font.lineHeight + 3, 0xFFFF0000);
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

		static class Entry extends ExtendedList.AbstractListEntry<PartyList.Entry> {

			private final ClientPartyInfo.Party party;

			Entry(ClientPartyInfo.Party party) {
				this.party = party;
			}

			@Override
			public void render(@Nonnull MatrixStack mStack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
				BufferBuilder buffer = Tessellator.getInstance().getBuilder();
				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
				buffer.vertex(left, top, 0F).color(0.1F, 0.1F, 0.1F, 1.0F).endVertex();
				buffer.vertex(left + entryWidth, top, 0F).color(0.1F, 0.1F, 0.1F, 1.0F).endVertex();
				buffer.vertex(left + entryWidth, top + entryHeight, 0F).color(0.1F, 0.1F, 0.1F, 1.0F).endVertex();
				buffer.vertex(left, top + entryHeight, 0F).color(0.1F, 0.1F, 0.1F, 1.0F).endVertex();
				RenderSystem.disableTexture();
				Tessellator.getInstance().end();
				RenderSystem.enableTexture();
				Minecraft.getInstance().font.draw(mStack, party.duty.display(), top + entryHeight / 2F - Minecraft.getInstance().font.lineHeight / 2F, left, 0xFFFFFFFF);
				Minecraft.getInstance().font.draw(mStack, party.members + "/" + party.max, top + entryHeight / 2F - Minecraft.getInstance().font.lineHeight / 2F, left, 0xFFFFFFFF);
			}
		}

	}

}
