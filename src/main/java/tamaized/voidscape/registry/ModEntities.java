package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.entity.*;

import java.util.function.Supplier;

@Component
public class ModEntities {

	public final Supplier<EntityType<VoidlingEntity>> VOIDLING = RegUtil.register(Registries.ENTITY_TYPE, "voidling",
		(key) -> build(
			ResourceKey.create(Registries.ENTITY_TYPE, key),
			makeCastedBuilder(VoidlingEntity.class, VoidlingEntity::new, MobCategory.MONSTER)
				.sized(0.7F, 0.5F)
				.setTrackingRange(256)
				.fireImmune()
		));

	public final Supplier<EntityType<CorruptedPawnEntity>> CORRUPTED_PAWN = RegUtil.register(Registries.ENTITY_TYPE, "corrupted_pawn",
		(key) -> build(
			ResourceKey.create(Registries.ENTITY_TYPE, key),
			makeCastedBuilder(CorruptedPawnEntity.class, CorruptedPawnEntity::new, MobCategory.MONSTER)
				.sized(2.5F, 2.5F)
				.setTrackingRange(256)
				.fireImmune()
		));

	public final Supplier<EntityType<AntiBoltEntity>> ANTI_BOLT = RegUtil.register(Registries.ENTITY_TYPE, "anti_bolt",
		(key) -> make(
			ResourceKey.create(Registries.ENTITY_TYPE, key),
			AntiBoltEntity::new,
			MobCategory.MISC,
			0.5F, 0.5F
		));

	public final Supplier<EntityType<IchorBoltEntity>> ICHOR_BOLT = RegUtil.register(Registries.ENTITY_TYPE, "ichor_bolt",
		(key) -> make(
			ResourceKey.create(Registries.ENTITY_TYPE, key),
			IchorBoltEntity::new,
			MobCategory.MISC,
			0.5F, 0.5F
		));

	public final Supplier<EntityType<NullServantIchorBoltEntity>> NULL_SERVANT_ICHOR_BOLT = RegUtil.register(Registries.ENTITY_TYPE, "null_servant_ichor_bolt",
		(key) -> make(
			ResourceKey.create(Registries.ENTITY_TYPE, key),
			NullServantIchorBoltEntity::new,
			MobCategory.MISC,
			0.5F, 0.5F
		));

	public final Supplier<EntityType<NullServantEntity>> NULL_SERVANT = RegUtil.register(Registries.ENTITY_TYPE, "null_servant",
		(key) -> build(
			ResourceKey.create(Registries.ENTITY_TYPE, key),
			makeCastedBuilder(NullServantEntity.class, NullServantEntity::new, MobCategory.MONSTER)
				.sized(0.6F, 1.95F)
				.setTrackingRange(256)
				.fireImmune()
		));

	public final Supplier<EntityType<NullServantAugmentBlockEntity>> NULL_SERVANT_AUGMENT_BLOCK = RegUtil.register(Registries.ENTITY_TYPE, "null_servant_augment_block",
		(key) -> make(
			ResourceKey.create(Registries.ENTITY_TYPE, key),
			NullServantAugmentBlockEntity::new,
			MobCategory.MISC, 1F, 1F
		));

	public final Supplier<EntityType<PhantomNullServantEntity>> NULL_SERVANT_PHANTOM = RegUtil.register(Registries.ENTITY_TYPE, "null_servant_phantom",
		(key) -> build(
			ResourceKey.create(Registries.ENTITY_TYPE, key),
			makeCastedBuilder(PhantomNullServantEntity.class, PhantomNullServantEntity::new, MobCategory.MISC)
				.sized(0.6F, 1.95F)
				.fireImmune()
		));

	public final Supplier<EntityType<VoidsWrathEntity>> VOIDS_WRATH = RegUtil.register(Registries.ENTITY_TYPE, "voids_wrath",
		(key) -> build(
			ResourceKey.create(Registries.ENTITY_TYPE, key),
			makeCastedBuilder(VoidsWrathEntity.class, VoidsWrathEntity::new, MobCategory.MONSTER)
				.sized(0.9F, 2.0F)
				.setTrackingRange(256)
				.fireImmune()
		));

	public final Supplier<EntityType<StrangePearlEntity>> STRANGE_PEARL = RegUtil.register(Registries.ENTITY_TYPE, "strange_pearl",
		(key) -> make(
			ResourceKey.create(Registries.ENTITY_TYPE, key),
			StrangePearlEntity::new,
			MobCategory.MISC,
			0.25F, 0.25F
		));

	private <E extends Entity> EntityType<E> make(ResourceKey<EntityType<?>> id, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height) {
		return build(id, makeBuilder(factory, classification).sized(width, height));
	}

	private <E extends Entity> EntityType<E> make(ResourceKey<EntityType<?>> id, EntityType.EntityFactory<E> factory, MobCategory classification) {
		return make(id, factory, classification, 0.6F, 1.8F);
	}

	private <E extends Entity> EntityType<E> build(ResourceKey<EntityType<?>> id, EntityType.Builder<E> builder) {
		return builder.build(id);
	}

	private <E extends Entity> EntityType.Builder<E> makeCastedBuilder(Class<E> cast, EntityType.EntityFactory<E> factory, MobCategory classification) {
		return makeBuilder(factory, classification);
	}

	private <E extends Entity> EntityType.Builder<E> makeBuilder(EntityType.EntityFactory<E> factory, MobCategory classification) {
		return EntityType.Builder.of(factory, classification).
			sized(0.6F, 1.8F).
			setTrackingRange(80).
			setUpdateInterval(3).
			setShouldReceiveVelocityUpdates(true);
	}

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::registerSpawnPlacements);
		bus.addListener(this::registerAttributes);
	}

	private void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
		registerGenericSpawnPlacement(VOIDLING, event);
		registerGenericSpawnPlacement(NULL_SERVANT, event);
		registerGenericSpawnPlacement(VOIDS_WRATH, event);
	}

	private <T extends Entity> void registerGenericSpawnPlacement(Supplier<EntityType<T>> entity, RegisterSpawnPlacementsEvent event) {
		event.register(entity.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (entityType, serverLevel, spawnType, pos, random) ->
				SpawnPlacementTypes.ON_GROUND.isSpawnPositionOk(serverLevel, pos, entityType)
					&& serverLevel.getEntities(null, new AABB(pos).inflate(20F, 3F, 20F)).isEmpty(),
			RegisterSpawnPlacementsEvent.Operation.REPLACE
		);
	}

	private void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(VOIDLING.get(), VoidlingEntity.createAttributes().build());
		event.put(CORRUPTED_PAWN.get(), CorruptedPawnEntity.createAttributes().build());
		event.put(VOIDS_WRATH.get(), VoidsWrathEntity.createAttributes().build());
		event.put(NULL_SERVANT.get(), NullServantEntity.createAttributes().build());
		event.put(NULL_SERVANT_PHANTOM.get(), NullServantEntity.createAttributes().build());
		event.put(NULL_SERVANT_AUGMENT_BLOCK.get(), NullServantAugmentBlockEntity.createAttributes().build());
	}

}
