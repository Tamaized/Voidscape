package tamaized.voidscape;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;

public final class VoidCommands {

	private VoidCommands() {

	}

	public static class Debug {
		public static ArgumentBuilder<CommandSource, ?> register() {
			return Commands.literal("debug").
					requires(cs -> cs.hasPermissionLevel(2)).
					then(Commands.literal("reset").
							executes(context -> {
								context.getSource().asPlayer().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(Turmoil::debug));
								return 0;
							})).
					then(Commands.literal("intro").
							executes(context -> {
								context.getSource().asPlayer().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(Turmoil::start));
								return 0;
							}));
		}
	}

}
