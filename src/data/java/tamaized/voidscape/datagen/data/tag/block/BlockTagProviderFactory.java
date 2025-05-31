package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class BlockTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Directory(IBlockTagProviderFactory.class)
	List<IBlockTagProviderFactory> factories;

	@Nullable
	private BlockTagsProvider provider;

	public BlockTagsProvider make(GatherDataEvent event) {
		return provider = new BlockTagsProviderAccessor(
			event.getGenerator().getPackOutput(),
			registryProvider.retrieve(event),
			Voidscape.MODID,
			event.getExistingFileHelper()
		) {
			@Override
			protected void addTags(HolderLookup.Provider provider) {
				factories.forEach(f -> f.make(this, provider));
			}
		};
	}

	public Optional<CompletableFuture<TagsProvider.TagLookup<Block>>> lookup() {
		return Optional.ofNullable(provider).map(BlockTagsProvider::contentsGetter);
	}

	public static abstract class BlockTagsProviderAccessor extends BlockTagsProvider {

		public BlockTagsProviderAccessor(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, modId, existingFileHelper);
		}

		@Override
		public IntrinsicTagAppender<Block> tag(TagKey<Block> tag) {
			return super.tag(tag);
		}
	}

}
