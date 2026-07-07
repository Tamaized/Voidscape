package tamaized.voidscape.effect;

import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import tamaized.voidscape.Voidscape;

public class StandardEffect extends MobEffect {

	private final Identifier texture;

	public StandardEffect(String texture, MobEffectCategory type, int color) {
		super(type, color);
		this.texture = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/effect/" + texture + ".png");
	}

	public final Identifier getTexture() {
		return texture;
	}

}