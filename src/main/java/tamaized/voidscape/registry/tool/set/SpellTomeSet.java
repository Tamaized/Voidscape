package tamaized.voidscape.registry.tool.set;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.entity.IchorBoltEntity;
import tamaized.voidscape.item.SpellTomeItem;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.registry.ModItemProperties;
import tamaized.voidscape.registry.item.MaterialItems;

@Component
public class SpellTomeSet {

	@Autowired
	private ModItemProperties itemProperties;

	@Autowired
	private MaterialItems materialItems;

	@Autowired
	private ModEffects modEffects;

	@Autowired
	private ModDataAttachments dataAttachments;

	private final DeferredRegister<Item> REGISTRY = RegUtil.create(Registries.ITEM);

	public final DeferredHolder<Item, Item> ICHOR_TOME = REGISTRY.register("ichor_tome", () -> new SpellTomeItem(
		itemProperties.LAVA_IMMUNE.get().durability(100),
		materialItems.ICHOR_CRYSTAL,
		20 * 10,
		context -> context.level().addFreshEntity(new IchorBoltEntity(context.parent()))
	));

	public final DeferredHolder<Item, Item> VOIDIC_TOME = REGISTRY.register("voidic_tome", () -> new SpellTomeItem(
		itemProperties.LAVA_IMMUNE.get().durability(100),
		materialItems.VOIDIC_CRYSTAL,
		20 * 45,
		context -> context.parent().addEffect(new MobEffectInstance(modEffects.AURA, 20 * 30))
	));

	public final DeferredHolder<Item, Item> CORRUPT_TOME = REGISTRY.register("corrupt_tome", () -> new SpellTomeItem(
		itemProperties.LAVA_IMMUNE.get().durability(100),
		materialItems.TENDRIL,
		20 * 5,
		context -> {
			context.parent().addDeltaMovement(context.parent().getLookAngle().scale(2.5D));
			context.level().playSound(null, context.parent(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1F, 0.75F + context.parent().getRandom().nextFloat() * 0.5F);
			context.parent().getData(dataAttachments.INSANITY).enableLeapParticles();
			context.parent().addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 10));
		}
	));

	public final DeferredHolder<Item, Item> TITANITE_TOME = REGISTRY.register("titanite_tome", () -> new SpellTomeItem(
		itemProperties.LAVA_IMMUNE.get().durability(100),
		materialItems.TITANITE_SHARD,
		20 * 45,
		context -> context.parent().addEffect(new MobEffectInstance(modEffects.FORTIFIED, 20 * 30))
	));

}
