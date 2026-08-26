package tamaized.voidscape.client.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.BiFunction;

public class ArmorModelCache<K, M> {

	private final Map<K, M> models;
	private final BiFunction<EntityModelSet, K, M> factory;

	@Nullable
	private EntityModelSet modelSet;

	public ArmorModelCache(Map<K, M> models, BiFunction<EntityModelSet, K, M> factory) {
		this.models = models;
		this.factory = factory;
	}

	public M get(K key) {
		EntityModelSet entityModels = Minecraft.getInstance().getEntityModels();
		if (modelSet != entityModels) {
			modelSet = entityModels;
			models.clear();
		}
		return models.computeIfAbsent(key, cacheKey -> factory.apply(entityModels, cacheKey));
	}

}
