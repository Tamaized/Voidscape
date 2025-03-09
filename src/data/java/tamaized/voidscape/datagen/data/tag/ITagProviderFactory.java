package tamaized.voidscape.datagen.data.tag;

import net.minecraft.data.tags.TagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public interface ITagProviderFactory<T> {

	TagsProvider<T> make(GatherDataEvent event);

}
