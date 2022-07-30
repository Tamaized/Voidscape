// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

    return {
        'effecthud': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.gui.Gui',
                'methodName': ASM.mapMethod('m_93028_'), // renderEffects
                'methodDesc': '(Lcom/mojang/blaze3d/vertex/PoseStack;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstMethodCall(methodNode,
                        ASM.MethodType.VIRTUAL,
                        'net/minecraftforge/client/extensions/common/IClientMobEffectExtensions',
                        'renderHUDEffect',
                        '(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/client/gui/Gui;Lcom/mojang/blaze3d/vertex/PoseStack;IIFF)V'
                        ),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 7),
                        new VarInsnNode(Opcodes.ALOAD, 11),
                        new VarInsnNode(Opcodes.ALOAD, 9),
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new VarInsnNode(Opcodes.ILOAD, 12),
                        new VarInsnNode(Opcodes.ILOAD, 13),
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new MethodInsnNode(
                            Opcodes.INVOKEVIRTUAL,
                            'net/minecraft/client/gui/Gui',
                            ASM.mapMethod('m_93252_'), // getBlitOffset
                            '()I'
                            ),
                        new InsnNode(Opcodes.I2F),
                        new VarInsnNode(Opcodes.FLOAD, 14),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'renderEffectHUD',
                            '(Ljava/util/List;Lnet/minecraftforge/client/extensions/common/IClientMobEffectExtensions;Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/client/gui/Gui;Lcom/mojang/blaze3d/vertex/PoseStack;IIFF)V'
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}
