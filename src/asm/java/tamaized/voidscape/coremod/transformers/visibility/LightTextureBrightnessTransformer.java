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

/**
 * {@link tamaized.voidscape.asm.ASMHooks#lightTextureBrightness}
 */
public class LightTextureBrightnessTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findInstructions(node, Opcodes.FRETURN)
			.forEach(instruction -> node.instructions.insertBefore(instruction, ASMAPI.listOf(
				new VarInsnNode(Opcodes.ILOAD, 1),
				ASMUtil.invokeAsmHook("lightTextureBrightness", "(FI)F")
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
			"getBrightness",
			"(Lnet/minecraft/world/level/dimension/DimensionType;I)F"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}
