package tamaized.voidscape.biome.genlayer.legacy;

import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

public interface Context {
	int nextRandom(int bound);

	ImprovedNoise getBiomeNoise();
}