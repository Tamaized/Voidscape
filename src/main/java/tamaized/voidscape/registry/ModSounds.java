package tamaized.voidscape.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;

import java.util.function.Supplier;

@Component
public class ModSounds {

	private final DeferredRegister<SoundEvent> REGISTERY = RegUtil.create(Registries.SOUND_EVENT);

	public final Holder<SoundEvent> AMBIENCE = REGISTERY.register("ambience", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Voidscape.MODID, "ambience")));
	public final Supplier<SoundEvent> PORTAL = REGISTERY.register("portal", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Voidscape.MODID, "portal")));
	public final Holder<SoundEvent> MUSIC = REGISTERY.register("music", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Voidscape.MODID, "music")));

}
