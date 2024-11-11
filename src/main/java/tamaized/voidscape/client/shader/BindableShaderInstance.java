package tamaized.voidscape.client.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import javax.annotation.Nullable;
import java.io.IOException;

public class BindableShaderInstance extends ShaderInstance {

	private ShaderInstance last;

	public BindableShaderInstance(ResourceProvider p_173336_, ResourceLocation shaderLocation, VertexFormat p_173338_) throws IOException {
		super(p_173336_, shaderLocation, p_173338_);
	}

	ShaderInstance getSelf() {
		return this;
	}

	public final void bind(@Nullable Runnable exec) {
		last = RenderSystem.getShader();
		RenderSystem.setShader(this::getSelf);
		if (exec != null)
			exec.run();
		apply();
	}

	public final void runThenClear(Runnable exec) {
		exec.run();
		clear();
		RenderSystem.setShader(() -> last);
		last = null;
	}

	public final void invokeThenClear(@Nullable Runnable execBind, Runnable execPost) {
		bind(execBind);
		runThenClear(execPost);
	}

	public final void invokeThenClear(Runnable execPost) {
		invokeThenClear(null, execPost);
	}

	public final void invokeThenUpload(@Nullable Runnable execBind, BufferBuilder buffer) {
		invokeThenClear(execBind, () -> BufferUploader.drawWithShader(buffer.buildOrThrow()));
	}

	public final void invokeThenUpload(BufferBuilder buffer) {
		invokeThenClear(() -> BufferUploader.drawWithShader(buffer.buildOrThrow()));
	}

}