package tamaized.voidscape.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class TransparentModelRenderer extends ModelRenderer {

	public float alpha = 1F;

	public TransparentModelRenderer(Model modelIn) {
		super(modelIn);
	}

	public TransparentModelRenderer(Model modelIn, int texOffX, int texOffY) {
		super(modelIn, texOffX, texOffY);
	}

	public TransparentModelRenderer(int textureWidthIn, int textureHeightIn, int textureOffsetXIn, int textureOffsetYIn) {
		super(textureWidthIn, textureHeightIn, textureOffsetXIn, textureOffsetYIn);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha * this.alpha);
		this.alpha = 1F;
	}
}
