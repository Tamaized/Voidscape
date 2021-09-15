// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

    return {
        'attackstrength': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.entity.player.Player',
                'methodName': ASM.mapMethod('m_5706_'), // attack
                'methodDesc': '(Lnet/minecraft/world/entity/Entity;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstMethodCall(methodNode,
                        ASM.MethodType.VIRTUAL,
                        'net/minecraft/world/entity/player/Player',
                        ASM.mapMethod('m_36403_'), // getAttackStrengthScale
                        '(F)F'
                        ),
                    ASM.listOf(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'getAttackStrengthScale',
                            '(F)F',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'attributes': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.entity.LivingEntity',
                'methodName': '<init>',
                'methodDesc': '(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var /*org.objectweb.asm.tree.FieldInsnNode*/ node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof FieldInsnNode &&

                        node.getOpcode() === Opcodes.PUTFIELD &&

                        node.name === ASM.mapField("f_20943_") // attributes

                    )
                        lastInstruction = node;
                }
                if (lastInstruction != null) {
                    instructions.insert(
                        lastInstruction,
                        ASM.listOf(
                            new VarInsnNode(Opcodes.ALOAD, 0),
                            new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'tamaized/voidscape/asm/ASMHooks',
                                'handleEntityAttributes',
                                '(Lnet/minecraft/world/entity/LivingEntity;)V',
                                false
                                )
                            )
                        );
                }
                return methodNode;
            }
        },
        'axesareweps': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.item.enchantment.EnchantmentCategory$6', // WEAPON
                'methodName': ASM.mapMethod('m_7454_'), // canEnchant
                'methodDesc': '(Lnet/minecraft/world/item/Item;)Z'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.IRETURN),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'axesRWeps',
                            '(ZLnet/minecraft/world/item/Item;)Z',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'death': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraftforge.common.ForgeHooks',
                'methodName': 'onLivingDeath',
                'methodDesc': '(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;)Z'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.GETSTATIC),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'death',
                            '(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;)Z',
                            false
                            ),
                        new org.objectweb.asm.tree.InsnNode(Opcodes.IRETURN)
                        )
                    );
                return methodNode;
            }
        },
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
        },
        'entityalpha': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.LivingEntityRenderer',
                'methodName': ASM.mapMethod('m_7392_'), // render
                'methodDesc': '(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstMethodCall(methodNode,
                        ASM.MethodType.VIRTUAL,
                        'net/minecraft/client/model/EntityModel',
                        ASM.mapMethod('m_7695_'), // renderToBuffer
                        '(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V'
                        ),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'handleEntityTransparency',
                            '(FLnet/minecraft/world/entity/LivingEntity;)F',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'entityalpharendertype': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.LivingEntityRenderer',
                'methodName': ASM.mapMethod('m_7225_'), // getRenderType
                'methodDesc': '(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstMethodCall(methodNode,
                        ASM.MethodType.VIRTUAL,
                        'net/minecraft/client/model/EntityModel',
                        ASM.mapMethod('m_103119_'), // renderType
                        '(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;'
                        ),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'handleEntityTransparencyRenderType',
                            '(Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/client/renderer/RenderType;',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'itsmybiomebruh': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraftforge.common.ForgeHooks',
                'methodName': 'enhanceBiome',
                'methodDesc': '(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/biome/Biome$ClimateSettings;Lnet/minecraft/world/level/biome/Biome$BiomeCategory;Ljava/lang/Float;Ljava/lang/Float;Lnet/minecraft/world/level/biome/BiomeSpecialEffects;Lnet/minecraft/world/level/biome/BiomeGenerationSettings;Lnet/minecraft/world/level/biome/MobSpawnSettings;Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;Lnet/minecraftforge/common/ForgeHooks$BiomeCallbackFunction;)Lnet/minecraft/world/level/biome/Biome;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var /*org.objectweb.asm.tree.FieldInsnNode*/ node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof FieldInsnNode &&

                        node.getOpcode() === Opcodes.GETSTATIC &&

                        node.name === 'EVENT_BUS'

                    )
                        lastInstruction = node;
                }
                instructions.insert(
                    lastInstruction,
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 12),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'fukUrBiomeEdits',
                            '(Lnet/minecraftforge/eventbus/api/IEventBus;Lnet/minecraftforge/event/world/BiomeLoadingEvent;)Lnet/minecraftforge/eventbus/api/IEventBus;',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'modelpathing': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.resources.model.ModelBakery',
                'methodName': 'processLoading', // Added by Forge
                'methodDesc': '(Lnet/minecraft/util/profiling/ProfilerFiller;I)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var /*org.objectweb.asm.tree.FieldInsnNode*/ node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof FieldInsnNode &&

                        node.getOpcode() === Opcodes.GETSTATIC &&

                        node.name === ASM.mapField("f_122827_") // ITEM

                    )
                        lastInstruction = node;
                }
                if (lastInstruction != null)
                    instructions.insertBefore(
                        lastInstruction,
                        ASM.listOf(
                            new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'tamaized/voidscape/asm/ASMHooks',
                                'redirectModels',
                                '()V',
                                false
                                )
                            )
                        );
                instructions.insertBefore(
                    ASM.findFirstMethodCall(methodNode, ASM.MethodType.STATIC,
                        "com/google/common/collect/Sets",
                        "newLinkedHashSet",
                        "()Ljava/util/LinkedHashSet;"),
                    ASM.listOf(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'cleanModels',
                            '()V',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'nosnow': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.biome.Biome',
                'methodName': ASM.mapMethod('m_47519_'), // shouldSnow
                'methodDesc': '(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var /*org.objectweb.asm.tree.InsnNode*/ node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof InsnNode &&

                        node.getOpcode() === Opcodes.ICONST_1

                    )
                        lastInstruction = node;
                }
                instructions.insert(
                    lastInstruction,
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'shouldSnow',
                            '(ZLnet/minecraft/world/biome/Biome;)Z',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'seed': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.gen.settings.DimensionGeneratorSettings',
                'methodName': '<init>',
                'methodDesc': '(JZZLnet/minecraft/core/MappedRegistry;Ljava/util/Optional;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.PUTFIELD),
                    ASM.listOf(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'seed',
                            '(J)J',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'visibility': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.LightTexture',
                'methodName': ASM.mapMethod('m_109888_'), // getBrightness
                'methodDesc': '(Lnet/minecraft/world/level/Level;I)F'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.FRETURN),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new VarInsnNode(Opcodes.ILOAD, 2),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'visibility',
                            '(FLnet/minecraft/world/level/Level;I)F',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'camerahurt': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.GameRenderer',
                'methodName': ASM.mapMethod('m_109117_'), // bobHurt
                'methodDesc': '(Lcom/mojang/blaze3d/vertex/PoseStack;F)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.IFEQ),
                    ASM.listOf(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'cancelBobHurt',
                            '(Z)Z',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}
