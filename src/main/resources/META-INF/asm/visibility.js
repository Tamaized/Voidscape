// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

    return {
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
        'nonightvision': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.LightTexture',
                'methodName': ASM.mapMethod('m_109881_'), // getBrightness
                'methodDesc': '(F)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var /*org.objectweb.asm.tree.VarInsnNode*/ node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof VarInsnNode &&

                        node.getOpcode() === Opcodes.FLOAD &&

                        node.var === 6

                    )
                        lastInstruction = node;
                }
                instructions.insert(
                    lastInstruction,
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 2),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'cancelNightVision',
                            '(FLnet/minecraft/world/level/Level;)F',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}
