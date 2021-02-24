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

					"(Press [%1$s])\n" +

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

					"Welcome to the Void\n" +

							"Tread carefully, the landscape is very... chaotic\n" +

							"We wouldn't want to fall into the abyss would we?\n" +

							"Be aware, while a material mortal such as us treads in this ethereal land...\n" +

							"Various effects will take place, both in Mind and Body\n" +

							"While we linger, our essence is spread onto the rock beneath us\n" +

							"This essence forms crystal. This crystal is useful for us\n" +

							"Covering ourselves in it we can slow the voidic decay\n" +

							"Move quickly before our body completely deteriorates!"),

			(host) -> host.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				data.setProgression(Progression.EnteredVoid);
				data.levelUp();
				data.setState(Turmoil.State.OPENING);
			})));

	public static Entry TUTORIAL_GUI = new Entry(new ResourceLocation(Voidscape.MODID, "tutorialgui"),

			new TranslationTextComponent(

					"This is where we can look into our inner turmoil\n" +

							"We may call upon our turmoil to accomplish various tasks\n" +

							"One such task is to have the Void consume us and take us back here at any time\n" +

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

							"You may however allow the void to reshape us\n" +

							"With a Voidic Crystal in possession, allow your turmoil to shatter it\n" +

							"This can only be done once an hour\n" +

							"(This entire section is a majorly work in progress)\n" +

							"(You can only claim one skill)\n" +

							"(This skill comes with an ability)\n" +

							"(You must slot this ability into your spell bar)\n" +

							"(You can find the spell bar configuration on the main ui)\n" +

							"(Simply click, hold, and drag the ability onto any slot on the bar)\n" +

							"(Check Controls for what keybinds to use or configure in order to use the spell bar)"),

			(host) -> host.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				data.setProgression(Progression.PostTutorial);
			})));

	public static Entry CORRUPT_PHANTOM = new Entry(new ResourceLocation(Voidscape.MODID, "corruptphantom"),

			new TranslationTextComponent(

					"The Corrupted Pawn\n" +

							"One of Xia's misguided henchmen\n" +

							"We only bore witness to its Phantom\n" +

							"It uses this form to banish outsiders back to their own Realm\n" +

							"It may only manifest in the minds of complete insanity\n" +

							"Infact its a great contributor to the maddening effects of the Void\n" +

							"Slay its true form and we may explore further in the Void\n" +

							"Our Turmoil can take us directly to the beast\n" +

							"This wont be easy, come prepared\n " +

							"Let us improve our Voidic Powers first..."),

			(host) -> host.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				data.setProgression(Progression.CorruptPawnPre);
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
