function initializeCoreMod() {
    return {
        'attackstrength': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.player.PlayerEntity',
                'methodName': Java.type("net.minecraftforge.coremod.api.ASMAPI").mapMethod('func_71059_n'),
                'methodDesc': '(Lnet/minecraft/entity/Entity;)V'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) { // Stupid way to cast in JS to avoid warnings and fix autocomplete
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    methodNode.instructions.insert(
                        ASM.findFirstMethodCall(methodNode,
                            ASM.MethodType.VIRTUAL,
                            'net/minecraft/entity/player/PlayerEntity',
                            ASM.mapMethod('func_184825_o'),
                            '(F)F'
                            ),
                        ASM.listOf(
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'tamaized/voidscape/asm/ASMHooks',
                                'getAttackStrengthScale',
                                '(F)F',
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
