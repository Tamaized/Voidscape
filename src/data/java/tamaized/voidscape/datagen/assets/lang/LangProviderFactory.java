package tamaized.voidscape.datagen.assets.lang;

import net.minecraft.locale.Language;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.datagenutil.assets.lang.ExtendedLangProvider;
import tamaized.datagenutil.assets.lang.LangProvider;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.registry.fluid.ModFluidBuckets;
import tamaized.voidscape.registry.fluid.ModFluidTypes;

import java.util.List;

@Component
public class LangProviderFactory {

	@Autowired
	private ModDimensions dimensions;

	@Autowired
	private ModBiomes biomes;

	@Autowired
	private ModEntities entities;

	@Autowired
	private ModFluidTypes fluids;

	@Autowired
	private ModFluidBuckets buckets;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ModEffects effects;

	@Autowired
	private ModDatapacks datapacks;

	@Directory(LangProvider.class)
	private List<LangProvider> langProviders;

	public LanguageProvider make(GatherDataEvent event) {
		return new ExtendedLangProvider(
			event.getGenerator().getPackOutput(),
			Voidscape.MODID,
			Language.DEFAULT,
			langProviders
		) {
			@Override
			protected void addAdditionalTranslations() {
				addCreativeTab("Voidscape");

				addDimension(dimensions.VOID, "The Void");

				addBiome(biomes.VOID, "Void");
				addBiome(biomes.NULL, "Null");
				addBiome(biomes.ANTISPIRES, "Anti-Spires");
				addBiome(biomes.THUNDERSPIRES, "Thunder Spires");
				addBiome(biomes.THUNDER_FOREST, "Thunder Forest");
				addBiome(biomes.OVERWORLD, "Overworld");
				addBiome(biomes.NETHER, "Nether");
				addBiome(biomes.END, "End");
				addBiome(biomes.AETHER, "Aether");

				// TODO: use ResourceKeys or something to reference the advancements
				addAdvancement("root", "The Void", "Get sucked into the Void");
				addAdvancement("essence", "Ethereal", "Obtain Ethereal Essence from a Monster and use it on Bedrock");
				addAdvancement("spidereggs", "It Crumbles", "Destroy Bedrock in the Void using Ethereal Spider Eggs");
				addAdvancement("gear", "Voidic Protection", "Equip a piece of voidic armor to protect yourself from the Void's harmful effects");
				addAdvancement("psychosis", "Psychosis", "Experience the psychotic effects of the Void and defeat the Corrupted Pawn");
				addAdvancement("purified", "Purified", "Use a Titanite Chunk on a Null Servant");
				addAdvancement("ichor", "Blood of the Gods", "Use Ichor on a Null Servant");
				addAdvancement("cracked", "Broken Glass", "Throw a Strange Pearl at an End Crystal");
				addAdvancement("astral", "Astral", "Use Astral Shards on an Anti-Rock and break it after an Anti-Bolt strikes it");
				addAdvancement("astral_crystal", "And The Stars Align", "Use Astral Essence on a Null Servant");
				addAdvancement("portal", "Dark Arts", "Open a portal to the Void using Voidic Crystal Blocks and a Charred Bone");
				addAdvancement("infused", "Infused", "Experience the full effects of Voidic Infusion");
				addAdvancement("hoe", "Green Thumb", "Use the fertilizing effect from a Titanite Hoe");
				addAdvancement("liquid", "LIQUID!", "Feed Voidic Crystals into the Voidic Liquifier to create Liquid Voidic Crystal");
				addAdvancement("bucket", "This is a Bucket", "Dear God... I mean.. Right click a filled Liquifier with an Empty Bucket");
				addAdvancement("defused", "Defused", "Power the Voidic Defuser with Liquid Voidic Crystal to remove Infusion from nearby players and mobs");
				addAdvancement("germinated", "Germinated", "Power the Voidic Germinator with Liquid Voidic Crystal to grow nearby crops and Ethereal Fruit");
				addAdvancement("well", "Bottomless Well", "Turn a Voidic Germinator into a Voidic Well and place a Cauldron (or any Tank) next to it");
				addAdvancement("coop", "These eggs smell weird?", "Turn a Voidic Germinator into a Voidic Coop and power it next to a chest");
				addAdvancement("hatchery", "Draconic", "Power a Draconic Hatchery with 100 buckets of Liquid Voidic Crystal");
				addAdvancement("infuser", "Artificial Infusion", "Power the Voidic Infuser with Liquid Voidic Crystal to harm nearby players and mobs");
				addAdvancement("collector", "The Collector", "Power the Voidic Collector with Liquid Voidic Crystal to collect nearby items into a connected chest");
				addAdvancement("hammer", "Hammer Time", "Mine blocks with a Charred War Hammer");
				addAdvancement("voidmancer", "Voidmancer", "Collect every Spell Tome");
				addAdvancement("terraform", "Terraformer", "Dig blocks with an Astral Shovel");

				// TODO: these should be referenced from somewhere
				addTooltip("fruit_salad_why", "But why?");
				addTooltip("fruit_salad_yummy", "Fruit Salad! Yummy, yummy!");
				addTooltip("broken", "Broken");
				addTooltip("elytra", "Elytra");
				addTooltip("draconic", "Draconic");
				addTooltip("augment.lingering_potion", "Right click with a lingering potion or a snowball to change the effect.");
				addTooltip("textures", "Textures Subject to Change");

				addEntityType(entities.VOIDLING, "Voidling");
				addEntityType(entities.NULL_SERVANT, "Null Servant");
				addEntityTypeSuffix(entities.NULL_SERVANT, "titanite", "Titanite Servant");
				addEntityTypeSuffix(entities.NULL_SERVANT, "ichor", "Ichor Servant");
				addEntityTypeSuffix(entities.NULL_SERVANT, "astral", "Astral Servant");
				addEntityType(entities.NULL_SERVANT_AUGMENT_BLOCK, "Block");
				addEntityType(entities.VOIDS_WRATH, "Void's Wrath");
				addEntityType(entities.CORRUPTED_PAWN, "The Corrupted Pawn");

				addFluid(fluids.VOIDIC, "Liquid Voidic Crystal");
				addItem(buckets.VOIDIC, "Liquid Voidic Crystal Bucket");

				addBlock(blocks.imposterBlocks().FRAGILE_VOIDIC_CRYSTAL_BLOCK, "Fragile Voidic Crystal");
				addBlock(blocks.functionalBlocks().PORTAL, "Voidic Portal");
				addBlock(blocks.machineBlocks().MACHINE_CORE, "Voidic Core");
				addBlock(blocks.machineBlocks().MACHINE_LIQUIFIER, "Voidic Liquifier");
				addBlock(blocks.machineBlocks().MACHINE_DEFUSER, "Voidic Defuser");
				addBlock(blocks.machineBlocks().MACHINE_GERMINATOR, "Voidic Germinator");
				addBlock(blocks.machineBlocks().MACHINE_INFUSER, "Voidic Infuser");
				addBlock(blocks.machineBlocks().MACHINE_COLLECTOR, "Voidic Collector");

				addAttribute(attributes.VOIDIC_INFUSION_RES, "Voidic Infusion Resistance");
				addAttribute(attributes.VOIDIC_PARANOIA_RES, "Voidic Paranoia Resistance");
				addAttribute(attributes.VOIDIC_RES, "Voidic Damage Resistance");
				addAttribute(attributes.VOIDIC_DMG, "Voidic Damage");
				addAttribute(attributes.VOIDIC_ARROW_DMG, "Voidic Arrow Damage");
				addAttribute(attributes.VOIDIC_VISIBILITY, "Voidic Visibility");
				addAttribute(attributes.VOIDIC_INFUSION, "Voidic Infusion");

				addEffectWithDescription(effects.AURA, "Voidic Aura", "Deals 2 Voidic damage every second to nearby entities.");
				addEffectWithDescription(effects.FORTIFIED, "Voidic Fortification", "Reduces incoming Voidic Damage to 25% with a 25% chance to expire on each reduction.");
				addEffectWithDescription(effects.ICHOR, "Ichor", "Doubles incoming Voidic Damage.");
				addEffectWithDescription(effects.TRAUMATIZED, "Traumatized", "Greatly Increases Voidic Damage and Defense.");

				addDatapack(datapacks.AETHER_INTEGRATION, "Enables Aether integration");

				addCommonConfig("bedrock_teleportation_dimension_blacklist", "Bedrock Teleportation Dimension Blacklist");
				addCommonConfig("bedrockTeleportationDimensionWhitelist", "Bedrock Teleportation Dimension Whitelist Toggle");

				addConfiguration("donatorSettings", "Donor Settings");
				addClientConfig("donatorSettings.enable", "Enable");
				addClientConfig("donatorSettings.color", "Color");
			}
		};
	}

}
