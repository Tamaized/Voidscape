// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

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
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var /*org.objectweb.asm.tree.FieldInsnNode*/ node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof FieldInsnNode &&

                        node.getOpcode() === Opcodes.PUTFIELD &&

                        node.name === 'attributes'

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
        }
    }
}
