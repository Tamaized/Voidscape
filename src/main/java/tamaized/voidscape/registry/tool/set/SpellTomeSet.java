package tamaized.voidscape.registry.tool.set;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.entity.IchorBoltEntity;
import tamaized.voidscape.item.LingeringPotionAugmentableSpellTomeItem;
import tamaized.voidscape.item.SpellTomeItem;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.registry.ModItemProperties;
import tamaized.voidscape.registry.ModItemTags;

@Component
public class SpellTomeSet {

	@Autowired
	private ModItemProperties itemProperties;

	@Autowired
	private ModEffects modEffects;

	@Autowired
	private ModDataAttachments dataAttachments;

	@Autowired
	private ModItemTags itemTags;

	public final DeferredHolder<Item, SpellTomeItem> ICHOR_TOME = RegUtil.register(Registries.ITEM, "ichor_tome", (id) -> new SpellTomeItem(
		itemProperties.LAVA_IMMUNE.apply(id).durability(100).repairable(itemTags.REPAIR_MATERIAL_ICHOR),
		20 * 10,
		context -> context.level().addFreshEntity(new IchorBoltEntity(context.parent()))
	));

	public final DeferredHolder<Item, SpellTomeItem> VOIDIC_TOME = RegUtil.register(Registries.ITEM, "voidic_tome", (id) -> new LingeringPotionAugmentableSpellTomeItem(
		itemProperties.LAVA_IMMUNE.apply(id).durability(100).repairable(itemTags.REPAIR_MATERIAL_VOIDIC_CRYSTAL),
		20 * 45,
		context -> {
			if (context.stack().has(DataComponents.POTION_CONTENTS))
				context.parent().setData(dataAttachments.AURA_EFFECT, context.stack().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY));
			else
				context.parent().removeData(dataAttachments.AURA_EFFECT);
			context.parent().addEffect(new MobEffectInstance(modEffects.AURA, 20 * 30));
		}
	));

	public final DeferredHolder<Item, SpellTomeItem> CORRUPT_TOME = RegUtil.register(Registries.ITEM, "corrupt_tome", (id) -> new SpellTomeItem(
		itemProperties.LAVA_IMMUNE.apply(id).durability(100).repairable(itemTags.REPAIR_MATERIAL_CORRUPT),
		20 * 5,
		context -> {
			context.parent().addDeltaMovement(context.parent().getLookAngle().scale(2.5D));
			context.level().playSound(null, context.parent(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1F, 0.75F + context.parent().getRandom().nextFloat() * 0.5F);
			context.parent().getData(dataAttachments.INSANITY).enableLeapParticles();
			context.parent().addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 10));
		}
	));

	public final DeferredHolder<Item, SpellTomeItem> TITANITE_TOME = RegUtil.register(Registries.ITEM, "titanite_tome", (id) -> new SpellTomeItem(
		itemProperties.LAVA_IMMUNE.apply(id).durability(100).repairable(itemTags.REPAIR_MATERIAL_TITANITE),
		20 * 45,
		context -> context.parent().addEffect(new MobEffectInstance(modEffects.FORTIFIED, 20 * 30))
	));

}
