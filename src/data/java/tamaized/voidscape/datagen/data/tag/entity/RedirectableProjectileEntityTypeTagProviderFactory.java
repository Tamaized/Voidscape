package tamaized.voidscape.datagen.data.tag.entity;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedEntityTypeTagProvider;
import tamaized.voidscape.registry.ModEntities;

@Component
public class RedirectableProjectileEntityTypeTagProviderFactory implements IEntityTypeTagProviderFactory {

	@Autowired
	private ModEntities entities;

	@Override
	public void make(ExposedEntityTypeTagProvider accessor, HolderLookup.Provider provider) {
		accessor.tag(EntityTypeTags.REDIRECTABLE_PROJECTILE).add(
			entities.STRANGE_PEARL.get()
		);
	}
}
