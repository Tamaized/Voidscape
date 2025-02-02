package tamaized.voidscape.registry;

import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.armor.set.*;

@Component
public record ModArmorSetComponentDirectory(
	@Autowired AstralArmorSet astralArmorSet,
	@Autowired CorruptArmorSet corruptArmorSet,
	@Autowired IchorArmorSet ichorArmorSet,
	@Autowired TitaniteArmorSet titaniteArmorSet,
	@Autowired VoidicCrystalArmorSet voidicCrystalArmorSet
) {
}
