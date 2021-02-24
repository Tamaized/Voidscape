package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.registry.ModItems;
import tamaized.voidscape.turmoil.SubCapability;

import javax.annotation.Nullable;

public class ServerPacketTurmoilResetSkills implements NetworkMessages.IMessage<ServerPacketTurmoilResetSkills> {

	public static final LazyValue<ItemStack> VOIDIC_CRYSTAL = new LazyValue<>(() -> new ItemStack(ModItems.VOIDIC_CRYSTAL.get()));

	@Override
	public void handle(@Nullable PlayerEntity player) {
		if (player != null && player.inventory.contains(VOIDIC_CRYSTAL.get()))
			player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				if (data.getResetCooldown() <= 0)
					cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> {
						data.resetSkills(stats);
						player.inventory.clearOrCountMatchingItems(stack -> stack.sameItem(VOIDIC_CRYSTAL.get()), 1, player.inventoryMenu.getCraftSlots());
						player.level.playSound(null, player, SoundEvents.TRIDENT_THUNDER, SoundCategory.PLAYERS, 1F, 0.75F + player.getRandom().nextFloat() * 0.5F);
						if (player.level instanceof ServerWorld)
							for (int i = 0; i < 50; i++) {
								Vector3d pos = new Vector3d(0.25F + player.getRandom().nextFloat() * 0.75F, 0, 0).yRot((float) Math.toRadians(player.getRandom().nextInt(360))).add(player.getX(), player.getEyeY() - 0.5F + player.getRandom().nextFloat(), player.getZ());
								((ServerWorld) player.level).sendParticles(ParticleTypes.ENCHANT, pos.x, pos.y, pos.z, 0, 0, 0, 0, 1);
							}
					});
			}));
	}

	@Override
	public void toBytes(PacketBuffer packet) {

	}

	@Override
	public ServerPacketTurmoilResetSkills fromBytes(PacketBuffer packet) {
		return this;
	}

}
