package tamaized.voidscape.datagen.data.tag.damagetype;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.RegistryProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class DamageTypeTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Directory(IDamageTypeTagProviderFactory.class)
	List<IDamageTypeTagProviderFactory> factories;

	public DamageTypeTagsProvider make(GatherDataEvent event) {
		return new DamageTypeTagsProviderAccessor(
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

	public static abstract class DamageTypeTagsProviderAccessor extends DamageTypeTagsProvider {

		public DamageTypeTagsProviderAccessor(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, modId, existingFileHelper);
		}

		@Override
		public TagAppender<DamageType> tag(TagKey<DamageType> tag) {
			return super.tag(tag);
		}
	}

}
