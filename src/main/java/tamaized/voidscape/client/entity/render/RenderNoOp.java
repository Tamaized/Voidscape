package tamaized.voidscape.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;

public class RenderNoOp<T extends Entity> extends EntityRenderer<T, EntityRenderState> {

	public RenderNoOp(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public EntityRenderState createRenderState() {
		return new EntityRenderState();
	}

}
