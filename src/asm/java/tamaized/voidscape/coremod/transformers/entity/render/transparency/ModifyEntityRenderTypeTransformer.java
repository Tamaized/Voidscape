package tamaized.voidscape.coremod.transformers.entity.render.transparency;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import tamaized.voidscape.coremod.ASMUtil;

import java.util.Set;

/**
 * {@link tamaized.voidscape.asm.ClientASMHooks#modifyEntityRenderType}
 */
public class ModifyEntityRenderTypeTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("modify_entity_render_type");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findVarInstructions(node, Opcodes.ILOAD, 3)
			.findFirst()
			.ifPresent(instruction -> node.instructions.insert(instruction, ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 1),
				ASMUtil.invokeClientAsmHook("modifyEntityRenderType", "(ZLnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Z")
			)));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.client.renderer.entity.LivingEntityRenderer",
			"getRenderType",
			"(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/renderer/rendertype/RenderType;"
		));
	}

}
