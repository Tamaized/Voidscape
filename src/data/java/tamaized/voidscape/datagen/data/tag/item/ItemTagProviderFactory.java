package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.datagen.data.tag.block.BlockTagProviderFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class ItemTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private BlockTagProviderFactory blockTagProviderFactory;

	@Directory(IItemTagProviderFactory.class)
	List<IItemTagProviderFactory> factories;

	public ItemTagsProvider make(GatherDataEvent event) {
		return new ItemTagsProviderAccessor(
			event.getGenerator().getPackOutput(),
			registryProvider.retrieve(event),
			blockTagProviderFactory.lookup().orElseThrow(),
			Voidscape.MODID,
			event.getExistingFileHelper()
		) {
			@Override
			protected void addTags(HolderLookup.Provider provider) {
				factories.forEach(f -> f.make(this, provider));
			}
		};
	}

	public static abstract class ItemTagsProviderAccessor extends ItemTagsProvider {

		public ItemTagsProviderAccessor(PackOutput output,
										CompletableFuture<HolderLookup.Provider> lookupProvider,
										CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
										String modId,
										@org.jetbrains.annotations.Nullable net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, blockTags, modId, existingFileHelper);
		}

		@Override
		public IntrinsicTagAppender<Item> tag(TagKey<Item> tag) {
			return super.tag(tag);
		}
	}

}
