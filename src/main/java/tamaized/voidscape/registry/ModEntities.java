package tamaized.voidscape.registry;

import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.model.ModelArmorCorrupt;
import tamaized.voidscape.client.entity.model.ModelCorruptedPawn;
import tamaized.voidscape.client.entity.model.ModelCorruptedPawnTentacle;
import tamaized.voidscape.client.entity.model.ModelNullServant;
import tamaized.voidscape.client.entity.render.RenderAntiBolt;
import tamaized.voidscape.client.entity.render.RenderCorruptedPawn;
import tamaized.voidscape.client.entity.render.RenderCorruptedPawnTentacle;
import tamaized.voidscape.client.entity.render.RenderNoOp;
import tamaized.voidscape.client.entity.render.RenderNullServant;
import tamaized.voidscape.client.entity.render.RenderSpellBolt;
import tamaized.voidscape.entity.EntityAntiBolt;
import tamaized.voidscape.entity.EntityCorruptedPawnBoss;
import tamaized.voidscape.entity.EntityCorruptedPawnPhantom;
import tamaized.voidscape.entity.EntityCorruptedPawnTentacle;
import tamaized.voidscape.entity.EntityNullServant;
import tamaized.voidscape.entity.abilities.EntitySpellAura;
import tamaized.voidscape.entity.abilities.EntitySpellBolt;

@Mod.EventBusSubscriber(modid = Voidscape.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities implements RegistryClass {

	private static final DeferredRegister<EntityType<?>> REGISTRY = RegUtil.create(ForgeRegistries.ENTITIES);

	public static final RegistryObject<EntityType<EntitySpellBolt>> SPELL_BOLT = REGISTRY.register("spell_bolt", () -> make(new ResourceLocation(Voidscape.MODID, "spell_bolt"), EntitySpellBolt::new, MobCategory.MISC, 0.5F, 0.5F));
	public static final RegistryObject<EntityType<EntitySpellAura>> SPELL_AURA = REGISTRY.register("spell_aura", () -> make(new ResourceLocation(Voidscape.MODID, "spell_aura"), EntitySpellAura::new, MobCategory.MISC, 0.5F, 0.5F));
	public static final RegistryObject<EntityType<EntityCorruptedPawnPhantom>> CORRUPTED_PAWN_PHANTOM = REGISTRY.register("corrupted_pawn_phantom", () -> build(new ResourceLocation(Voidscape.MODID, "corrupted_pawn_phantom"), makeCastedBuilder(EntityCorruptedPawnPhantom.class, EntityCorruptedPawnPhantom::new, MobCategory.MONSTER).sized(2.5F, 2.5F).setTrackingRange(256).fireImmune()));
	public static final RegistryObject<EntityType<EntityCorruptedPawnBoss>> CORRUPTED_PAWN_BOSS = REGISTRY.register("corrupted_pawn_boss", () -> build(new ResourceLocation(Voidscape.MODID, "corrupted_pawn_boss"), makeCastedBuilder(EntityCorruptedPawnBoss.class, EntityCorruptedPawnBoss::new, MobCategory.MONSTER).sized(2.5F, 2.5F).setTrackingRange(256).fireImmune()));
	public static final RegistryObject<EntityType<EntityCorruptedPawnTentacle>> CORRUPTED_PAWN_TENTACLE = REGISTRY.register("corrupted_pawn_tentacle", () -> build(new ResourceLocation(Voidscape.MODID, "corrupted_pawn_tentacle"), makeCastedBuilder(EntityCorruptedPawnTentacle.class, EntityCorruptedPawnTentacle::new, MobCategory.MISC).sized(3F, 5F).setTrackingRange(256).fireImmune()));
	public static final RegistryObject<EntityType<EntityAntiBolt>> ANTI_BOLT = REGISTRY.register("anti_bolt", () -> make(new ResourceLocation(Voidscape.MODID, "anti_bolt"), EntityAntiBolt::new, MobCategory.MISC, 0.5F, 0.5F));
	public static final RegistryObject<EntityType<EntityNullServant>> NULL_SERVANT = REGISTRY.register("null_servant", () -> {
		EntityType<EntityNullServant> type = build(new ResourceLocation(Voidscape.MODID, "null_servant"), makeCastedBuilder(EntityNullServant.class, EntityNullServant::new, MobCategory.MONSTER).sized(0.6F, 1.95F).setTrackingRange(256).fireImmune());
		SpawnPlacements.register(type, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type1, level, spawn, pos, rand) -> !level.
				getBlockState(pos.below()).is(Blocks.BEDROCK) && NaturalSpawner.
				canSpawnAtBody(SpawnPlacements.Type.ON_GROUND, level, pos, type1) && level.getEntities(null,
				new AABB(pos).inflate(20F, 3F, 20F)).isEmpty());
		return type;
	});

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
		event.put(CORRUPTED_PAWN_PHANTOM.get(), Mob.createMobAttributes().build());
		event.put(CORRUPTED_PAWN_BOSS.get(), Mob.createMobAttributes().build());
		event.put(NULL_SERVANT.get(), EntityNullServant.createAttributes().build());
		event.put(CORRUPTED_PAWN_TENTACLE.get(), LivingEntity.createLivingAttributes().build());
	}

	@OnlyIn(Dist.CLIENT)
	public static class ModelLayerLocations {

		public static final ModelLayerLocation CORRUPTED_PAWN = make("corruptedpawn");
		public static final ModelLayerLocation CORRUPTED_PAWN_TENTACLE = make("corruptedpawntentacle");
		public static final ModelLayerLocation NULL_SERVANT = make("nullservant");

		public static final ModelLayerLocation MODEL_ARMOR_INSANE_OUTER = make("insane_outer");
		public static final ModelLayerLocation MODEL_ARMOR_INSANE_INNER = make("insane_inner");

		private static ModelLayerLocation make(String name) {
			return new ModelLayerLocation(new ResourceLocation(Voidscape.MODID, "main"), name);
		}

	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModelLayerLocations.CORRUPTED_PAWN, ModelCorruptedPawn::createMesh);
		event.registerLayerDefinition(ModelLayerLocations.CORRUPTED_PAWN_TENTACLE, ModelCorruptedPawnTentacle::createMesh);
		event.registerLayerDefinition(ModelLayerLocations.NULL_SERVANT, ModelNullServant::createMesh);

		event.registerLayerDefinition(ModelLayerLocations.MODEL_ARMOR_INSANE_OUTER, () -> ModelArmorCorrupt.makeMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0F));
		event.registerLayerDefinition(ModelLayerLocations.MODEL_ARMOR_INSANE_INNER, () -> ModelArmorCorrupt.makeMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0F));

	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SPELL_BOLT.get(), RenderSpellBolt::new);
		event.registerEntityRenderer(SPELL_AURA.get(), RenderNoOp::new);
		event.registerEntityRenderer(CORRUPTED_PAWN_PHANTOM.get(), RenderCorruptedPawn::factory);
		event.registerEntityRenderer(CORRUPTED_PAWN_BOSS.get(), RenderCorruptedPawn::factory);
		event.registerEntityRenderer(CORRUPTED_PAWN_TENTACLE.get(), RenderCorruptedPawnTentacle::new);
		event.registerEntityRenderer(NULL_SERVANT.get(), RenderNullServant::new);
		event.registerEntityRenderer(ANTI_BOLT.get(), RenderAntiBolt::new);
	}

}
