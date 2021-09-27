// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

    return {
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
        }
    }
}
