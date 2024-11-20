package tamaized.voidscape.registry.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.item.EtherealFruitItem;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModItemProperties;

import java.util.function.Supplier;

@Component
public class EtherealFruitItems {

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ModItemProperties itemProperties;

	private final DeferredRegister<Item> REGISTRY = RegUtil.create(Registries.ITEM);

	public final Supplier<Item> ETHEREAL_FRUIT_VOID = REGISTRY.register("ethereal_fruit_void", () -> new EtherealFruitItem(
		entity -> entity.getData(ModDataAttachments.INSANITY).addInfusion(150, entity),
		itemProperties.ETHEREAL_FRUIT.get()
	));

	public final Supplier<Item> ETHEREAL_FRUIT_NULL = REGISTRY.register("ethereal_fruit_null", () -> new EtherealFruitItem(
		entity -> entity.getData(ModDataAttachments.INSANITY).removeInfusion(150),
		itemProperties.ETHEREAL_FRUIT.get()
	));

	public final Supplier<Item> ETHEREAL_FRUIT_OVERWORLD = REGISTRY.register("ethereal_fruit_overworld", () -> new EtherealFruitItem(
		entity -> entity.getData(ModDataAttachments.INSANITY).removeParanoia(150),
		itemProperties.ETHEREAL_FRUIT.get()
	));

	public final Supplier<Item> ETHEREAL_FRUIT_NETHER = REGISTRY.register("ethereal_fruit_nether", () -> new EtherealFruitItem(
		entity -> entity.getData(ModDataAttachments.INSANITY).addParanoia(150),
		itemProperties.ETHEREAL_FRUIT.get()
	));

	public final Supplier<Item> ETHEREAL_FRUIT_END = REGISTRY.register("ethereal_fruit_end", () -> new EtherealFruitItem(
		entity -> entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 3)),
		itemProperties.ETHEREAL_FRUIT.get()
	));

}
