package tamaized.voidscape.registry.tool;

import com.google.common.base.Suppliers;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModItemTags;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class VoidToolLadder {

	public record Rung(
		int level,
		TagKey<Item> tools,
		Optional<TagKey<Block>> needsTool,
		Optional<TagKey<Block>> incorrectBlocks
	) {
	}

	@Autowired
	private ModItemTags itemTags;

	@Autowired
	private IncorrectBlocksForToolModTagKeys tagKeys;

	private final Supplier<List<Rung>> rungs = Suppliers.memoize(() -> List.of(
		new Rung(0, itemTags.VOIDIC_CRYSTAL_TOOLS, Optional.of(tagKeys.NEEDS_VOIDIC_TOOL), Optional.of(tagKeys.INCORRECT_FOR_VOIDIC_CRYSTAL)),
		new Rung(1, itemTags.CHARRED_TOOLS, Optional.empty(), Optional.empty()),
		new Rung(2, itemTags.CORRUPT_TOOLS, Optional.of(tagKeys.NEEDS_CORRUPT_TOOL), Optional.of(tagKeys.INCORRECT_FOR_CORRUPT)),
		new Rung(3, itemTags.TITANITE_TOOLS, Optional.of(tagKeys.NEEDS_TITANITE_TOOL), Optional.of(tagKeys.INCORRECT_FOR_TITANITE)),
		new Rung(4, itemTags.ICHOR_TOOLS, Optional.of(tagKeys.NEEDS_ICHOR_TOOL), Optional.of(tagKeys.INCORRECT_FOR_ICHOR)),
		new Rung(5, itemTags.ASTRAL_TOOLS, Optional.of(tagKeys.NEEDS_ASTRAL_TOOL), Optional.of(tagKeys.INCORRECT_FOR_ASTRAL))
	));

	public List<Rung> rungs() {
		return rungs.get();
	}

	public List<TagKey<Block>> needsToolTags() {
		return rungs().stream()
			.map(Rung::needsTool)
			.flatMap(Optional::stream)
			.toList();
	}

	public List<TagKey<Block>> blocksAbove(Rung rung) {
		return rungs().stream()
			.filter(other -> other.level() > rung.level())
			.map(Rung::needsTool)
			.flatMap(Optional::stream)
			.toList();
	}

	public int requiredLevel(BlockState state) {
		return rungs().stream()
			.filter(rung -> rung.needsTool().filter(state::is).isPresent())
			.mapToInt(Rung::level)
			.max()
			.orElse(-1);
	}

	public int toolLevel(ItemStack stack) {
		return rungs().stream()
			.filter(rung -> stack.is(rung.tools()))
			.mapToInt(Rung::level)
			.max()
			.orElse(-1);
	}

}
