function initializeCoreMod() {
    return {
        'attributes': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraftforge.common.ForgeHooks',
                'methodName': 'onLivingDeath',
                'methodDesc': '(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/DamageSource;)Z'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) { // Stupid way to cast in JS to avoid warnings and fix autocomplete
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    methodNode.instructions.insertBefore(
                        ASM.findFirstInstruction(methodNode, Opcodes.GETSTATIC),
                        ASM.listOf(
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 0),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 1),
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'tamaized/voidscape/asm/ASMHooks',
                                'death',
                                '(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/DamageSource;)Z',
                                false
                                ),
                                new org.objectweb.asm.tree.InsnNode(Opcodes.IRETURN)
                            )
                        );
                }
                return methodNode;
            }
        }
    }
}
