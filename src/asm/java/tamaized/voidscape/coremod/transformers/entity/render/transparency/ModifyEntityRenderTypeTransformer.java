package tamaized.voidscape.coremod.transformers.entity.render.transparency;

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
 * {@link tamaized.voidscape.asm.ASMHooks#modifyEntityRenderType}
 */
public class ModifyEntityRenderTypeTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findMethodInstructions(node, Opcodes.INVOKEVIRTUAL, "net/minecraft/client/model/EntityModel", "renderType", "(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;")
			.findFirst()
			.ifPresent(instruction -> node.instructions.insert(instruction, ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new VarInsnNode(Opcodes.ALOAD, 1),
				ASMUtil.invokeAsmHook("modifyEntityRenderType", "(Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/client/renderer/RenderType;")
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
			"net.minecraft.client.renderer.entity.LivingEntityRenderer",
			"getRenderType",
			"(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}
