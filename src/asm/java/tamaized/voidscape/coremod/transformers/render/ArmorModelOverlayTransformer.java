package tamaized.voidscape.coremod.transformers.render;

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
 * {@link tamaized.voidscape.asm.ASMHooks#armorOverlay}
 */
public class ArmorModelOverlayTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findMethodInstructions(
			node,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/client/renderer/entity/layers/HumanoidArmorLayer",
			"renderModel",
			"(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/Model;ILnet/minecraft/resources/ResourceLocation;)V"
		).forEach(instruction -> {
			node.instructions.insert(instruction, ASMAPI.listOf(
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
		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer",
			"renderArmorPiece",
			"(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}