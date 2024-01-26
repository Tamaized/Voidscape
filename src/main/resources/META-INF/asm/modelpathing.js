// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    ASM.loadFile('META-INF/asm/util/util.js');
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
                instructions.insertBefore(
                    findLastFieldInstruction(methodNode, Opcodes.GETSTATIC, 'net/minecraft/core/registries/BuiltInRegistries', 'ITEM'),
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
                    findLastMethodInstruction(methodNode, Opcodes.INVOKESTATIC, 'com/google/common/collect/Sets', 'newHashSet', '()Ljava/util/HashSet;'),
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
