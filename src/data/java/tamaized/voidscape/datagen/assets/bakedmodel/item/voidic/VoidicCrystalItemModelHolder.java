package tamaized.voidscape.datagen.assets.bakedmodel.item.voidic;

import tamaized.voidscape.datagen.assets.bakedmodel.ItemModelHolder;

public abstract class VoidicCrystalItemModelHolder extends ItemModelHolder {

	@Override
	protected String nameToUse() {
		return super.nameToUse().replaceFirst("_crystal", "");
	}
}
