package tamaized.voidscape.datagen.util;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import tamaized.beanification.Component;

import java.util.function.Function;

@Component
public class TagProviderUtil {

	@SafeVarargs
	public final <T> void tagMany(Function<TagKey<T>, TagsProvider.TagAppender<T>> provider, ResourceKey<T> type, TagKey<T>... tags) {
		for (TagKey<T> key : tags) {
			provider.apply(key).add(type);
		}
	}

}
