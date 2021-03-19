function initializeCoreMod() {
    return {
        'deepfreeze': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.server.ServerChunkProvider',
                'methodName': '<init>',
                'methodDesc': '(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/storage/SaveFormat$LevelSave;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/world/gen/feature/template/TemplateManager;Ljava/util/concurrent/Executor;Lnet/minecraft/world/gen/ChunkGenerator;IZLnet/minecraft/world/chunk/listener/IChunkStatusListener;Ljava/util/function/Supplier;)V'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) { // Stupid way to cast in JS to avoid warnings and fix autocomplete
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    methodNode.instructions.insert(
                        ASM.findFirstMethodCall(methodNode,
                            ASM.MethodType.SPECIAL,
                            'net/minecraft/world/server/ChunkManager',
                            '<init>',
                            '(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/storage/SaveFormat$LevelSave;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/world/gen/feature/template/TemplateManager;Ljava/util/concurrent/Executor;Lnet/minecraft/util/concurrent/ThreadTaskExecutor;Lnet/minecraft/world/chunk/IChunkLightProvider;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/world/chunk/listener/IChunkStatusListener;Ljava/util/function/Supplier;IZ)V'
                            ),
                        ASM.listOf(
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 1),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 2),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 3),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 4),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 5),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 0),
                            new org.objectweb.asm.tree.FieldInsnNode(
                                Opcodes.GETFIELD,
                                'net/minecraft/world/server/ServerChunkProvider',
                                ASM.mapField('field_217243_i'),
                                'Lnet/minecraft/world/server/ServerChunkProvider$ChunkExecutor;'
                                ),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 0),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 0),
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKEVIRTUAL,
                                'net/minecraft/world/server/ServerChunkProvider',
                                ASM.mapMethod('func_201711_g'),
                                '()Lnet/minecraft/world/gen/ChunkGenerator;'
                                ),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 9),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 10),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ILOAD, 7),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ILOAD, 8),
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'tamaized/voidscape/asm/ASMHooks',
                                'chunkManager',
                                '(Lnet/minecraft/world/server/ChunkManager;Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/storage/SaveFormat$LevelSave;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/world/gen/feature/template/TemplateManager;Ljava/util/concurrent/Executor;Lnet/minecraft/util/concurrent/ThreadTaskExecutor;Lnet/minecraft/world/chunk/IChunkLightProvider;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/world/chunk/listener/IChunkStatusListener;Ljava/util/function/Supplier;IZ)Lnet/minecraft/world/server/ChunkManager;',
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
