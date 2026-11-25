package tamaized.voidscape;

import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tamaized.beanification.connector.fml.FMLBeanContextConnector;
import tamaized.datagenutil.DataGenUtilConstants;
import tamaized.regutil.RegUtil;

@Mod(Voidscape.MODID)
public class Voidscape {

	public static final String MODID = "voidscape";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	static {
		FMLBeanContextConnector.init(MODID, RegUtil.MODULE_NAME, DataGenUtilConstants.MODULE_NAME);
	}

}
