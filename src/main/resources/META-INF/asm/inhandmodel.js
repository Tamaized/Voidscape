// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

    return {
        'inhandmodel': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.ItemRenderer',
                'methodName': ASM.mapMethod('m_174264_'), // getModel
                'methodDesc': '(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)Lnet/minecraft/client/resources/model/BakedModel;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstMethodCall(methodNode,
                        ASM.MethodType.VIRTUAL,
                        'net/minecraft/client/renderer/ItemModelShaper',
                        ASM.mapMethod('m_109406_'), // getItemModel
                        '(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;'
                        ),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        // itemModelShaper
                        new FieldInsnNode(Opcodes.GETFIELD, 'net/minecraft/client/renderer/entity/ItemRenderer', ASM.mapField('f_115095_'), 'Lnet/minecraft/client/renderer/ItemModelShaper;'),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'inHandModel',
                            '(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/ItemModelShaper;)Lnet/minecraft/client/resources/model/BakedModel;',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'inguimodel': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.ItemRenderer',
                'methodName': ASM.mapMethod('m_115143_'), // render
                'methodDesc': '(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstMethodCall(methodNode,
                        ASM.MethodType.STATIC,
                        'net/minecraftforge/client/ForgeHooksClient',
                        'handleCameraTransforms',
                        '(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemDisplayContext;Z)Lnet/minecraft/client/resources/model/BakedModel;'
                    ),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        // itemModelShaper
                        new FieldInsnNode(Opcodes.GETFIELD, 'net/minecraft/client/renderer/entity/ItemRenderer', ASM.mapField('f_115095_'), 'Lnet/minecraft/client/renderer/ItemModelShaper;'),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'inGuiModel',
                            '(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/ItemModelShaper;)Lnet/minecraft/client/resources/model/BakedModel;',
                            false
                        )
                    )
                );
                return methodNode;
            }
        }
    }
}
