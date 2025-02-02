package tamaized.voidscape.registry;

import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.tool.set.*;

@Component
public record ModToolSetComponentDirectory(
	@Autowired AstralToolSet astralToolSet,
	@Autowired CharredToolSet charredToolSet,
	@Autowired CorruptToolSet corruptToolSet,
	@Autowired IchorToolSet ichorToolSet,
	@Autowired SpellTomeSet spellTomeSet,
	@Autowired TitaniteToolSet titaniteToolSet,
	@Autowired VoidicCrystalToolSet voidicCrystalToolSet
) {
}
