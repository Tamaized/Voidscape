package tamaized.voidscape.coremod.transformers.visibility;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.neoforged.coremod.api.ASMAPI;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import tamaized.voidscape.coremod.ASMUtil;

import java.util.Set;
import java.util.stream.Stream;

/**
 * {@link tamaized.voidscape.asm.ASMHooks#lightTextureBrightness}
 */
public class LightTextureNightVisionAndGammaTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		Stream.concat(
				ASMUtil.findVarInstructions(node, Opcodes.FSTORE, 9), // Night Vision
				ASMUtil.findVarInstructions(node, Opcodes.FSTORE, 20) // Gamma
			)
			.forEach(instruction -> node.instructions.insertBefore(instruction, ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 2),
				ASMUtil.invokeAsmHook("nightVisionAndGamma", "(FLnet/minecraft/world/level/Level;)F")
			)));
		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.client.renderer.LightTexture",
			"updateLightTexture",
			"(F)V"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}
