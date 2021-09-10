package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class RenderNull<T extends Entity> extends EntityRenderer<T> {

	public RenderNull(EntityRendererProvider.Context p_i46179_1_) {
		super(p_i46179_1_);
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {

	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public ResourceLocation getTextureLocation(T entityIn) {
		return null;
	}
}
