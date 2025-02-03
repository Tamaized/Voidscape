package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.*;

import java.util.function.Supplier;

@Component
public class ModEntities {

	private final DeferredRegister<EntityType<?>> REGISTRY = RegUtil.create(Registries.ENTITY_TYPE);

	public final Supplier<EntityType<VoidlingEntity>> VOIDLING = REGISTRY.register("voidling", () -> build(
		ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "voidling"),
		makeCastedBuilder(VoidlingEntity.class, VoidlingEntity::new, MobCategory.MONSTER)
			.sized(0.7F, 0.5F)
			.setTrackingRange(256)
			.fireImmune()
	));

	public final Supplier<EntityType<CorruptedPawnEntity>> CORRUPTED_PAWN = REGISTRY.register("corrupted_pawn", () -> build(
		ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "corrupted_pawn"),
		makeCastedBuilder(CorruptedPawnEntity.class, CorruptedPawnEntity::new, MobCategory.MONSTER)
			.sized(2.5F, 2.5F)
			.setTrackingRange(256)
			.fireImmune()
	));

	public final Supplier<EntityType<AntiBoltEntity>> ANTI_BOLT = REGISTRY.register("anti_bolt", () -> make(
		ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "anti_bolt"),
		AntiBoltEntity::new,
		MobCategory.MISC,
		0.5F, 0.5F
	));

	public final Supplier<EntityType<IchorBoltEntity>> ICHOR_BOLT = REGISTRY.register("ichor_bolt", () -> make(
		ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "ichor_bolt"),
		IchorBoltEntity::new,
		MobCategory.MISC,
		0.5F, 0.5F
	));

	public final Supplier<EntityType<NullServantIchorBoltEntity>> NULL_SERVANT_ICHOR_BOLT = REGISTRY.register("null_servant_ichor_bolt", () -> make(
		ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "null_servant_ichor_bolt"),
		NullServantIchorBoltEntity::new,
		MobCategory.MISC,
		0.5F, 0.5F
	));

	public final Supplier<EntityType<NullServantEntity>> NULL_SERVANT = REGISTRY.register("null_servant", () -> build(
		ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "null_servant"),
		makeCastedBuilder(NullServantEntity.class, NullServantEntity::new, MobCategory.MONSTER)
			.sized(0.6F, 1.95F)
			.setTrackingRange(256)
			.fireImmune()
	));

	public final Supplier<EntityType<NullServantAugmentBlockEntity>> NULL_SERVANT_AUGMENT_BLOCK = REGISTRY.register("null_servant_augment_block", () -> make(
		ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "null_servant_augment_block"),
		NullServantAugmentBlockEntity::new,
		MobCategory.MISC, 1F, 1F
	));

	public final Supplier<EntityType<PhantomNullServantEntity>> NULL_SERVANT_PHANTOM = REGISTRY.register("null_servant_phantom", () -> build(
		ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "null_servant_phantom"),
		makeCastedBuilder(PhantomNullServantEntity.class, PhantomNullServantEntity::new, MobCategory.MISC)
			.sized(0.6F, 1.95F)
			.fireImmune()
	));

	public final Supplier<EntityType<VoidsWrathEntity>> VOIDS_WRATH = REGISTRY.register("voids_wrath", () -> build(
		ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "voids_wrath"),
		makeCastedBuilder(VoidsWrathEntity.class, VoidsWrathEntity::new, MobCategory.MONSTER)
			.sized(0.9F, 2.0F)
			.setTrackingRange(256)
			.fireImmune()
	));

	public final Supplier<EntityType<StrangePearlEntity>> STRANGE_PEARL = REGISTRY.register("strange_pearl", () -> make(
		ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "strange_pearl"),
		StrangePearlEntity::new,
		MobCategory.MISC,
		0.25F, 0.25F
	));

	private <E extends Entity> EntityType<E> make(ResourceLocation id, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height) {
		return build(id, makeBuilder(factory, classification).sized(width, height));
	}

	private <E extends Entity> EntityType<E> make(ResourceLocation id, EntityType.EntityFactory<E> factory, MobCategory classification) {
		return make(id, factory, classification, 0.6F, 1.8F);
	}

	private <E extends Entity> EntityType<E> build(ResourceLocation id, EntityType.Builder<E> builder) {
		return builder.build(id.toString());
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
