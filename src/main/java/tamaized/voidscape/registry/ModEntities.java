package tamaized.voidscape.registry;

import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.model.*;
import tamaized.voidscape.client.entity.render.*;
import tamaized.voidscape.entity.*;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Voidscape.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities implements RegistryClass {

	private static final DeferredRegister<EntityType<?>> REGISTRY = RegUtil.create(Registries.ENTITY_TYPE);

	public static final Supplier<EntityType<VoidlingEntity>> VOIDLING = REGISTRY.register("voidling", () ->
			build(new ResourceLocation(Voidscape.MODID, "voidling"), makeCastedBuilder(VoidlingEntity.class, VoidlingEntity::new, MobCategory.MONSTER).sized(0.7F, 0.5F).setTrackingRange(256).fireImmune()));

	public static final Supplier<EntityType<CorruptedPawnEntity>> CORRUPTED_PAWN = REGISTRY.register("corrupted_pawn", () ->
			build(new ResourceLocation(Voidscape.MODID, "corrupted_pawn"), makeCastedBuilder(CorruptedPawnEntity.class, CorruptedPawnEntity::new, MobCategory.MONSTER).sized(2.5F, 2.5F).setTrackingRange(256).fireImmune()));

	public static final Supplier<EntityType<AntiBoltEntity>> ANTI_BOLT = REGISTRY.register("anti_bolt", () ->
			make(new ResourceLocation(Voidscape.MODID, "anti_bolt"), AntiBoltEntity::new, MobCategory.MISC, 0.5F, 0.5F));

	public static final Supplier<EntityType<IchorBoltEntity>> ICHOR_BOLT = REGISTRY.register("ichor_bolt", () ->
			make(new ResourceLocation(Voidscape.MODID, "ichor_bolt"), IchorBoltEntity::new, MobCategory.MISC, 0.5F, 0.5F));

	public static final Supplier<EntityType<NullServantIchorBoltEntity>> NULL_SERVANT_ICHOR_BOLT = REGISTRY.register("null_servant_ichor_bolt", () ->
			make(new ResourceLocation(Voidscape.MODID, "null_servant_ichor_bolt"), NullServantIchorBoltEntity::new, MobCategory.MISC, 0.5F, 0.5F));

	public static final Supplier<EntityType<NullServantEntity>> NULL_SERVANT = REGISTRY.register("null_servant", () -> {
		EntityType<NullServantEntity> type = build(new ResourceLocation(Voidscape.MODID, "null_servant"), makeCastedBuilder(NullServantEntity.class, NullServantEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).setTrackingRange(256).fireImmune());
		SpawnPlacements.register(type, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type1, level, spawn, pos, rand) ->
				NaturalSpawner.canSpawnAtBody(SpawnPlacements.Type.ON_GROUND, level, pos, type1) &&
						level.getEntities(null, new AABB(pos).inflate(20F, 3F, 20F)).isEmpty());
		return type;
	});
	public static final Supplier<EntityType<NullServantAugmentBlockEntity>> NULL_SERVANT_AUGMENT_BLOCK = REGISTRY.register("null_servant_augment_block", () ->
			make(new ResourceLocation(Voidscape.MODID, "null_servant_augment_block"), NullServantAugmentBlockEntity::new, MobCategory.MISC, 1F, 1F));
	public static final Supplier<EntityType<PhantomNullServantEntity>> NULL_SERVANT_PHANTOM = REGISTRY.register("null_servant_phantom", () ->
			build(new ResourceLocation(Voidscape.MODID, "null_servant_phantom"), makeCastedBuilder(PhantomNullServantEntity.class, PhantomNullServantEntity::new, MobCategory.MISC).sized(0.6F, 1.95F).fireImmune()));

	public static final Supplier<EntityType<VoidsWrathEntity>> VOIDS_WRATH = REGISTRY.register("voids_wrath", () -> {
		EntityType<VoidsWrathEntity> type = build(new ResourceLocation(Voidscape.MODID, "voids_wrath"), makeCastedBuilder(VoidsWrathEntity.class, VoidsWrathEntity::new, MobCategory.MONSTER).sized(0.9F, 2.0F).setTrackingRange(256).fireImmune());
		SpawnPlacements.register(type, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type1, level, spawn, pos, rand) ->
				rand.nextInt(3) == 0 &&
						NaturalSpawner.canSpawnAtBody(SpawnPlacements.Type.ON_GROUND, level, pos, type1) &&
						level.getEntities(null, new AABB(pos).inflate(20F, 3F, 20F)).isEmpty());
		return type;
	});

	public static final Supplier<EntityType<StrangePearlEntity>> STRANGE_PEARL = REGISTRY.register("strange_pearl", () ->
			make(new ResourceLocation(Voidscape.MODID, "strange_pearl"), StrangePearlEntity::new, MobCategory.MISC, 0.25F, 0.25F));

	@Override
	public void init(IEventBus bus) {

	}

	private static <E extends Entity> EntityType<E> make(ResourceLocation id, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height) {
		return build(id, makeBuilder(factory, classification).sized(width, height));
	}

	private static <E extends Entity> EntityType<E> make(ResourceLocation id, EntityType.EntityFactory<E> factory, MobCategory classification) {
		return make(id, factory, classification, 0.6F, 1.8F);
	}

	private static <E extends Entity> EntityType<E> build(ResourceLocation id, EntityType.Builder<E> builder) {
		return builder.build(id.toString());
	}

	private static <E extends Entity> EntityType.Builder<E> makeCastedBuilder(Class<E> cast, EntityType.EntityFactory<E> factory, MobCategory classification) {
		return makeBuilder(factory, classification);
	}

	private static <E extends Entity> EntityType.Builder<E> makeBuilder(EntityType.EntityFactory<E> factory, MobCategory classification) {
		return EntityType.Builder.of(factory, classification).
				sized(0.6F, 1.8F).
				setTrackingRange(80).
				setUpdateInterval(3).
				setShouldReceiveVelocityUpdates(true);
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(VOIDLING.get(), VoidlingEntity.createAttributes().build());
		event.put(CORRUPTED_PAWN.get(), CorruptedPawnEntity.createAttributes().build());
		event.put(VOIDS_WRATH.get(), VoidsWrathEntity.createAttributes().build());
		event.put(NULL_SERVANT.get(), NullServantEntity.createAttributes().build());
		event.put(NULL_SERVANT_PHANTOM.get(), NullServantEntity.createAttributes().build());
		event.put(NULL_SERVANT_AUGMENT_BLOCK.get(), NullServantAugmentBlockEntity.createAttributes().build());
	}

	@OnlyIn(Dist.CLIENT)
	public static class ModelLayerLocations {

		public static final ModelLayerLocation VOIDLING = make("voidling");
		public static final ModelLayerLocation CORRUPTED_PAWN = make("corruptedpawn");
		public static final ModelLayerLocation VOIDS_WRATH = make("voidswrath");
		public static final ModelLayerLocation VOIDS_WRATH_CHARGED = make("voidswrathcharged");
		public static final ModelLayerLocation NULL_SERVANT = make("nullservant");

		public static final ModelLayerLocation MODEL_ARMOR_CORRUPT_OUTER = make("corrupt_outer");
		public static final ModelLayerLocation MODEL_ARMOR_CORRUPT_INNER = make("corrupt_inner");
		public static final ModelLayerLocation MODEL_ARMOR_TITANITE = make("titanite");
		public static final ModelLayerLocation MODEL_ARMOR_ICHOR = make("ichor");
		public static final ModelLayerLocation MODEL_ARMOR_ASTRAL = make("astral");

		private static ModelLayerLocation make(String name) {
			return new ModelLayerLocation(new ResourceLocation(Voidscape.MODID, "main"), name);
		}

	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModelLayerLocations.VOIDLING, ModelVoidling::createMesh);
		event.registerLayerDefinition(ModelLayerLocations.CORRUPTED_PAWN, ModelCorruptedPawn::createMesh);
		event.registerLayerDefinition(ModelLayerLocations.VOIDS_WRATH, () -> ModelVoidsWrath.createMesh(CubeDeformation.NONE));
		event.registerLayerDefinition(ModelLayerLocations.VOIDS_WRATH_CHARGED, () -> ModelVoidsWrath.createMesh(new CubeDeformation(1.0F)));
		event.registerLayerDefinition(ModelLayerLocations.NULL_SERVANT, ModelNullServant::createMesh);

		event.registerLayerDefinition(ModelLayerLocations.MODEL_ARMOR_CORRUPT_OUTER, () -> ModelArmorCorrupt.makeMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0F));
		event.registerLayerDefinition(ModelLayerLocations.MODEL_ARMOR_CORRUPT_INNER, () -> ModelArmorCorrupt.makeMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0F));
		event.registerLayerDefinition(ModelLayerLocations.MODEL_ARMOR_TITANITE, () -> ModelArmorTitanite.makeMesh(CubeDeformation.NONE, 0F));
		event.registerLayerDefinition(ModelLayerLocations.MODEL_ARMOR_ICHOR, () -> ModelArmorTitanite.makeMesh(CubeDeformation.NONE, 0F));
		event.registerLayerDefinition(ModelLayerLocations.MODEL_ARMOR_ASTRAL, () -> ModelArmorTitanite.makeMesh(CubeDeformation.NONE, 0F));

	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(VOIDLING.get(), RenderVoidling::factory);
		event.registerEntityRenderer(CORRUPTED_PAWN.get(), RenderCorruptedPawn::factory);
		event.registerEntityRenderer(VOIDS_WRATH.get(), RenderVoidsWrath::new);
		event.registerEntityRenderer(NULL_SERVANT.get(), RenderNullServant::new);
		event.registerEntityRenderer(NULL_SERVANT_AUGMENT_BLOCK.get(), RenderNullServantAugmentBlock::new);
		event.registerEntityRenderer(NULL_SERVANT_PHANTOM.get(), RenderNullServant::new);
		event.registerEntityRenderer(ANTI_BOLT.get(), RenderAntiBolt::new);
		event.registerEntityRenderer(ICHOR_BOLT.get(), context -> new RenderSpellBolt<>(context, 0xFF7700));
		event.registerEntityRenderer(NULL_SERVANT_ICHOR_BOLT.get(), context -> new RenderSpellBolt<>(context, 0xFF0000));
		event.registerEntityRenderer(STRANGE_PEARL.get(), context -> new ThrownItemRenderer<>(context, 1F, true));
	}

}
