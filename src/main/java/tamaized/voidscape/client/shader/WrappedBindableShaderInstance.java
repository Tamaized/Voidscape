package tamaized.voidscape.client.shader;

import net.minecraft.client.renderer.ShaderInstance;
import tamaized.beanification.Autowired;
import tamaized.voidscape.util.UnsafeUtil;

import java.io.IOException;
import java.util.function.Supplier;

public class WrappedBindableShaderInstance extends BindableShaderInstance {

	@Autowired
	private static UnsafeUtil unsafeUtil;

	private Supplier<ShaderInstance> wrapped;

	/*
		DO NOT USE
	 */
	@SuppressWarnings("ConstantConditions")
	private WrappedBindableShaderInstance() throws IOException {
		super(null, null, null);
	}

	static WrappedBindableShaderInstance make(Supplier<ShaderInstance> instance) {
		WrappedBindableShaderInstance wrapper = unsafeUtil.newInstance(WrappedBindableShaderInstance.class);
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