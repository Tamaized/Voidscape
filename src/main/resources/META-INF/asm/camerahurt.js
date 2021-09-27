// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

    return {
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
