package tamaized.voidscape.asm;

import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.world.VoidTeleporter;

@SuppressWarnings({"JavadocReference", "unused"})
public class ASMHooks {

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.entity.Entity#func_241829_a}<br>
	 * First [ARETURN] (return null)
	 */
	public static PortalInfo portalInfo(Entity entity, ServerWorld world) {
		if (Voidscape.checkForVoidDimension(entity.level) || Voidscape.checkForVoidDimension(world))
			return VoidTeleporter.position(entity, world);
		return null;
	}

}
