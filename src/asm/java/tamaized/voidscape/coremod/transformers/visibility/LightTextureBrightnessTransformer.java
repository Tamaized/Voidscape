package tamaized.voidscape.coremod.transformers.visibility;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import tamaized.voidscape.coremod.ASMUtil;

import java.util.Set;

/**
 * {@link tamaized.voidscape.asm.ASMHooks#lightTextureBrightness}
 */
public class LightTextureBrightnessTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("light_texture_brightness");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findInstructions(node, Opcodes.FRETURN)
			.forEach(instruction -> node.instructions.insertBefore(instruction, ASMUtil.listOf(
				new VarInsnNode(Opcodes.ILOAD, 1),
				ASMUtil.invokeAsmHook("lightTextureBrightness", "(FI)F")
			)));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.client.renderer.LightTexture",
			"getBrightness",
			"(Lnet/minecraft/world/level/dimension/DimensionType;I)F"
		));
	}

}
