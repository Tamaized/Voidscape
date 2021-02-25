package tamaized.voidscape.registry;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.voidscape.Voidscape;

import java.util.UUID;

public class ModAttributes {

	static void classload() {

	}

	private static final DeferredRegister<Attribute> ATTRIBUTE_REGISTERY = RegUtil.create(ForgeRegistries.ATTRIBUTES);

	public static final RegistryObject<RegUtil.ModAttribute> VOIDIC_INFUSION_RES = make("voidic_infusion_res", 1F, "Voidic Infusion Resistance", UUID.fromString("15d20c76-90c0-4d15-affd-c974e36ac35d"));
	public static final RegistryObject<RegUtil.ModAttribute> VOIDIC_RES = make("voidic_res", 1F, "Voidic Damage Resistance", UUID.fromString("a1fa645b-70ca-459b-becc-bcf7bdf090c0"));
	public static final RegistryObject<RegUtil.ModAttribute> VOIDIC_DMG = make("voidic_dmg", 1F, "Voidic Damage", UUID.fromString("eeacdd6d-bc33-4d30-b2f0-807e7ed333d6"));

	private static RegistryObject<RegUtil.ModAttribute> make(String name, float defaultVal, String type, UUID id) {
		return ATTRIBUTE_REGISTERY.register(name, () -> new RegUtil.ModAttribute(Voidscape.MODID.concat(".attribute.").concat(name), defaultVal, id, type));
	}

}
