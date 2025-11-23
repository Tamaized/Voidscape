package tamaized.voidscape.datagen.data.tag.entity;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class EntityTypeTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Directory(IEntityTypeTagProviderFactory.class)
	List<IEntityTypeTagProviderFactory> factories;

	public EntityTypeTagsProvider make(GatherDataEvent event) {
		return new EntityTypeTagsProviderAccessor(
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

	public static abstract class EntityTypeTagsProviderAccessor extends EntityTypeTagsProvider {

		public EntityTypeTagsProviderAccessor(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, modId, existingFileHelper);
		}

		@Override
		public IntrinsicTagAppender<EntityType<?>> tag(TagKey<EntityType<?>> tag) {
			return super.tag(tag);
		}
	}

}
