package tamaized.voidscape.client.entity.model;

import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import tamaized.voidscape.entity.VoidlingEntity;

public class ModelVoidling<T extends VoidlingEntity> extends SpiderModel<T> {

	public ModelVoidling(ModelPart pRoot) {
		super(pRoot);
	}

	public static LayerDefinition createMesh() {
		return SpiderModel.createSpiderBodyLayer();
	}

}
