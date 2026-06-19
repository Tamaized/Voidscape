package tamaized.voidscape.datagen.util;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import tamaized.beanification.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class TagProviderUtil {

	@SafeVarargs
	@SuppressWarnings({"UseBulkOperation", "ManualArrayToCollectionCopy"})
	public final <T> void tagMany(Function<TagKey<T>, TagsProvider.TagAppender<T>> provider, ResourceKey<T> type, TagKey<T>... tags) {
		List<TagKey<T>> list = new ArrayList<>();
		for (TagKey<T> tag : tags) {
			list.add(tag);
		}
		tagMany(provider, type, list);
	}

	public final <T> void tagMany(Function<TagKey<T>, TagsProvider.TagAppender<T>> provider, ResourceKey<T> type, List<TagKey<T>> tags) {
		tags.forEach(tag -> provider.apply(tag).add(type));
	}

	@SafeVarargs
	@SuppressWarnings({"UseBulkOperation", "ManualArrayToCollectionCopy"})
	public final <T> MultiTagAppender<T> multiTagAll(Function<TagKey<T>, TagsProvider.TagAppender<T>> provider, TagKey<T>... tags) {
		List<TagKey<T>> list = new ArrayList<>();
		for (TagKey<T> tag : tags) {
			list.add(tag);
		}
		return new MultiTagAppender<>(this, provider, list);
	}

	public record MultiTagAppender<T>(TagProviderUtil util, Function<TagKey<T>, TagsProvider.TagAppender<T>> provider, List<TagKey<T>> tags) {

		@SafeVarargs
		@SuppressWarnings("unchecked")
		public final void add(ResourceKey<T>... types) {
			for (ResourceKey<T> type : types) {
				util.tagMany(provider, type, tags);
			}
		}

	}

}
