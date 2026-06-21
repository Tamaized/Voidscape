package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

@Component
public class ModDamageSource {

	public final ResourceKey<DamageType> VOIDIC = create("voidic");

	private ResourceKey<DamageType> create(String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Voidscape.MODID, name));
	}

	public DamageSource getDamageSource(Level level, ResourceKey<DamageType> type) {
		return getEntityDamageSource(level, type, null);
	}

	public DamageSource getEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker) {
		return getIndirectEntityDamageSource(level, type, attacker, attacker);
	}

	public DamageSource getIndirectEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker, @Nullable Entity indirectAttacker) {
		return new DamageSource(level.registryAccess().getOrThrow(type), attacker, indirectAttacker);
	}

}
