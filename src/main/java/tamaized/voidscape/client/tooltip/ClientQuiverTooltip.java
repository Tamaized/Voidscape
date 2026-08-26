package tamaized.voidscape.client.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import tamaized.voidscape.data.QuiverContents;

public class ClientQuiverTooltip implements ClientTooltipComponent {
	private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("container/bundle/background");
	private static final Identifier SLOT_HIGHLIGHT_BACK_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_highlight_back");
	private static final Identifier SLOT_HIGHLIGHT_FRONT_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_highlight_front");
	private static final int SLOT_SIZE_X = 18;
	private static final int SLOT_SIZE_Y = 20;
	private final QuiverContents contents;

	public ClientQuiverTooltip(QuiverContents contents) {
		this.contents = contents;
	}

	@Override
	public int getHeight(Font font) {
		return this.backgroundHeight() + 4;
	}

	@Override
	public int getWidth(Font font) {
		return this.backgroundWidth();
	}

	private int backgroundWidth() {
		return this.gridSizeX() * SLOT_SIZE_X + 2;
	}

	private int backgroundHeight() {
		return this.gridSizeY() * SLOT_SIZE_Y + 2;
	}

	@Override
	public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
		int i = this.gridSizeX();
		int j = this.gridSizeY();
		boolean flag = this.contents.fullPercentage() >= 1F;
		int k = 0;

		for (int l = 0; l < j; l++) {
			for (int i1 = 0; i1 < i; i1++) {
				int j1 = x + i1 * SLOT_SIZE_X + 1;
				int k1 = y + l * SLOT_SIZE_Y + 1;
				this.renderSlot(j1, k1, k++, flag, graphics, font);
			}
		}
	}

	private void renderSlot(int x, int y, int itemIndex, boolean isBundleFull, GuiGraphicsExtractor graphics, Font font) {
		if (itemIndex == 0) {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_BACK_SPRITE, x, y, this.backgroundWidth(), this.backgroundHeight());
		} else {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, x, y, this.backgroundWidth(), this.backgroundHeight());
		}
		if (itemIndex >= this.contents.view().size()) {
			this.blit(graphics, x, y, isBundleFull ? Texture.BLOCKED_SLOT : Texture.SLOT);
		} else {
			ItemStack itemstack = this.contents.view().get(itemIndex);
			this.blit(graphics, x, y, Texture.SLOT);
			graphics.item(itemstack, x + 1, y + 1, itemIndex);
			graphics.itemDecorations(font, itemstack, x + 1, y + 1);
			if (itemIndex == 0) {
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_FRONT_SPRITE, x, y, this.backgroundWidth(), this.backgroundHeight());
			}
		}
	}

	private void blit(GuiGraphicsExtractor graphics, int x, int y, Texture texture) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, texture.sprite, x, y, texture.w, texture.h);
	}

	private int gridSizeX() {
		return 5;
	}

	private int gridSizeY() {
		return 1;
	}

	enum Texture {
		BLOCKED_SLOT(Identifier.withDefaultNamespace("container/bundle/blocked_slot"), 18, 20),
		SLOT(Identifier.withDefaultNamespace("container/bundle/slot"), 18, 20);

		public final Identifier sprite;
		public final int w;
		public final int h;

		Texture(Identifier sprite, int w, int h) {
			this.sprite = sprite;
			this.w = w;
			this.h = h;
		}
	}

}