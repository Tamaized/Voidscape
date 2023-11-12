// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

function transform(clazz, name, desc) {
    return {
        'target': {
            'type': 'METHOD',
            'class': clazz,
            'methodName': name,
            'methodDesc': desc
        },
        'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
            var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
            var inst = [];
            for (var index = 0; index < instructions.size(); index++) {
                var /*org.objectweb.asm.tree.MethodInsnNode*/ node = instructions.get(index);
                if (node instanceof MethodInsnNode &&

                    node.getOpcode() === Opcodes.INVOKEVIRTUAL &&

                    node.owner === 'net/minecraft/world/item/ItemStack' &&

                    node.name === 'is' &&

                    node.desc === '(Lnet/minecraft/world/item/Item;)Z'

                )
                    inst.push(node);
            }
            inst.forEach(function (/*org.objectweb.asm.tree.AbstractInsnNode*/ value, index, array) {
                var /*org.objectweb.asm.tree.FieldInsnNode*/ prev = value.getPrevious();
                var /*org.objectweb.asm.tree.VarInsnNode*/ prevPrev = prev.getPrevious();
                instructions.insert(
                    value,
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, prevPrev.var),
                        new FieldInsnNode(Opcodes.GETSTATIC, prev.owner, prev.name, prev.desc),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'isMyBow',
                            '(ZLnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item;)Z'
                            )
                        )
                    )
            });
            return methodNode;
        }
    }
}

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'evaluatewhichhandstorender': transform(
            'net.minecraft.client.renderer.ItemInHandRenderer',
            'evaluateWhichHandsToRender',
            '(Lnet/minecraft/client/player/LocalPlayer;)Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;'
            ),
        'selectionusingitemwhileholdingbowlike': transform(
            'net.minecraft.client.renderer.ItemInHandRenderer',
            'selectionUsingItemWhileHoldingBowLike',
            '(Lnet/minecraft/client/player/LocalPlayer;)Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;'
            ),
        'ischargedcrossbow': transform(
            'net.minecraft.client.renderer.ItemInHandRenderer',
            'isChargedCrossbow',
            '(Lnet/minecraft/world/item/ItemStack;)Z'
            )
    }
}
