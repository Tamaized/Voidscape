package tamaized.voidscape.util;

import com.google.gson.internal.UnsafeAllocator;
import tamaized.beanification.Component;

@Component
public class UnsafeUtil {

	public <T> T newInstance(Class<T> type) {
		try {
			return UnsafeAllocator.INSTANCE.newInstance(type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
