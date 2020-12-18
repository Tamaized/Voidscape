package tamaized.voidscape.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.abilities.mage.EntitySpellBolt;

public class RenderSpellBolt<T extends EntitySpellBolt> extends EntityRenderer<T> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Voidscape.MODID, "textures/spells/mage/bolt.png");

	public RenderSpellBolt(EntityRendererManager p_i46179_1_) {
		super(p_i46179_1_);
	}

	@Override
	public ResourceLocation getTextureLocation(T entityIn) {
		return new ResourceLocation("");
	}
}
