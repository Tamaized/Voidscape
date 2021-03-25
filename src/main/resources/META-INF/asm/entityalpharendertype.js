function initializeCoreMod() {
    return {
        'entityalpharendertype': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.LivingRenderer',
                'methodName': Java.type("net.minecraftforge.coremod.api.ASMAPI").mapMethod('func_230496_a_'),
                'methodDesc': '(Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) { // Stupid way to cast in JS to avoid warnings and fix autocomplete
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    methodNode.instructions.insert(
                        ASM.findFirstMethodCall(methodNode,
                            ASM.MethodType.VIRTUAL,
                            'net/minecraft/client/renderer/entity/model/EntityModel',
                            ASM.mapMethod('func_228282_a_'),
                            '(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;'
                            ),
                        ASM.listOf(
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 0),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 1),
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'tamaized/voidscape/asm/ASMHooks',
                                'handleEntityTransparencyRenderType',
                                '(Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/entity/LivingRenderer;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/client/renderer/RenderType;',
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
