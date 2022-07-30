// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
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
                'methodDesc': '(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/client/color/block/BlockColors;Lnet/minecraft/util/profiling/ProfilerFiller;I)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var /*org.objectweb.asm.tree.FieldInsnNode*/ node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof FieldInsnNode &&

                        node.getOpcode() === Opcodes.GETSTATIC &&

                        node.name === ASM.mapField("f_122827_") // ITEM

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
                instructions.insertBefore(
                    ASM.findFirstMethodCall(methodNode, ASM.MethodType.STATIC,
                        "com/google/common/collect/Sets",
                        "newLinkedHashSet",
                        "()Ljava/util/LinkedHashSet;"),
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
