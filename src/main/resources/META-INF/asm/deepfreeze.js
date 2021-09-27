// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

    return {
        'deepfreeze': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.server.level.ServerChunkCache',
                'methodName': '<init>',
                'methodDesc': '(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureManager;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/chunk/ChunkGenerator;IZLnet/minecraft/server/level/progress/ChunkProgressListener;Lnet/minecraft/world/level/entity/ChunkStatusUpdateListener;Ljava/util/function/Supplier;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstMethodCall(methodNode,
                        ASM.MethodType.SPECIAL,
                        'net/minecraft/server/level/ChunkMap',
                        '<init>',
                        '(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureManager;Ljava/util/concurrent/Executor;Lnet/minecraft/util/thread/BlockableEventLoop;Lnet/minecraft/world/level/chunk/LightChunkGetter;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/server/level/progress/ChunkProgressListener;Lnet/minecraft/world/level/entity/ChunkStatusUpdateListener;Ljava/util/function/Supplier;IZ)V'
                        ),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new VarInsnNode(Opcodes.ALOAD, 2),
                        new VarInsnNode(Opcodes.ALOAD, 3),
                        new VarInsnNode(Opcodes.ALOAD, 4),
                        new VarInsnNode(Opcodes.ALOAD, 5),
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new FieldInsnNode(
                            Opcodes.GETFIELD,
                            'net/minecraft/server/level/ServerChunkCache',
                            ASM.mapField('f_8332_'), // mainThreadProcessor
                            'Lnet/minecraft/server/level/ServerChunkCache$MainThreadExecutor;'
                            ),
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new MethodInsnNode(
                            Opcodes.INVOKEVIRTUAL,
                            'net/minecraft/server/level/ServerChunkCache',
                            ASM.mapMethod('m_8481_'), // getGenerator
                            '()Lnet/minecraft/world/level/chunk/ChunkGenerator;'
                            ),
                        new VarInsnNode(Opcodes.ALOAD, 9),
                        new VarInsnNode(Opcodes.ALOAD, 10),
                        new VarInsnNode(Opcodes.ALOAD, 11),
                        new VarInsnNode(Opcodes.ILOAD, 7),
                        new VarInsnNode(Opcodes.ILOAD, 8),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'chunkManager',
                            '(Lnet/minecraft/server/level/ChunkMap;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureManager;Ljava/util/concurrent/Executor;Lnet/minecraft/util/thread/BlockableEventLoop;Lnet/minecraft/world/level/chunk/LightChunkGetter;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/server/level/progress/ChunkProgressListener;Lnet/minecraft/world/level/entity/ChunkStatusUpdateListener;Ljava/util/function/Supplier;IZ)Lnet/minecraft/server/level/ChunkMap;',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'entityStorage': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.server.level.ServerLevel',
                'methodName': '<init>',
                'methodDesc': '(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/world/level/storage/ServerLevelData;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/dimension/DimensionType;Lnet/minecraft/server/level/progress/ChunkProgressListener;Lnet/minecraft/world/level/chunk/ChunkGenerator;ZJLjava/util/List;Z)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstMethodCall(methodNode,
                        ASM.MethodType.SPECIAL,
                        'net/minecraft/world/level/chunk/storage/EntityStorage',
                        '<init>',
                        '(Lnet/minecraft/server/level/ServerLevel;Ljava/io/File;Lcom/mojang/datafixers/DataFixer;ZLjava/util/concurrent/Executor;)V'
                        ),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 8),
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new VarInsnNode(Opcodes.ALOAD, 3),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'entityStorage',
                            '(Lnet/minecraft/world/level/chunk/storage/EntityStorage;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;)Lnet/minecraft/world/level/chunk/storage/EntityStorage;',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}
