package tamaized.voidscape.client;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import tamaized.voidscape.Voidscape;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Shaders {

	public static AlphaShaderInstance ALPHA_POS_COLOR;
	public static AlphaShaderInstance ALPHA_POS_TEX_COLOR;
	public static OptimalAlphaShaderInstance OPTIMAL_ALPHA_LESSTHAN_POS_COLOR;
	public static OptimalAlphaShaderInstance OPTIMAL_ALPHA_LESSTHAN_POS_TEX_COLOR;
	public static OptimalAlphaShaderInstance OPTIMAL_ALPHA_GREATERTHAN_POS_COLOR;
	public static OptimalAlphaShaderInstance OPTIMAL_ALPHA_GREATERTHAN_POS_TEX;
	public static OptimalAlphaShaderInstance OPTIMAL_ALPHA_GREATERTHAN_POS_TEX_COLOR;
	public static WrappedBindableShaderInstance WRAPPED_POS_COLOR;
	public static WrappedBindableShaderInstance WRAPPED_POS_TEX;
	public static WrappedBindableShaderInstance WRAPPED_POS_TEX_COLOR;
	public static BindableShaderInstance LINES;
	public static BindableShaderInstance VOIDSKY;
	public static BindableShaderInstance VOIDSKY_ENTITY;
	public static BindableShaderInstance VOIDSKY_WINGS;

	public static void init() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<RegisterShadersEvent>) event -> {
			try {
				event.registerShader(new AlphaShaderInstance(event.getResourceManager(), new ResourceLocation(Voidscape.MODID, "alpha/pos_color"), DefaultVertexFormat.
						POSITION_COLOR), shader -> ALPHA_POS_COLOR = (AlphaShaderInstance) shader);
				event.registerShader(new AlphaShaderInstance(event.getResourceManager(), new ResourceLocation(Voidscape.MODID, "alpha/pos_tex_color"), DefaultVertexFormat.
						POSITION_TEX_COLOR), shader -> ALPHA_POS_TEX_COLOR = (AlphaShaderInstance) shader);
				event.registerShader(new OptimalAlphaShaderInstance(event.getResourceManager(), new ResourceLocation(Voidscape.MODID, "optimal_alpha/lessthan/pos_color"), DefaultVertexFormat.
						POSITION_COLOR), shader -> OPTIMAL_ALPHA_LESSTHAN_POS_COLOR = (OptimalAlphaShaderInstance) shader);
				event.registerShader(new OptimalAlphaShaderInstance(event.getResourceManager(), new ResourceLocation(Voidscape.MODID, "optimal_alpha/lessthan/pos_tex_color"), DefaultVertexFormat.
						POSITION_TEX_COLOR), shader -> OPTIMAL_ALPHA_LESSTHAN_POS_TEX_COLOR = (OptimalAlphaShaderInstance) shader);
				event.registerShader(new OptimalAlphaShaderInstance(event.getResourceManager(), new ResourceLocation(Voidscape.MODID, "optimal_alpha/greaterthan/pos_color"), DefaultVertexFormat.
						POSITION_COLOR), shader -> OPTIMAL_ALPHA_GREATERTHAN_POS_COLOR = (OptimalAlphaShaderInstance) shader);
				event.registerShader(new OptimalAlphaShaderInstance(event.getResourceManager(), new ResourceLocation(Voidscape.MODID, "optimal_alpha/greaterthan/pos_tex"), DefaultVertexFormat.
						POSITION_TEX), shader -> OPTIMAL_ALPHA_GREATERTHAN_POS_TEX = (OptimalAlphaShaderInstance) shader);
				event.registerShader(new OptimalAlphaShaderInstance(event.getResourceManager(), new ResourceLocation(Voidscape.MODID, "optimal_alpha/greaterthan/pos_tex_color"), DefaultVertexFormat.
						POSITION_TEX_COLOR), shader -> OPTIMAL_ALPHA_GREATERTHAN_POS_TEX_COLOR = (OptimalAlphaShaderInstance) shader);
				event.registerShader(new BindableShaderInstance(event.getResourceManager(), new ResourceLocation(Voidscape.MODID, "voidsky/sky"), DefaultVertexFormat.
						POSITION), shader -> VOIDSKY = (BindableShaderInstance) shader);
				event.registerShader(new BindableShaderInstance(event.getResourceManager(), new ResourceLocation(Voidscape.MODID, "voidsky/entity"), DefaultVertexFormat.
						NEW_ENTITY), shader -> VOIDSKY_ENTITY = (BindableShaderInstance) shader);
				event.registerShader(new BindableShaderInstance(event.getResourceManager(), new ResourceLocation(Voidscape.MODID, "voidsky/wings"), DefaultVertexFormat.
						POSITION_TEX), shader -> VOIDSKY_WINGS = (BindableShaderInstance) shader);
				event.registerShader(new BindableShaderInstance(event.getResourceManager(), new ResourceLocation(Voidscape.MODID, "lines/lines"), DefaultVertexFormat.
						POSITION_COLOR_NORMAL), shader -> LINES = (BindableShaderInstance) shader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		WRAPPED_POS_COLOR = WrappedBindableShaderInstance.make(GameRenderer::getPositionColorShader);
		WRAPPED_POS_TEX = WrappedBindableShaderInstance.make(GameRenderer::getPositionTexShader);
		WRAPPED_POS_TEX_COLOR = WrappedBindableShaderInstance.make(GameRenderer::getPositionTexColorShader);
	}

	public static class BindableShaderInstance extends ShaderInstance {

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

		public final void invokeThenEndTesselator(@Nullable Runnable execBind) {
			invokeThenClear(execBind, () -> Tesselator.getInstance().end());
		}

		public final void invokeThenEndTesselator() {
			invokeThenClear(() -> Tesselator.getInstance().end());
		}

	}

	public static class WrappedBindableShaderInstance extends BindableShaderInstance {

		private Supplier<ShaderInstance> wrapped;

		/*
			DO NOT USE
		 */
		@SuppressWarnings("ConstantConditions")
		private WrappedBindableShaderInstance() throws IOException {
			super(null, null, null);
		}

		private static WrappedBindableShaderInstance make(Supplier<ShaderInstance> instance) {
			WrappedBindableShaderInstance wrapper = UnsafeHacks.newInstance(WrappedBindableShaderInstance.class);
			wrapper.wrapped = instance;
			return wrapper;
		}

		@Override
		ShaderInstance getSelf() {
			return wrapped.get();
		}

		@Override
		public void apply() {
			getSelf().apply();
		}

		@Override
		public void clear() {
			getSelf().clear();
		}
	}

	public static class OptimalAlphaShaderInstance extends BindableShaderInstance {

		@Nullable
		public final Uniform ALPHA;

		public OptimalAlphaShaderInstance(ResourceProvider p_173336_, ResourceLocation shaderLocation, VertexFormat p_173338_) throws IOException {
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

		public final void invokeThenEndTesselator(float val) {
			invokeThenClear(val, () -> Tesselator.getInstance().end());
		}

	}

	public static class AlphaShaderInstance extends BindableShaderInstance {

		@Nullable
		public final Uniform ALPHA;

		/**
		 * Use {@link AlphaShaderInstance#setType(Type)}<p/>
		 * 0: == <br>
		 * 1: < <br>
		 * 2: > <br>
		 * 3: <= <br>
		 * 4: >= <br>
		 * 5: != <br>
		 */
		@Nullable
		public final Uniform TYPE;

		public AlphaShaderInstance(ResourceProvider p_173336_, ResourceLocation shaderLocation, VertexFormat p_173338_) throws IOException {
			super(p_173336_, shaderLocation, p_173338_);
			ALPHA = getUniform("Alpha");
			TYPE = getUniform("Type");
		}

		public final void setValue(float val) {
			if (ALPHA != null) {
				ALPHA.set(val);
			}
		}

		public final void setType(Type type) {
			if (TYPE != null) {
				TYPE.set(type.type);
			}
		}

		public final void setTypeAndValue(Type type, float val) {
			setValue(val);
			setType(type);
		}

		public final void setTypeValueBindApply(Type type, float val) {
			bind(() -> setTypeAndValue(type, val));
		}

		public final void reset() {
			setTypeAndValue(Type.LESS_THAN, 0.2F);
		}

		public final void resetClear() {
			runThenClear(this::reset);
		}

		public final void invokeThenClear(Type type, float val, Runnable exec) {
			setTypeValueBindApply(type, val);
			exec.run();
			resetClear();
		}

		public final void invokeThenEndTesselator(Type type, float val) {
			invokeThenClear(type, val, () -> Tesselator.getInstance().end());
		}

		public enum Type {
			EQUALS(0),
			LESS_THAN(1),
			GREATER_THAN(2),
			LESS_THAN_EQUALS(3),
			GREATER_THAN_EQUALS(4),
			NOT(5);
			final int type;

			Type(int i) {
				type = i;
			}
		}
	}

}
