package tamaized.voidscape.asm;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import tamaized.beanification.Autowired;
import tamaized.regutil.ToolAndArmorHelper;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.event.QuiverHandler;
import tamaized.voidscape.registry.ModDataAttachments;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression"})
public class ASMHooks {

	@Autowired
	private static ModDataAttachments dataAttachments;

	@Autowired
	private static QuiverHandler quiverHandler;

	@Autowired
	private static ToolAndArmorHelper toolAndArmorHelper;
	/**
	 * {@link tamaized.voidscape.coremod.transformers.biome.BiomeSnowAndFreezeTransformer}<p>
	 *
	 * Injection Point:<br>
	 * {@link Biome#shouldSnow(LevelReader, BlockPos)} and {@link Biome#shouldFreeze(LevelReader, BlockPos, boolean)}<br>
	 */
	public static boolean shouldBiomeHaveSnowfallAndLiquidFreeze(boolean o, Biome biome, LevelReader level) {
		if (!o) // Short-circuit
			return false;

		RegistryAccess registryAccess = level instanceof ServerLevel serverLevel ? serverLevel.registryAccess() :
			level instanceof WorldGenRegion worldGenRegion ? worldGenRegion.registryAccess() :
				null;
		return registryAccess == null || !registryAccess.lookupOrThrow(Registries.BIOME).getResourceKey(biome)
			.map(key -> key.identifier().getNamespace().equals(Voidscape.MODID))
			.orElse(false);
	}

	/**
	 * {@link tamaized.voidscape.coremod.transformers.render.ItemInHandRendererIsBowTransformer}<p>
	 *
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.ItemInHandRenderer#evaluateWhichHandsToRender(LocalPlayer)}<br>
	 * {@link net.minecraft.client.renderer.ItemInHandRenderer#selectionUsingItemWhileHoldingBowLike(LocalPlayer)}<br>
	 * {@link net.minecraft.client.renderer.ItemInHandRenderer#isChargedCrossbow(ItemStack)}
	 */
	public static boolean isBowInRenderingHand(boolean o, ItemStack stack, Item item) {
		return o || toolAndArmorHelper.isMyBow(stack, item);
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.item.ProjectileWeaponItem#useAmmo(ItemStack, ItemStack, LivingEntity, boolean)} <br>
	 */
	public static ItemStack useAmmo(ItemStack result, ItemStack bow, ItemStack ammo, LivingEntity shooter) {
		return quiverHandler.useAmmo(result, bow, ammo, shooter);
	}

}
