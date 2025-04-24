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
 * {@link tamaized.voidscape.asm.ASMHooks#modifyEntityTransparency}
 */
public class ModifyEntityRenderTransparencyTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findMethodInstructions(node, Opcodes.INVOKEVIRTUAL, "net/minecraft/client/model/EntityModel", "renderToBuffer", "(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V")
			.findFirst()
			.ifPresent(instruction -> {
				node.instructions.insertBefore(instruction, ASMAPI.listOf(
					new VarInsnNode(Opcodes.ALOAD, 1),
					ASMUtil.invokeAsmHook("modifyEntityTransparency", "(FLnet/minecraft/world/entity/LivingEntity;)F")
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
			"net.minecraft.client.renderer.entity.LivingEntityRenderer",
			"render",
			"(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}
