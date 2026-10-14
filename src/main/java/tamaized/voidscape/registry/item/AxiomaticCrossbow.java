package tamaized.voidscape.registry.item;

import tamaized.regutil.ExtraTooltipContext;
import tamaized.regutil.item.BreakableCrossbow;

import java.util.function.Consumer;

public class AxiomaticCrossbow extends BreakableCrossbow {

	public AxiomaticCrossbow(Properties properties, Consumer<ExtraTooltipContext> tooltipConsumer) {
		super(properties, tooltipConsumer);
	}

}
