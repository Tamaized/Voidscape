package tamaized.voidscape.registry.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.item.EtherealFruitItem;
import tamaized.voidscape.registry.*;

import java.util.List;
import java.util.function.Consumer;

@Component
public class EtherealFruitItems {

	@Autowired
	private ModItemProperties itemProperties;

	@Autowired
	private ModDataAttachments dataAttachments;

	@Autowired
	private ModToolSetComponentDirectory toolSetComponentDirectory;

	@Autowired
	private ModEffects effects;

	public final DeferredHolder<Item, EtherealFruitItem> ETHEREAL_FRUIT_VOID = RegUtil.register(Registries.ITEM, "ethereal_fruit_void", () -> new EtherealFruitItem(
		context -> context.parent().getData(dataAttachments.INSANITY).addInfusion(150, context.parent()),
		itemProperties.ETHEREAL_FRUIT.get()
	));

	public final DeferredHolder<Item, EtherealFruitItem> ETHEREAL_FRUIT_NULL = RegUtil.register(Registries.ITEM, "ethereal_fruit_null", () -> new EtherealFruitItem(
		context -> context.parent().getData(dataAttachments.INSANITY).removeInfusion(150),
		itemProperties.ETHEREAL_FRUIT.get()
	));

	public final DeferredHolder<Item, EtherealFruitItem> ETHEREAL_FRUIT_OVERWORLD = RegUtil.register(Registries.ITEM, "ethereal_fruit_overworld", () -> new EtherealFruitItem(
		context -> context.parent().getData(dataAttachments.INSANITY).removeParanoia(150),
		itemProperties.ETHEREAL_FRUIT.get()
	));

	public final DeferredHolder<Item, EtherealFruitItem> ETHEREAL_FRUIT_NETHER = RegUtil.register(Registries.ITEM, "ethereal_fruit_nether", () -> new EtherealFruitItem(
		context -> context.parent().getData(dataAttachments.INSANITY).addParanoia(150),
		itemProperties.ETHEREAL_FRUIT.get()
	));

	public final DeferredHolder<Item, EtherealFruitItem> ETHEREAL_FRUIT_END = RegUtil.register(Registries.ITEM, "ethereal_fruit_end", () -> new EtherealFruitItem(
		context -> context.parent().addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 3)),
		itemProperties.ETHEREAL_FRUIT.get()
	));

	public final DeferredHolder<Item, EtherealFruitItem> ETHEREAL_FRUIT_SALAD = RegUtil.register(Registries.ITEM, "ethereal_fruit_salad", () -> new EtherealFruitItem(
		context -> {
			switch (context.parent().getRandom().nextInt(11)) {
				case 1 -> ETHEREAL_FRUIT_VOID.get().doAction(context);
				case 2 -> ETHEREAL_FRUIT_NULL.get().doAction(context);
				case 3 -> ETHEREAL_FRUIT_OVERWORLD.get().doAction(context);
				case 4 -> ETHEREAL_FRUIT_NETHER.get().doAction(context);
				case 5 -> ETHEREAL_FRUIT_END.get().doAction(context);
				case 6 -> toolSetComponentDirectory.spellTomeSet().VOIDIC_TOME.get().doAction(context);
				case 7 -> context.parent().addEffect(new MobEffectInstance(effects.ICHOR, 20 * 30));
				case 8 -> toolSetComponentDirectory.spellTomeSet().TITANITE_TOME.get().doAction(context);
				case 10 -> context.parent().addEffect(new MobEffectInstance(effects.TRAUMATIZED, 20 * 30));
				default -> { /* Do nothing */}
			}
		},
		itemProperties.LAVA_IMMUNE.get().food(new FoodProperties.Builder()
			.nutrition(18)
			.saturationModifier(0.5F)
			.alwaysEdible()
			.build())
			.usingConvertsTo(Items.BOWL)
			.rarity(Rarity.RARE)
			.stacksTo(16)
	) {

		@Override
		public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<net.minecraft.network.chat.Component> builder, TooltipFlag tooltipFlag) {
			super.appendHoverText(stack, context, display, builder, tooltipFlag);
			builder.accept(net.minecraft.network.chat.Component.empty());
			builder.accept(net.minecraft.network.chat.Component.translatable(Voidscape.MODID + ".tooltip.fruit_salad_why").withStyle(
				ChatFormatting.DARK_GRAY
			));
			builder.accept(net.minecraft.network.chat.Component.empty());
			builder.accept(net.minecraft.network.chat.Component.translatable(Voidscape.MODID + ".tooltip.fruit_salad_yummy").withStyle(
				ChatFormatting.LIGHT_PURPLE,
				ChatFormatting.ITALIC
			));
		}
	});

}
