package tamaized.voidscape.asm;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.capability.SubCapability;
import tamaized.voidscape.client.ModelBakeListener;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.registry.ModArmors;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModItems;
import tamaized.voidscape.registry.ModTools;
import tamaized.voidscape.world.VoidTeleporter;

import java.util.Map;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression"})
public class ASMHooks {

	public static float PlayerEntity_getAttackStrengthScale;

	/**
	 * Injection Point:<br>
	 * {@link LivingEntity#LivingEntity(EntityType, Level)}<br>
	 * [AFTER] PUTFIELD : attributes
	 */
	public static void handleEntityAttributes(LivingEntity entity) {
		AttributeSupplier.Builder n = AttributeSupplier.builder();
		n.builder.putAll(entity.attributes.supplier.instances);
		ModAttributes.assignAttributes(n);
		entity.attributes = new AttributeMap(n.build());
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.layers.CapeLayer#render(PoseStack, MultiBufferSource, int, AbstractClientPlayer, float, float, float, float, float, float)}<br>
	 * [AFTER] INVOKEVIRTUAL {@link ItemStack#is(Item)}
	 */
	public static boolean capeLayer(boolean o, ItemStack stack) {
		return o || stack.is(ModArmors.CORRUPT_CHEST.get()) || ModArmors.elytra(stack);
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.layers.ElytraLayer#shouldRender(ItemStack, LivingEntity)}<br>
	 * [BEFORE] IRETURN
	 */
	public static boolean elytraLayer(boolean o, ItemStack stack) {
		return o || ModArmors.elytra(stack);
	}

	/**
	 * Injection Point:<br>
	 * {@link LivingEntityRenderer#render(LivingEntity, float, float, PoseStack, MultiBufferSource, int)}<br>
	 * [BEFORE] INVOKEVIRTUAL : {@link EntityModel#renderToBuffer(PoseStack, VertexConsumer, int, int, float, float, float, float)}
	 */
	public static float handleEntityTransparency(float alpha, LivingEntity entity) {
		return Math.min(entity.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapInsanity).map(data -> Mth.clamp(1F - data.getInfusion() / 600F, 0F, 1F)).orElse(1F)).orElse(1F), alpha);
	}

	/**
	 * Injection Point:<br>
	 * {@link LivingEntityRenderer#getRenderType(LivingEntity, boolean, boolean, boolean)}<br>
	 * [AFTER] INVOKEVIRTUAL : {@link EntityModel#renderType(ResourceLocation)}
	 */
	@OnlyIn(Dist.CLIENT)
	public static RenderType handleEntityTransparencyRenderType(RenderType type, LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer, LivingEntity entity) {
		return entity.level() != null && Voidscape.checkForVoidDimension(entity.level()) && !(entity instanceof IEthereal) ? RenderType.entityTranslucentCull(renderer.getTextureLocation(entity)) : type;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraftforge.common.ForgeHooks#onLivingDeath(LivingEntity, DamageSource)}<br>
	 * [BEFORE FIRST GETSTATIC]
	 */
	public static boolean death(LivingEntity entity, DamageSource source) {
		if (entity instanceof ServerPlayer player) {
			if (Voidscape.checkForVoidDimension(player.level())) {
				player.setHealth(entity.getMaxHealth() * 0.1F);
				player.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
				if (!player.level().isClientSide())
					player.changeDimension(Voidscape.getPlayersSpawnLevel(player), VoidTeleporter.INSTANCE);
				return true;
			}
		} else {
			if ((source.getDirectEntity() instanceof Player || source.getEntity() instanceof Player) && Voidscape.checkForVoidDimension(entity.level()) && entity.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapInsanity).map(data -> data.
					getInfusion() > 200).orElse(false)).orElse(false) && entity.getRandom().nextInt(3) == 0) {
				Containers.dropItemStack(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ModItems.ETHEREAL_ESSENCE.get()));
			}
		}
		return MinecraftForge.EVENT_BUS.post(new LivingDeathEvent(entity, source));
	}

	/**
	 * Injection Point:<br>
	 * {@link Player#attack(Entity)}<br>
	 * [AFTER INVOKEVIRTUAL {@link Player#getAttackStrengthScale(float)}]
	 */
	public static synchronized float getAttackStrengthScale(float o) {
		PlayerEntity_getAttackStrengthScale = o;
		return o;
	}

	/**
	 * Injection Point:<br>
	 * {@link Biome#shouldSnow(LevelReader, BlockPos)} and {@link Biome#shouldFreeze(LevelReader, BlockPos, boolean)}<br>
	 * [AFTER ICONST_1]
	 */
	public static boolean shouldSnow(boolean o, Biome biome, LevelReader level) {
		RegistryAccess registryAccess = level instanceof ServerLevel serverLevel ? serverLevel.registryAccess() :
				level instanceof WorldGenRegion worldGenRegion ? worldGenRegion.registryAccess() :
						null;
		if (registryAccess != null && registryAccess.registryOrThrow(Registries.BIOME).getResourceKey(biome)
				.map(key -> key.location().getNamespace().equals(Voidscape.MODID))
				.orElse(false))
			return false;
		return o;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.item.enchantment.EnchantmentCategory#canEnchant(Item)}<br>
	 * [BEFORE IRETURN]
	 */
	public static boolean axesRWeps(boolean o, Item i) { // TODO: why is this here? Can't we just use IForgeItem#canApplyAtEnchantingTable ??????
		return o || i instanceof RegUtil.ToolAndArmorHelper.LootingAxe || i instanceof ModTools.LootingWarhammer;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.LightTexture#getBrightness(net.minecraft.world.level.dimension.DimensionType, int)}<br>
	 * [BEFORE FRETURN]
	 */
	public static float visibility(float o, int light) {
		if (Voidscape.checkForVoidDimension(Minecraft.getInstance().level))
			return VoidVisibilityCache.value(o, light);
		return o;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.LightTexture#updateLightTexture(float)}<br>
	 * [AFTER FIRST FLOAD 6]
	 */
	public static float cancelNightVision(float o, Level level) {
		if (o > 0 && level.isClientSide() && Voidscape.checkForVoidDimension(level))
			return 0;
		return o;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.LightTexture#updateLightTexture(float)}<br>
	 * [AFTER GETFIELD {@link net.minecraft.client.Options#gamma}]
	 */
	public static float cancelGamma(float o, Level level) {
		if (o > 0 && level.isClientSide() && Voidscape.checkForVoidDimension(level))
			return 0;
		return o;
	}

	/**
	 * Injection Point:<br>
	 * {@link ModelBakery#ModelBakery(BlockColors, ProfilerFiller, Map, Map)}<br>
	 * [BEFORE FIRST GETSTATIC {@link net.minecraft.core.registries.BuiltInRegistries#ITEM)}]
	 */
	@OnlyIn(Dist.CLIENT)
	public static void redirectModels(ModelBakery bakery) {
		try {
			ModelBakeListener.redirectModels(bakery);
		} catch (NullPointerException e) {
			// Another mod crashed earlier on, this will throw a NPE when the registry isn't populated, just fail silently and let the game error properly later
		}
	}

	/**
	 * Injection Point:<br>
	 * {@link ModelBakery#ModelBakery(BlockColors, ProfilerFiller, Map, Map)}<br>
	 * [BEFORE LAST INVOKESTATIC {@link com.google.common.collect.Sets#newHashSet()}]
	 */
	@OnlyIn(Dist.CLIENT)
	public static void cleanModels(ModelBakery bakery) {
		ModelBakeListener.clearOldModels(bakery);
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.ItemInHandRenderer) all potential methods}<br>
	 * [AFTER ALL INVOKESTATIC {@link ItemStack#is(Item)}]
	 */
	@OnlyIn(Dist.CLIENT)
	public static boolean isMyBow(boolean o, ItemStack stack, Item item) {
		return o || RegUtil.isMyBow(stack, item);
	}

	/**
	 * Injection Point:<br>
	 * {@link HumanoidArmorLayer#renderArmorPiece(PoseStack, MultiBufferSource, LivingEntity, EquipmentSlot, int, HumanoidModel)}<br>
	 * [AFTER LAST INVOKEVIRTUAL {@link HumanoidArmorLayer#renderModel(PoseStack, MultiBufferSource, int, net.minecraft.world.item.ArmorItem, Model, boolean, float, float, float, ResourceLocation)}]
	 */
	@OnlyIn(Dist.CLIENT)
	public static void armorOverlay(HumanoidArmorLayer<?, ?, ?> layer, PoseStack poseStack, MultiBufferSource bufferSource, int light, boolean p_117111_, Model model, LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
		if (RegUtil.isArmorOverlay(stack)) {
			RegUtil.renderingArmorOverlay = true;
			ResourceLocation texture = layer.getArmorResource(entity, stack, slot, "overlay");
			VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.armorCutoutNoCull(texture), false, false);
			model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			RegUtil.renderingArmorOverlay = false;
		}
	}

}
