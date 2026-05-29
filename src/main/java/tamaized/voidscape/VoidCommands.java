package tamaized.voidscape;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.PlayerInventoryWrapper;
import tamaized.beanification.Autowired;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.armor.set.AstralArmorSet;
import tamaized.voidscape.registry.tool.set.CorruptToolSet;

@tamaized.beanification.Component
public class VoidCommands {

	@Autowired
	private CorruptToolSet corruptToolSet;

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private AstralArmorSet astralArmorSet;

	@Autowired
	private ModDataAttachments dataAttachments;

	private int getArgAsInt(CommandContext<CommandSourceStack> context, String id) {
		return context.getArgument(id, Integer.class);
	}

	private ItemStack sword() {
		ItemStack stack = new ItemStack(corruptToolSet.CORRUPT_SWORD.get());
		stack.set(DataComponents.ATTRIBUTE_MODIFIERS, stack.getAttributeModifiers().withModifierAdded(
			attributes.VOIDIC_DMG,
			new AttributeModifier(Identifier.fromNamespaceAndPath(Voidscape.MODID, "god"), 100, AttributeModifier.Operation.ADD_VALUE),
			EquipmentSlotGroup.MAINHAND
		));
		return stack;
	}

	private ItemStack eyes() {
		ItemStack stack = new ItemStack(astralArmorSet.ASTRAL_HELMET.get());
		stack.set(DataComponents.ATTRIBUTE_MODIFIERS, stack.getAttributeModifiers().withModifierAdded(
				attributes.VOIDIC_VISIBILITY,
				new AttributeModifier(Identifier.fromNamespaceAndPath(Voidscape.MODID, "god"), 1, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.HEAD
			).withModifierAdded(
				attributes.VOIDIC_INFUSION_RES,
				new AttributeModifier(Identifier.fromNamespaceAndPath(Voidscape.MODID, "god"), 1, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.HEAD
			).withModifierAdded(
				attributes.VOIDIC_PARANOIA_RES,
				new AttributeModifier(Identifier.fromNamespaceAndPath(Voidscape.MODID, "god"), 1, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.HEAD
			)
		);
		return stack;
	}

	public LiteralArgumentBuilder<CommandSourceStack> factory() {
		return LiteralArgumentBuilder.<CommandSourceStack>literal("voidscape")
			.then(Commands.literal("debug")
				.requires(cs -> cs.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
				.then(Commands.literal("dev-clear-inventory-and-effects")
					.executes(context -> {
						Player me = context.getSource().getPlayerOrException();
						me.removeAllEffects();
						PlayerInventoryWrapper inv = PlayerInventoryWrapper.of(me);
						me.getInventory().clearContent();
						inv.getArmorSlot(EquipmentSlot.HEAD)
						me.getInventory().setItem(EquipmentSlot.HEAD.getIndex(me.getInventory().getNonEquipmentItems().size()), eyes());
						me.getInventory().getNonEquipmentItems().set(EquipmentSlot.MAINHAND.getIndex(), sword());
						me.getData(dataAttachments.INSANITY).setInfusion(0);
						me.getData(dataAttachments.INSANITY).setParanoia(0);
						return 0;
					}))
				.then(Commands.literal("sword")
					.executes(context -> {
						Player me = context.getSource().getPlayerOrException();
						me.getInventory().add(sword());
						return 0;
					}))
				.then(Commands.literal("eyes")
					.executes(context -> {
						Player me = context.getSource().getPlayerOrException();
						me.getInventory().add(eyes());
						return 0;
					}))
				.then(Commands.literal("get")
					.then(Commands.literal("infusion")
						.executes(context -> {
							Player me = context.getSource().getPlayerOrException();
							context.getSource().sendSuccess(() -> Component.literal(String.valueOf(me.getData(dataAttachments.INSANITY).getInfusion())), false);
							return 0;
						}))
					.then(Commands.literal("paranoia")
						.executes(context -> {
							Player me = context.getSource().getPlayerOrException();
							context.getSource().sendSuccess(() -> Component.literal(String.valueOf(me.getData(dataAttachments.INSANITY).getParanoia())), false);
							return 0;
						})))
				.then(Commands.literal("set")
					.then(Commands.literal("infusion")
						.then(Commands.argument("amount", IntegerArgumentType.integer(0))
							.executes(context -> {
								Player me = context.getSource().getPlayerOrException();
								me.getData(dataAttachments.INSANITY).setInfusion(getArgAsInt(context, "amount"));
								return 0;
							})))
					.then(Commands.literal("paranoia")
						.then(Commands.argument("amount", IntegerArgumentType.integer(0))
							.executes(context -> {
								Player me = context.getSource().getPlayerOrException();
								me.getData(dataAttachments.INSANITY).setParanoia(getArgAsInt(context, "amount"));
								return 0;
							})))));
	}

}
