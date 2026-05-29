package tamaized.voidscape.client;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.item.BreakableHelper;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;
import tamaized.voidscape.registry.armor.set.*;
import tamaized.voidscape.registry.tool.set.*;

import java.util.HashMap;
import java.util.Map;

@Component(dist = Dist.CLIENT)
public class ModelBakeListener {

	@Autowired(dist = Dist.CLIENT)
	private ItemModelOverridePredicates itemModelOverridePredicates;

	@Autowired(dist = Dist.CLIENT)
	private ModToolSetComponentDirectory toolSets;

	@Autowired(dist = Dist.CLIENT)
	private ModArmorSetComponentDirectory armorSets;

	private final Map<Identifier, Identifier> REMAPPER = new HashMap<>();

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::modelBake);
	}

	private void modelBake(ModelEvent.ModifyBakingResult event) {
		// Broken Variants
		impBroken(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_SWORD.get());
		impBroken(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_AXE.get());
		impBroken(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_BOW.get());
		impBroken(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_XBOW.get());
		impBroken(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_PICKAXE.get());
		impBroken(armorSets.voidicCrystalArmorSet().VOIDIC_CRYSTAL_HELMET.get());
		impBroken(armorSets.voidicCrystalArmorSet().VOIDIC_CRYSTAL_CHEST.get());
		impBroken(armorSets.voidicCrystalArmorSet().VOIDIC_CRYSTAL_LEGS.get());
		impBroken(armorSets.voidicCrystalArmorSet().VOIDIC_CRYSTAL_BOOTS.get());

		impBroken(toolSets.charredToolSet().CHARRED_WARHAMMER.get());

		impBroken(toolSets.corruptToolSet().CORRUPT_SWORD.get());
		impBroken(toolSets.corruptToolSet().CORRUPT_AXE.get());
		impBroken(toolSets.corruptToolSet().CORRUPT_BOW.get());
		impBroken(toolSets.corruptToolSet().CORRUPT_XBOW.get());
		impBroken(armorSets.corruptArmorSet().CORRUPT_HELMET.get());
		impBroken(armorSets.corruptArmorSet().CORRUPT_CHEST.get());
		impBroken(armorSets.corruptArmorSet().CORRUPT_LEGS.get());
		impBroken(armorSets.corruptArmorSet().CORRUPT_BOOTS.get());

		impBroken(toolSets.titaniteToolSet().TITANITE_SWORD.get());
		impBroken(toolSets.titaniteToolSet().TITANITE_AXE.get());
		impBroken(toolSets.titaniteToolSet().TITANITE_PICKAXE.get());
		impBroken(toolSets.titaniteToolSet().TITANITE_HOE.get());
		impBroken(toolSets.titaniteToolSet().TITANITE_BOW.get());
		impBroken(toolSets.titaniteToolSet().TITANITE_XBOW.get());
		impBroken(armorSets.titaniteArmorSet().TITANITE_HELMET.get());
		impBroken(armorSets.titaniteArmorSet().TITANITE_CHEST.get());
		impBroken(armorSets.titaniteArmorSet().TITANITE_LEGS.get());
		impBroken(armorSets.titaniteArmorSet().TITANITE_BOOTS.get());

		impBroken(toolSets.ichorToolSet().ICHOR_SWORD.get());
		impBroken(toolSets.ichorToolSet().ICHOR_AXE.get());
		impBroken(toolSets.ichorToolSet().ICHOR_PICKAXE.get());
		impBroken(toolSets.ichorToolSet().ICHOR_BOW.get());
		impBroken(toolSets.ichorToolSet().ICHOR_XBOW.get());
		impBroken(armorSets.ichorArmorSet().ICHOR_HELMET.get());
		impBroken(armorSets.ichorArmorSet().ICHOR_CHEST.get());
		impBroken(armorSets.ichorArmorSet().ICHOR_LEGS.get());
		impBroken(armorSets.ichorArmorSet().ICHOR_BOOTS.get());

		impBroken(toolSets.astralToolSet().ASTRAL_SWORD.get());
		impBroken(toolSets.astralToolSet().ASTRAL_AXE.get());
		impBroken(toolSets.astralToolSet().ASTRAL_PICKAXE.get());
		impBroken(toolSets.astralToolSet().ASTRAL_SHOVEL.get());
		impBroken(toolSets.astralToolSet().ASTRAL_BOW.get());
		impBroken(toolSets.astralToolSet().ASTRAL_XBOW.get());
		impBroken(armorSets.astralArmorSet().ASTRAL_HELMET.get());
		impBroken(armorSets.astralArmorSet().ASTRAL_CHEST.get());
		impBroken(armorSets.astralArmorSet().ASTRAL_LEGS.get());
		impBroken(armorSets.astralArmorSet().ASTRAL_BOOTS.get());

		// Bows
		impBow(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_BOW.get());
		impBow(toolSets.corruptToolSet().CORRUPT_BOW.get());
		impBow(toolSets.titaniteToolSet().TITANITE_BOW.get());
		impBow(toolSets.ichorToolSet().ICHOR_BOW.get());
		impBow(toolSets.astralToolSet().ASTRAL_BOW.get());

		// XBows
		impXBow(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_XBOW.get());
		impXBow(toolSets.corruptToolSet().CORRUPT_XBOW.get());
		impXBow(toolSets.titaniteToolSet().TITANITE_XBOW.get());
		impXBow(toolSets.ichorToolSet().ICHOR_XBOW.get());
		impXBow(toolSets.astralToolSet().ASTRAL_XBOW.get());

		// Shields
		impShield(toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_SHIELD.get());

	}

	private void impBroken(Item item) {
		ItemProperties.register(item, itemModelOverridePredicates.BROKEN, (stack, level, entity, prop) -> BreakableHelper.isBroken(stack) ? 1F : 0F);
	}

	private void impBow(Item item) {
		ItemProperties.register(item, itemModelOverridePredicates.PULL, (stack, level, entity, prop) ->

			entity == null ? 0.0F : entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F);

		ItemProperties.register(item, itemModelOverridePredicates.PULLING, (stack, level, entity, prop) ->

			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
	}

	private void impXBow(Item item) {
		ItemProperties.register(item, itemModelOverridePredicates.PULL, (stack, level, entity, prop) ->

			entity == null ? 0.0F : CrossbowItem.isCharged(stack) ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(stack, entity));

		ItemProperties.register(item, itemModelOverridePredicates.PULLING, (stack, level, entity, prop) ->

			entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

		ItemProperties.register(item, itemModelOverridePredicates.CHARGED, (stack, level, entity, prop) ->

			entity != null && CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

		ItemProperties.register(item, itemModelOverridePredicates.FIREWORK, (stack, level, entity, prop) ->

			entity != null && CrossbowItem.isCharged(stack) && stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY).contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
	}

	private void impShield(Item item) {
		ItemProperties.register(item, itemModelOverridePredicates.BLOCKING, (stack, level, entity, prop) ->

			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
	}

}