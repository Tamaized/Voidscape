// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    ASM.loadFile('META-INF/asm/util/util.js');
    return {
        'attributes': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.entity.LivingEntity',
                'methodName': '<init>',
                'methodDesc': '(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    findLastFieldInstruction(methodNode, Opcodes.PUTFIELD, 'net/minecraft/world/entity/LivingEntity', 'attributes'),
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
                return methodNode;
            }
        }
    }
}
