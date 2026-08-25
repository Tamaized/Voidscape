package tamaized.voidscape.client.entity.render.state;

import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.data.DonatorData;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModItemComponents;

@Component
public class ShroudWingLayerRenderStateExtension {

	@Autowired(dist = Dist.CLIENT)
	private ModDataAttachments dataAttachments;

	@Autowired(dist = Dist.CLIENT)
	private ModItemComponents itemComponents;

	public ContextKey<Boolean> isDonatorAndEnabled = createKey("is_donator_and_enabled");

	public ContextKey<Integer> donatorColor = createKey("donator_color");

	public ContextKey<Boolean> hasDraconicAttribute = createKey("has_draconic_attribute");

	private <T> ContextKey<T> createKey(String name) {
		return new ContextKey<>(Identifier.fromNamespaceAndPath(Voidscape.MODID, "shroud_wing_layer_").withSuffix(name));
	}

	public <T extends Avatar & ClientAvatarEntity> void apply(T avatar, AvatarRenderState state) {
		DonatorData data = avatar.getData(dataAttachments.DONATOR);
		state.setRenderData(isDonatorAndEnabled, data.enabled);
		state.setRenderData(donatorColor, data.color);
		state.setRenderData(hasDraconicAttribute, avatar.getItemBySlot(EquipmentSlot.CHEST).getOrDefault(itemComponents.DRACONIC, false));
	}

}
