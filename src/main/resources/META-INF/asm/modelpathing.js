// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

    return {
        'modelpathing': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.resources.model.ModelBakery',
                'methodName': '<init>',
                'methodDesc': '(Lnet/minecraft/client/color/block/BlockColors;Lnet/minecraft/util/profiling/ProfilerFiller;Ljava/util/Map;Ljava/util/Map;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var /*org.objectweb.asm.tree.FieldInsnNode*/ node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof FieldInsnNode &&

                        node.getOpcode() === Opcodes.GETSTATIC &&

                        node.name === ASM.mapField("ITEM")

                    )
                        lastInstruction = node;
                }
                if (lastInstruction != null)
                    instructions.insertBefore(
                        lastInstruction,
                        ASM.listOf(
                            new VarInsnNode(Opcodes.ALOAD, 0),
                            new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'tamaized/voidscape/asm/ASMHooks',
                                'redirectModels',
                                '(Lnet/minecraft/client/resources/model/ModelBakery;)V',
                                false
                                )
                            )
                        );
                lastInstruction = null;
                for (var index = instructions.size() - 1; index > 0; index--) {
                    var /*org.objectweb.asm.tree.FieldInsnNode*/ node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof MethodInsnNode &&

                        node.getOpcode() === Opcodes.INVOKESTATIC &&

                        node.name === "newHashSet"

                    )
                        lastInstruction = node;
                }
                if (lastInstruction != null)
                    instructions.insertBefore(
                        lastInstruction,
                        ASM.listOf(
                            new VarInsnNode(Opcodes.ALOAD, 0),
                            new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'tamaized/voidscape/asm/ASMHooks',
                                'cleanModels',
                                '(Lnet/minecraft/client/resources/model/ModelBakery;)V',
                                false
                                )
                            )
                        );
                return methodNode;
            }
        }
    }
}
