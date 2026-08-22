package tamaized.voidscape.coremod.transformers.render;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import tamaized.voidscape.coremod.ASMUtil;

import java.util.Set;

/**
 * {@link tamaized.voidscape.asm.ASMHooks#isBowInRenderingHand}
 */
public class ItemInHandRendererIsBowTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("item_in_hand_renderer_is_bow");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(node, Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "is", "(Lnet/minecraft/world/item/Item;)Z")
			.forEach(instruction -> {
				if (!(instruction.getPrevious() instanceof FieldInsnNode item))
					return;
				if (!(item.getPrevious() instanceof VarInsnNode stack))
					return;
				node.instructions.insert(instruction, ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, stack.var),
					new FieldInsnNode(Opcodes.GETSTATIC, item.owner, item.name, item.desc),
					ASMUtil.invokeAsmHook("isBowInRenderingHand", "(ZLnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item;)Z")
				));
			});
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.client.renderer.ItemInHandRenderer",
			"evaluateWhichHandsToRender",
			"(Lnet/minecraft/client/player/LocalPlayer;)Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;"
		), new Target(
			"net.minecraft.client.renderer.ItemInHandRenderer",
			"selectionUsingItemWhileHoldingBowLike",
			"(Lnet/minecraft/client/player/LocalPlayer;)Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;"
		), new Target(
			"net.minecraft.client.renderer.ItemInHandRenderer",
			"isChargedCrossbow",
			"(Lnet/minecraft/world/item/ItemStack;)Z"
		));
	}

}