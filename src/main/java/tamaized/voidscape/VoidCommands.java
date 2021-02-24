package tamaized.voidscape;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import tamaized.voidscape.turmoil.Progression;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;
import tamaized.voidscape.turmoil.TurmoilStats;
import tamaized.voidscape.world.InstanceManager;

import java.util.function.Consumer;

public final class VoidCommands {

	private VoidCommands() {

	}

	private static <T extends SubCapability.ISubCap.ISubCapData> int getDataAndRun(SubCapability.ISubCap.SubCapKey<T> type, CommandContext<CommandSource> context, Consumer<T> exec) throws CommandSyntaxException {
		context.getSource().getPlayerOrException().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(type).ifPresent(exec));
		return 0;
	}

	private static int getArgAsInt(CommandContext<CommandSource> context, String id) {
		return context.getArgument(id, Integer.class);
	}

	public static class Debug {
		public static ArgumentBuilder<CommandSource, ?> register() {
			return Commands.literal("debug").
					requires(cs -> cs.hasPermission(2)).
					then(Commands.literal("resetCooldowns").
							executes(context -> getDataAndRun(Voidscape.subCapTurmoilStats, context, TurmoilStats::resetCooldowns))).
					then(Commands.literal("reset_resetSkillTimer").
							executes(context -> getDataAndRun(Voidscape.subCapTurmoilData, context, Turmoil::resetResetCooldown))).
					then(Commands.literal("reset").
							executes(context -> getDataAndRun(Voidscape.subCapTurmoilData, context, Turmoil::debug))).
					then(Commands.literal("intro").
							executes(context -> getDataAndRun(Voidscape.subCapTurmoilData, context, Turmoil::start))).
					then(Commands.literal("force_start").
							executes(context -> getDataAndRun(Voidscape.subCapTurmoilData, context, Turmoil::forceStart))).
					then(Commands.literal("get").
							then(Commands.literal("progress").
									executes(context -> getDataAndRun(Voidscape.subCapTurmoilData, context, data -> context.getSource().sendSuccess(new StringTextComponent(data.getProgression().name()), false)))).
							then(Commands.literal("state").
									executes(context -> getDataAndRun(Voidscape.subCapTurmoilData, context, data -> context.getSource().sendSuccess(new StringTextComponent(data.getState().name()), false)))).
							then(Commands.literal("level").
									executes(context -> getDataAndRun(Voidscape.subCapTurmoilData, context, data -> context.getSource().sendSuccess(new StringTextComponent(String.valueOf(data.getLevel())), false)))).
							then(Commands.literal("infusion").
									executes(context -> getDataAndRun(Voidscape.subCapInsanity, context, data -> context.getSource().sendSuccess(new StringTextComponent(String.valueOf(data.getInfusion())), false)))).
							then(Commands.literal("paranoia").
									executes(context -> getDataAndRun(Voidscape.subCapInsanity, context, data -> context.getSource().sendSuccess(new StringTextComponent(String.valueOf(data.getParanoia())), false))))).
					then(Commands.literal("set").
							then(Commands.literal("progress").
									then(Commands.argument("id", IntegerArgumentType.integer(0, Progression.values().length - 1)).
											executes(context -> getDataAndRun(Voidscape.subCapTurmoilData, context, data -> data.setProgression(Progression.get(getArgAsInt(context, "id"))))))).
							then(Commands.literal("state").
									then(Commands.argument("id", IntegerArgumentType.integer(0, Turmoil.State.values().length - 1)).
											executes(context -> getDataAndRun(Voidscape.subCapTurmoilData, context, data -> data.setState(Turmoil.State.get(getArgAsInt(context, "id"))))))).
							then(Commands.literal("level").
									then(Commands.argument("level", IntegerArgumentType.integer(0)).
											executes(context -> getDataAndRun(Voidscape.subCapTurmoilData, context, data -> data.setLevel(getArgAsInt(context, "level")))))).
							then(Commands.literal("infusion").
									then(Commands.argument("amount", IntegerArgumentType.integer(0)).
											executes(context -> getDataAndRun(Voidscape.subCapInsanity, context, data -> data.setInfusion(getArgAsInt(context, "amount")))))).
							then(Commands.literal("paranoia").
									then(Commands.argument("amount", IntegerArgumentType.integer(0)).
											executes(context -> getDataAndRun(Voidscape.subCapInsanity, context, data -> data.setParanoia(getArgAsInt(context, "amount"))))))).
					then(Commands.literal("pawn").
							executes(context -> {
								PlayerEntity player = context.getSource().getPlayerOrException();
								try {
									InstanceManager.findFreeInstanceByGroup(new ResourceLocation(Voidscape.MODID, "pawn")).ifPresent(instance -> instance.addPlayer(player));
								} catch (Throwable e) {
									e.printStackTrace();
									throw e;
								}
								return 0;
							}));
		}
	}

}
