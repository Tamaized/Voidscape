// noinspection ES6ConvertVarToLetConst,JSUndefinedPropertyAssignment

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

    return {
        'stripfinal': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.world.level.levelgen.NoiseSettings'
            },
            'transformer': function (/*org.objectweb.asm.tree.ClassNode*/ classNode) {
                classNode.access &= ~Opcodes.ACC_FINAL;
                return classNode;
            }
        },
        'extendclass': {
            'target': {
                'type': 'CLASS',
                'name': 'tamaized.voidscape.world.VoidChunkGenerator$CorrectedNoiseSettings'
            },
            'transformer': function (/*org.objectweb.asm.tree.ClassNode*/ classNode) {
                classNode.superName = 'net/minecraft/world/level/levelgen/NoiseSettings';
                return classNode;
            }
        },
        'superctor': {
            'target': {
                'type': 'METHOD',
                'class': 'tamaized.voidscape.world.VoidChunkGenerator$CorrectedNoiseSettings',
                'methodName': '<init>',
                'methodDesc': '(IILnet/minecraft/world/level/levelgen/NoiseSamplingSettings;Lnet/minecraft/world/level/levelgen/NoiseSlider;Lnet/minecraft/world/level/levelgen/NoiseSlider;IIZZZLnet/minecraft/world/level/biome/TerrainShaper;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var /*org.objectweb.asm.tree.AbstractInsnNode*/
                    insn = ASM.findFirstInstruction(methodNode, Opcodes.INVOKESPECIAL);
                var /*org.objectweb.asm.tree.AbstractInsnNode*/ inst = insn.getNext();
                instructions.remove(insn);
                instructions.insertBefore(
                    inst,
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ILOAD, 1),
                        new VarInsnNode(Opcodes.ILOAD, 2),
                        new VarInsnNode(Opcodes.ALOAD, 3),
                        new VarInsnNode(Opcodes.ALOAD, 4),
                        new VarInsnNode(Opcodes.ALOAD, 5),
                        new VarInsnNode(Opcodes.ILOAD, 6),
                        new VarInsnNode(Opcodes.ILOAD, 7),
                        new VarInsnNode(Opcodes.ILOAD, 8),
                        new VarInsnNode(Opcodes.ILOAD, 9),
                        new VarInsnNode(Opcodes.ILOAD, 10),
                        new VarInsnNode(Opcodes.ALOAD, 11),
                        new MethodInsnNode(
                            Opcodes.INVOKESPECIAL,
                            'net/minecraft/world/level/levelgen/NoiseSettings',
                            '<init>',
                            '(IILnet/minecraft/world/level/levelgen/NoiseSamplingSettings;Lnet/minecraft/world/level/levelgen/NoiseSlider;Lnet/minecraft/world/level/levelgen/NoiseSlider;IIZZZLnet/minecraft/world/level/biome/TerrainShaper;)V'
                            )
                        )
                    );
                return methodNode;
            }
        },
        'fix': {
            'target': {
                'type': 'METHOD',
                'class': 'tamaized.voidscape.world.VoidChunkGenerator',
                'methodName': 'fixSettings',
                'methodDesc': '(Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;)Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.set(
                    ASM.findFirstInstruction(methodNode, Opcodes.NEW),
                    new TypeInsnNode(
                        Opcodes.NEW,
                        'tamaized/voidscape/world/VoidChunkGenerator$CorrectedNoiseSettings'
                        )
                    );
                instructions.set(
                    ASM.findFirstInstruction(methodNode, Opcodes.INVOKESPECIAL),
                    new MethodInsnNode(
                        Opcodes.INVOKESPECIAL,
                        'tamaized/voidscape/world/VoidChunkGenerator$CorrectedNoiseSettings',
                        '<init>',
                        '(IILnet/minecraft/world/level/levelgen/NoiseSamplingSettings;Lnet/minecraft/world/level/levelgen/NoiseSlider;Lnet/minecraft/world/level/levelgen/NoiseSlider;IIZZZLnet/minecraft/world/level/biome/TerrainShaper;)V'
                        )
                    );
                return methodNode;
            }
        }
    }
}
