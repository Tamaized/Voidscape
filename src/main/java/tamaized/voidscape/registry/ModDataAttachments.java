package tamaized.voidscape.registry;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.data.DonatorData;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.data.NetworkedDataAttachment;
import tamaized.voidscape.data.Shroud;
import tamaized.voidscape.model.QuiverData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class ModDataAttachments {

	public final Map<String, Supplier<AttachmentType<? extends NetworkedDataAttachment>>> networkedAttachments = new HashMap<>();

	public final Supplier<AttachmentType<Insanity>> INSANITY = registerNetworked("insanity", name -> RegUtil.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, name,
		() -> AttachmentType
			.serializable(() -> new Insanity(name))
			.build()
	));

	public final Supplier<AttachmentType<DonatorData>> DONATOR = registerNetworked("donator", name -> RegUtil.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, name,
		() -> AttachmentType
			.serializable(() -> new DonatorData(name))
			.copyOnDeath()
			.build()
	));

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

	public final Supplier<AttachmentType<Shroud>> SHROUD = registerNetworked("shroud", name -> RegUtil.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, name,
		() -> AttachmentType
			.serializable(() -> new Shroud(name))
			.build()
	));

	private <T extends NetworkedDataAttachment> Supplier<AttachmentType<T>> registerNetworked(String name, Function<String, Supplier<AttachmentType<T>>> named) {
		var attachment = named.apply(name);
		networkedAttachments.put(name, attachment::get);
		return attachment;
	}

	@Nullable
	public Supplier<AttachmentType<? extends NetworkedDataAttachment>> getNetworkedAttachment(String name) {
		return networkedAttachments.get(name);
	}

	@PostConstruct
	private void setup(IEventBus bus) {
		NeoForge.EVENT_BUS.addListener(EntityTickEvent.Post.class, event -> {
			event.getEntity().getData(INSANITY).tick(event.getEntity());
			event.getEntity().getData(DONATOR).tick(event.getEntity());
			event.getEntity().getData(SHROUD).tick(event.getEntity());
		});
	}

}
