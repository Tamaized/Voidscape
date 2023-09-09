package tamaized.voidscape;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import tamaized.voidscape.capability.SubCapability;
import tamaized.voidscape.registry.ModArmors;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModTools;

import java.util.function.Consumer;

public final class VoidCommands {

    private VoidCommands() {

    }

    private static <T extends SubCapability.ISubCap.ISubCapData> int getDataAndRun(SubCapability.ISubCap.SubCapKey<T> type, CommandContext<CommandSourceStack> context, Consumer<T> exec) throws CommandSyntaxException {
        context.getSource().getPlayerOrException().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(type).ifPresent(exec));
        return 0;
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
                                stack.addAttributeModifier(ModAttributes.VOIDIC_DMG.get(), new AttributeModifier("god", 50, AttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
                                me.inventory.add(stack);
                                return 0;
                            }))
                    .then(Commands.literal("eyes")
                            .executes(context -> {
                                Player me = context.getSource().getPlayerOrException();
                                ItemStack stack = new ItemStack(ModArmors.CORRUPT_HELMET.get());
                                stack.addAttributeModifier(ModAttributes.VOIDIC_VISIBILITY.get(), new AttributeModifier("god", 1, AttributeModifier.Operation.MULTIPLY_BASE), EquipmentSlot.HEAD);
                                stack.addAttributeModifier(ModAttributes.VOIDIC_INFUSION_RES.get(), new AttributeModifier("god", 1, AttributeModifier.Operation.MULTIPLY_BASE), EquipmentSlot.HEAD);
                                stack.addAttributeModifier(ModAttributes.VOIDIC_PARANOIA_RES.get(), new AttributeModifier("god", 1, AttributeModifier.Operation.MULTIPLY_BASE), EquipmentSlot.HEAD);
                                me.inventory.add(stack);
                                return 0;
                            }))
                    .then(Commands.literal("get")
                            .then(Commands.literal("infusion")
                                    .executes(context -> getDataAndRun(Voidscape.subCapInsanity, context, data -> context.getSource().sendSuccess(() -> Component.literal(String.valueOf(data.getInfusion())), false))))
                            .then(Commands.literal("paranoia")
                                    .executes(context -> getDataAndRun(Voidscape.subCapInsanity, context, data -> context.getSource().sendSuccess(() -> Component.literal(String.valueOf(data.getParanoia())), false)))))
                    .then(Commands.literal("set")
                            .then(Commands.literal("infusion")
                                    .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                            .executes(context -> getDataAndRun(Voidscape.subCapInsanity, context, data -> data.setInfusion(getArgAsInt(context, "amount"))))))
                            .then(Commands.literal("paranoia")
                                    .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                            .executes(context -> getDataAndRun(Voidscape.subCapInsanity, context, data -> data.setParanoia(getArgAsInt(context, "amount")))))));
        }
    }

}
