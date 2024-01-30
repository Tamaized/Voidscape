package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;

import java.util.UUID;
import java.util.function.Supplier;

public class ModAttributes implements RegistryClass {

	@Override
	public void init(IEventBus bus) {

	}

	private static final DeferredRegister<Attribute> ATTRIBUTE_REGISTERY = RegUtil.create(Registries.ATTRIBUTE);

	public static final Supplier<RegUtil.ModAttribute> VOIDIC_VISIBILITY = make("voidic_visibility", 1F, "Voidic Visibility", UUID.fromString("177f92ef-452c-49e0-acd3-235bc58c9297"), true);
	public static final Supplier<RegUtil.ModAttribute> VOIDIC_INFUSION_RES = make("voidic_infusion_res", 1F, "Voidic Infusion Resistance", UUID.fromString("15d20c76-90c0-4d15-affd-c974e36ac35d"), true);
	public static final Supplier<RegUtil.ModAttribute> VOIDIC_PARANOIA_RES = make("voidic_paranoia_res", 1F, "Voidic Paranoia Resistance", UUID.fromString("c265b4c7-78b8-4a1b-b99b-c2a6c2af4f57"), true);
	public static final Supplier<RegUtil.ModAttribute> VOIDIC_RES = make("voidic_res", 0F, "Voidic Damage Resistance", UUID.fromString("a1fa645b-70ca-459b-becc-bcf7bdf090c0"));
	public static final Supplier<RegUtil.ModAttribute> VOIDIC_DMG = make("voidic_dmg", 0F, "Voidic Damage", UUID.fromString("eeacdd6d-bc33-4d30-b2f0-807e7ed333d6"));
	public static final Supplier<RegUtil.ModAttribute> VOIDIC_ARROW_DMG = make("voidic_arrow_dmg", 0F, "Voidic Arrow Damage", UUID.fromString("43812ed1-d129-44b9-8b51-673c91d498c6"));

	public static final CustomVanillaAttribute DRACONIC_HEALTH = new CustomVanillaAttribute("Draconic Health", UUID.fromString("BCCA14E2-2754-4ED9-86A6-6C42CADC25CF"));

	private static Supplier<RegUtil.ModAttribute> make(String name, double defaultVal, String type, UUID id) {
		return make(name, defaultVal, type, id, false);
	}

	private static Supplier<RegUtil.ModAttribute> make(String name, double defaultVal, String type, UUID id, boolean sync) {
		return ATTRIBUTE_REGISTERY.register(name, () -> {
			RegUtil.ModAttribute attribute = new RegUtil.ModAttribute(Voidscape.MODID.concat(".attribute.").concat(name), defaultVal, id, type);
			if (sync)
				attribute.setSyncable(true);
			return attribute;
		});
	}

	public static void assignAttributes(AttributeSupplier.Builder n) {
		n.add(ModAttributes.VOIDIC_VISIBILITY.get(), 1F);
		n.add(ModAttributes.VOIDIC_INFUSION_RES.get(), 1F);
		n.add(ModAttributes.VOIDIC_PARANOIA_RES.get(), 1F);
		n.add(ModAttributes.VOIDIC_RES.get(), 0F);
		n.add(ModAttributes.VOIDIC_DMG.get(), 0F);
		n.add(ModAttributes.VOIDIC_ARROW_DMG.get(), 0F);
	}

	public record CustomVanillaAttribute(String type, UUID id) {

	}

}
