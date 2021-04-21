function initializeCoreMod() {
    return {
        'axesareweps': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.enchantment.EnchantmentType$6',
                'methodName': Java.type("net.minecraftforge.coremod.api.ASMAPI").mapMethod('func_77557_a'),
                'methodDesc': '(Lnet/minecraft/item/Item;)Z'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) { // Stupid way to cast in JS to avoid warnings and fix autocomplete
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    methodNode.instructions.insertBefore(
                        ASM.findFirstInstruction(methodNode, Opcodes.IRETURN),
                        ASM.listOf(
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 1),
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'tamaized/voidscape/asm/ASMHooks',
                                'axesRWeps',
                                '(ZLnet/minecraft/item/Item;)Z',
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
