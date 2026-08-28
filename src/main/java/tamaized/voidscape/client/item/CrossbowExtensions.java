package tamaized.voidscape.client.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jspecify.annotations.Nullable;

public class CrossbowExtensions implements IClientItemExtensions {

	@Override
	public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
		return !entityLiving.swinging && CrossbowItem.isCharged(itemStack) ? HumanoidModel.ArmPose.CROSSBOW_HOLD : null;
	}

}
