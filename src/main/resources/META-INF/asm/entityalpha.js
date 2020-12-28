function initializeCoreMod() {
    return {
        'entityalpha': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.LivingRenderer',
                'methodName': Java.type("net.minecraftforge.coremod.api.ASMAPI").mapMethod('func_225623_a_'),
                'methodDesc': '(Lnet/minecraft/entity/LivingEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) { // Stupid way to cast in JS to avoid warnings and fix autocomplete
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    methodNode.instructions.insertBefore(
                        ASM.findFirstMethodCall(methodNode,
                            ASM.MethodType.VIRTUAL,
                            'net/minecraft/client/renderer/entity/model/EntityModel',
                            ASM.mapMethod('func_225598_a_'),
                            '(Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFF)V'
                            ),
                        ASM.listOf(
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 1), // PUSH {method param 1 (LivingEntity)} TO THE TOP OF THE STACK
                            new org.objectweb.asm.tree.MethodInsnNode( // INVOKE tamaized.voidscape.asm.ASMHooks#handleEntityTransparency(float, LivingEntity)
                                Opcodes.INVOKESTATIC,
                                'tamaized/voidscape/asm/ASMHooks',
                                'handleEntityTransparency',
                                '(FLnet/minecraft/entity/LivingEntity;)F',
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
