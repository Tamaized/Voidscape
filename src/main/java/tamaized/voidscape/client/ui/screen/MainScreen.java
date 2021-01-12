package tamaized.voidscape.client.ui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import org.lwjgl.opengl.GL11;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.StencilBufferUtil;
import tamaized.voidscape.client.ui.RenderTurmoil;
import tamaized.voidscape.network.server.ServerPacketTurmoilProgressTutorial;
import tamaized.voidscape.party.ClientPartyInfo;
import tamaized.voidscape.turmoil.Progression;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;

import java.util.Optional;
import java.util.function.Consumer;

public class MainScreen extends TurmoilScreen {

	private long tick;

	public MainScreen() {
		super(new TranslationTextComponent(Voidscape.MODID.concat(".screen.main")));
	}

	@Override
	protected void init() {
		super.init();
		if (minecraft == null)
			return;
		tick = minecraft.level == null ? 0 : minecraft.level.getGameTime();
		MainWindow window = minecraft.getWindow();
		final int buttonWidth = 180;
		final int buttonHeight = 20;
		final int spacingHeight = (int) (buttonHeight * 1.5F);
		Button teleport = new Button(

				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),

				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F),

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Enter the Void"),

				button -> {
					if (minecraft.player != null)
						minecraft.player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(Turmoil::clientTeleport));
				}

		);
		teleport.active = !Voidscape.checkForVoidDimension(minecraft.level);
		addButton(teleport);
		addButton(new Button(

				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),

				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F) + spacingHeight,

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Voidic Powers"),

				button -> {
					Turmoil data = getData(Voidscape.subCapTurmoilData);
					if (data != null && data.getProgression() == Progression.MidTutorial)
						Voidscape.NETWORK.sendToServer(new ServerPacketTurmoilProgressTutorial());
					minecraft.setScreen(new SkillsScreen());
				}

		));
		Button spells = new Button(

				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),

				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F) + spacingHeight * 2,

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Configure Voidic Spells"),

				button -> minecraft.setScreen(new SpellsScreen())

		);
		Turmoil data = getData(Voidscape.subCapTurmoilData);
		spells.active = data != null && data.hasCoreSkill();
		addButton(spells);
		Button instances = new Button(

				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),

				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F) + spacingHeight * 3,

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Duties"),

				button -> {
					if (ClientPartyInfo.host == null)
						minecraft.setScreen(new DutyScreen());
					else
						minecraft.setScreen(new FormPartyScreen(ClientPartyInfo.duty, ClientPartyInfo.type));
				}

		);
		instances.active = data != null && data.getProgression().ordinal() >= Progression.CorruptPawnPre.ordinal();
		addButton(instances);
		addButton(new Button(

				(int) (window.getGuiScaledWidth() / 2F - buttonWidth / 2F),

				window.getGuiScaledHeight() - buttonHeight - 5,

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Close"),

				button -> onClose()

		));
	}

	@Override
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (minecraft == null || minecraft.level == null) {
			onClose();
			return;
		}
		RenderSystem.enableBlend();
		RenderSystem.enableAlphaTest();
		{

			BufferBuilder buffer = Tessellator.getInstance().getBuilder();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);

			MainWindow window = Minecraft.getInstance().getWindow();

			float x = 0F;
			float y = 0F;
			float w = window.getGuiScaledWidth();
			float h = window.getGuiScaledHeight();
			float z = 0F;

			Consumer<RenderTurmoil.Color24> verticies = color -> {
				final float r = RenderTurmoil.Color24.asFloat(color.bit24);
				final float g = RenderTurmoil.Color24.asFloat(color.bit16);
				final float b = RenderTurmoil.Color24.asFloat(color.bit8);
				final float a = RenderTurmoil.Color24.asFloat(color.bit0);
				buffer.vertex(x, y + h, z).color(r, g, b, a).uv(0F, 1F).endVertex();
				buffer.vertex(x + w, y + h, z).color(r, g, b, a).uv(1F, 1F).endVertex();
				buffer.vertex(x + w, y, z).color(r, g, b, a).uv(1F, 0F).endVertex();
				buffer.vertex(x, y, z).color(r, g, b, a).uv(0F, 0F).endVertex();
			};
			verticies.accept(RenderTurmoil.colorHolder.set(1F, 1F, 1F, 1F));

			Minecraft.getInstance().getTextureManager().bind(RenderTurmoil.TEXTURE_MASK);

			final int stencilIndex = 12;

			StencilBufferUtil.setup(stencilIndex, () -> {
				float perc = Math.min(1F, (minecraft.level.getGameTime() - tick) / (20 * 3F));
				RenderSystem.alphaFunc(GL11.GL_LESS, perc);
				Tessellator.getInstance().end();
				RenderSystem.defaultAlphaFunc();
			});


			StencilBufferUtil.render(stencilIndex, () -> {
				super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
			}, true);
		}
		RenderSystem.disableAlphaTest();
		RenderSystem.disableBlend();
		if (minecraft == null || minecraft.player == null) {
			onClose();
			return;
		}
		LazyOptional<SubCapability.ISubCap> o = minecraft.player.getCapability(SubCapability.CAPABILITY);
		if (!o.isPresent()) {
			onClose();
			return;
		}
		o.ifPresent(cap -> {
			Optional<Turmoil> t = cap.get(Voidscape.subCapTurmoilData);
			if (!t.isPresent()) {
				onClose();
				return;
			}
			t.ifPresent(data -> {
				if (data.getState() != Turmoil.State.OPEN)
					onClose();
			});
		});
	}
}
