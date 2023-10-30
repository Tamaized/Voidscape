// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

    return {
        'overlay': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer',
                'methodName': ASM.mapMethod('m_117118_'), // renderArmorPiece
                'methodDesc': '(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var /*org.objectweb.asm.tree.MethodInsnNode*/ node = instructions.get(index);
                    if (node.getOpcode() === Opcodes.INVOKEVIRTUAL &&
                        node.owner === 'net/minecraft/client/renderer/entity/layers/HumanoidArmorLayer' &&
                        node.name === 'renderModel') // Forge Method
                        lastInstruction = node; // Get the last INVOKEVIRTUAL
                }
                ASM.log('INFO', lastInstruction);
                if (lastInstruction != null) {
                    instructions.insert(
                        lastInstruction,
                        ASM.listOf(
                            new VarInsnNode(Opcodes.ALOAD, 0),
                            new VarInsnNode(Opcodes.ALOAD, 1),
                            new VarInsnNode(Opcodes.ALOAD, 2),
                            new VarInsnNode(Opcodes.ILOAD, 5),
                            new VarInsnNode(Opcodes.ILOAD, 11),
                            new VarInsnNode(Opcodes.ALOAD, 10),
                            new VarInsnNode(Opcodes.ALOAD, 3),
                            new VarInsnNode(Opcodes.ALOAD, 7),
                            new VarInsnNode(Opcodes.ALOAD, 4),
                            new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'tamaized/voidscape/asm/ASMHooks',
                                'armorOverlay',
                                '(Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IZLnet/minecraft/client/model/Model;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)V',
                                false
                                )
                            )
                        );
                }
                return methodNode;
            }
        }
    }
}
