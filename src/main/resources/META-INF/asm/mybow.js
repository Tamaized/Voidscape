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

                    node.name === ASM.mapMethod("is") &&

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
            ASM.mapMethod('m_172914_'),
            '(Lnet/minecraft/client/player/LocalPlayer;)Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;'
            ),
        'selectionusingitemwhileholdingbowlike': transform(
            'net.minecraft.client.renderer.ItemInHandRenderer',
            ASM.mapMethod('m_172916_'),
            '(Lnet/minecraft/client/player/LocalPlayer;)Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;'
            ),
        'ischargedcrossbow': transform(
            'net.minecraft.client.renderer.ItemInHandRenderer',
            ASM.mapMethod('m_172912_'),
            '(Lnet/minecraft/world/item/ItemStack;)Z'
            ),
        'renderarmwithitem': transform(
            'net.minecraft.client.renderer.ItemInHandRenderer',
            ASM.mapMethod('m_109371_'),
            '(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V'
            ),
        'getarmpose': transform(
            'net.minecraft.client.renderer.entity.player.PlayerRenderer',
            ASM.mapMethod('m_117794_'),
            '(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;'
            )
    }
}
