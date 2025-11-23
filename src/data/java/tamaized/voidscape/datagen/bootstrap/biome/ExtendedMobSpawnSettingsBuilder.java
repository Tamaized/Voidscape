package tamaized.voidscape.datagen.bootstrap.biome;

import com.aetherteam.aether.Aether;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.stream.Stream;

public class ExtendedMobSpawnSettingsBuilder extends MobSpawnSettings.Builder {

	public ExtendedMobSpawnSettingsBuilder() {
		super();
		this.spawners = Stream.of(MobCategory.values())
			.filter(c -> !c.getName().startsWith(Aether.MODID))
			.collect(ImmutableMap.toImmutableMap(k -> k, v -> Lists.newArrayList()));
	}

}
