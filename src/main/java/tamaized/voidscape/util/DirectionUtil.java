package tamaized.voidscape.util;

import net.minecraft.core.Direction;
import tamaized.beanification.Component;

@Component
public class DirectionUtil {

	private final Direction[] DIRECTIONS = Direction.values();

	public Direction[] getAllDirections() {
		return DIRECTIONS;
	}

}
