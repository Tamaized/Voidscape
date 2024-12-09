package tamaized.voidscape.item.tool;

import com.google.common.collect.Sets;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import tamaized.regutil.RegUtil;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LootingWarhammer extends BreakableWarhammer {

	private static final Set<ItemAbility> ACTIONS = Stream.concat(
		ItemAbilities.DEFAULT_PICKAXE_ACTIONS.stream(),
		Stream.of(ItemAbilities.SWORD_SWEEP)
	).collect(Collectors.toCollection(Sets::newIdentityHashSet));

	public LootingWarhammer(Tier tier, Properties properties, Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer) {
		super(tier, properties, tooltipConsumer);
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
		HolderLookup.RegistryLookup<Enchantment> registry = CommonHooks.resolveLookup(Registries.ENCHANTMENT);
		if (registry == null)
			return super.canPerformAction(stack, itemAbility);
		return registry.get(Enchantments.SWEEPING_EDGE)
			.filter(value -> stack.getEnchantmentLevel(value) > 0)
			.map(value -> ACTIONS.contains(itemAbility))
			.orElseGet(() -> super.canPerformAction(stack, itemAbility));
	}

}
