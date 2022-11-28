package tamaized.voidscape.registry;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.abilities.HealerAbilities;
import tamaized.voidscape.turmoil.abilities.MageAbilities;
import tamaized.voidscape.turmoil.abilities.MeleeAbilities;
import tamaized.voidscape.turmoil.abilities.TankAbilities;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;
import tamaized.voidscape.turmoil.caps.IEffectContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModEffects implements RegistryClass {

	private static final List<MobEffect> CONTEXT_EFFECTS = new ArrayList<>();
	private static final DeferredRegister<MobEffect> REGISTRY = RegUtil.create(ForgeRegistries.MOB_EFFECTS);
	public static final RegistryObject<ToggleEffect> FIRE_ARROW = REGISTRY.register("fire_arrow", () -> new ToggleEffect(MobEffectCategory.BENEFICIAL, 0xFFAA00, TurmoilAbility.Toggle.ArrowShot).
			texture(MageAbilities.FLAME_SHOT.getTexture()));
	public static final RegistryObject<DotEffect> TRAUMATIZE = REGISTRY.register("traumatize", () -> context(new DotEffect(MobEffectCategory.HARMFUL, 0x7700FF).
			texture(MageAbilities.TRAUMATIZE.getTexture())));
	public static final RegistryObject<MobEffect> BULWARK = REGISTRY.register("bulwark", () -> new ToggleEffect(MobEffectCategory.BENEFICIAL, 0x00FFFF).
			texture(TankAbilities.BULWARK.getTexture()).
			addAttributeModifier(ModAttributes.VOIDIC_RES.get(), "360ac01a-0be0-4c85-a078-bbc0cf90a6e1", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL));
	public static final RegistryObject<MobEffect> ADRENALINE = REGISTRY.register("adrenaline", () -> new ToggleEffect(MobEffectCategory.BENEFICIAL, 0xFF7700).
			texture(TankAbilities.ADRENALINE.getTexture()).
			addAttributeModifier(ModAttributes.VOIDIC_RES.get(), "777e0d84-3984-4502-973e-851766de8bc7", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL));
	public static final RegistryObject<MobEffect> TUNNEL_VISION = REGISTRY.register("tunnel_vision", () -> context(new StandardEffect(MobEffectCategory.BENEFICIAL, 0xFF0000).
			texture(TankAbilities.TUNNEL_VISION.getTexture())));
	public static final RegistryObject<ToggleEffect> EMPOWER_SHIELD_2X_NULL = REGISTRY.register("empower_shield_2x_null", () -> new ToggleEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF, TurmoilAbility.Toggle.Empower).
			texture(TankAbilities.EMPOWER_SHIELD_2X_NULL.getTexture()));
	public static final RegistryObject<ToggleEffect> EMPOWER_ATTACK_SLICING = REGISTRY.register("empower_attack_slicing", () -> new ToggleEffect(MobEffectCategory.BENEFICIAL, 0xFF0000, TurmoilAbility.Toggle.Empower).
			texture(MeleeAbilities.EMPOWER_ATTACK_SLICING.getTexture()));
	public static final RegistryObject<DotEffect> EMPOWER_ATTACK_SLICING_DOT = REGISTRY.register("empower_attack_slicing_dot", () -> context(new DotEffect(MobEffectCategory.HARMFUL, 0xFF0000).
			texture(MeleeAbilities.EMPOWER_ATTACK_SLICING.getTexture())));
	public static final RegistryObject<ToggleEffect> EMPOWER_ATTACK_BLEED = REGISTRY.register("empower_attack_bleed", () -> new ToggleEffect(MobEffectCategory.BENEFICIAL, 0xFF0000, TurmoilAbility.Toggle.Empower).
			texture(MeleeAbilities.EMPOWER_ATTACK_BLEED.getTexture()));
	public static final RegistryObject<DotEffect> EMPOWER_ATTACK_BLEED_DOT = REGISTRY.register("empower_attack_bleed_dot", () -> context(new DotEffect(MobEffectCategory.HARMFUL, 0xFF0000).
			texture(MeleeAbilities.EMPOWER_ATTACK_BLEED.getTexture())));
	public static final RegistryObject<MobEffect> SENSE_WEAKNESS = REGISTRY.register("sense_weakness", () -> context(new StandardEffect(MobEffectCategory.BENEFICIAL, 0xFF0000).
			texture(MeleeAbilities.SENSE_WEAKNESS.getTexture())));
	public static final RegistryObject<DotEffect> MIND_WARP = REGISTRY.register("mind_warp", () -> context(new DotEffect(MobEffectCategory.HARMFUL, 0xFF0077).
			texture(HealerAbilities.MIND_WARP.getTexture())));
	public static final RegistryObject<ToggleEffect> EMPOWER_SWORD_OSMOSIS = REGISTRY.register("empower_sword_osmosis", () -> new ToggleEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF, TurmoilAbility.Toggle.Empower).
			texture(HealerAbilities.EMPOWER_SWORD_OSMOSIS.getTexture()));

	private static <T extends MobEffect> T context(T effect) {
		CONTEXT_EFFECTS.add(effect);
		return effect;
	}

	public static boolean hasContext(MobEffect effect) {
		return CONTEXT_EFFECTS.contains(effect);
	}

	public static boolean apply(LivingEntity entity, ToggleEffect effect, int duration, int amp) {
		if (effect.toggle() != TurmoilAbility.Toggle.None) {
			List<MobEffect> remove = new ArrayList<>();
			for (MobEffectInstance e : entity.getActiveEffects())
				if (e.getEffect() instanceof ToggleEffect && ((ToggleEffect) e.getEffect()).toggle() == effect.toggle())
					remove.add(e.getEffect());
			remove.forEach(entity::removeEffect);
		}
		return entity.addEffect(new MobEffectInstance(effect, duration, amp));
	}

	public static boolean dot(LivingEntity caster, LivingEntity entity, DotEffect effect, int duration, int amp, float damage) {
		MobEffectInstance instance = new MobEffectInstance(effect, duration, amp);
		entity.getCapability(SubCapability.CAPABILITY_EFFECTCONTEXT).ifPresent(cap -> cap.add(instance, caster, damage));
		return entity.addEffect(instance);
	}

	public static boolean target(LivingEntity caster, LivingEntity entity, MobEffect effect, int duration, int amp, boolean self) {
		MobEffectInstance instance = new MobEffectInstance(effect, duration, amp);
		(self ? caster : entity).getCapability(SubCapability.CAPABILITY_EFFECTCONTEXT).ifPresent(cap -> cap.add(instance, self ? entity : caster, 0));
		return self ? caster.addEffect(instance) : entity.addEffect(instance);
	}

	@Override
	public void init(IEventBus bus) {

	}

	public static class ToggleEffect extends StandardEffect {

		private final TurmoilAbility.Toggle toggle;

		private ToggleEffect(MobEffectCategory type, int color) {
			this(type, color, TurmoilAbility.Toggle.None);
		}

		private ToggleEffect(MobEffectCategory type, int color, TurmoilAbility.Toggle toggle) {
			super(type, color);
			this.toggle = toggle;
		}

		public TurmoilAbility.Toggle toggle() {
			return toggle;
		}

	}

	public static class DotEffect extends StandardEffect {

		private final int hurtTick;

		private DotEffect(MobEffectCategory type, int color) {
			this(type, color, 20);
		}

		private DotEffect(MobEffectCategory type, int color, int hurtTick) {
			super(type, color);
			this.hurtTick = hurtTick;
		}

		@Override
		public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
			return p_76397_1_ % hurtTick == 0;
		}

		@Override
		public void applyEffectTick(LivingEntity entity, int amp) {
			Optional<IEffectContext.Context> context = entity.getCapability(SubCapability.CAPABILITY_EFFECTCONTEXT).map(cap -> cap.context(this)).orElse(Optional.empty());
			if (context.isPresent())
				entity.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(context.get().source()), context.get().amount());
			else
				entity.hurt(ModDamageSource.VOIDIC, 1F);
		}
	}

	public static class StandardEffect extends MobEffect {

		private Supplier<Supplier<ResourceLocation>> texture = () -> ClientUtil::getMissingTexture;

		private StandardEffect(MobEffectCategory type, int color) {
			super(type, color);
		}

		@Override
		public List<ItemStack> getCurativeItems() {
			return new ArrayList<>(); // Disable Milk
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
				public boolean renderInventoryIcon(MobEffectInstance effect, EffectRenderingInventoryScreen<?> gui, PoseStack mStack, int x, int y, int z) {
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
				public boolean renderGuiIcon(MobEffectInstance effect, Gui gui, PoseStack mStack, int x, int y, float z, float alpha) {
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
					return true;
				}
			});
		}
	}

}
