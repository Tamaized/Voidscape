package tamaized.voidscape.client.entity.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.monster.spider.SpiderModel;

public class ModelVoidling extends SpiderModel {

	public ModelVoidling(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createMesh() {
		return SpiderModel.createSpiderBodyLayer();
	}

}
