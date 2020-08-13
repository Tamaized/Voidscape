package tamaized.voidscape.turmoil;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import tamaized.voidscape.Voidscape;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class Talk {

	public static Entry TEST = new Entry(new ResourceLocation(Voidscape.MODID, "test"),

			new TranslationTextComponent(

					"(Press [%s])\n" +

							"Do not be afraid\n" +

							"Allow the void to consume you" +

							"Everything will be alright" +

							"Summon me when you're ready",

					"($voidscape:keybind)"),

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
