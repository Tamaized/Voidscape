function initializeCoreMod() {
    return {
        'modelpathing': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.model.ModelBakery',
                'methodName': 'processLoading',
                'methodDesc': '(Lnet/minecraft/profiler/IProfiler;I)V'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) {
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    var lastInstruction = null;
                    for (var index = 0; index < methodNode.instructions.size(); index++) {
                        var node = methodNode.instructions.get(index);
                        if (lastInstruction == null &&

                            node instanceof org.objectweb.asm.tree.FieldInsnNode &&

                            node.getOpcode() === Opcodes.GETSTATIC &&

                            node.name === ASM.mapField("field_212630_s")

                        )
                            lastInstruction = node;
                    }
                    if (lastInstruction != null)
                        methodNode.instructions.insertBefore(
                            lastInstruction,
                            ASM.listOf(
                                new org.objectweb.asm.tree.MethodInsnNode(
                                    Opcodes.INVOKESTATIC,
                                    'tamaized/voidscape/asm/ASMHooks',
                                    'redirectModels',
                                    '()V',
                                    false
                                    )
                                )
                            );
                    methodNode.instructions.insertBefore(
                        ASM.findFirstMethodCall(methodNode, ASM.MethodType.STATIC,
                            "com/google/common/collect/Sets",
                            "newLinkedHashSet",
                            "()Ljava/util/LinkedHashSet;"),
                        ASM.listOf(
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'tamaized/voidscape/asm/ASMHooks',
                                'cleanModels',
                                '()V',
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
