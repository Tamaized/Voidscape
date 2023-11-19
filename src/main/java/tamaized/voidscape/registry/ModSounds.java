package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;

import java.util.function.Supplier;

public class ModSounds implements RegistryClass {

	private static final DeferredRegister<SoundEvent> REGISTERY = RegUtil.create(Registries.SOUND_EVENT);

	public static final Supplier<SoundEvent> AMBIENCE = REGISTERY.register("ambience", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Voidscape.MODID, "ambience")));
	public static final Supplier<SoundEvent> PORTAL = REGISTERY.register("portal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Voidscape.MODID, "portal")));
	public static final Supplier<SoundEvent> MUSIC = REGISTERY.register("music", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Voidscape.MODID, "music")));

	@Override
	public void init(IEventBus bus) {

	}

}
