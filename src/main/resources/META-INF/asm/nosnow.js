function initializeCoreMod() {
    return {
        'nosnow': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.biome.Biome',
                'methodName': Java.type("net.minecraftforge.coremod.api.ASMAPI").mapMethod('func_201850_b'),
                'methodDesc': '(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;)Z'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) { // Stupid way to cast in JS to avoid warnings and fix autocomplete
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    var lastInstruction = null;
                    for (var index = 0; index < methodNode.instructions.size(); index++) {
                        var node = methodNode.instructions.get(index);
                        if (lastInstruction == null &&

                            node instanceof org.objectweb.asm.tree.InsnNode &&

                            node.getOpcode() === Opcodes.ICONST_1

                        )
                            lastInstruction = node;
                    }
                    methodNode.instructions.insert(
                        lastInstruction,
                        ASM.listOf(
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 0),
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'tamaized/voidscape/asm/ASMHooks',
                                'shouldSnow',
                                '(ZLnet/minecraft/world/biome/Biome;)Z',
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
