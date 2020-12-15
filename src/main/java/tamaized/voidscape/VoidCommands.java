package tamaized.voidscape;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import tamaized.voidscape.turmoil.Progression;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;

import java.util.function.Consumer;

public final class VoidCommands {

	private VoidCommands() {

	}

	private static int getTurmoilAndRun(CommandContext<CommandSource> context, Consumer<Turmoil> exec) throws CommandSyntaxException {
		context.getSource().getPlayerOrException().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(exec));
		return 0;
	}

	private static int getArgAsInt(CommandContext<CommandSource> context, String id) {
		return context.getArgument(id, Integer.class);
	}

	public static class Debug {
		public static ArgumentBuilder<CommandSource, ?> register() {
			return Commands.literal("debug").
					requires(cs -> cs.hasPermission(2)).
					then(Commands.literal("reset").
							executes(context -> getTurmoilAndRun(context, Turmoil::debug))).
					then(Commands.literal("intro").
							executes(context -> getTurmoilAndRun(context, Turmoil::start))).
					then(Commands.literal("force_start").
							executes(context -> getTurmoilAndRun(context, Turmoil::forceStart))).
					then(Commands.literal("get").
							then(Commands.literal("progress").
									executes(context -> getTurmoilAndRun(context, data -> context.getSource().sendSuccess(new StringTextComponent(data.getProgression().name()), false)))).
							then(Commands.literal("state").
									executes(context -> getTurmoilAndRun(context, data -> context.getSource().sendSuccess(new StringTextComponent(data.getState().name()), false)))).
							then(Commands.literal("level").
									executes(context -> getTurmoilAndRun(context, data -> context.getSource().sendSuccess(new StringTextComponent(String.valueOf(data.getLevel())), false))))).
					then(Commands.literal("set").
							then(Commands.literal("progress").
									then(Commands.argument("id", IntegerArgumentType.integer(0, Progression.values().length - 1)).
											executes(context -> getTurmoilAndRun(context, data -> data.setProgression(Progression.get(getArgAsInt(context, "id"))))))).
							then(Commands.literal("state").
									then(Commands.argument("id", IntegerArgumentType.integer(0, Turmoil.State.values().length - 1)).
											executes(context -> getTurmoilAndRun(context, data -> data.setState(Turmoil.State.get(getArgAsInt(context, "id"))))))).
							then(Commands.literal("level").
									then(Commands.argument("level", IntegerArgumentType.integer(0)).
											executes(context -> getTurmoilAndRun(context, data -> data.setLevel(getArgAsInt(context, "level")))))));
		}
	}

}
