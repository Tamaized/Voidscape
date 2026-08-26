package tamaized.voidscape.client.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import tamaized.voidscape.client.armor.model.IOverlayArmorModel;

public class ArmorOverlayLayer<S extends HumanoidRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {

	private static final int SUBMIT_ORDER_ABOVE_ARMOR = 4;

	public ArmorOverlayLayer(RenderLayerParent<S, M> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
		submitOverlay(poseStack, submitNodeCollector, lightCoords, state, state.headEquipment, EquipmentSlot.HEAD);
		submitOverlay(poseStack, submitNodeCollector, lightCoords, state, state.chestEquipment, EquipmentSlot.CHEST);
		submitOverlay(poseStack, submitNodeCollector, lightCoords, state, state.legsEquipment, EquipmentSlot.LEGS);
		submitOverlay(poseStack, submitNodeCollector, lightCoords, state, state.feetEquipment, EquipmentSlot.FEET);
	}

	@SuppressWarnings("unchecked")
	private void submitOverlay(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, ItemStack stack, EquipmentSlot armorSlot) {
		if (stack.isEmpty())
			return;
		EquipmentClientInfo.LayerType layerType = armorSlot == EquipmentSlot.LEGS ?
			EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS :
			EquipmentClientInfo.LayerType.HUMANOID;
		var model = IClientItemExtensions.of(stack).getGenericArmorModel(stack, layerType, getParentModel());
		if (!(model instanceof IOverlayArmorModel overlayArmorModel))
			return;
		Identifier overlayTexture = overlayArmorModel.overlayTexture();
		if (overlayTexture == null)
			return;
		submitNodeCollector.order(SUBMIT_ORDER_ABOVE_ARMOR).submitModel(
			model,
			state,
			poseStack,
			RenderTypes.armorCutoutNoCull(overlayTexture),
			overlayArmorModel.overlayFullbright() ? LightCoordsUtil.FULL_BRIGHT : lightCoords,
			OverlayTexture.NO_OVERLAY,
			state.outlineColor,
			null
		);
	}

}
