package tamaized.voidscape.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import tamaized.voidscape.data.QuiverContents;

public record QuiverTooltip(QuiverContents contents) implements TooltipComponent {
}
