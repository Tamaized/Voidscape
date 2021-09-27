// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

    return {
        'itsmybiomebruh': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraftforge.common.ForgeHooks',
                'methodName': 'enhanceBiome',
                'methodDesc': '(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/biome/Biome$ClimateSettings;Lnet/minecraft/world/level/biome/Biome$BiomeCategory;Ljava/lang/Float;Ljava/lang/Float;Lnet/minecraft/world/level/biome/BiomeSpecialEffects;Lnet/minecraft/world/level/biome/BiomeGenerationSettings;Lnet/minecraft/world/level/biome/MobSpawnSettings;Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;Lnet/minecraftforge/common/ForgeHooks$BiomeCallbackFunction;)Lnet/minecraft/world/level/biome/Biome;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var /*org.objectweb.asm.tree.FieldInsnNode*/ node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof FieldInsnNode &&

                        node.getOpcode() === Opcodes.GETSTATIC &&

                        node.name === 'EVENT_BUS'

                    )
                        lastInstruction = node;
                }
                instructions.insert(
                    lastInstruction,
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 12),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/voidscape/asm/ASMHooks',
                            'fukUrBiomeEdits',
                            '(Lnet/minecraftforge/eventbus/api/IEventBus;Lnet/minecraftforge/event/world/BiomeLoadingEvent;)Lnet/minecraftforge/eventbus/api/IEventBus;',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}
