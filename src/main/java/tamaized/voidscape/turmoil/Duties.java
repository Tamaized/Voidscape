package tamaized.voidscape.turmoil;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import tamaized.voidscape.Voidscape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Duties {

	private static final List<Duty> DUTY_LIST = new ArrayList<>();

	public static final Duty PAWN = new Duty(rl("pawn"), Progression.CorruptPawnPre, "pawn");

	public static List<Duty> duties() {
		return ImmutableList.copyOf(DUTY_LIST);
	}

	public static int getID(Duty duty) {
		return DUTY_LIST.indexOf(duty);
	}

	@Nullable
	public static Duty fromID(int id) {
		if (id < 0 || id >= DUTY_LIST.size())
			return null;
		return DUTY_LIST.get(id);
	}

	private static ResourceLocation rl(String path) {
		return new ResourceLocation(Voidscape.MODID, path);
	}

	public static class Duty {

		private final ResourceLocation group;
		private final Progression req;
		private final Component display;

		Duty(ResourceLocation group, Progression req, String display) {
			this.group = group;
			this.req = req;
			this.display = new TranslatableComponent(Voidscape.MODID + ".gui.duty." + display);
			DUTY_LIST.add(this);
		}

		public ResourceLocation group() {
			return group;
		}

		public Progression progression() {
			return req;
		}

		public Component display() {
			return display;
		}

	}

}
