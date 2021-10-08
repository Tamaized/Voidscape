// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'nosnow': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.level.biome.Biome',
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
                            '(ZLnet/minecraft/world/level/biome/Biome;)Z',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}
