package tamaized.voidscape.datagen.util;

import net.minecraft.core.Holder;
import tamaized.beanification.Component;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

@Component
public class HolderReferenceBinderUtil {

	private final MethodHandles.Lookup lookup = MethodHandles.lookup();

	private final MethodHandle Holder_Reference_bindValue;

	{
		try {
			Method bindValue = Holder.Reference.class.getDeclaredMethod("bindValue", Object.class);
			bindValue.setAccessible(true);
			Holder_Reference_bindValue = lookup.unreflect(bindValue);
		} catch (IllegalAccessException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> void bindValue(Holder.Reference<T> ref, T value) {
		try {
			Holder_Reference_bindValue.invoke(ref, value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
