function initializeCoreMod() {
    return {
        'attributes': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.LivingEntity',
                'methodName': '<init>',
                'methodDesc': '(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) { // Stupid way to cast in JS to avoid warnings and fix autocomplete
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    var lastInstruction = null;
                    for (var index = 0; index < methodNode.instructions.size(); index++) {
                        var node = methodNode.instructions.get(index);
                        if (lastInstruction == null &&

                            node instanceof org.objectweb.asm.tree.FieldInsnNode &&

                            node.getOpcode() === Opcodes.PUTFIELD &&

                            node.name === ASM.mapField("field_110155_d")

                        )
                            lastInstruction = node;
                    }
                    if (lastInstruction != null) {
                        methodNode.instructions.insert(
                            lastInstruction,
                            ASM.listOf(
                                new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 0),
                                new org.objectweb.asm.tree.MethodInsnNode(
                                    Opcodes.INVOKESTATIC,
                                    'tamaized/voidscape/asm/ASMHooks',
                                    'handleEntityAttributes',
                                    '(Lnet/minecraft/entity/LivingEntity;)V',
                                    false
                                    )
                                )
                            );
                    }
                }
                return methodNode;
            }
        }
    }
}
