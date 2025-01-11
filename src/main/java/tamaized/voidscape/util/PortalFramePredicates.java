package tamaized.voidscape.util;

import net.minecraft.world.level.block.state.BlockBehaviour;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.block.FunctionalBlocks;
import tamaized.voidscape.registry.block.ImposterBlocks;
import tamaized.voidscape.registry.block.MaterialBlocks;

@Component
public class PortalFramePredicates {

	public final BlockBehaviour.StatePredicate FRAME_TEST;
	public final BlockBehaviour.StatePredicate PORTAL_TEST;
	public final BlockBehaviour.StatePredicate IGNITER_TEST;

	public PortalFramePredicates(@Autowired MaterialBlocks materialBlocks, @Autowired ImposterBlocks imposterBlocks, @Autowired FunctionalBlocks functionalBlocks) {
		FRAME_TEST = (state, reader, pos) -> state.is(materialBlocks.VOIDIC_CRYSTAL_BLOCK.get()) || state.is(imposterBlocks.FRAGILE_VOIDIC_CRYSTAL_BLOCK.get());
		PORTAL_TEST = (state, reader, pos) -> state.is(functionalBlocks.PORTAL.get());
		IGNITER_TEST = (state, reader, pos) -> false;
	}

}
