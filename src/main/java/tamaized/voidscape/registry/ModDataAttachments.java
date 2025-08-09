package tamaized.voidscape.registry;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
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

	private final DeferredRegister<AttachmentType<?>> REGISTRY = RegUtil.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES);

	public final Supplier<AttachmentType<Insanity>> INSANITY = REGISTRY.register("insanity", () -> AttachmentType
		.serializable(Insanity::new)
		.build()
	);
	public final Supplier<AttachmentType<DonatorData>> DONATOR = REGISTRY.register("donator", () -> AttachmentType
		.serializable(DonatorData::new)
		.copyOnDeath()
		.build()
	);
	public final Supplier<AttachmentType<Float>> VOIDIC_ARROW = REGISTRY.register("voidicarrow", () -> AttachmentType
		.builder(() -> 0F)
		.serialize(Codec.FLOAT)
		.build()
	);
	public final Supplier<AttachmentType<Float>> INFUSION_ARROW = REGISTRY.register("infusionarrow", () -> AttachmentType
		.builder(() -> 0F)
		.serialize(Codec.FLOAT)
		.build()
	);
	public final Supplier<AttachmentType<QuiverData>> QUIVER_NOCKED = REGISTRY.register("quiver_knocked", () -> AttachmentType
		.builder(QuiverData::new)
		.build()
	);
	public final Supplier<AttachmentType<Integer>> DATA_CORRECTION = REGISTRY.register("datacorrection", () -> AttachmentType
		.builder(() -> 0)
		.serialize(Codec.INT)
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
