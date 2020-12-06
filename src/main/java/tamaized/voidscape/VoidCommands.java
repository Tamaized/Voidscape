package tamaized.voidscape;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import tamaized.voidscape.turmoil.Progression;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;

public final class VoidCommands {

	private VoidCommands() {

	}

	public static class Debug {
		public static ArgumentBuilder<CommandSource, ?> register() {
			return Commands.literal("debug").
					requires(cs -> cs.hasPermission(2)).
					then(Commands.literal("reset").
							executes(context -> {
								context.getSource().getPlayerOrException().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(Turmoil::debug));
								return 0;
							})).
					then(Commands.literal("intro").
							executes(context -> {
								context.getSource().getPlayerOrException().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(Turmoil::start));
								return 0;
							})).
					then(Commands.literal("force_start").
							executes(context -> {
								context.getSource().getPlayerOrException().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(Turmoil::forceStart));
								return 0;
							})).
					then(Commands.literal("get").
							then(Commands.literal("progress").
									executes(context -> {
										context.getSource().getPlayerOrException().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> context.getSource().sendSuccess(new StringTextComponent(data.getProgression().name()), false)));
										return 0;
									})).
							then(Commands.literal("state").
									executes(context -> {
										context.getSource().getPlayerOrException().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> context.getSource().sendSuccess(new StringTextComponent(data.getState().name()), false)));
										return 0;
									}))).
					then(Commands.literal("set").
							then(Commands.literal("progress").
									then(Commands.argument("id", IntegerArgumentType.integer(0, Progression.values().length - 1)).
											executes(context -> {
												context.getSource().getPlayerOrException().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> data.setProgression(Progression.get(context.getArgument("id", Integer.class)))));
												return 0;
											}))).
							then(Commands.literal("state").
									then(Commands.argument("id", IntegerArgumentType.integer(0, Turmoil.State.values().length - 1)).
											executes(context -> {
												context.getSource().getPlayerOrException().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> data.setState(Turmoil.State.get(context.getArgument("id", Integer.class)))));
												return 0;
											}))));
		}
	}

}
