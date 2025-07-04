package tamaized.voidscape.coremod.transformers.render;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.neoforged.coremod.api.ASMAPI;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import tamaized.voidscape.coremod.ASMUtil;

import java.util.Set;

/**
 * {@link tamaized.voidscape.asm.ASMHooks#isBowInRenderingHand}
 */
public class ItemInHandRendererIsBowTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findMethodInstructions(node, Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "is", "(Lnet/minecraft/world/item/Item;)Z")
			.forEach(instruction -> {
				if (!(instruction.getPrevious() instanceof FieldInsnNode item))
					return;
				if (!(item.getPrevious() instanceof VarInsnNode stack))
					return;
				node.instructions.insert(instruction, ASMAPI.listOf(
					new VarInsnNode(Opcodes.ALOAD, stack.var),
					new FieldInsnNode(Opcodes.GETSTATIC, item.owner, item.name, item.desc),
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
			"net.minecraft.client.renderer.ItemInHandRenderer",
			"evaluateWhichHandsToRender",
			"(Lnet/minecraft/client/player/LocalPlayer;)Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;"
		), Target.targetMethod(
			"net.minecraft.client.renderer.ItemInHandRenderer",
			"selectionUsingItemWhileHoldingBowLike",
			"(Lnet/minecraft/client/player/LocalPlayer;)Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;"
		), Target.targetMethod(
			"net.minecraft.client.renderer.ItemInHandRenderer",
			"isChargedCrossbow",
			"(Lnet/minecraft/world/item/ItemStack;)Z"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}