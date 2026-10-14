package tamaized.voidscape.client.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.IItemDecorator;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.data.AxiomBurst;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;
import tamaized.voidscape.registry.item.AxiomaticCrossbow;

@Component(dist = Dist.CLIENT)
public class AxiomCrossbowDecorator implements IItemDecorator {

	private static final int BAR_WIDTH = 13;
	private static final int BACKGROUND_COLOR = ARGB.color(0, 0, 0);
	private static final int ACTIVE_COLOR = ARGB.color(85, 255, 255);
	private static final int COOLDOWN_COLOR = ARGB.color(170, 0, 170);

	@Autowired(dist = Dist.CLIENT)
	private ModToolSetComponentDirectory toolSets;

	@Autowired(dist = Dist.CLIENT)
	private ModItemComponents itemComponents;

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::registerItemDecorations);
	}

	private void registerItemDecorations(RegisterItemDecorationsEvent event) {
		event.register(toolSets.axiomToolSet().AXIOM_XBOW.get(), this);
	}

	@Override
	public boolean render(GuiGraphicsExtractor guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		AxiomBurst burst = stack.get(itemComponents.AXIOM_BURST.get());
		if (level == null || burst == null)
			return false;
		float time = level.getGameTime() + minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true);
		if (time < burst.activeUntil())
			renderBar(guiGraphics, xOffset, yOffset, (burst.activeUntil() - time) / AxiomaticCrossbow.BURST_TICKS, ACTIVE_COLOR);
		else if (time < burst.cooldownUntil())
			renderBar(guiGraphics, xOffset, yOffset, 1F - (burst.cooldownUntil() - time) / AxiomaticCrossbow.COOLDOWN_TICKS, COOLDOWN_COLOR);
		return false;
	}

	private void renderBar(GuiGraphicsExtractor guiGraphics, int x, int y, float fraction, int color) {
		int left = x + 2;
		int top = y + 11;
		int width = Mth.ceil(BAR_WIDTH * Mth.clamp(fraction, 0F, 1F));
		guiGraphics.fill(RenderPipelines.GUI, left, top, left + BAR_WIDTH, top + 2, BACKGROUND_COLOR);
		guiGraphics.fill(RenderPipelines.GUI, left, top, left + width, top + 1, color);
	}

}
