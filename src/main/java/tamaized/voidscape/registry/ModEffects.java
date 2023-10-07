package tamaized.voidscape.registry;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ClientUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModEffects implements RegistryClass {

	private static final DeferredRegister<MobEffect> REGISTRY = RegUtil.create(ForgeRegistries.MOB_EFFECTS);

	public static final RegistryObject<MobEffect> ICHOR = REGISTRY.register("ichor", () -> new StandardEffect(MobEffectCategory.HARMFUL, 0xFF7700, true)
			.texture("effect/ichor.png"));
	public static final RegistryObject<MobEffect> AURA = REGISTRY.register("aura", () -> new StandardEffect(MobEffectCategory.BENEFICIAL, 0xFF0077, false)
			.texture("effect/aura.png"));
	public static final RegistryObject<MobEffect> FORTIFIED = REGISTRY.register("fortified", () -> new StandardEffect(MobEffectCategory.BENEFICIAL, 0x00FFAA, false)
			.texture("effect/fortified.png"));

	@Override
	public void init(IEventBus bus) {

	}

	public static class StandardEffect extends MobEffect {

		private Supplier<Supplier<ResourceLocation>> texture = () -> ClientUtil::getMissingTexture;
		private final boolean allowCure;

		private StandardEffect(MobEffectCategory type, int color, boolean allowCure) {
			super(type, color);
			this.allowCure = allowCure;
		}

		@Override
		public List<ItemStack> getCurativeItems() {
			return allowCure ? super.getCurativeItems() : new ArrayList<>(); // Disable Milk
		}

		<T extends StandardEffect> T texture(String loc) {
			return texture(new ResourceLocation(Voidscape.MODID, "textures/" + loc));
		}

		@SuppressWarnings("unchecked")
		<T extends StandardEffect> T texture(ResourceLocation loc) {
			texture = () -> () -> loc;
			return (T) this;
		}

		@Override
		public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
			consumer.accept(new IClientMobEffectExtensions() {
				@Override
				public boolean renderInventoryIcon(MobEffectInstance instance, EffectRenderingInventoryScreen<?> screen, GuiGraphics guiGraphics, int x, int y, int z) {
					RenderSystem.setShaderTexture(0, texture.get().get());
					float y1 = y + 7;
					float x2 = x + 18;
					float y2 = y1 + 18;
					RenderSystem.setShader(GameRenderer::getPositionTexShader);
					BufferBuilder buffer = Tesselator.getInstance().getBuilder();
					buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
					buffer.vertex(x, y2, z).uv(0, 1).endVertex();
					buffer.vertex(x2, y2, z).uv(1, 1).endVertex();
					buffer.vertex(x2, y1, z).uv(1, 0).endVertex();
					buffer.vertex(x, y1, z).uv(0, 0).endVertex();
					Tesselator.getInstance().end();
					return true;
				}
				@Override
				public boolean renderGuiIcon(MobEffectInstance instance, Gui gui, GuiGraphics guiGraphics, int x, int y, float z, float alpha) {
					RenderSystem.setShaderTexture(0, texture.get().get());
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
					float x1 = x + 3;
					float y1 = y + 3;
					float x2 = x1 + 18;
					float y2 = y1 + 18;
					RenderSystem.setShader(GameRenderer::getPositionTexShader);
					BufferBuilder buffer = Tesselator.getInstance().getBuilder();
					buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
					buffer.vertex(x1, y2, z).uv(0, 1).endVertex();
					buffer.vertex(x2, y2, z).uv(1, 1).endVertex();
					buffer.vertex(x2, y1, z).uv(1, 0).endVertex();
					buffer.vertex(x1, y1, z).uv(0, 0).endVertex();
					Tesselator.getInstance().end();
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
					return true;
				}
			});
		}
	}
}
