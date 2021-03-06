package tamaized.voidscape.registry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.render.RenderAntiBolt;
import tamaized.voidscape.client.entity.render.RenderCorruptedPawn;
import tamaized.voidscape.client.entity.render.RenderCorruptedPawnTentacle;
import tamaized.voidscape.client.entity.render.RenderNull;
import tamaized.voidscape.client.entity.render.RenderSpellBolt;
import tamaized.voidscape.entity.EntityAntiBolt;
import tamaized.voidscape.entity.EntityCorruptedPawnBoss;
import tamaized.voidscape.entity.EntityCorruptedPawnPhantom;
import tamaized.voidscape.entity.EntityCorruptedPawnTentacle;
import tamaized.voidscape.entity.abilities.EntitySpellAura;
import tamaized.voidscape.entity.abilities.EntitySpellBolt;

@Mod.EventBusSubscriber(modid = Voidscape.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

	private static final DeferredRegister<EntityType<?>> REGISTRY = RegUtil.create(ForgeRegistries.ENTITIES);

	public static final RegistryObject<EntityType<EntitySpellBolt>> SPELL_BOLT = REGISTRY.register("spell_bolt", () -> make(new ResourceLocation(Voidscape.MODID, "spell_bolt"), EntitySpellBolt::new, EntityClassification.MISC, 0.5F, 0.5F));
	public static final RegistryObject<EntityType<EntitySpellAura>> SPELL_AURA = REGISTRY.register("spell_aura", () -> make(new ResourceLocation(Voidscape.MODID, "spell_aura"), EntitySpellAura::new, EntityClassification.MISC, 0.5F, 0.5F));
	public static final RegistryObject<EntityType<EntityCorruptedPawnPhantom>> CORRUPTED_PAWN_PHANTOM = REGISTRY.register("corrupted_pawn_phantom", () -> build(new ResourceLocation(Voidscape.MODID, "corrupted_pawn_phantom"), makeCastedBuilder(EntityCorruptedPawnPhantom.class, EntityCorruptedPawnPhantom::new, EntityClassification.MONSTER).sized(2.5F, 2.5F).setTrackingRange(256).fireImmune()));
	public static final RegistryObject<EntityType<EntityCorruptedPawnBoss>> CORRUPTED_PAWN_BOSS = REGISTRY.register("corrupted_pawn_boss", () -> build(new ResourceLocation(Voidscape.MODID, "corrupted_pawn_boss"), makeCastedBuilder(EntityCorruptedPawnBoss.class, EntityCorruptedPawnBoss::new, EntityClassification.MONSTER).sized(2.5F, 2.5F).setTrackingRange(256).fireImmune()));
	public static final RegistryObject<EntityType<EntityCorruptedPawnTentacle>> CORRUPTED_PAWN_TENTACLE = REGISTRY.register("corrupted_pawn_tentacle", () -> build(new ResourceLocation(Voidscape.MODID, "corrupted_pawn_tentacle"), makeCastedBuilder(EntityCorruptedPawnTentacle.class, EntityCorruptedPawnTentacle::new, EntityClassification.MISC).sized(3F, 5F).setTrackingRange(256).fireImmune()));
	public static final RegistryObject<EntityType<EntityAntiBolt>> ANTI_BOLT = REGISTRY.register("anti_bolt", () -> make(new ResourceLocation(Voidscape.MODID, "anti_bolt"), EntityAntiBolt::new, EntityClassification.MISC, 0.5F, 0.5F));

	static void classload() {

	}

	private static <E extends Entity> EntityType<E> make(ResourceLocation id, EntityType.IFactory<E> factory, EntityClassification classification, float width, float height) {
		return build(id, makeBuilder(factory, classification).sized(width, height));
	}

	private static <E extends Entity> EntityType<E> make(ResourceLocation id, EntityType.IFactory<E> factory, EntityClassification classification) {
		return make(id, factory, classification, 0.6F, 1.8F);
	}

	private static <E extends Entity> EntityType<E> build(ResourceLocation id, EntityType.Builder<E> builder) {
		return builder.build(id.toString());
	}

	private static <E extends Entity> EntityType.Builder<E> makeCastedBuilder(Class<E> cast, EntityType.IFactory<E> factory, EntityClassification classification) {
		return makeBuilder(factory, classification);
	}

	private static <E extends Entity> EntityType.Builder<E> makeBuilder(EntityType.IFactory<E> factory, EntityClassification classification) {
		return EntityType.Builder.of(factory, classification).
				sized(0.6F, 1.8F).
				setTrackingRange(80).
				setUpdateInterval(3).
				setShouldReceiveVelocityUpdates(true);
	}

	@SubscribeEvent
	public static void registerAttributes(FMLCommonSetupEvent event) {
		GlobalEntityTypeAttributes.put(CORRUPTED_PAWN_PHANTOM.get(), MobEntity.createMobAttributes().build());
		GlobalEntityTypeAttributes.put(CORRUPTED_PAWN_BOSS.get(), MobEntity.createMobAttributes().build());
		GlobalEntityTypeAttributes.put(CORRUPTED_PAWN_TENTACLE.get(), LivingEntity.createLivingAttributes().build());
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void registerEntityRenderer(FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(SPELL_BOLT.get(), RenderSpellBolt::new);
		RenderingRegistry.registerEntityRenderingHandler(SPELL_AURA.get(), RenderNull::new);
		RenderingRegistry.registerEntityRenderingHandler(CORRUPTED_PAWN_PHANTOM.get(), RenderCorruptedPawn::factory);
		RenderingRegistry.registerEntityRenderingHandler(CORRUPTED_PAWN_BOSS.get(), RenderCorruptedPawn::factory);
		RenderingRegistry.registerEntityRenderingHandler(CORRUPTED_PAWN_TENTACLE.get(), RenderCorruptedPawnTentacle::new);
		RenderingRegistry.registerEntityRenderingHandler(ANTI_BOLT.get(), RenderAntiBolt::new);
	}

}
