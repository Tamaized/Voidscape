package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.Nullable;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;

import java.util.function.Function;

public class ModDamageSource implements RegistryClass {

	public static final ResourceKey<DamageType> VOIDIC = create("voidic");

	public static ResourceKey<DamageType> create(String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Voidscape.MODID, name));
	}

	public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> type) {
		return getEntityDamageSource(level, type, null);
	}

	public static DamageSource getEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker) {
		return getIndirectEntityDamageSource(level, type, attacker, attacker);
	}

	public static DamageSource getIndirectEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker, @Nullable Entity indirectAttacker) {
		return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), attacker, indirectAttacker);
	}

	@Override
	public void init(IEventBus bus) {

	}

}
