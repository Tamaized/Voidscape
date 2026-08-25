package tamaized.voidscape.client.entity.render.state;

import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class NullServantAugmentBlockRenderState extends EntityRenderState {

	public final MovingBlockRenderState mimic = new MovingBlockRenderState();

	public boolean visible;

	public float rotation;

}
