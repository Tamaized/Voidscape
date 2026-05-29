package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

@Component
public class ModBiomes {

	public final ResourceKey<Biome> ANTISPIRES = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Voidscape.MODID, "antispires"));

	public final ResourceKey<Biome> END = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Voidscape.MODID, "end"));

	public final ResourceKey<Biome> NETHER = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Voidscape.MODID, "nether"));

	public final ResourceKey<Biome> NULL = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Voidscape.MODID, "null"));

	public final ResourceKey<Biome> OVERWORLD = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Voidscape.MODID, "overworld"));

	public final ResourceKey<Biome> THUNDER_FOREST = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Voidscape.MODID, "thunder_forest"));

	public final ResourceKey<Biome> THUNDERSPIRES = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Voidscape.MODID, "thunderspires"));

	public final ResourceKey<Biome> VOID = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Voidscape.MODID, "void"));

	public final ResourceKey<Biome> AETHER = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Voidscape.MODID, "aether"));

}
