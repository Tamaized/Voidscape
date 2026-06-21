package tamaized.voidscape.registry;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.data.DonatorData;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.model.QuiverData;

import java.util.function.Supplier;

@Component
public class ModDataAttachments {

	public final Supplier<AttachmentType<Insanity>> INSANITY = RegUtil.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "insanity",
		() -> AttachmentType
			.serializable(Insanity::new)
			.build()
	);
	public final Supplier<AttachmentType<DonatorData>> DONATOR = RegUtil.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "donator",
		() -> AttachmentType
			.serializable(DonatorData::new)
			.copyOnDeath()
			.build()
	);
	public final Supplier<AttachmentType<Float>> VOIDIC_ARROW = RegUtil.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "voidicarrow",
		(key) -> AttachmentType
			.builder(() -> 0F)
			.serialize(Codec.FLOAT.fieldOf(key.getPath()))
			.build()
	);
	public final Supplier<AttachmentType<Float>> INFUSION_ARROW = RegUtil.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "infusionarrow",
		(key) -> AttachmentType
			.builder(() -> 0F)
			.serialize(Codec.FLOAT.fieldOf(key.getPath()))
			.build()
	);
	public final Supplier<AttachmentType<QuiverData>> QUIVER_NOCKED = RegUtil.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "quiver_knocked",
		() -> AttachmentType
			.builder(QuiverData::new)
			.build()
	);
	public final Supplier<AttachmentType<PotionContents>> AURA_EFFECT = RegUtil.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "aura_effect",
		(key) -> AttachmentType
			.builder(() -> PotionContents.EMPTY)
			.serialize(PotionContents.CODEC.fieldOf(key.getPath()))
			.build()
	);
	public final Supplier<AttachmentType<Integer>> DATA_CORRECTION = RegUtil.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "datacorrection",
		(key) -> AttachmentType
			.builder(() -> 0)
			.serialize(Codec.INT.fieldOf(key.getPath()))
			.copyOnDeath()
			.build()
	);

	@PostConstruct
	private void setup(IEventBus bus) {
		NeoForge.EVENT_BUS.addListener(EntityTickEvent.Post.class, event -> {
			event.getEntity().getData(INSANITY).tick(event.getEntity());
			event.getEntity().getData(DONATOR).tick(event.getEntity());
		});
	}

}
