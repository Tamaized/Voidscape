function initializeCoreMod() {
    return {
        'attributes': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraftforge.common.ForgeHooks',
                'methodName': 'enhanceBiome',
                'methodDesc': '(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/world/biome/Biome$Climate;Lnet/minecraft/world/biome/Biome$Category;Ljava/lang/Float;Ljava/lang/Float;Lnet/minecraft/world/biome/BiomeAmbience;Lnet/minecraft/world/biome/BiomeGenerationSettings;Lnet/minecraft/world/biome/MobSpawnInfo;Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;Lnet/minecraftforge/common/ForgeHooks$BiomeCallbackFunction;)Lnet/minecraft/world/biome/Biome;'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) { // Stupid way to cast in JS to avoid warnings and fix autocomplete
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    methodNode.instructions.insertBefore(
                        ASM.findFirstInstruction(methodNode, Opcodes.NEW),
                        ASM.listOf(
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 0),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 1),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 2),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 3),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 4),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 5),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 6),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 7),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 8),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 9),
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'tamaized/voidscape/asm/ASMHooks',
                                'fukUrBiomeEdits',
                                '(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/world/biome/Biome$Climate;Lnet/minecraft/world/biome/Biome$Category;Ljava/lang/Float;Ljava/lang/Float;Lnet/minecraft/world/biome/BiomeAmbience;Lnet/minecraft/world/biome/BiomeGenerationSettings;Lnet/minecraft/world/biome/MobSpawnInfo;Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;Lnet/minecraftforge/common/ForgeHooks$BiomeCallbackFunction;)Lnet/minecraft/world/biome/Biome;',
                                false
                                ),
                                new org.objectweb.asm.tree.InsnNode(Opcodes.ARETURN)
                            )
                        );
                }
                return methodNode;
            }
        }
    }
}
