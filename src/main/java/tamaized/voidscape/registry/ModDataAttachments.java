package tamaized.voidscape.registry;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.data.DonatorData;
import tamaized.voidscape.data.Insanity;

import java.util.function.Supplier;

public class ModDataAttachments implements RegistryClass {

	private static final DeferredRegister<AttachmentType<?>> REGISTRY = RegUtil.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES);

	public static final Supplier<AttachmentType<Insanity>> INSANITY = REGISTRY.register("insanity", () -> AttachmentType.serializable(Insanity::new).build());
	public static final Supplier<AttachmentType<DonatorData>> DONATOR = REGISTRY.register("donator", () -> AttachmentType.serializable(DonatorData::new).copyOnDeath().build());
	public static final Supplier<AttachmentType<Float>> VOIDIC_ARROW = REGISTRY.register("voidicarrow", () -> AttachmentType.builder(() -> 0F).serialize(Codec.FLOAT).build());
	public static final Supplier<AttachmentType<Float>> INFUSION_ARROW = REGISTRY.register("infusionarrow", () -> AttachmentType.builder(() -> 0F).serialize(Codec.FLOAT).build());

	@Override
	public void init(IEventBus bus) {
		NeoForge.EVENT_BUS.addListener(LivingEvent.LivingTickEvent.class, event -> {
			event.getEntity().getData(INSANITY).tick(event.getEntity());
			event.getEntity().getData(DONATOR).tick(event.getEntity());
		});
	}

}
