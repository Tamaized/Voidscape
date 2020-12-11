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

	public static Entry INTRO = new Entry(new ResourceLocation(Voidscape.MODID, "intro"),

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

					format(FORMAT_KEYBIND)),

			(host) -> host.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(Turmoil::start)));

	public static Entry TUTORIAL = new Entry(new ResourceLocation(Voidscape.MODID, "tutorial"),

			new TranslationTextComponent(

					"Welcome to the Void"),

			(host) -> host.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				data.setProgression(Progression.EnteredVoid);
				data.levelUp();
				data.setState(Turmoil.State.OPENING);
			})));

	public static Entry TUTORIAL_GUI = new Entry(new ResourceLocation(Voidscape.MODID, "tutorialgui"),

			new TranslationTextComponent(

					"This is where we can look into our inner turmoil\n" +

							"We may call upon our turmoil to do various tasks\n" +

							"One such task is to call upon the Void to consume us and take us back here at any time\n" +

							"Another is the ability to shape our own power to our liking\n" +

							"Let us shape our Voidic Powers now...\n" +

							"(Press [%1$s] to open the UI at any time)",

					format(FORMAT_KEYBIND)),

			(host) -> host.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				data.setProgression(Progression.MidTutorial);
			})));

	public static Entry TUTORIAL_SKILLS = new Entry(new ResourceLocation(Voidscape.MODID, "tutorialskills"),

			new TranslationTextComponent(

					"Here we can shape our power how we wish\n" +

							"We can only go down a single path at a time\n" +

							"Let us make our choices wisely\n" +

							"(Be sure to hover over each skill and read what it does)\n" +

							"(Your choices for now are permanent)\n" +

							"(they can be reset at a later time as you advance through the mod)\n" +

							"(You may only go down a single path at a time, there are 3 specific types of classes)\n" +

							"(Damage Dealer (DPS), Tank, or a Healer)\n" +

							"(As of now, there are two types of DPS, Melee Physical and a Ranged Magical)\n" +

							"(You can come back to this screen at any time to spend your points)"),

			(host) -> host.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				data.setProgression(Progression.PostTutorial);
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
