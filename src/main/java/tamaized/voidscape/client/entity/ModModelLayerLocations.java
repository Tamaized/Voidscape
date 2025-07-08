package tamaized.voidscape.client.entity;

import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.model.*;
import tamaized.voidscape.client.entity.render.*;
import tamaized.voidscape.registry.ModEntities;

@Component(dist = Dist.CLIENT)
public class ModModelLayerLocations {

	@Autowired(dist = Dist.CLIENT)
	private ModEntities entities;

	public final ModelLayerLocation VOIDLING = make("voidling");
	public final ModelLayerLocation CORRUPTED_PAWN = make("corruptedpawn");
	public final ModelLayerLocation VOIDS_WRATH = make("voidswrath");
	public final ModelLayerLocation VOIDS_WRATH_CHARGED = make("voidswrathcharged");
	public final ModelLayerLocation NULL_SERVANT = make("nullservant");

	public final ModelLayerLocation MODEL_ARMOR_CORRUPT_OUTER = make("corrupt_outer");
	public final ModelLayerLocation MODEL_ARMOR_CORRUPT_INNER = make("corrupt_inner");
	public final ModelLayerLocation MODEL_ARMOR_TITANITE = make("titanite");
	public final ModelLayerLocation MODEL_ARMOR_ICHOR = make("ichor");
	public final ModelLayerLocation MODEL_ARMOR_ASTRAL = make("astral");

	private ModelLayerLocation make(String name) {
		return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "main"), name);
	}

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::registerLayerDefinitions);
		bus.addListener(this::registerEntityRenderers);
	}

	private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(VOIDLING, ModelVoidling::createMesh);
		event.registerLayerDefinition(CORRUPTED_PAWN, ModelCorruptedPawn::createMesh);
		event.registerLayerDefinition(VOIDS_WRATH, () -> ModelVoidsWrath.createMesh(CubeDeformation.NONE));
		event.registerLayerDefinition(VOIDS_WRATH_CHARGED, () -> ModelVoidsWrath.createMesh(new CubeDeformation(1.0F)));
		event.registerLayerDefinition(NULL_SERVANT, ModelNullServant::createMesh);

		event.registerLayerDefinition(MODEL_ARMOR_CORRUPT_OUTER, () -> ModelArmorCorrupt.makeMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0F));
		event.registerLayerDefinition(MODEL_ARMOR_CORRUPT_INNER, () -> ModelArmorCorrupt.makeMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0F));
		event.registerLayerDefinition(MODEL_ARMOR_TITANITE, () -> ModelArmorCrystalline.makeMesh(CubeDeformation.NONE, 0F));
		event.registerLayerDefinition(MODEL_ARMOR_ICHOR, () -> ModelArmorCrystalline.makeMesh(CubeDeformation.NONE, 0F));
		event.registerLayerDefinition(MODEL_ARMOR_ASTRAL, () -> ModelArmorCrystalline.makeMesh(CubeDeformation.NONE, 0F));

	}

	private void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(entities.VOIDLING.get(), RenderVoidling::factory);
		event.registerEntityRenderer(entities.CORRUPTED_PAWN.get(), RenderCorruptedPawn::factory);
		event.registerEntityRenderer(entities.VOIDS_WRATH.get(), RenderVoidsWrath::new);
		event.registerEntityRenderer(entities.NULL_SERVANT.get(), RenderNullServant::new);
		event.registerEntityRenderer(entities.NULL_SERVANT_AUGMENT_BLOCK.get(), RenderNullServantAugmentBlock::new);
		event.registerEntityRenderer(entities.NULL_SERVANT_PHANTOM.get(), RenderNullServant::new);
		event.registerEntityRenderer(entities.ANTI_BOLT.get(), RenderAntiBolt::new);
		event.registerEntityRenderer(entities.ICHOR_BOLT.get(), context -> new RenderSpellBolt<>(context, 0xFF7700));
		event.registerEntityRenderer(entities.NULL_SERVANT_ICHOR_BOLT.get(), context -> new RenderSpellBolt<>(context, 0xFF0000));
		event.registerEntityRenderer(entities.STRANGE_PEARL.get(), context -> new ThrownItemRenderer<>(context, 1F, true));
	}

}
