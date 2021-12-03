package tamaized.voidscape.registry;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.voidscape.Voidscape;

import java.util.UUID;

public class ModAttributes {

	static void classload() {

	}

	private static final DeferredRegister<Attribute> ATTRIBUTE_REGISTERY = RegUtil.create(ForgeRegistries.ATTRIBUTES);

	public static final RegistryObject<RegUtil.ModAttribute> VOIDIC_VISIBILITY = make("voidic_visibility", 1F, "Voidic Visibility", UUID.fromString("177f92ef-452c-49e0-acd3-235bc58c9297"), true);
	public static final RegistryObject<RegUtil.ModAttribute> VOIDIC_INFUSION_RES = make("voidic_infusion_res", 1F, "Voidic Infusion Resistance", UUID.fromString("15d20c76-90c0-4d15-affd-c974e36ac35d"), true);
	public static final RegistryObject<RegUtil.ModAttribute> VOIDIC_RES = make("voidic_res", 0F, "Voidic Damage Resistance", UUID.fromString("a1fa645b-70ca-459b-becc-bcf7bdf090c0"));
	public static final RegistryObject<RegUtil.ModAttribute> VOIDIC_DMG = make("voidic_dmg", 0F, "Voidic Damage", UUID.fromString("eeacdd6d-bc33-4d30-b2f0-807e7ed333d6"));
	public static final RegistryObject<RegUtil.ModAttribute> VOIDIC_ARROW_DMG = make("voidic_arrow_dmg", 0F, "Voidic Arrow Damage", UUID.fromString("43812ed1-d129-44b9-8b51-673c91d498c6"));

	private static RegistryObject<RegUtil.ModAttribute> make(String name, float defaultVal, String type, UUID id) {
		return make(name, defaultVal, type, id, false);
	}

	private static RegistryObject<RegUtil.ModAttribute> make(String name, float defaultVal, String type, UUID id, boolean sync) {
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
		n.add(ModAttributes.VOIDIC_RES.get(), 0F);
		n.add(ModAttributes.VOIDIC_DMG.get(), 0F);
		n.add(ModAttributes.VOIDIC_ARROW_DMG.get(), 0F);
	}

}
