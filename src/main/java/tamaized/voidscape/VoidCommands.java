package tamaized.voidscape;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import tamaized.voidscape.registry.ModArmors;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModTools;

public final class VoidCommands {

	private VoidCommands() {

	}

	private static int getArgAsInt(CommandContext<CommandSourceStack> context, String id) {
		return context.getArgument(id, Integer.class);
	}

	public static class Debug {
		public static ArgumentBuilder<CommandSourceStack, ?> register() {
			return Commands.literal("debug")
					.requires(cs -> cs.hasPermission(2))
					.then(Commands.literal("sword")
							.executes(context -> {
								Player me = context.getSource().getPlayerOrException();
								ItemStack stack = new ItemStack(ModTools.CORRUPT_SWORD.get());
								stack.addAttributeModifier(ModAttributes.VOIDIC_DMG.get(), new AttributeModifier("god", 100, AttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
								me.inventory.add(stack);
								return 0;
							}))
					.then(Commands.literal("eyes")
							.executes(context -> {
								Player me = context.getSource().getPlayerOrException();
								ItemStack stack = new ItemStack(ModArmors.ASTRAL_HELMET.get());
								stack.addAttributeModifier(ModAttributes.VOIDIC_VISIBILITY.get(), new AttributeModifier("god", 1, AttributeModifier.Operation.MULTIPLY_BASE), EquipmentSlot.HEAD);
								stack.addAttributeModifier(ModAttributes.VOIDIC_INFUSION_RES.get(), new AttributeModifier("god", 1, AttributeModifier.Operation.MULTIPLY_BASE), EquipmentSlot.HEAD);
								stack.addAttributeModifier(ModAttributes.VOIDIC_PARANOIA_RES.get(), new AttributeModifier("god", 1, AttributeModifier.Operation.MULTIPLY_BASE), EquipmentSlot.HEAD);
								me.inventory.add(stack);
								return 0;
							}))
					.then(Commands.literal("get")
							.then(Commands.literal("infusion")
									.executes(context -> {
										Player me = context.getSource().getPlayerOrException();
										context.getSource().sendSuccess(() -> Component.literal(String.valueOf(me.getData(ModDataAttachments.INSANITY).getInfusion())), false);
										return 0;
									}))
							.then(Commands.literal("paranoia")
									.executes(context -> {
										Player me = context.getSource().getPlayerOrException();
										context.getSource().sendSuccess(() -> Component.literal(String.valueOf(me.getData(ModDataAttachments.INSANITY).getParanoia())), false);
										return 0;
									})))
					.then(Commands.literal("set")
							.then(Commands.literal("infusion")
									.then(Commands.argument("amount", IntegerArgumentType.integer(0))
											.executes(context -> {
												Player me = context.getSource().getPlayerOrException();
												me.getData(ModDataAttachments.INSANITY).setInfusion(getArgAsInt(context, "amount"));
												return 0;
											})))
							.then(Commands.literal("paranoia")
									.then(Commands.argument("amount", IntegerArgumentType.integer(0))
											.executes(context -> {
												Player me = context.getSource().getPlayerOrException();
												me.getData(ModDataAttachments.INSANITY).setParanoia(getArgAsInt(context, "amount"));
												return 0;
											}))));
		}
	}

}
