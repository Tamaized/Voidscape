package tamaized.voidscape.coremod.transformers.elytra;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import tamaized.voidscape.coremod.ASMUtil;

import java.util.Set;

/**
 * {@link tamaized.voidscape.asm.ASMHooks#disableCapeRender}
 */
public class DisableCapeRenderTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("disable_cape_render");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(node, Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "is", "(Lnet/minecraft/world/item/Item;)Z")
			.findFirst()
			.ifPresent(instruction -> {
				node.instructions.insert(instruction, ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 12),
					ASMUtil.invokeAsmHook("disableCapeRender", "(ZLnet/minecraft/world/item/ItemStack;)Z")
				));
			});
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.client.renderer.entity.layers.CapeLayer",
			"render",
			"(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V"
		));
	}

}
