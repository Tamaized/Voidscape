package tamaized.voidscape.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;

public class ModSounds implements RegistryClass {

	public static final RegistryObject<SoundEvent> AMBIENCE = RegUtil.create(ForgeRegistries.SOUND_EVENTS).register("ambience", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Voidscape.MODID, "ambience")));
	public static final RegistryObject<SoundEvent> PORTAL = RegUtil.create(ForgeRegistries.SOUND_EVENTS).register("portal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Voidscape.MODID, "portal")));
	public static final RegistryObject<SoundEvent> MUSIC = RegUtil.create(ForgeRegistries.SOUND_EVENTS).register("music", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Voidscape.MODID, "music")));

	@Override
	public void init(IEventBus bus) {

	}

}
