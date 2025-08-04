package tamaized.voidscape.client.shader;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;

public class AuroraShaderInstance extends BindableShaderInstance {

	private final Uniform uniform_seed;
	private final Uniform uniform_position;

	public AuroraShaderInstance(ResourceProvider p_173336_, ResourceLocation shaderLocation, VertexFormat p_173338_) throws IOException {
		super(p_173336_, shaderLocation, p_173338_);
		uniform_seed = getUniform("SeedContext");
		uniform_position = getUniform("PositionContext");
	}

	public void invokeThenUpload(int seed, float x, float y, float z, BufferBuilder buffer) {
		uniform_seed.set(seed);
		uniform_position.set(x, y, z);
		invokeThenUpload(buffer);
	}

}
