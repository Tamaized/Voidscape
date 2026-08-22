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
 * {@link tamaized.voidscape.asm.ASMHooks#modifyEntityRenderType}
 */
public class ModifyEntityRenderTypeTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("modify_entity_render_type");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(node, Opcodes.INVOKEVIRTUAL, "net/minecraft/client/model/EntityModel", "renderType", "(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/RenderType;")
			.findFirst()
			.ifPresent(instruction -> node.instructions.insert(instruction, ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new VarInsnNode(Opcodes.ALOAD, 1),
				ASMUtil.invokeAsmHook("modifyEntityRenderType", "(Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/client/renderer/RenderType;")
			)));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.client.renderer.entity.LivingEntityRenderer",
			"getRenderType",
			"(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;"
		));
	}

}
