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

	public static final ResourceLocation FORMAT_KEYBIND = new ResourceLocation(Voidscape.MODID, "keybind");
	public static final String KEY_BASE = Voidscape.MODID + ".talk.entry.";

	public static Entry INTRO = new Entry(new ResourceLocation(Voidscape.MODID, "intro"),

			new TranslationTextComponent(KEY_BASE + "intro", format(FORMAT_KEYBIND)),

			(host) -> host.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(Turmoil::start)));

	public static Entry TUTORIAL = new Entry(new ResourceLocation(Voidscape.MODID, "tutorial"),

			new TranslationTextComponent(KEY_BASE + "tutorial"),

			(host) -> host.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				data.progressTo(Progression.EnteredVoid);
				data.levelUp();
				data.setState(Turmoil.State.OPENING);
			})));

	public static Entry TUTORIAL_GUI = new Entry(new ResourceLocation(Voidscape.MODID, "tutorialgui"),

			new TranslationTextComponent(KEY_BASE + "tutorialgui", format(FORMAT_KEYBIND)),

			(host) -> host.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				data.progressTo(Progression.MidTutorial);
			})));

	public static Entry TUTORIAL_SKILLS = new Entry(new ResourceLocation(Voidscape.MODID, "tutorialskills"),

			new TranslationTextComponent(KEY_BASE + "tutorialskills"),

			(host) -> host.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				data.progressTo(Progression.PostTutorial);
			})));

	public static Entry CORRUPT_PHANTOM = new Entry(new ResourceLocation(Voidscape.MODID, "corruptphantom"),

			new TranslationTextComponent(KEY_BASE + "corruptphantom"),

			(host) -> host.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				data.progressTo(Progression.CorruptPawnPre);
				data.levelUp();
			})));

	public static Entry CORRUPT_PAWN = new Entry(new ResourceLocation(Voidscape.MODID, "corruptpawn"),

			new TranslationTextComponent(KEY_BASE + "corruptpawn"),

			(host) -> host.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				data.progressTo(Progression.CorruptPawnPost);
				data.levelUp();
			})));

	public static String format(ResourceLocation key) {
		return "($".concat(key.toString()).concat(")");
	}

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

		public static Optional<Entry> findOrExec(ResourceLocation id, Runnable exec) {
			Optional<Entry> e = find(id);
			if (!e.isPresent())
				exec.run();
			return e;
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
