package tamaized.voidscape.coremod.transformers.visibility;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import tamaized.voidscape.coremod.ASMUtil;

import java.util.Set;
import java.util.stream.Stream;

/**
 * {@link tamaized.voidscape.asm.ASMHooks#lightTextureBrightness}
 */
public class LightTextureNightVisionAndGammaTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("light_texture_night_vision_and_gamma");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		Stream.concat(
				ASMUtil.findVarInstructions(node, Opcodes.FSTORE, 9), // Night Vision
				ASMUtil.findVarInstructions(node, Opcodes.FSTORE, 20) // Gamma
			)
			.forEach(instruction -> node.instructions.insertBefore(instruction, ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 2),
				ASMUtil.invokeAsmHook("nightVisionAndGamma", "(FLnet/minecraft/world/level/Level;)F")
			)));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.client.renderer.LightTexture",
			"updateLightTexture",
			"(F)V"
		));
	}

}
