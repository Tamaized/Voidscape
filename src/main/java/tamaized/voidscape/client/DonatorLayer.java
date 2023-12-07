package tamaized.voidscape.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.joml.Matrix4f;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ui.RenderTurmoil;
import tamaized.voidscape.data.DonatorData;
import tamaized.voidscape.registry.ModDataAttachments;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DonatorLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Voidscape.MODID, "textures/entity/donator.png");

	private static final Function<Supplier<ShaderInstance>, RenderType> RENDER_TYPE = Util.memoize(shader -> RenderType.create("voidscape_wings", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().
			setTransparencyState(RenderStateAccessor.TRANSLUCENT_TRANSPARENCY()).
			setCullState(RenderStateAccessor.NO_CULL()).
			setShaderState(new RenderStateShard.ShaderStateShard(shader)).
			setTextureState(new RenderStateShard.MultiTextureStateShard.Builder().add(TEXTURE, false, false).add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false).build()).
			createCompositeState(true)));
	private static final RenderType WRAPPED_POS_TEX_COLOR = RENDER_TYPE.apply(GameRenderer::getPositionTexColorShader);
	private static final RenderType WINGS = RENDER_TYPE.apply(() -> Shaders.VOIDSKY_WINGS);

	private static final MultiBufferSource.BufferSource BUFFERS = MultiBufferSource.immediateWithBuffers(Util.make(new Object2ObjectLinkedOpenHashMap<>(), map -> {
		map.put(WRAPPED_POS_TEX_COLOR, new BufferBuilder(WRAPPED_POS_TEX_COLOR.bufferSize()));
		map.put(WINGS, new BufferBuilder(WINGS.bufferSize()));
	}), new BufferBuilder(256));

	public DonatorLayer(RenderLayerParent<T, M> p_117346_) {
		super(p_117346_);
	}

	public static void setup() {
		NeoForge.EVENT_BUS.addListener(RenderLevelStageEvent.class, event -> {
			if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)
				BUFFERS.endBatch();
		});
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource multibuffer, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		DonatorData data = entity.getData(ModDataAttachments.DONATOR);
		if (data.enabled) {
			VertexConsumer buffer = BUFFERS.getBuffer(WRAPPED_POS_TEX_COLOR);

			float x1 = 0.10F;
			float y1 = -0.75F;
			float z1 = 0.75F;

			float x2 = x1 + 1.0F;
			float y2 = y1 + 1.0F;
			float z2 = z1 - 0.75F;

			RenderTurmoil.Color24 color = RenderTurmoil.Color24.INSTANCE;

			Consumer<VertexConsumer> vertexColor = verticies -> verticies.color(color.bit16, color.bit8, color.bit0, color.bit24).endVertex();

			color.set(0.25F, 0F, 0F, 0F);

			stack.pushPose();
			{
				stack.mulPose(Axis.ZN.rotationDegrees(-25));
				stack.mulPose(Axis.XN.rotationDegrees(15));
				Matrix4f pose = stack.last().pose();
				vertexColor.accept(buffer.vertex(pose, x2, y2, z1).uv(0, 1));
				vertexColor.accept(buffer.vertex(pose, x2, y1, z1).uv(0, 0));
				vertexColor.accept(buffer.vertex(pose, x1, y1, z2).uv(1, 0));
				vertexColor.accept(buffer.vertex(pose, x1, y2, z2).uv(1, 1));
			}
			stack.popPose();
			stack.pushPose();
			{
				Matrix4f pose = stack.last().pose();
				stack.mulPose(Axis.ZN.rotationDegrees(25));
				stack.mulPose(Axis.XN.rotationDegrees(15));
				float offset = -1.2F;
				vertexColor.accept(buffer.vertex(pose, x1 + offset, y2, z1).uv(0, 1));
				vertexColor.accept(buffer.vertex(pose, x1 + offset, y1, z1).uv(0, 0));
				vertexColor.accept(buffer.vertex(pose, x2 + offset, y1, z2).uv(1, 0));
				vertexColor.accept(buffer.vertex(pose, x2 + offset, y2, z2).uv(1, 1));
			}
			stack.popPose();

			buffer = BUFFERS.getBuffer(WINGS);

			color.unpack(data.color);
			color.bit24 = (int) (0.25F * 255);

			stack.pushPose();
			{
				stack.mulPose(Axis.ZN.rotationDegrees(-25));
				stack.mulPose(Axis.XN.rotationDegrees(15));
				Matrix4f pose = stack.last().pose();
				vertexColor.accept(buffer.vertex(pose, x2, y2, z1).uv(0, 1));
				vertexColor.accept(buffer.vertex(pose, x2, y1, z1).uv(0, 0));
				vertexColor.accept(buffer.vertex(pose, x1, y1, z2).uv(1, 0));
				vertexColor.accept(buffer.vertex(pose, x1, y2, z2).uv(1, 1));
			}
			stack.popPose();
			stack.pushPose();
			{
				Matrix4f pose = stack.last().pose();
				stack.mulPose(Axis.ZN.rotationDegrees(25));
				stack.mulPose(Axis.XN.rotationDegrees(15));
				float offset = -1.2F;
				vertexColor.accept(buffer.vertex(pose, x1 + offset, y2, z1).uv(0, 1));
				vertexColor.accept(buffer.vertex(pose, x1 + offset, y1, z1).uv(0, 0));
				vertexColor.accept(buffer.vertex(pose, x2 + offset, y1, z2).uv(1, 0));
				vertexColor.accept(buffer.vertex(pose, x2 + offset, y2, z2).uv(1, 1));
			}
			stack.popPose();

		}
	}
}
