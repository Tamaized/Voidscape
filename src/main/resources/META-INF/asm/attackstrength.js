// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

    return {
        'attackstrength': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.entity.player.Player',
                'methodName': ASM.mapMethod('attack'),
                'methodDesc': '(Lnet/minecraft/world/entity/Entity;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstMethodCall(methodNode,
                        ASM.MethodType.VIRTUAL,
                        'net/minecraft/world/entity/player/Player',
                        ASM.mapMethod('getAttackStrengthScale'),
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
        }
    }
}
