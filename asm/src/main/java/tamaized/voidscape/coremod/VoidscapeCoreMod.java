package tamaized.voidscape.coremod;

import cpw.mods.modlauncher.api.ITransformer;
import net.neoforged.neoforgespi.coremod.ICoreMod;
import tamaized.voidscape.coremod.transformers.armor.InjectLivingEntityAttributesTransformer;

import java.util.List;

public class VoidscapeCoreMod implements ICoreMod {
	@Override
	public Iterable<? extends ITransformer<?>> getTransformers() {
		return List.of(
			new InjectLivingEntityAttributesTransformer()
		);
	}
}