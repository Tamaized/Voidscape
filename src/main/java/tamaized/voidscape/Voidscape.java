package tamaized.voidscape;

import net.minecraft.core.registries.Registries;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tamaized.beanification.BeanContext;
import tamaized.regutil.RegUtil;

@Mod(Voidscape.MODID)
public class Voidscape {

	public static final String MODID = "voidscape";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	static {
		RegUtil.create(Registries.ITEM); // Ensure this exists during bean init
		BeanContext.init(MODID);
		RegUtil.setup();
	}

}
