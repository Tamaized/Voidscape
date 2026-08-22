package tamaized.voidscape.coremod.transformers.biome;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import tamaized.voidscape.coremod.ASMUtil;

import java.util.Set;

/**
 * {@link tamaized.voidscape.asm.ASMHooks#shouldBiomeHaveSnowfallAndLiquidFreeze}
 */
public class BiomeSnowAndFreezeTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("biome_snow_and_freeze");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findInstructions(node, Opcodes.IRETURN)
			.forEach(instruction -> node.instructions.insertBefore(instruction, ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new VarInsnNode(Opcodes.ALOAD, 1),
				ASMUtil.invokeAsmHook("shouldBiomeHaveSnowfallAndLiquidFreeze", "(ZLnet/minecraft/world/level/biome/Biome;Lnet/minecraft/world/level/LevelReader;)Z")
			)));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.world.level.biome.Biome",
			"shouldSnow",
			"(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"
		), new Target(
			"net.minecraft.world.level.biome.Biome",
			"shouldFreeze",
			"(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Z)Z"
		));
	}

}
