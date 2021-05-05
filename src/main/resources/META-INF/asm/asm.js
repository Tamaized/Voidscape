// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');

    return {
        'attackstrength': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.player.PlayerEntity',
                'methodName': ASM.mapMethod('func_71059_n'),
                'methodDesc': '(Lnet/minecraft/entity/Entity;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
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
                return methodNode;
            }
        },
        'attributes': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.LivingEntity',
                'methodName': '<init>',
                'methodDesc': '(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof org.objectweb.asm.tree.FieldInsnNode &&

                        node.getOpcode() === Opcodes.PUTFIELD &&

                        node.name === ASM.mapField("field_110155_d")

                    )
                        lastInstruction = node;
                }
                if (lastInstruction != null) {
                    instructions.insert(
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
                return methodNode;
            }
        },
        'axesareweps': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.enchantment.EnchantmentType$6',
                'methodName': ASM.mapMethod('func_77557_a'),
                'methodDesc': '(Lnet/minecraft/item/Item;)Z'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
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
                return methodNode;
            }
        },
        'death': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraftforge.common.ForgeHooks',
                'methodName': 'onLivingDeath',
                'methodDesc': '(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/DamageSource;)Z'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
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
                return methodNode;
            }
        },
        'deepfreeze': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.server.ServerChunkProvider',
                'methodName': '<init>',
                'methodDesc': '(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/storage/SaveFormat$LevelSave;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/world/gen/feature/template/TemplateManager;Ljava/util/concurrent/Executor;Lnet/minecraft/world/gen/ChunkGenerator;IZLnet/minecraft/world/chunk/listener/IChunkStatusListener;Ljava/util/function/Supplier;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
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
                return methodNode;
            }
        },
        'entityalpha': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.LivingRenderer',
                'methodName': ASM.mapMethod('func_225623_a_'),
                'methodDesc': '(Lnet/minecraft/entity/LivingEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstMethodCall(methodNode,
                        ASM.MethodType.VIRTUAL,
                        'net/minecraft/client/renderer/entity/model/EntityModel',
                        ASM.mapMethod('func_225598_a_'),
                        '(Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFF)V'
                        ),
                    ASM.listOf(
                        new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 1),
                        new org.objectweb.asm.tree.MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'handleEntityTransparency',
                            '(FLnet/minecraft/entity/LivingEntity;)F',
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
                'class': 'net.minecraft.client.renderer.entity.LivingRenderer',
                'methodName': ASM.mapMethod('func_230496_a_'),
                'methodDesc': '(Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
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
                return methodNode;
            }
        },
        'itsmybiomebruh': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraftforge.common.ForgeHooks',
                'methodName': 'enhanceBiome',
                'methodDesc': '(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/world/biome/Biome$Climate;Lnet/minecraft/world/biome/Biome$Category;Ljava/lang/Float;Ljava/lang/Float;Lnet/minecraft/world/biome/BiomeAmbience;Lnet/minecraft/world/biome/BiomeGenerationSettings;Lnet/minecraft/world/biome/MobSpawnInfo;Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;Lnet/minecraftforge/common/ForgeHooks$BiomeCallbackFunction;)Lnet/minecraft/world/biome/Biome;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof org.objectweb.asm.tree.VarInsnNode &&

                        node.getOpcode() === Opcodes.ASTORE &&

                        node.var === 12

                    )
                        lastInstruction = node;
                }
                instructions.insert(
                    lastInstruction,
                    ASM.listOf(
                        new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 12),
                        new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 9),
                        new org.objectweb.asm.tree.MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'fukUrBiomeEdits',
                            '(Lnet/minecraftforge/event/world/BiomeLoadingEvent;Lnet/minecraftforge/common/ForgeHooks$BiomeCallbackFunction;)Lnet/minecraft/world/biome/Biome;',
                            false
                            ),
                        new org.objectweb.asm.tree.InsnNode(Opcodes.ARETURN)
                        )
                    );
                return methodNode;
            }
        },
        'modelpathing': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.model.ModelBakery',
                'methodName': 'processLoading',
                'methodDesc': '(Lnet/minecraft/profiler/IProfiler;I)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof org.objectweb.asm.tree.FieldInsnNode &&

                        node.getOpcode() === Opcodes.GETSTATIC &&

                        node.name === ASM.mapField("field_212630_s")

                    )
                        lastInstruction = node;
                }
                if (lastInstruction != null)
                    instructions.insertBefore(
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
                instructions.insertBefore(
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
                return methodNode;
            }
        },
        'nosnow': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.biome.Biome',
                'methodName': ASM.mapMethod('func_201850_b'),
                'methodDesc': '(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;)Z'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof org.objectweb.asm.tree.InsnNode &&

                        node.getOpcode() === Opcodes.ICONST_1

                    )
                        lastInstruction = node;
                }
                instructions.insert(
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
                return methodNode;
            }
        },
        'seed': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.gen.settings.DimensionGeneratorSettings',
                'methodName': '<init>',
                'methodDesc': '(JZZLnet/minecraft/util/registry/SimpleRegistry;Ljava/util/Optional;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.PUTFIELD),
                    ASM.listOf(
                        new org.objectweb.asm.tree.MethodInsnNode(
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
                'methodName': ASM.mapMethod('func_228452_a_'),
                'methodDesc': '(Lnet/minecraft/world/World;I)F'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.FRETURN),
                    ASM.listOf(
                        new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 1),
                        new org.objectweb.asm.tree.VarInsnNode(Opcodes.ILOAD, 2),
                        new org.objectweb.asm.tree.MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'visibility',
                            '(FLnet/minecraft/world/World;I)F',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}
