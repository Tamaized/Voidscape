package tamaized.voidscape.coremod.transformers.render;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import tamaized.voidscape.coremod.ASMUtil;

import java.util.Set;

/**
 * {@link tamaized.voidscape.asm.ASMHooks#armorOverlay}
 */
public class ArmorModelOverlayTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("armor_model_overlay");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(
			node,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/client/renderer/entity/layers/HumanoidArmorLayer",
			"renderModel",
			"(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/Model;ILnet/minecraft/resources/Identifier;)V"
		).forEach(instruction -> {
			node.instructions.insert(instruction, ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new VarInsnNode(Opcodes.ALOAD, 21),
				new VarInsnNode(Opcodes.ALOAD, 1),
				new VarInsnNode(Opcodes.ALOAD, 2),
				new VarInsnNode(Opcodes.ILOAD, 5),
				new VarInsnNode(Opcodes.ALOAD, 15),
				new VarInsnNode(Opcodes.ALOAD, 3),
				new VarInsnNode(Opcodes.ALOAD, 13),
				new VarInsnNode(Opcodes.ALOAD, 4),
				ASMUtil.invokeAsmHook("armorOverlay", "(Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;Lnet/minecraft/world/item/ArmorMaterial$Layer;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/Model;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)V")
			));
		});
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer",
			"renderArmorPiece",
			"(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V"
		));
	}

}