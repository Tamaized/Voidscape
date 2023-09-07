package tamaized.voidscape;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import tamaized.voidscape.registry.ModArmors;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModTools;

public final class VoidCommands {

    private VoidCommands() {

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
                                me.inventory.add(stack);
                                return 0;
                            }));
        }
    }

}
