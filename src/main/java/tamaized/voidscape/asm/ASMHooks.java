package tamaized.voidscape.asm;

public class ASMHooks {

	/*
	 * Injection Point:<br>
	 * {@link net.minecraft.client.shader.Framebuffer#func_216492_b}<br>
	 * [BEFORE] {@link net.minecraft.client.shader.Framebuffer#checkFramebufferComplete}
	 */
	/*public static void enableStencilBuffer(Framebuffer fbo) {
		if (fbo.useDepth) {
			GlStateManager.bindRenderbuffer(FramebufferConstants.renderBufferTarget, fbo.depthBuffer);
			GlStateManager.renderbufferStorage(FramebufferConstants.renderBufferTarget, /*GL14.GL_DEPTH_COMPONENT24*//*org.lwjgl.opengl.EXTPackedDepthStencil.GL_DEPTH24_STENCIL8_EXT, fbo.framebufferTextureWidth, fbo.framebufferTextureHeight);
			GlStateManager.framebufferRenderbuffer(FramebufferConstants.target, FramebufferConstants.attachment, FramebufferConstants.renderBufferTarget, fbo.depthBuffer);
			GlStateManager.framebufferRenderbuffer(FramebufferConstants.target, org.lwjgl.opengl.EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, FramebufferConstants.renderBufferTarget, fbo.depthBuffer);
		}
	}*/

}
