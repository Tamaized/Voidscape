package tamaized.voidscape.item.tool;

import com.google.common.collect.Sets;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import tamaized.regutil.ExtraTooltipContext;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LootingWarhammer extends BreakableWarhammer {

	private static final Set<ItemAbility> ACTIONS = Stream.of(
		ItemAbilities.SWORD_SWEEP
	).collect(Collectors.toCollection(Sets::newIdentityHashSet));

	public LootingWarhammer(ToolMaterial material, Item.Properties properties, Consumer<ExtraTooltipContext> tooltipConsumer) {
		super(material, properties, tooltipConsumer);
	}

	@Override
	public boolean canPerformAction(ItemInstance stack, ItemAbility itemAbility) {
		HolderLookup.RegistryLookup<Enchantment> registry = CommonHooks.resolveLookup(Registries.ENCHANTMENT);
		if (registry == null)
			return super.canPerformAction(stack, itemAbility);
		return registry.get(Enchantments.SWEEPING_EDGE)
			.filter(value -> stack.getEnchantmentLevel(value) > 0)
			.map(_ -> ACTIONS.contains(itemAbility))
			.orElseGet(() -> super.canPerformAction(stack, itemAbility));
	}

}
