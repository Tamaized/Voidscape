package tamaized.voidscape.client.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import tamaized.voidscape.data.QuiverContents;

public class ClientQuiverTooltip implements ClientTooltipComponent {
	private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("container/bundle/background");
	private static final int MARGIN_Y = 4;
	private static final int BORDER_WIDTH = 1;
	private static final int SLOT_SIZE_X = 18;
	private static final int SLOT_SIZE_Y = 20;
	private final QuiverContents contents;

	public ClientQuiverTooltip(QuiverContents contents) {
		this.contents = contents;
	}

	@Override
	public int getHeight() {
		return this.backgroundHeight() + 4;
	}

	@Override
	public int getWidth(Font font) {
		return this.backgroundWidth();
	}

	private int backgroundWidth() {
		return this.gridSizeX() * 18 + 2;
	}

	private int backgroundHeight() {
		return this.gridSizeY() * 20 + 2;
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
		int i = this.gridSizeX();
		int j = this.gridSizeY();
		guiGraphics.blitSprite(BACKGROUND_SPRITE, x, y, this.backgroundWidth(), this.backgroundHeight());
		boolean flag = this.contents.fullPercentage() >= 1F;
		int k = 0;

		for (int l = 0; l < j; l++) {
			for (int i1 = 0; i1 < i; i1++) {
				int j1 = x + i1 * 18 + 1;
				int k1 = y + l * 20 + 1;
				this.renderSlot(j1, k1, k++, flag, guiGraphics, font);
			}
		}
	}

	private void renderSlot(int x, int y, int itemIndex, boolean isBundleFull, GuiGraphics guiGraphics, Font font) {
		if (itemIndex >= this.contents.view().size()) {
			this.blit(guiGraphics, x, y, isBundleFull ? Texture.BLOCKED_SLOT : Texture.SLOT);
		} else {
			ItemStack itemstack = this.contents.view().get(itemIndex);
			this.blit(guiGraphics, x, y, Texture.SLOT);
			guiGraphics.renderItem(itemstack, x + 1, y + 1, itemIndex);
			guiGraphics.renderItemDecorations(font, itemstack, x + 1, y + 1);
			if (itemIndex == 0) {
				AbstractContainerScreen.renderSlotHighlight(guiGraphics, x + 1, y + 1, 0);
			}
		}
	}

	private void blit(GuiGraphics guiGraphics, int x, int y, Texture texture) {
		guiGraphics.blitSprite(texture.sprite, x, y, 0, texture.w, texture.h);
	}

	private int gridSizeX() {
		return 5;
	}

	private int gridSizeY() {
		return 1;
	}

	@OnlyIn(Dist.CLIENT)
	static enum Texture {
		BLOCKED_SLOT(Identifier.withDefaultNamespace("container/bundle/blocked_slot"), 18, 20),
		SLOT(Identifier.withDefaultNamespace("container/bundle/slot"), 18, 20);

		public final Identifier sprite;
		public final int w;
		public final int h;

		private Texture(Identifier sprite, int w, int h) {
			this.sprite = sprite;
			this.w = w;
			this.h = h;
		}
	}

}