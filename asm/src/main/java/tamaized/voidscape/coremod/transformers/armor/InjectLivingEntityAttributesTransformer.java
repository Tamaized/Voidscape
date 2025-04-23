package tamaized.voidscape.coremod.transformers.armor;

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
 * {@link tamaized.voidscape.asm.ASMHooks#injectLivingEntityAttributes}
 */
public class InjectLivingEntityAttributesTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findLast(ASMUtil.findFieldInstructions(node, Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "attributes")).ifPresent(instruction -> {
			node.instructions.insert(instruction, ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				ASMUtil.invokeAsmHook("injectLivingEntityAttributes", "(Lnet/minecraft/world/entity/LivingEntity;)V")
			));
		});
		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.world.entity.LivingEntity",
			"<init>",
			"(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}
