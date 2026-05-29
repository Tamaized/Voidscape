package tamaized.voidscape.client.shader;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceProvider;

import javax.annotation.Nullable;
import java.io.IOException;

public class OptimalAlphaShaderInstance extends BindableShaderInstance {

	@Nullable
	public final Uniform ALPHA;

	public OptimalAlphaShaderInstance(ResourceProvider p_173336_, Identifier shaderLocation, VertexFormat p_173338_) throws IOException {
		super(p_173336_, shaderLocation, p_173338_);
		ALPHA = getUniform("Alpha");
	}

	public final void setValue(float val) {
		if (ALPHA != null) {
			ALPHA.set(val);
		}
	}

	public final void setValueBindApply(float val) {
		bind(() -> setValue(val));
	}

	public final void reset() {
		setValue(0.2F);
	}

	public final void resetClear() {
		runThenClear(this::reset);
	}

	public final void invokeThenClear(float val, Runnable exec) {
		setValueBindApply(val);
		exec.run();
		resetClear();
	}

	public final void invokeThenUpload(float val, BufferBuilder buffer) {
		invokeThenClear(val, () -> BufferUploader.drawWithShader(buffer.buildOrThrow()));
	}

}