package tamaized.voidscape.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.data.Shroud;
import tamaized.voidscape.registry.ModDataAttachments;

@Component(dist = Dist.CLIENT)
public class ShroudHeartOverlay {

	@Autowired(dist = Dist.CLIENT)
	private ModDataAttachments dataAttachments;

	private final Identifier SPRITE_SHROUD_HEART = Identifier.fromNamespaceAndPath(Voidscape.MODID, "hud/shroud_heart");

	private final RandomSource random = RandomSource.create();

	private int heartsLineBase;

	@PostConstruct
	private void setup(IEventBus modBus) {
		modBus.addListener(this::registerLayer);
	}

	private void registerLayer(RegisterGuiLayersEvent event) {
		event.registerBelow(VanillaGuiLayers.PLAYER_HEALTH, Identifier.fromNamespaceAndPath(Voidscape.MODID, "shroud_hearts_anchor"),
			(graphics, _) -> heartsLineBase = graphics.guiHeight() - Minecraft.getInstance().gui.leftHeight
		);
		event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH, Identifier.fromNamespaceAndPath(Voidscape.MODID, "shroud_hearts"), (graphics, _) -> {
			Minecraft minecraft = Minecraft.getInstance();
			Player player = minecraft.player;
			if (minecraft.level == null || player == null || minecraft.gameMode == null || !minecraft.gameMode.canHurtPlayer() || minecraft.options.hideGui)
				return;
			Shroud shroud = player.getData(dataAttachments.SHROUD);
			int shroudHearts = Mth.ceil(shroud.getShields() / 2F);
			if (shroudHearts <= 0)
				return;
			render(graphics, minecraft.gui, player, shroudHearts);
		});
	}

	@SuppressWarnings("IntegerMultiplicationImplicitCastToLong") // Vanilla is also using an int, causing an overflow...
	private void render(GuiGraphicsExtractor graphics, Gui gui, Player player, int shroudHearts) {
		int ticks = gui.getGuiTicks();
		int currentHealth = Mth.ceil(player.getHealth());
		float maxHealth = Math.max((float) player.getAttributeValue(Attributes.MAX_HEALTH), (float) Math.max(gui.displayHealth, currentHealth));
		int totalAbsorption = Mth.ceil(player.getAbsorptionAmount());
		int numHealthRows = Mth.ceil((maxHealth + totalAbsorption) / 2.0F / 10.0F);
		int healthRowHeight = Math.max(10 - (numHealthRows - 2), 3);
		int xLeft = graphics.guiWidth() / 2 - 90;
		int healthContainerCount = Mth.ceil(maxHealth / 2.0);
		int absorptionContainerCount = Mth.ceil(totalAbsorption / 2.0);
		int heartOffsetIndex = player.hasEffect(MobEffects.REGENERATION) ? ticks % Mth.ceil(maxHealth + 5.0F) : -1;
		boolean lowHealth = currentHealth + totalAbsorption <= 4;
		random.setSeed(ticks * 312871);
		for (int containerIndex = healthContainerCount + absorptionContainerCount - 1; containerIndex >= 0; containerIndex--) {
			int xo = xLeft + (containerIndex % 10) * 8;
			int yo = heartsLineBase - (containerIndex / 10) * healthRowHeight;
			if (lowHealth)
				yo += random.nextInt(2);
			if (containerIndex < healthContainerCount && containerIndex == heartOffsetIndex)
				yo -= 2;
			if (containerIndex < shroudHearts)
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE_SHROUD_HEART, xo - 4, yo - 3, 16, 16);
		}
	}

}
