package tamaized.voidscape.registry.tool;

import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;
import tamaized.voidscape.registry.item.*;

@Component
public record ModItemComponentDirectory(
	@Autowired AugmentItems augmentItems,
	@Autowired EtherealFruitItems etherealFruitItems,
	@Autowired MaterialItems materialItems,
	@Autowired MiscItems miscItems,
	@Autowired PartItems partItems,
	@Autowired ModToolSetComponentDirectory toolSetComponentDirectory,
	@Autowired ModArmorSetComponentDirectory modArmorSetComponentDirectory
	) {
}
