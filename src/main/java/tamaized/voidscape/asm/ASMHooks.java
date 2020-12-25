package tamaized.voidscape.asm;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.turmoil.SubCapability;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression"})
public class ASMHooks {

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.entity.LivingEntity#LivingEntity(EntityType, World)}<br>
	 * [AFTER] PUTFIELD : attributes
	 */
	public static void handleEntityAttributes(LivingEntity entity) {
		AttributeModifierMap.MutableAttribute n = AttributeModifierMap.builder();
		n.builder.putAll(entity.attributes.supplier.instances);
		n.add(ModAttributes.VOIDIC_INFUSION_RES.get(), 1F);
		n.add(ModAttributes.VOIDIC_RES.get(), 0F);
		n.add(ModAttributes.VOIDIC_DMG.get(), 0F);
		entity.attributes = new AttributeModifierManager(n.build());
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.LivingRenderer#render(LivingEntity, float, float, MatrixStack, IRenderTypeBuffer, int)}<br>
	 * [BEFORE] INVOKEVIRTUAL : {@link net.minecraft.client.renderer.entity.model.EntityModel#renderToBuffer(MatrixStack, IVertexBuilder, int, int, float, float, float, float)}
	 */
	public static float handleEntityTransparency(float alpha, LivingEntity entity) {
		return Math.min(entity.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapInsanity).map(data -> MathHelper.clamp(1F - data.getInfusion() / 600F, 0F, 1F)).orElse(1F)).orElse(1F), alpha);
	}

}
