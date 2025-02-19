package tamaized.voidscape.datagen.assets.lang;

import net.minecraft.core.Holder;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelProviderFactory;
import tamaized.voidscape.datagen.assets.bakedmodel.ItemModelProviderFactory;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.registry.fluid.ModFluidBuckets;
import tamaized.voidscape.registry.fluid.ModFluidTypes;
import tamaized.voidscape.registry.fluid.ModFluids;

import java.util.function.Supplier;

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
	private BlockModelProviderFactory blockModelProviderFactory;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ItemModelProviderFactory itemModelProviderFactory;

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ModEffects effects;

	@Autowired
	private ModDatapacks datapacks;

	public LanguageProvider make(GatherDataEvent event) {
		return new LanguageProvider(
			event.getGenerator().getPackOutput(),
			Voidscape.MODID,
			Language.DEFAULT
		) {
			@Override
			protected void addTranslations() {
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
				addTooltip("broken", "Broken");
				addTooltip("elytra", "Elytra");
				addTooltip("draconic", "Draconic");
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

				blockModelProviderFactory.addLangEntries(this);
				addBlock(blocks.imposterBlocks().FRAGILE_VOIDIC_CRYSTAL_BLOCK, "Fragile Voidic Crystal");
				addBlock(blocks.functionalBlocks().PORTAL, "Voidic Portal");
				addBlock(blocks.machineBlocks().MACHINE_CORE, "Voidic Core");
				addBlock(blocks.machineBlocks().MACHINE_LIQUIFIER, "Voidic Liquifier");
				addBlock(blocks.machineBlocks().MACHINE_DEFUSER, "Voidic Defuser");
				addBlock(blocks.machineBlocks().MACHINE_GERMINATOR, "Voidic Germinator");
				addBlock(blocks.machineBlocks().MACHINE_INFUSER, "Voidic Infuser");
				addBlock(blocks.machineBlocks().MACHINE_COLLECTOR, "Voidic Collector");

				itemModelProviderFactory.addLangEntries(this);

				addAttribute(attributes.VOIDIC_INFUSION_RES, "Voidic Infusion Resistance");
				addAttribute(attributes.VOIDIC_PARANOIA_RES, "Voidic Paranoia Resistance");
				addAttribute(attributes.VOIDIC_RES, "Voidic Damage Resistance");
				addAttribute(attributes.VOIDIC_DMG, "Voidic Damage");
				addAttribute(attributes.VOIDIC_ARROW_DMG, "Voidic Arrow Damage");
				addAttribute(attributes.VOIDIC_VISIBILITY, "Voidic Visibility");
				addAttribute(attributes.VOIDIC_INFUSION, "Voidic Infusion");

				addEffect(effects.AURA, "Voidic Aura");
				addEffect(effects.FORTIFIED, "Voidic Fortification");
				addEffect(effects.ICHOR, "Ichor");

				addDatapack(datapacks.AETHER_INTEGRATION, "Enables Aether integration");

			}

			private void addCreativeTab(String translation) {
				add(Voidscape.MODID + ".item_group", translation);
			}

			private void addBiome(ResourceKey<Biome> biome, String translation) {
				addResourceKey(biome, "biome", translation);
			}

			private void addAdvancement(String name, String title, String desc) {
				add("advancement." + Voidscape.MODID + "." + name, title);
				add("advancement." + Voidscape.MODID + "." + name + ".desc", desc);
			}

			private void addTooltip(String name, String translation) {
				add(Voidscape.MODID + ".tooltip." + name, translation);
			}

			private void addEntityTypeWithSpawnEgg(Supplier<? extends EntityType<? extends Entity>> entity, Supplier<Item> spawnEgg, String translation) {
				addEntityType(entity, translation);
				addItem(spawnEgg, translation.concat(" Spawn Egg"));
			}

			private void addEntityTypeSuffix(Supplier<? extends EntityType<? extends Entity>> entity, String suffix, String translation) {
				add(entity.get().getDescriptionId() + "." + suffix, translation);
			}

			private void addFluid(Supplier<FluidType> fluid, String translation) {
				add(fluid.get().getDescriptionId(), translation);
			}

			private void addAttribute(Holder<Attribute> attribute, String translation) {
				add("attribute." + attribute.value().getDescriptionId(), translation);
			}

			private void addDeathMessage(ResourceKey<DamageType> key, String translation) {
				addResourceKey(key, "death.attack", translation);
			}

			private void addSubtitle(SoundEvent key, String translation) {
				add(key.getLocation().toLanguageKey("subtitles"), translation);
			}

			private void addConfiguration(String configuration, String translation) {
				add(Voidscape.MODID + ".configuration." + configuration, translation);
			}

			private void addConfig(String config, String translation) {
				add(Voidscape.MODID + ".config." + config, translation);
			}

			private void addCommonConfig(String config, String translation) {
				addConfig("common." + config, translation);
			}

			private void addClientConfig(String config, String translation) {
				addConfig("client." + config, translation);
			}

			private void addResourceKey(ResourceKey<?> key, String prefix, String translation) {
				add(key.location().toLanguageKey(prefix), translation);
			}

			private void addDatapack(Lazy<Pack> pack, String translation) {
				add("pack." + Voidscape.MODID + "." + pack.get().getId(), translation);
			}
		};
	}

}
