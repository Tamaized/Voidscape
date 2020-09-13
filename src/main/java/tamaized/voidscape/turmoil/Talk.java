package tamaized.voidscape.turmoil;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.OverlayMessageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class Talk {

	public static Entry TEST = new Entry(new ResourceLocation(Voidscape.MODID, "test"),

			new TranslationTextComponent(

					"([%1$s])\n" +

							"Do not be afraid.\n" +

							"I am you.\n" +

							"As you are me.\n" +

							"We are one the same.\n" +

							"This is just a nightmare.\n" +

							"We are the nightmare.\n" +

							"We seek power.\n" +

							"We can gain power.\n" +

							"We will guide each other through oblivion to gain such power.\n" +

							"Power greater than that of eld.\n" +

							"You are the Blade.\n" +

							"I am the Helm.\n" +

							"Together we shall rise above all.\n" +

							"Do we trust one another?\n" +

							"Let the Void consume us.\n" +

							"We're heading to oblivion.\n" +

							"Let the nightmare begin.\n" +

							"(Everything past this point is heavily a work in progress)\n" +

							"(To exit the dimension you must die, you will not lose your inventory)\n" +

							"(You may enter at any time with [%1$s])",

					OverlayMessageHandler.format(OverlayMessageHandler.FORMAT_KEYBIND)),

			(host) -> host.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(Turmoil::start)));

	public static class Entry {

		private static final Map<ResourceLocation, Entry> REGISTRY = new HashMap<>();
		private final ResourceLocation id;
		private final Consumer<Entity> finish;
		private final TranslationTextComponent message;

		public Entry(ResourceLocation id, TranslationTextComponent message, Consumer<Entity> finish) {
			this.id = id;
			this.finish = finish;
			this.message = message;
			REGISTRY.put(id, this);
		}

		public static Optional<Entry> find(ResourceLocation id) {
			return Optional.ofNullable(REGISTRY.get(id));
		}

		public ResourceLocation getId() {
			return id;
		}

		public TranslationTextComponent getMessage() {
			return message;
		}

		public void finish(Entity host) {
			finish.accept(host);
		}
	}

}
