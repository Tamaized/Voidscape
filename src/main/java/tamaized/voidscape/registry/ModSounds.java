package tamaized.voidscape.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.voidscape.Voidscape;

public class ModSounds {

	public static final RegistryObject<SoundEvent> AMBIENCE = RegUtil.create(ForgeRegistries.SOUND_EVENTS).register("ambience", () -> new SoundEvent(new ResourceLocation(Voidscape.MODID, "ambience")));
	public static final RegistryObject<SoundEvent> MUSIC = RegUtil.create(ForgeRegistries.SOUND_EVENTS).register("music", () -> new SoundEvent(new ResourceLocation(Voidscape.MODID, "music")));

	static void classload() {

	}

}
