package tamaized.voidscape.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.voidscape.Voidscape;

public class ModSounds {

	public static final RegistryObject<SoundEvent> AMBIENCE = RegUtil.create(ForgeRegistries.SOUND_EVENTS).register("ambience", () -> new SoundEvent(new ResourceLocation(Voidscape.MODID, "ambience")));

	static void classload() {

	}

}
