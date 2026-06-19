package tamaized.voidscape.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

@Component
public class NamespaceUtils {

	public String dot(ResourceKey<?> key) {
		return dot(key.identifier());
	}

	public String dot(Identifier key) {
		return key.getNamespace() + "." + key.getPath();
	}

	public String slash(ResourceKey<?> key) {
		return slash(key.identifier());
	}

	public String slash(Identifier key) {
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
