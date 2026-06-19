package tamaized.voidscape.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;

import java.util.function.Supplier;

@Component
public class ModSounds {

	public final Holder<SoundEvent> AMBIENCE = RegUtil.register(Registries.SOUND_EVENT, "ambience",
		() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Voidscape.MODID, "ambience")));

	public final Supplier<SoundEvent> PORTAL = RegUtil.register(Registries.SOUND_EVENT, "portal",
		() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Voidscape.MODID, "portal")));

	public final Holder<SoundEvent> MUSIC = RegUtil.register(Registries.SOUND_EVENT, "music",
		() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Voidscape.MODID, "music")));

}
