package tamaized.voidscape.client.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import tamaized.voidscape.data.QuiverContents;

public class ClientQuiverTooltip implements ClientTooltipComponent {

	private static final Identifier SLOT_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_background");
	private static final Identifier SLOT_HIGHLIGHT_BACK_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_highlight_back");
	private static final Identifier SLOT_HIGHLIGHT_FRONT_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_highlight_front");
	private static final int SLOT_SIZE = 24;
	private static final int SLOT_MARGIN = 4;
	private static final int GRID_SIZE_X = 5;
	private static final int GRID_SIZE_Y = 1;

	private final QuiverContents contents;

	public ClientQuiverTooltip(QuiverContents contents) {
		this.contents = contents;
	}

	@Override
	public int getHeight(Font font) {
		return GRID_SIZE_Y * SLOT_SIZE;
	}

	@Override
	public int getWidth(Font font) {
		return GRID_SIZE_X * SLOT_SIZE;
	}

	@Override
	public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
		int slotIndex = 0;

		for (int row = 0; row < GRID_SIZE_Y; row++) {
			for (int column = 0; column < GRID_SIZE_X; column++) {
				this.extractSlot(x + column * SLOT_SIZE, y + row * SLOT_SIZE, slotIndex++, font, graphics);
			}
		}
	}

	private void extractSlot(int x, int y, int slotIndex, Font font, GuiGraphicsExtractor graphics) {
		boolean hasHighlight = slotIndex == 0 && !this.contents.isEmpty();
		graphics.blitSprite(
			RenderPipelines.GUI_TEXTURED,
			hasHighlight ? SLOT_HIGHLIGHT_BACK_SPRITE : SLOT_BACKGROUND_SPRITE,
			x, y,
			SLOT_SIZE, SLOT_SIZE
		);

		if (slotIndex < this.contents.view().size()) {
			ItemStack stack = this.contents.view().get(slotIndex);
			graphics.item(stack, x + SLOT_MARGIN, y + SLOT_MARGIN, slotIndex);
			graphics.itemDecorations(font, stack, x + SLOT_MARGIN, y + SLOT_MARGIN);
		}

		if (hasHighlight) {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_FRONT_SPRITE, x, y, SLOT_SIZE, SLOT_SIZE);
		}
	}

}
