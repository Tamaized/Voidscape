package tamaized.voidscape.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

@Component
public class NamespaceUtils {

	public String dot(ResourceKey<?> key) {
		return dot(key.location());
	}

	public String dot(ResourceLocation key) {
		return key.getNamespace() + "." + key.getPath();
	}

	public String slash(ResourceKey<?> key) {
		return slash(key.location());
	}

	public String slash(ResourceLocation key) {
		return key.getNamespace() + "/" + key.getPath();
	}

	public String prefixId(String text) {
		return Voidscape.MODID + "." + text;
	}

	public String suffixId(String text) {
		return text + "." + Voidscape.MODID;
	}

	public String insertId(String prefix, String suffix) {
		return prefix + "." + Voidscape.MODID + "." + suffix;
	}

}
